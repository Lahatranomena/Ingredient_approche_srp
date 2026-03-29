package com.example.demo.controller;


import com.example.demo.entity.Dish;
import com.example.demo.entity.Ingredient;
import com.example.demo.service.DishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dishes")
public class DishController {

    private DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping
    public ResponseEntity<List<Dish>> getAllDishes(){
        return ResponseEntity.ok(dishService.findAllDishes());
    }

    @PutMapping("/{id}/ingredients")
    public ResponseEntity<?> updateDishIngredients(@PathVariable Integer id,
                                                   @RequestBody(required = false) List<Ingredient> ingredients){
        if(ingredients == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Request body is empty");
        }

        try {
            Dish updatedDish = dishService.updateDishIngredients(id, ingredients);
            return ResponseEntity.ok(updatedDish);
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
