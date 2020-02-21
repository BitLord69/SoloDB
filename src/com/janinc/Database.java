package com.janinc;

import com.janinc.exceptions.*;
import com.janinc.field.FieldManager;
import com.janinc.interfaces.ISingletonDB;
import com.janinc.pubsub.Channel;
import com.janinc.pubsub.Subscriber;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class Database extends ISingletonDB {
    protected static Database mInstance = null;
    protected boolean initialized = false;

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
        initialized = true;
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

    public LinkedHashMap<String, Table<? extends DataObject>> getTables() throws DatabaseNotInitializedException {
        if (!initialized) throw new DatabaseNotInitializedException();
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
        if (!initialized) throw new DatabaseNotInitializedException();
        tables.remove(name);
    } // removeTable

    public boolean dropTable(String name) {
        com.janinc.Table<? extends DataObject> t = tables.get(name);
        removeTable(name);
        return t.deleteTable();
    } // dropTable

    public com.janinc.Table<? extends DataObject> getTable(String table) throws DatabaseNotInitializedException {
        if (!initialized) throw new DatabaseNotInitializedException();
        return tables.get(table);
    } // getTable

    public com.janinc.Table<? extends DataObject> getTable(Class<? extends DataObject> dataClass) throws DatabaseNotInitializedException {
        if (!initialized) throw new DatabaseNotInitializedException();
        return tables.get(dataClassList.get(dataClass));
    } // getTable

    public String getTableName(Class<? extends DataObject> dataClass) {
        if (!initialized) throw new DatabaseNotInitializedException();
        return dataClassList.get(dataClass);
    } // getTableName

    public boolean tableExists(String table) throws DatabaseNotInitializedException {
        return getTable(table) != null;
    } // tableExists

    public boolean tableExists(Class<? extends DataObject> dataClass) throws DatabaseNotInitializedException {
        return getTable(dataClassList.get(dataClass)) != null;
    } // getTable
    public void save(DataObject data) throws ValidationException, DatabaseNotInitializedException, InvocationTargetException, IllegalAccessException {
        String className = dataClassList.get(data.getClass());
        getTable(className).save(data);
    } // save

    public void refresh(DataObject data) {
        String className = dataClassList.get(data.getClass());
        getTable(className).refresh(data);
    } // save

    public void addRecord(DataObject data) throws ValidationException, DatabaseNotInitializedException, InvocationTargetException, IllegalAccessException {
        String className = dataClassList.get(data.getClass());
        getTable(className).addRecord(data);
    } // addRecord

    public boolean deleteRecord(DataObject data) throws DatabaseNotInitializedException {
        String className = dataClassList.get(data.getClass());
        return getTable(className).deleteRecord(data);
    } // deleteRecord

    public DataObject getRecord(Class<? extends DataObject> dataClass, String id) {
        return getTable(dataClass).getRecord(id);
    } // getRecord

    public HashMap<String, ? extends DataObject> getRecords(String table) throws DatabaseNotInitializedException {
        return getTable(table).getRecords();
    } // getRecords

    public HashMap<String, ? extends DataObject> getRecords(Class<? extends DataObject> dataClass) {
        return getTable(dataClass).getRecords();
    } // getRecords

    public Iterator<? extends Map.Entry<String,? extends DataObject>> getIterator(String table) throws DatabaseNotInitializedException {
        return getTable(table).getIterator();
    } // getIterator

    public Iterator<? extends Map.Entry<String,? extends DataObject>> getIterator(Class<? extends DataObject> dataClass) {
        return getTable(dataClass).getIterator();
    } // getIterator

    public long getNumberOfRecords(String table) throws DatabaseNotInitializedException {
        return  getTable(table).getNumberOfRecords();
    } // getNumberOfRecords

    public long getNumberOfRecords(Class<? extends DataObject> dataClass) {
        return getTable(dataClass).getNumberOfRecords();
    } // getNumberOfRecords

    public FieldManager getFieldManager(String table) {
        return getTable(table).getFieldManager();
    } // getFieldManager

    public FieldManager getFieldManager(Class<? extends DataObject> dataClass) {
        return getTable(dataClass).getFieldManager();
    } // getFieldManager

    public void subscribe(String table, Channel channel, Subscriber subscriber) {
        getTable(table).getPublisherService().addSubscriber(channel, subscriber);
    } // subscribe

    public void subscribe(Class<? extends DataObject> dataClass, Channel channel, Subscriber subscriber) {
        getTable(dataClass).getPublisherService().addSubscriber(channel, subscriber);
    } // subscribe

    public void unsubscribe(String table, Channel channel, Subscriber subscriber) {
        getTable(table).getPublisherService().removeSubscriber(channel, subscriber);
    } // unsubscribe

    public void unsubscribe(Class<? extends DataObject> dataClass, Channel channel, Subscriber subscriber) {
        getTable(dataClass).getPublisherService().removeSubscriber(channel, subscriber);
    } // unsubscribe

    @Override
    public String toString() {
        String tableNames = tables.values().stream().map(Table::toString).collect(Collectors.joining("\n"));
        String s = String.format("-------------------- Database name: '%s', number of tables: %d --------------------", name, tables.size());
        return String.format("%s%n%s%n%s", s, tableNames, "-".repeat(s.length()));
    } // toString
} // class Database
