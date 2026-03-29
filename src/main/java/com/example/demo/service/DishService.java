package com.example.demo.service;


import com.example.demo.entity.Dish;
import com.example.demo.entity.Ingredient;
import com.example.demo.repository.DishRepository;
import org.springframework.stereotype.Service;

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
}
