package com.janinc.testapp.testdb;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-01
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/
import com.janinc.DataObject;
import com.janinc.annotations.*;

@Table(name="disc")
public class Disc extends DataObject {
    @StringField(maxlength = 300, uniquevalue = true)
    private String name;

    @StringField(lookup=true, lookupTable="Manufacturer.class", lookupKey="id", lookupField="name", shadowField="brandShadow")
    private String brand;
//getTable(UserTable.TABLE_NAME).addReference(new Reference(getTable(HobbyTable.TABLE_NAME), User.HOBBIES, Data.ID, Hobby.NAME));

    private transient String brandShadow;

    @IntField(minvalue = 140, maxvalue = 200, useValidation = true)
    private int weight;

    @StringField()
    private String color;

    @StringField()
    private String plastic;

    public Disc() {
    }

    public Disc(String name, String brand, int weight, String color, String plastic) {
        this.name = name;
        this.brand = brand;
        this.weight = weight;
        this.color = color;
        this.plastic = plastic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPlastic() {
        return plastic;
    }

    public void setPlastic(String plastic) {
        this.plastic = plastic;
    }

    public String getBrandShadow() { return brandShadow; }

    @Override
    public String toString() {
        return String.format("%s, manufactured by: %s, weight: %d, color: %s, Plastic: %s", getName(), getBrand(), getWeight(), getColor(), getPlastic());
    } // toString
} // class Disc

//    public Disc(String fileName) {
//        super(fileName);
//    }
//
//    public Disc(HashMap<String, String> hm){
//        super("");
//        setName(hm.get(NAME));
//    } // User
//
//    public Disc(String name, String brand, String weight, String color, String plastic) {
//        this("", name, brand, weight, color, plastic);
//    }
//
//    public Disc(String fileName, String name, String brand, String weight, String color, String plastic) {
//        super(fileName);
//        setName(name);
//        setBrand(brand);
//        setWeight(weight);
//        setColor(color);
//        setPlastic(plastic);
//    }
//
//    @Override
//    public boolean load() {
//        super.load();
//        return true;
//    } // load
//
//    @Override
//    public String getFolderName() {
//        return DiscTable.TABLE_NAME;
//    } // getFolderName
//
//    public String getName() {
//        return (String)getData().get(Disc.NAME);
//    }
//
//    public void setName(String name) {
//        getData().put(Disc.NAME, name);
//    }
//
//    public String getBrand() {
//        return (String)getData().get(Disc.BRAND);
//    }
//
//    public void setBrand(String brand) {
//        getData().put(Disc.BRAND, brand);
//    }
//
//    public String getWeight() {
//        return (String)getData().get(Disc.WEIGHT);
//    }
//
//    public void setWeight(String weight) {
//        getData().put(Disc.WEIGHT, weight);
//    }
//
//    public String getColor() {
//        return (String)getData().get(Disc.COLOR);
//    }
//
//    public void setColor(String color) {
//        getData().put(Disc.COLOR, color);
//    }
//
//    public String getPlastic() {
//        return (String)getData().get(Disc.PLASTIC);
//    }
//
//    public void setPlastic(String plastic) {
//        getData().put(Disc.PLASTIC, plastic);
//    }
//
//    @Override
//    public String toString() {
//        return String.format("%s", getName());
//    } // toString
//} // class User
