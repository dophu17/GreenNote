package net.dauhuthom.greennote;

/**
 * Created by phu on 11/7/2016.
 */

public class Note {
    public int id, service_id;
    public double price;
    public String date, description;

    public Note(int id, int service_id, double price, String date, String description) {
        this.id = id;
        this.service_id = service_id;
        this.price = price;
        this.date = date;
        this.description = description;
    }
}
