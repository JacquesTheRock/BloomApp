package tech.bloomgenetics.bloomapp;

/**
 * Created by mdric on 11/21/2016.
 */

public class Trait {

    private String dr;
    private String carrier;
    private String shower;
    private String name;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDR() {
        return dr;
    }

    public void setDR(String dr) {
        this.dr = dr;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String c) {
        carrier = c;
    }

    public String getShower() {
        return shower;
    }

    public void setShower(String s) {
        shower = s;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {
        String out = "{";
        out += "\"id\": " + getId();
        out += "}";
        return out;
    }

}