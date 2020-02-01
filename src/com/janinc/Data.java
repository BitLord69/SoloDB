package com.janinc;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Data {
    public final static String ID = "id";
    private final static String EXTENSION = ".row";

    private String fileName = "";
    private HashMap<String, String> data = new HashMap<>();
    public abstract String getFolderName();

    public Data(String fileName){
        if(getFolderName().equals("") || fileName.contains("/") || fileName.contains("\\"))
            this.fileName = fileName;
        else
            this.fileName = String.format("%s/%s", getFolderName(), fileName);
    } // Data

    public String getFileName() {
        return fileName;
    }

    public HashMap<String, String> getData() {
        return data;
    }

    public String getID() {
        return (String)data.get(ID);
    }

    public HashMap<String, String> getResolvedDataHM(HashMap<String, Reference> references) {

        HashMap <String, String> newData = (HashMap<String, String>) data.clone();

        data.forEach((fieldKey, v) -> {
            if (references.containsKey(fieldKey)) {
                Reference ref = (Reference) references.get(fieldKey);
                String[] keyList = newData.get(ref.getKey()).split(",");

                if (keyList.length > 0)
                {
                    String newList = replaceData(ref, keyList);
                    newData.put(ref.getKey(), newList);
                } // if keyList...
            } // if references...
        });
        return newData;
    } // getResolvedData

    private String replaceData(Reference ref, String[] keyList) {
        StringBuilder newList = new StringBuilder();

        for (String s : keyList){
            if (s != null && !s.equals("")){
                ArrayList res = ref.getRefTable().search(ref.getRefKey(), s);

                if (res.size() > 0) {
                    Data refRecord = (Data) res.get(0);
                    if (refRecord != null) {
                        newList.append(refRecord.getData().get(ref.getRefTextKey()));
                        newList.append(",");
                    } // if refData...
                } // if res...
            } // if !s...
        } // for s...

        String temp = newList.toString();
        if (temp.length() > 0)
            temp = temp.substring(0, temp.length() - 1);
        return temp;
    } // replaceData

    private void createFileName(){
        fileName = String.format("%s/%d%s", getFolderName(), System.currentTimeMillis(), EXTENSION);
        data.put(ID, fileName);
    } //createFileName

    public boolean save(){
        if(getFolderName().equals("")){
            System.out.println("folderName not set :(");
            return false;
        }
        if(!fileName.contains(".row")) createFileName();

        Path path = Paths.get(fileName);
        StringBuilder output = new StringBuilder();

        data.forEach((k, v) -> output.append(k).append("|").append(v).append(System.lineSeparator()));

        try {
            Files.write(path, output.toString().getBytes());
        } catch (IOException e) {
            return false;
        }
        return true;
    } // save

    public boolean load(){
        if(fileName.equals("")){
            return false;
        }
        Path path = Paths.get(fileName);
        try {
            List<String> list = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (String s : list) {
                String[] line = s.split("\\|");
                if (line.length == 1)
                    data.put(line[0], "");
                else
                    data.put(line[0], line[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    } // load

    public boolean delete() {
        File file = new File(fileName);
        System.out.println("data.delete = " + fileName);
        return file.delete();
    } // delete
} // class Data