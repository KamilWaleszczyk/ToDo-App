package com.example.todo;

public class Grupa {

    private String name;
    private Integer id;

    public Grupa(){}
    public Grupa(String name, Integer id) {
        this.name = name;
        this.id = id;
    }
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
}
