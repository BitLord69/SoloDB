package com.janinc;
import com.janinc.exceptions.ValidationException;
import com.janinc.field.Field;
import com.janinc.interfaces.ISingletonDB;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Database extends ISingletonDB {
    protected static Database mInstance = null;

    private String name = "";
    private String baseDir = "";
    private Map<Class<? extends DataObject>, String> dataClassList = new LinkedHashMap<>();
    private Map<String, com.janinc.Table<? extends DataObject>> tables = new LinkedHashMap<>();

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

    public void addClass(Class<? extends DataObject> dataClassName) {
        dataClassList.put(dataClassName, "");
    } // addClass

    private void createClasses() {
        dataClassList.forEach(this::createTableClass);
    } // inspectClasses

    private <c extends DataObject> void createTableClass(Class<? extends DataObject> c, String s) {
        com.janinc.annotations.Table annotation = c.getAnnotation(com.janinc.annotations.Table.class);
        String name = (annotation == null) ? c.getSimpleName() : annotation.name();
        Table<c> table = new Table<>(name, c);
        addTable(name, table);
        dataClassList.put(c, name);
        table.populateFieldManager();
        table.loadRecords();
    } // createTableClass

    public String getName() {
        return name;
    } // getName

    public String getBaseDir() {
        return baseDir;
    } // getBaseDir

    public LinkedHashMap<String, Table<? extends DataObject>> getTables() {
        return (LinkedHashMap<String, Table<? extends DataObject>>) tables;
    } // getTables

    private void checkCreateFolder() {
        File folder = new File("./" + name);
        if (!folder.exists()) {
            folder.mkdir();
        } // if !folder...
    } // createFolder

    public void addTable(String name, Table<? extends DataObject> table) {
        tables.put(name, table);
    } // addTable

    public void removeTable(String name) {
        tables.remove(name);
    } // removeTable

    public boolean dropTable(String name) {
        com.janinc.Table<? extends DataObject> t = tables.get(name);
        removeTable(name);
        return t.deleteTable();
    } // dropTable

    public com.janinc.Table<? extends DataObject> getTable(String table) {
        return tables.get(table);
    } // getTable

    public com.janinc.Table<? extends DataObject> getTable(Class<? extends DataObject> dataClass) {
        var t = dataClassList.get(dataClass);
        return tables.get(t);
    } // getTable

    public boolean tableExists(String table) {
        return getTable(table) != null;
    } // tableExists

    public void save(DataObject data) throws ValidationException {
        String className = dataClassList.get(data.getClass());
        getTable(className).save(data);
    } // save

    public boolean addRecord(DataObject data) throws ValidationException {
        String className = dataClassList.get(data.getClass());
        return getTable(className).addRecord(data);
    } // addRecord

    public boolean deleteRecord(DataObject data) {
        String className = dataClassList.get(data.getClass());
        return getTable(className).deleteRecord(data);
    } // deleteRecord

    public DataObject getRecord(Class<? extends DataObject> dataClass, String id) {
        return getTable(dataClass).getRecord(id);
    } // getRecord

    public HashMap getRecords(String table) {
        return getTable(table).getRecords();
    } // getRecords

    public HashMap getRecords(Class<? extends DataObject> dataClass) {
        return getTable(dataClass).getRecords();
    } // getRecords

    public long getNumberOfRecords(String table) {
        return  getTable(table).getNumberOfRecords();
    } // getNumberOfRecords

    public long getNumberOfRecords(Class<? extends DataObject> dataClass) {
        return getTable(dataClass).getNumberOfRecords();
    } // getNumberOfRecords

    public ArrayList<DataObject> search(String table, String searchField, String searchTerm) {
//        return getTable(table).search(searchField, searchTerm);
        return null;
    } // search

    @Override
    public String toString() {
        String tableNames = tables.values().stream().map(Table::toString).collect(Collectors.joining("\n"));
        String s = String.format("-------------------- Database name: '%s', number of tables: %d --------------------", name, tables.size());
        return String.format("%s%n%s%n%s", s, tableNames, "-".repeat(s.length()));
    } // toString
} // class Database
