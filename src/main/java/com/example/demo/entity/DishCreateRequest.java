package com.example.demo.entity;

public class DishCreateRequest {
    private String name;
    private DishTypeEnum dishType;
    private Double price;

    public DishCreateRequest() {}

    public DishCreateRequest(String name, DishTypeEnum dishType, Double price) {
        this.name = name;
        this.dishType = dishType;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DishTypeEnum getDishType() {
        return dishType;
    }

    public void setDishType(DishTypeEnum dishType) {
        this.dishType = dishType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}