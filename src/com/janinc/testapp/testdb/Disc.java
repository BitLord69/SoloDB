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
    public static final long serialVersionUID = 4242L;

    @StringField(maxlength = 300, uniquevalue = true)
    private String name;

    @StringField(lookup=true, lookupTable=Manufacturer.class, lookupForeignKey="id", lookupForeignField="name", targetField="brandShadow")
    private String brand;
    private transient String brandShadow;

    @IntField(minvalue = 140, maxvalue = 200, useValidation = true)
    private int weight;
    private float fade;
    private String color;
    private String plastic;
    private boolean brandNew = true;

    public Disc(String name, String brand, int weight, String color, String plastic, float fade) {
        this.name = name;
        this.brand = brand;
        this.weight = weight;
        this.color = color;
        this.plastic = plastic;
        this.fade = fade;
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

    public float getFade() { return fade; }

    public void setFade(float fade) { this.fade = fade; }

    public boolean isBrandNew() { return brandNew; }

    public void setBrandNew(boolean brandNew) { this.brandNew = brandNew; }

    @Override
    public String toString() {
        return String.format("%s, manufactured by: %s (%s), weight: %d, color: %s, Plastic: %s, fade: %.1f, new: %b",
                getName(), getBrandShadow(), getBrand(), getWeight(), getColor(), getPlastic(), getFade(), isBrandNew());
    } // toString
} // class Disc
