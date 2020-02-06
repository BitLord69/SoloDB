package com.janinc;

import com.janinc.annotations.*;
import com.janinc.exceptions.ValidationException;
import com.janinc.field.FieldManager;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Table<D extends DataObject> {
    private final static String DATAFILE_EXTENSION = ".row";

    private Class<?> dataClass;
    protected String name;
    private FieldManager fieldManager;
    private HashMap<String, D> dataMap = new HashMap<>();
    private HashMap<String, Reference> references = new HashMap<>();

    public Table (String name, Class<?> dataClass){
        this.dataClass = dataClass;

        fieldManager = new FieldManager(dataClass);

        this.name = name;
        checkCreateFolder();

        loadRecords();
    } // Table:Table

    private void checkCreateFolder(){
        File folder = new File("./" + name);
        if (!folder.exists()){
            folder.mkdir();
        } // if !folder.. .
    } // checkCreateFolder

    private void loadRecords() {
        try (Stream<Path> walk = Files.walk(Paths.get("./" + name))) {
            List<String> result = walk.filter(Files::isRegularFile)
                    .map(Path::toString).collect(Collectors.toList());

            result.forEach(fileName -> {
                Object data;

                try {
                    FileInputStream fileIn = new FileInputStream(fileName);
                    ObjectInputStream in = new ObjectInputStream(fileIn);
                    data = dataClass.cast(in.readObject());
                    in.close();
                    fileIn.close();
                } catch (IOException i) {
                    i.printStackTrace();
                    return;
                } catch (ClassNotFoundException c) {
                    System.out.println("Class not found!");
                    c.printStackTrace();
                    return;
                }

                 dataMap.put(((DataObject)data).getId(), (D) data);
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

    public boolean deleteRecord(D data){
        String id = ((DataObject)data).getId();
        dataMap.remove(id);
        return new File(id).delete();
    } // deleteRecord

    public void deleteAll(){
        dataMap.forEach((k, v) -> deleteRecord(v));
        dataMap.clear();
    } // deleteAll

    private void createUniqueId(D data){
        String fileName = String.format("%s/%d%s", name, System.currentTimeMillis(), DATAFILE_EXTENSION);
        ((DataObject)data).setId(fileName);
    } // createUniqueId

    public void save(D data) throws ValidationException{
        if(((DataObject)data).getId().equals("")){
            createUniqueId(data);
        } // if DataObject...

        try {
            if (!fieldManager.validateData(data))
                return;

            FileOutputStream fileOut = new FileOutputStream(((DataObject)data).getId());
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(data);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in %s%n", ((DataObject)data).getId());
        } catch (IOException i) {
            i.printStackTrace();
        } // catch
    } // save

    public void saveAll() throws ValidationException {
        for (Map.Entry<String, D> entry : dataMap.entrySet()) {
            String k = entry.getKey();
            D v = entry.getValue();
                save(v);
          } // for entry...
    } // saveAll

    public boolean addRecord(D data) throws ValidationException {
        save(data);
        dataMap.put(((DataObject)data).getId(), data);
        return true;
    }  // addRecord

    public D getRecord(String id){
        return dataMap.get(id);
    }

    public ArrayList<D> search(String key, String value){
//        ArrayList<D> result = new ArrayList<>();
//        dataMap.forEach((k, d) -> {
//           if (((Data)d).getData().containsKey(key))
//               if (((Data)d).getData().get(key).contains(value)){
//                   result.add(d);
//           }
//        });
//        return result;
        return null;
    } // search

    public void addReference(Reference ref){
        references.put(ref.getKey(), ref);
    }

    public HashMap<String, D> getRecords(){
        return dataMap;
    }

    public String getName() { return name; }

    public void populateFieldManager() {
        Field[] fields = dataClass.getDeclaredFields();

        // TODO: 2020-02-06 Check fields from superclass(es) as well!
        
        for (Field field: fields) {
            AnnotationHandlerParams ahp = getAnnotationParams(field);

            if (ahp != null) {
                Annotation annotation = field.getAnnotation(ahp.getFieldClass());
                if (annotation != null) {
                    ahp.getHandler().accept(field, annotation);
                } // if annotation...
            } // if ahp...
        } // for Field...
    } // populateFieldManager

    private void handleIntField(Object field, Object annotation) {
        Field theField = (Field)field;
        IntField myA = (IntField)annotation;
        String name = theField.getName();

        fieldManager.addField(new com.janinc.field.IntField(name, myA.minvalue(), myA.maxvalue(), myA.useValidation(), myA.uniqueValue()));
    } // handleIntField

    private void handleBooleanField(Object field, Object annotation) {
        Field theField = (Field)field;
        BooleanField myA = (BooleanField)annotation;
        String name = theField.getName();

        // TODO: 2020-02-06 Create fields for booleans
    } // handleBooleanField

    private void handleFloatField(Object field, Object annotation) {
        Field theField = (Field)field;
        FloatField myA = (FloatField)annotation;
        String name = theField.getName();

        // TODO: 2020-02-06 Create fields for floats
    } // handleFloatField

    private void handleStringField(Object field, Object annotation) {
        Field theField = (Field)field;
        StringField myA = (StringField)annotation;

        String name = theField.getName();
        int maxLength = myA.maxlength();
        // name, maxLength
        com.janinc.field.StringField stringField =
                new com.janinc.field.StringField(name, maxLength, myA.mandatory(), myA.uniquevalue());
        fieldManager.addField(stringField);

        // TODO: 2020-02-06 Check for correct lookup syntax and create a reference object
        if (myA.lookup()) {
            ;
        } // if myAnnotation...
    } // handleStringField

    private AnnotationHandlerParams getAnnotationParams(Field field) {
        Object fieldClass = field.getType();
        AnnotationHandlerParams ahp = new AnnotationHandlerParams();

        System.out.println("I table.getAnnotationClass: namn="  + field.getName() + ", typ=" + field.getType());

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
        else if(field.getType().isAssignableFrom(Boolean.class)) {
            ahp.setFieldClass(BooleanField.class);
            ahp.setHandler(this::handleBooleanField);
            return ahp;
        }
        else if (field.getType() == int.class) {
            ahp.setFieldClass(IntField.class);
            ahp.setHandler(this::handleIntField);
            return ahp;
        } else if (field.getType() == boolean.class) {
            ahp.setFieldClass(BooleanField.class);
            ahp.setHandler(this::handleBooleanField);
            return ahp;
        }

        return null;
    } // getAnnotationParams

//    public HashMap<String, String> getResolvedDataHM(HashMap<String, Reference> references) {
//
//        HashMap <String, String> newData = (HashMap<String, String>) data.clone();
//
//        data.forEach((fieldKey, v) -> {
//            if (references.containsKey(fieldKey)) {
//                Reference ref = (Reference) references.get(fieldKey);
//                String[] keyList = newData.get(ref.getKey()).split(",");
//
//                if (keyList.length > 0)
//                {
//                    String newList = replaceData(ref, keyList);
//                    newData.put(ref.getKey(), newList);
//                } // if keyList...
//            } // if references...
//        });
//        return newData;
//    } // getResolvedData
//
//    private String replaceData(Reference ref, String[] keyList) {
//        StringBuilder newList = new StringBuilder();
//
//        for (String s : keyList){
//            if (s != null && !s.equals("")){
//                ArrayList res = ref.getRefTable().search(ref.getRefKey(), s);
//
//                if (res.size() > 0) {
//                    Data refRecord = (Data) res.get(0);
//                    if (refRecord != null) {
//                        newList.append(refRecord.getData().get(ref.getRefTextKey()));
//                        newList.append(",");
//                    } // if refData...
//                } // if res...
//            } // if !s...
//        } // for s...
//
//        String temp = newList.toString();
//        if (temp.length() > 0)
//            temp = temp.substring(0, temp.length() - 1);
//        return temp;
//    } // replaceData

    @Override
    public String toString() {
        return String.format("Table: '%s', number of records: %d", name, dataMap.size());
    } // toString

//    public HashMap<String, String> getResolvedDataRaw(D data){
//        return ((Data)data).getResolvedDataHM(references);
//    } // getResolvedData

//    public D getResolvedData(D data){
//        return (D) createDataObject(getResolvedDataRaw(data));
//    } // getResolvedData
} // class Table