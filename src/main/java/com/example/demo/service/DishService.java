package com.example.demo.service;


import com.example.demo.entity.Dish;
import com.example.demo.entity.DishCreateRequest;
import com.example.demo.entity.Ingredient;
import com.example.demo.repository.DishRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DishService {

    public DishRepository dishRepository;

    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public List<Dish> findAllDishes(){
        return dishRepository.findAll();
    }

    public Dish updateDishIngredients(Integer idDish, List<Ingredient> ingredients){
        dishRepository.findById(idDish);
        return dishRepository.updateDishIngredients(idDish, ingredients);
    }

    public List<Dish> createDishes(List<DishCreateRequest> requests) throws SQLException {
        List<Dish> createdDishes = new ArrayList<>();
        for (DishCreateRequest req : requests) {
            if (dishRepository.existsByName(req.getName())) {
                throw new IllegalArgumentException("Dish.name=" + req.getName() + " already exists");
            }

            Dish dish = new Dish();
            dish.setName(req.getName());
            dish.setDishType(req.getDishType());
            dish.setPrice(req.getPrice());

            Dish saved = dishRepository.save(dish);
            createdDishes.add(saved);
        }
        return createdDishes;
    }

    public List<Dish> findFiltered(Double priceUnder, Double priceOver, String name) {
        return dishRepository.findFiltered(priceUnder, priceOver, name);
    }
}
