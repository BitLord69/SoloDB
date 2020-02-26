package com.janinc;

import com.janinc.util.*;
import com.janinc.pubsub.*;
import com.janinc.exceptions.*;
import com.janinc.annotations.*;
import com.janinc.pubsub.iface.*;
import com.janinc.field.FieldManager;
import com.janinc.annotations.AnnotationHandlerParams;

import java.util.*;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Table<D extends DataObject> implements Publisher, Subscriber, AcknowledgeSubscriber, AcknowledgePublisher {
    private final static String DATAFILE_EXTENSION = ".row";

    private Class<?> dataClass;
    protected String name;
    private FieldManager fieldManager;
    private Map<String, D> dataMap = new HashMap<>();
    private Map<String, Reference> references = new HashMap<>();
    private List<Table<? extends DataObject>> referenceTo = new ArrayList<>();
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

    private boolean doDelete(DataObject data) {
        String id = data.getId();
        dataMap.remove(id);
        publish(new Message(Channel.DELETE_RECORD, null, data));
        return new File(id).delete();
    } // doDelete

   public boolean deleteTable(){
       return new File(name).delete();
   } // delete

    public boolean deleteRecord(DataObject data) throws ReferentialIntegrityError {
        if (referenceTo.size() > 0) {
            if (!publishAndWait(new Message(Channel.REFERRER_DELETE, null, data)))
                throw new ReferentialIntegrityError(getName(), data);
        } // if referenceTo...

        return doDelete(data);
    } // deleteRecord

    public boolean cascadeDelete(DataObject data) {
        publish(new Message(Channel.CASCADE_DELETE, null, data));
        return doDelete(data);
    } // cascadeDelete

    public void deleteAll(){
        dataMap.forEach((k, v) -> deleteRecord(v));
        dataMap.clear();
    } // deleteAll

    private void createUniqueId(DataObject data){
        String fileName = String.format("%s/%d%s", name, Instant.now().get(ChronoField.NANO_OF_SECOND), DATAFILE_EXTENSION);
        data.setId(fileName);
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
        publish(new Message(newData ? Channel.ADD_RECORD: Channel.EDIT_RECORD, null, data));
        referenceTo.forEach(t -> publish(new Message(Channel.REFERRER_CHANGED, t, data)));
    } // save

    public void refresh(DataObject data) {
        data.refresh();
        fieldManager.updateDirtyFields(data);
        publish(new Message(Channel.REFRESH, null, data));
    } // refresh

    public void saveAll() throws ValidationException, InvocationTargetException, IllegalAccessException {
        for (Map.Entry<String, D> entry : dataMap.entrySet()) {
            D v = entry.getValue();
                save(v);
        } // for Map...
    } // saveAll

    public void addRecord(DataObject data) throws ValidationException, InvocationTargetException, IllegalAccessException {
        save(data);
        dataMap.put(data.getId(), (D)data);
    }  // addRecord

    public D getRecord(String id) {return dataMap.get(id); }

    public Iterator<? extends Map.Entry<String,? extends DataObject>> getIterator() {
        return dataMap.entrySet().iterator();
    } // getIterator

    public void addReference(Reference ref) { references.put(ref.getKey(), ref); }
    public HashMap<String, D> getRecords() { return (HashMap<String, D>) dataMap; }
    public String getName() { return name; }
    public Class<?> getDataClass() { return dataClass; }
    public void addReferenceTo(Table<? extends DataObject> table) { referenceTo.add(table); }
    public boolean isReferenceTo(Table<? extends DataObject> table) { return referenceTo.contains(table); }
    public List<Table<? extends DataObject>> getReferenceTo() { return referenceTo; }

    public long getNumberOfRecords() {
        return dataMap.size();
    } // getNumberOfRecords

    private void checkClass(Class<?> aClass) {
        Field[] fields = aClass.getDeclaredFields();

        for (Field field: fields) {
            AnnotationHandlerParams ahp = getAnnotationParams(field);
            // TODO: 2020-02-23 Check for multiple annotations for the field - if Lookup, Unique (index?) should be an annotation of it's own
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
            Reference r = new Reference(theField.getName(), strAnn);
            addReference(r);
            r.getRefTable().addReferenceTo(this);
            Database.getInstance().addSubscriber(r.getRefTable().getName(), Channel.REFERRER_CHANGED, this);
            Database.getInstance().addSubscriber(r.getRefTable().getName(), Channel.CASCADE_DELETE, this);
            Database.getInstance().addAcknowledgeSubscriber(r.getRefTable().getName(), Channel.REFERRER_DELETE, this);
        } // if strAnn...
    } // handleStringField

    private AnnotationHandlerParams getAnnotationParams(Field field) {
        if (Debug.ON) System.out.println("I table.getAnnotationParams: namn="  + field.getName() + ", typ=" + field.getType());
        if (Modifier.isStatic(field.getModifiers())){
            if (Debug.ON) System.out.println("Static field encountered, skipping...");
            return null;
        } // if Modifier...

        AnnotationHandlerParams ahp = new AnnotationHandlerParams();

        if(field.getType().isAssignableFrom(String.class)) {
            ahp.setFieldClass(StringField.class);
            ahp.setHandler(this::handleStringField);
            return ahp;
        } // String
        else if(field.getType().isAssignableFrom(Float.class)) {
            ahp.setFieldClass(FloatField.class);
            ahp.setHandler(this::handleFloatField);
            return ahp;
        } // flaot
        else if (field.getType() == int.class) {
            ahp.setFieldClass(IntField.class);
            ahp.setHandler(this::handleIntField);
            return ahp;
        } // int

        return null;
    } // getAnnotationParams

    public void resolveData(DataObject d) {
        references.forEach((k, v) -> {
                v.resolve(d);
        });
    } // resolveData

    private boolean isReferredBy(Table<? extends DataObject> table) {
        return referenceTo.contains(table);
    } // isReferenceTo

    @Override
    public void publish(Message message) {
        pubService.broadcast(message);
    } // publish

    public PublisherService getPublisherService() { return pubService; }

    @Override
    public void addSubscriber(Channel channel, Subscriber subscriber) {
        pubService.addSubscriber(channel, subscriber);
    } // addSubscriber

    @Override
    public void removeSubscriber(Channel channel, Subscriber subscriber) {
        pubService.removeSubscriber(channel, subscriber);
    } // removeSubscriber

    @Override
    public void receiveSubscription(Message message) {
        if (message.getChannel() == Channel.REFERRER_CHANGED && message.getTarget() == this) {
            Table<? extends DataObject> t = message.getTarget();
            references.forEach((k, v) -> {
                if (v.getRefTable().equals(t)) {
                  v.refValueChanged(this, message.getData());
                } // if v...
            });
        } // if message...

        if (message.getChannel() == Channel.CASCADE_DELETE) {
            DataObject d = message.getData();
            references.forEach((k, v) -> {
                if (v.getRefTable().equals(Database.getInstance().getTable(d.getClass()))) {
                    v.cascadeDelete(this, d);
                } // if v...
            });
        } // if message...
    } // update

    @Override
    public boolean publishAndWait(Message message) {
        return pubService.broadcastAndWait(message);
    } // publishAndWait

    @Override
    public void subscribeAcknowledge(Channel channel, AcknowledgeSubscriber subscriber) {
        pubService.addAcknowledgeSubscriber(channel, subscriber);
    } // subscribeAcknowledge

    @Override
    public void unsubscribeAcknowledge(Channel channel, AcknowledgeSubscriber subscriber) {
        pubService.removeAcknowledgeSubscriber(channel, subscriber);
    } // unsubscribeAcknowledge

    @Override
    public boolean receiveSubscriptionAndAcknowledge(Message message) {
        if (message.getChannel() == Channel.REFERRER_DELETE) {
            DataObject d = message.getData();
            for (Map.Entry<String, Reference> entry : references.entrySet()) {
                Reference v = entry.getValue();
                try {
                    if (v.hasChildRecords(this, d))
                        return false;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return false;
                } // catch
            } // for Map...
        } // if message...

        return true;
    } // updateAndAcknowledge

    @Override
    public String toString() {
        String refs = references.size() == 0 ? "" : "\n" + this.references.values().stream().map(Reference::toString).collect(Collectors.joining("\n"));
        String refTo = referenceTo.size() == 0 ? "-- none --" : this.referenceTo.stream().map(Table::getName).collect(Collectors.joining(", "));
        return String.format("Table: '%s', number of records: %d, number of references: %d%s%nIs referred to by: %s%n%s", name, dataMap.size(), references.size(), refs, refTo, fieldManager);
    } // toString
} // class Table