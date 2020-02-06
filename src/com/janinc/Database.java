package com.janinc;
import com.janinc.interfaces.ISingletonDB;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Database extends ISingletonDB {
    protected static Database mInstance = null;

    private String name = "";
    private String baseDir = "";
    private Map<Class, String> dataClassList = new HashMap<>();
    private Map<String, com.janinc.Table> tables = new HashMap<>();

    public static Database getInstance() {
        if (mInstance == null) {
            mInstance = new Database();
        } // if mInstance...

        return mInstance;
    } // getInstance

    protected Database() {
    } // Database:Database

    protected Database(String name) {
        this();
        this.name = name;
    } // Database:Database

    protected Database(String name, String baseDir) {
        this(name);
        this.baseDir = baseDir;
        if (!baseDir.equals(""))
            checkCreateFolder();
    } // Database:Database

    public void initializeDB() {
        createClasses();
    } // initializeDB

    public void addClass(Class dataClassName) {
        dataClassList.put(dataClassName, "");
    } // addClass

    private void createClasses() {
        dataClassList.forEach(this::createTableClass);
    } // inspectClasses

    private <c> void createTableClass(Class c, String s) {
        Annotation annotation = c.getAnnotation(com.janinc.annotations.Table.class);
        com.janinc.annotations.Table t = (com.janinc.annotations.Table)annotation;
        String name = (annotation == null) ? c.getSimpleName() : t.name();
        Table<c> table = new Table<c>(name, c);
        addTable(name, table);
        dataClassList.put(c, name);
        table.populateFieldManager();
    } // createTableClass

    public String getName() {
        return name;
    } // getName

    public String getBaseDir() {
        return baseDir;
    } // getBaseDir

    public HashMap<String, com.janinc.Table> getTables() {
        return (HashMap<String, Table>) tables;
    } // getTables

    private void checkCreateFolder() {
        File folder = new File("./" + name);
        if (!folder.exists()) {
            folder.mkdir();
        } // if !folder...
    } // createFolder

    public void addTable(String name, com.janinc.Table table) {
        tables.put(name, table);
    } // addTable

    public void removeTable(String name) {
        tables.remove(name);
    } // removeTable

    public boolean dropTable(String name) {
        com.janinc.Table t = tables.get(name);
        removeTable(name);
        return t.deleteTable();
    } // dropTable

    public com.janinc.Table getTable(String table) {
        return tables.get(table);
    } // getTable

    public com.janinc.Table getTable(Class dataClass) {
        return tables.get(dataClassList.get(dataClass));
    } // getTable

    public boolean tableExists(String table) {
        return getTable(table) != null;
    } // tableExists

    public void save(DataObject data) {
        String className = dataClassList.get(data.getClass());
        getTable(className).save(data);
    } // save

    public boolean addRecord(DataObject data) {
        String className = dataClassList.get(data.getClass());

        return getTable(className).addRecord(data);
    } // addRecord

    public boolean deleteRecord(DataObject data) {
        String className = dataClassList.get(data.getClass());
        return getTable(className).deleteRecord(data);
    } // deleteRecord

    public HashMap<String, DataObject> getRecords(String table) {
        return getTable(table).getRecords();
    } // getRecords

    public HashMap<String, DataObject> getRecords(Class dataClass) {
        return getTable(dataClass).getRecords();
    } // getRecords

    public ArrayList<DataObject> search(String table, String searchField, String searchTerm) {
        return getTable(table).search(searchField, searchTerm);
    } // search

    @Override
    public String toString() {
        String tableNames = (String) tables.entrySet().stream(). map(Map.Entry::getValue).map(v -> v.toString()).collect(Collectors.joining("\n"));
        return String.format("-------------------------------%nDatabase name:%s, number of tables: %d%n%s%n-------------------------------", name, tables.size(), tableNames);
    } // toString
//    public HashMap<String, String> getResolvedDataRaw(Data data) {
//        return getTable(data.getFolderName()).getResolvedDataRaw(data);
//    } // getResolvedData
//
//    public Data getResolvedData(Data data) {
//        return getTable(data.getFolderName()).getResolvedData(data);
//    } // getResolvedData
} // class Database
