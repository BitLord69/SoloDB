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

    @StringField(maxlength = 300, unique = true, mandatory = true)
    private String name;

    @StringField(lookup=true, lookupTable=Manufacturer.class, lookupForeignKey="abbreviation", lookupForeignField="name", targetField="brandShadow")
    private String brand;
    private transient String brandShadow;

    @StringField(lookup=true, lookupTable=Category.class, lookupForeignKey="abbreviation", lookupForeignField="name", targetField="categoryShadow")
    private String category;
    private transient String categoryShadow;

    @StringField(lookup=true, lookupTable=Plastic.class, lookupForeignKey="id", lookupForeignField="name", targetField="plasticShadow")
    private String plastic;
    private transient String plasticShadow;

    @IntField(minvalue = 140, maxvalue = 200, useValidation = true)
    private int weight;
    private int speed; /* 1 - 15*/
    private int glide; /* 1 - 7 */
    private int turn; /* -5 - +2*/
    private int fade; /* 0 - 5 */
    private String color;
    private boolean brandNew = true;

    public Disc(String name, String brand, int weight, String color, String plastic, int speed, int turn, int glide, int fade, String category) {
        this.name = name;
        this.brand = brand;
        this.weight = weight;
        this.color = color;
        this.plastic = plastic;
        this.speed = speed;
        this.turn = turn;
        this.glide = glide;
        this.fade = fade;
        this.category = category;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getPlastic() { return plastic; }
    public void setPlastic(String plastic) { this.plastic = plastic; }
    public String getBrandShadow() { return brandShadow; }
    public void setBrandShadow(String brandShadow) { this.brandShadow = brandShadow; }
    public int getSpeed() { return speed; }
    public void setSpeed(int speed) { this.speed = speed; }
    public int getGlide() { return glide; }
    public void setGlide(int glide) { this.glide = glide; }
    public int getTurn() { return turn; }
    public void setTurn(int turn) { this.turn = turn; }
    public int getFade() { return fade; }
    public void setFade(int fade) { this.fade = fade; }
    public boolean isBrandNew() { return brandNew; }
    public void setBrandNew(boolean brandNew) { this.brandNew = brandNew; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public void setCategoryShadow(String categoryShadow) { this.categoryShadow = categoryShadow; }
    private String getCategoryShadow() { return categoryShadow; }
    private String getPlasticShadow() { return plasticShadow; }
    public void setPlasticShadow(String plasticShadow) { this.plasticShadow = plasticShadow; }

    @Override
    public String toString() {
        return String.format("%s, manufactured by: '%s' (%s), weight: %d, color: %s, Plastic: '%s' (%s), fade: %d, new: %b, Category: %s",
                getName(), getBrandShadow(), getBrand(), getWeight(), getColor(), getPlasticShadow(), getPlastic(), getFade(), isBrandNew(), getCategoryShadow());
    } // toString
} // class Disc
