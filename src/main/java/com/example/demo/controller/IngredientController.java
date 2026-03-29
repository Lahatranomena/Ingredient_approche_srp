package com.example.demo.controller;


import com.example.demo.entity.Ingredient;
import com.example.demo.entity.StockValue;
import com.example.demo.entity.Unit;
import com.example.demo.service.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService){
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public ResponseEntity<List<Ingredient>> findAll(){
        return ResponseEntity.ok(ingredientService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id){
        try {
            return ResponseEntity.ok(ingredientService.findById(id));
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ingredient.id = "+id+" is not found");
        }
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<?> getStockValue(
            @PathVariable Integer id,
            @RequestParam(required = false) String at,
            @RequestParam(required = false) String unit
    ) {
        if (at == null || unit == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Either mandatory query parameter `at` or `unit` is not provided.");
        }

        try {
            Instant t = Instant.parse(at);
            Unit unitEnum = Unit.valueOf(unit.toUpperCase());
            StockValue stockValue = ingredientService.getStockValue(id, t, unitEnum);
            return ResponseEntity.ok(stockValue);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
