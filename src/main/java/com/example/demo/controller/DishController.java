package com.example.demo.controller;


import com.example.demo.entity.Dish;
import com.example.demo.entity.DishCreateRequest;
import com.example.demo.entity.Ingredient;
import com.example.demo.repository.DishRepository;
import com.example.demo.service.DishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @PostMapping
    public ResponseEntity<?> createDishes(@RequestBody List<DishCreateRequest> requests) {
        try {
            List<Dish> createdDishes = dishService.createDishes(requests);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDishes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Dish>> getDishes(
            @RequestParam(required = false) Double priceUnder,
            @RequestParam(required = false) Double priceOver,
            @RequestParam(required = false) String name
    ) {
        List<Dish> dishes = dishService.findFiltered(priceUnder, priceOver, name);
        return ResponseEntity.ok(dishes);
    }
}
