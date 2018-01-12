package ua.glushko.model.entity.tmp;

import ua.glushko.model.dao.Identified;

public class RepairDevice implements Identified<Integer>{
    private int id;
    private String name;
    private String description;
    private int repairService;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRepairService() {
        return repairService;
    }

    public void setRepairService(int repairService) {
        this.repairService = repairService;
    }

    @Override
    public String toString() {
        return "RepairDevice{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", repairService=" + repairService +
                '}';
    }
}
