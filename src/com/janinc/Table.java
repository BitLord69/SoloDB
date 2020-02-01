package com.janinc;

import com.janinc.field.FieldManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

abstract public class Table<D> {
    protected String name;
    private HashMap<String, D> dataMap = new HashMap<>();
    private HashMap<String, Reference> references = new HashMap<>();

    private FieldManager fieldManager = new FieldManager();

    abstract public D createDataObject(String fileName);
    abstract public D createDataObject(HashMap<String, String> hm);

    public Table (String name){
        this.name = name;
        checkCreateFolder();

        // TODO: 2020-01-13 Om vi vill att den ska läsas automatiskt så ska det göras här.
        loadRecords();
    } // Table:Table

    private void checkCreateFolder(){
        File folder = new File("./" + name);
        if (!folder.exists()){
            folder.mkdir();
        } // if !folder...
    } // checkCreateFolder

    private void loadRecords() {
        try (Stream<Path> walk = Files.walk(Paths.get("./" + name))) {
            List<String> result = walk.filter(Files::isRegularFile)
                    .map(Path::toString).collect(Collectors.toList());

            result.forEach(fileName -> {
                    D data = createDataObject(fileName);
                    ((Data)data).load();
                    dataMap.put(((Data)data).getFileName(), data);
            });

        } catch (IOException e) {
            e.printStackTrace();
        } // catch
    } // loadRecords

    public FieldManager getFieldManager() {
        return fieldManager;
    } // getFieldManager

   public boolean delete(){
       File file = new File(name);
       return file.delete();
   } // delete

    public boolean deleteRecord(Data data){
        dataMap.remove(data.getID());
        return data.delete();
    } // deleteRecord

    public void deleteAll(){
        dataMap.forEach((k, d) -> ((Data)d).delete());
        dataMap.clear();
    } // deleteAll

    public void saveAll(Data data){
        dataMap.forEach((k, d) -> ((Data)d).save());
    }

    public boolean addRecord(D data){
        ((Data)data).save();
        dataMap.put(((Data)data).getFileName(), data);
        return true;
    }  // addRecord

    public Set getKeys(){
        if(dataMap.isEmpty()){
            return null;
        } // if dataMap...
        else{
            Map.Entry<String, D> entry = dataMap.entrySet().iterator().next();
            D d = entry.getValue();
            HashMap<String, String> h = ((Data)d).getData();
            return h.keySet();
        } // else
    } // getKeys

    public D getRecord(String id){
        return dataMap.get(id);
    }

    public ArrayList<D> search(String key, String value){
        ArrayList<D> result = new ArrayList<>();
        dataMap.forEach((k, d) -> {
           if (((Data)d).getData().containsKey(key))
               if (((Data)d).getData().get(key).contains(value)){
                   result.add(d);
           }
        });
        return result;
    } // search

    public void addReference(Reference ref){
        references.put(ref.getKey(), ref);
    }

    public HashMap<String, D> getRecords(){
        return dataMap;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, String> getResolvedDataRaw(D data){
        return ((Data)data).getResolvedDataHM(references);
    } // getResolvedData

    public D getResolvedData(D data){
        return (D) createDataObject(getResolvedDataRaw(data));
    } // getResolvedData
} // class Table