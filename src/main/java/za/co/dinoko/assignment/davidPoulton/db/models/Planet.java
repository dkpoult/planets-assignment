package za.co.dinoko.assignment.davidPoulton.db.models;

import com.opencsv.bean.CsvBindByName;

import javax.persistence.*;

@Entity
@Table(name = "Planets")
public class Planet {

    @Id
    @GeneratedValue()
    private int id;

    @CsvBindByName(column = "Planet Node", required = true)
    private String node;
    @CsvBindByName(column = "Planet Name", required = true)
    private String name;

    public Planet() {

    }

    public Planet(int id, String node, String name) {
        this.id = id;
        this.node = node;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return id + " (" + this.node + "): " + name;
    }
}
