package com.janinc;

import com.janinc.annotations.AnnotationHandlerParams;
import com.janinc.annotations.FloatField;
import com.janinc.annotations.IntField;
import com.janinc.annotations.StringField;
import com.janinc.exceptions.ValidationException;
import com.janinc.field.FieldManager;
import com.janinc.pubsub.*;
import com.janinc.util.Debug;
import com.janinc.util.FileHandler;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Table<D extends DataObject> implements Publisher {
    private final static String DATAFILE_EXTENSION = ".row";

    private Class<?> dataClass;
    protected String name;
    private FieldManager fieldManager;
    private HashMap<String, D> dataMap = new HashMap<>();
    private HashMap<String, Reference> references = new HashMap<>();
    private PublisherService pubService = new PublisherService();

    public Table (String name, Class<?> dataClass){
        this.name = name;
        this.dataClass = dataClass;
        fieldManager = new FieldManager(dataClass);

        checkCreateFolder();
    } // Table:Table

    private void checkCreateFolder(){
        File folder = new File("./" + name);
        if (!folder.exists()){
            folder.mkdir();
        } // if !folder.. .
    } // checkCreateFolder

    public void loadRecords() {
        try (Stream<Path> walk = Files.walk(Paths.get("./" + name))) {
            List<String> result = walk.filter(Files::isRegularFile)
                    .map(Path::toString).collect(Collectors.toList());

            result.forEach(fileName -> {
                Object data = dataClass.cast(FileHandler.readFile("", fileName));
                dataMap.put(((DataObject)data).getId(), (D)data);
                fieldManager.updateDirtyFields((D)data);
                resolveData((D)data);
            });
        } catch (IOException e) {
            e.printStackTrace();
        } // catch
    } // loadRecords

    public FieldManager getFieldManager() {
        return fieldManager;
    } // getFieldManager

   public boolean deleteTable(){
       return new File(name).delete();
   } // delete

    public boolean deleteRecord(DataObject data){
        String id = data.getId();
        dataMap.remove(id);
        publish(new Message(Channel.DELETE_RECORD, data));
        return new File(id).delete();
    } // deleteRecord

    public void deleteAll(){
        dataMap.forEach((k, v) -> deleteRecord(v));
        dataMap.clear();
    } // deleteAll

    private void createUniqueId(DataObject data){
        String fileName = String.format("%s/%d%s", name, Instant.now().get(ChronoField.NANO_OF_SECOND), DATAFILE_EXTENSION);
        ((DataObject)data).setId(fileName);
    } // createUniqueId

    public void save(DataObject data) throws ValidationException, InvocationTargetException, IllegalAccessException {
        boolean newData = false;
        fieldManager.validateData(data);

        if(data.getId().equals("")){
            createUniqueId(data);
            newData= true;
        } // if DataObject...

        FileHandler.writeFile("", data.getId(), data);
        resolveData((D)data);
        fieldManager.updateDirtyFields((D)data);
        publish(new Message(newData ? Channel.ADD_RECORD: Channel.EDIT_RECORD, data));
    } // save

    public void refresh(DataObject data) {
        data.refresh();
        fieldManager.updateDirtyFields((D)data);
        publish(new Message(Channel.REFRESH, data));
    } // refresh

    public void saveAll() throws ValidationException, InvocationTargetException, IllegalAccessException {
        for (Map.Entry<String, D> entry : dataMap.entrySet()) {
            String k = entry.getKey();
            D v = entry.getValue();
                save(v);
        } // for Map...
    } // saveAll

    public void addRecord(DataObject data) throws ValidationException, InvocationTargetException, IllegalAccessException {
        save(data);
        dataMap.put(data.getId(), (D) data);
    }  // addRecord

    public D getRecord(String id) {return dataMap.get(id); }

    public Iterator<? extends Map.Entry<String,? extends DataObject>> getIterator() {
        return dataMap.entrySet().iterator();
    } // getIterator

    public void addReference(Reference ref) { references.put(ref.getKey(), ref); }
    public HashMap<String, D> getRecords() { return dataMap; }
    public String getName() { return name; }
    public Class<?> getDataClass() { return dataClass; }

    public long getNumberOfRecords() {
        return dataMap.size();
    } // getNumberOfRecords

    private void checkClass(Class<?> aClass) {
        Field[] fields = aClass.getDeclaredFields();

        for (Field field: fields) {
            AnnotationHandlerParams ahp = getAnnotationParams(field);

            if (ahp != null) {
                Annotation annotation = field.getAnnotation(ahp.getFieldClass());
                if (annotation != null) {
                    ahp.getHandler().accept(field, annotation);
                } // if annotation...
            } // if ahp...
        } // for Field...
    } // checkClass

    public void populateFieldManager() {
        Class<?> theClass = dataClass;
        while (!(theClass.getName().equals(Object.class.getName()))) {
            checkClass(theClass);
            theClass = theClass.getSuperclass();
        } // while...
    } // populateFieldManager

    private void handleIntField(Object field, Object annotation) {
        Field theField = (Field)field;
        fieldManager.addField(new com.janinc.field.IntField<D>(theField.getName(), (IntField)annotation));
    } // handleIntField

    private void handleFloatField(Object field, Object annotation) {
        Field theField = (Field)field;
        fieldManager.addField(new com.janinc.field.FloatField<D>(theField.getName(), (FloatField)annotation));
    } // handleFloatField

    private void handleStringField(Object field, Object annotation) {
        Field theField = (Field)field;
        String name = theField.getName();
        StringField strAnn = (StringField)annotation;

        com.janinc.field.StringField<D> stringField = new com.janinc.field.StringField<>(name, strAnn);
        fieldManager.addField(stringField);

        if (strAnn.lookup()) {
            addReference(new Reference(theField.getName(), strAnn));
        } // if strAnn...
    } // handleStringField

    private AnnotationHandlerParams getAnnotationParams(Field field) {
        Object fieldClass = field.getType();
        AnnotationHandlerParams ahp = new AnnotationHandlerParams();

        if (Debug.ON) System.out.println("I table.getAnnotationParams: namn="  + field.getName() + ", typ=" + field.getType());

        if (Modifier.isStatic(field.getModifiers())){
            if (Debug.ON) System.out.println("Static field encountered, skipping...");
            return null;
        } // if Modifier...

        if(field.getType().isAssignableFrom(String.class)) {
            ahp.setFieldClass(StringField.class);
            ahp.setHandler(this::handleStringField);
            return ahp;
        }
        else if(field.getType().isAssignableFrom(Float.class)) {
            ahp.setFieldClass(FloatField.class);
            ahp.setHandler(this::handleFloatField);
            return ahp;
        }
        else if (field.getType() == int.class) {
            ahp.setFieldClass(IntField.class);
            ahp.setHandler(this::handleIntField);
            return ahp;
        }

        return null;
    } // getAnnotationParams

    public void resolveData(DataObject d) {
        references.forEach((k, v) -> {
            v.resolve(d);
        });
    } // resolveData

    @Override
    public void publish(Message message) {
        pubService.broadcast(message);
    } // publish

    @Override
    public void subscribe(Channel channel, Subscriber subscriber) {
        pubService.addSubscriber(channel, subscriber);
    } // subscribe

    public PublisherService getPublisherService() { return pubService; }

    @Override
    public void unsubscribe(Channel channel, Subscriber subscriber) {
        pubService.removeSubscriber(channel, subscriber);
    } // unsubscribe

    @Override
    public String toString() {
        String refs = this.references.values().stream().map(Reference::toString).collect(Collectors.joining("\n"));
        return String.format("Table: '%s', number of records: %d, number of references: %d%n%s%n%s", name, dataMap.size(), references.size(), refs, fieldManager);
    } // toString
} // class Table