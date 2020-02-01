package com.janinc;
import com.janinc.interfaces.ISingletonDB;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Database extends ISingletonDB {
    protected static Database mInstance;

    private String name = "";
    private String baseDir = "";
    private HashMap<String, Table<Data>> tables = new HashMap<>();

    public static Database getInstance() {
        if (mInstance == null) {
            mInstance = new Database();
        }
        return mInstance;
    } // getInstance

    private Database() {
    }

    protected Database(String name) {
        this.name = name;
    } // Database:Database

    protected Database(String name, String baseDir) {
        this(name);
        this.baseDir = baseDir;
        if (!baseDir.equals(""))
            checkCreateFolder();
    } // Database:Database

    public String getName() {
        return name;
    } // getName

    public String getBaseDir() {
        return baseDir;
    } // getBaseDir

    public HashMap<String, Table<Data>> getTables() {
        return tables;
    } // getTables

    private void checkCreateFolder() {
        File folder = new File("./" + name);
        if (!folder.exists()) {
            folder.mkdir();
        } // if !folder...
    } // createFolder

    public void addTable(String name, Table table) {
        tables.put(name, table);
    } // addTable

    public void removeTable(String name) {
        tables.remove(name);
    } // removeTable

    public boolean dropTable(String name) {
        Table<Data> t = tables.get(name);
        removeTable(name);
        return t.delete();
    } // dropTable

    public Table<Data> getTable(String table) {
        return tables.get(table);
    } // getTable

    public boolean tableExists(String table) {
        return getTable(table) != null;
    } // tableExists

    public boolean addRecord(Data data) {
        return getTable(data.getFolderName()).addRecord(data);
    } // addRecord

    public boolean deleteRecord(Data data) {
        return getTable(data.getFolderName()).deleteRecord(data);
    } // deleteRecord

    public HashMap<String, Data> getRecords(String table) {
        return getTable(table).getRecords();
    } // getRecords

    public ArrayList<Data> search(String table, String searchField, String searchTerm) {
        return getTable(table).search(searchField, searchTerm);
    } // search

    public HashMap<String, String> getResolvedDataRaw(Data data) {
        return getTable(data.getFolderName()).getResolvedDataRaw(data);
    } // getResolvedData

    public Data getResolvedData(Data data) {
        return getTable(data.getFolderName()).getResolvedData(data);
    } // getResolvedData
} // class Database
