package ua.glushko.model.entity;

import java.io.Serializable;

/** Repair Services */
public class RepairService implements GenericEntity, Serializable{
    private Integer id;
    private int parent;
    private String name;
    private String nameRu;

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameRu() {
        return nameRu;
    }

    public void setNameRu(String name_ru) {
        this.nameRu = name_ru;
    }

    @Override
    public String toString() {
        return "RepairService{" +
                "parent=" + parent +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nameRu='" + nameRu + '\'' +
                '}';
    }
}
