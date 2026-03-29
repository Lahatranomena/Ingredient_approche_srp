package com.example.demo.service;


import com.example.demo.entity.Ingredient;
import com.example.demo.entity.StockValue;
import com.example.demo.entity.Unit;
import com.example.demo.repository.IngredientRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class IngredientService {

    private IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<Ingredient> findAll(){
        return ingredientRepository.findAll();
    }

    public Ingredient findById(Integer id){
        return ingredientRepository.findById(id);
    }

    public StockValue getStockValue(Integer id, Instant instant, Unit unit){
        ingredientRepository.findById(id);
        return ingredientRepository.getStockValue(instant, id);
    }
}
