package com.example.demo.repository;


import com.example.demo.entity.*;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DishRepository {
    private DataSource dataSource;

    public DishRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Dish> findAll(){
        List<Dish> dishes = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("""
                select id, name, dish_type, price from dish;
                """);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Dish dish = new Dish();
                dish.setId(rs.getInt("id"));
                dish.setName(rs.getString("name"));
                dish.setDishType(DishTypeEnum.valueOf(rs.getString("dish_type")));
                dish.setPrice(rs.getDouble("price"));

                dish.setDishIngredients(findDishIngredientsByDishId(dish.getId()));
                dishes.add(dish);
            }
            return dishes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dish findById(Integer id){
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("""
                    select id, name, dish_type, price from dish where id = ?;
                    """);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Dish dish = new Dish();
                dish.setId(rs.getInt(id));
                dish.setName(rs.getString("name"));
                dish.setDishType(DishTypeEnum.valueOf(rs.getString("dish_type")));
                dish.setPrice(rs.getDouble("price"));

                return dish;
            }
            throw new RuntimeException("Dish.id = "+id+" not found");
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public List<DishIngredient> findDishIngredientsByDishId(Integer dishId) {
        List<DishIngredient> dishIngredients = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("""
                SELECT dishingredient.id_ingredient as id, ingredient.name as name,
                       ingredient.price as price, dishingredient.id as id_dishingredient,
                        dishingredient.quantity_required as quantity_required,
                       dishingredient.unit as unit, ingredient.category as category
                FROM dishingredient
                         JOIN ingredient ON dishingredient.id_ingredient = ingredient.id
                WHERE id_dish = ?;
                """);
            ps.setInt(1, dishId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DishIngredient dishIngredient = new DishIngredient();

                Ingredient ingredient = new Ingredient();
                ingredient.setId(rs.getInt("id"));
                ingredient.setName(rs.getString("name"));
                ingredient.setPrice(rs.getDouble("price"));
                String category = rs.getString("category");
                if (category != null) {
                    ingredient.setCategory(CategoryEnum.valueOf(category));
                }
                dishIngredient.setId(rs.getInt("id_dishingredient"));
                dishIngredient.setQuantity(rs.getDouble("quantity_required"));
                dishIngredient.setUnit(Unit.valueOf(rs.getString("unit")));
                dishIngredient.setIngredient(ingredient);
                dishIngredients.add(dishIngredient);
            }
            return dishIngredients;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dish updateDishIngredients(Integer dishId, List<Ingredient> ingredients) {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            PreparedStatement deletePs = conn.prepareStatement(
                    "DELETE FROM dishingredient WHERE id_dish = ?");
            deletePs.setInt(1, dishId);
            deletePs.executeUpdate();

            String insertSql = """
                INSERT INTO dishingredient (id_dish, id_ingredient, quantity_required, unit)
                SELECT ?, i.id, 1, 'KG'
                FROM ingredient i WHERE i.id = ?
                """;
            PreparedStatement insertPs = conn.prepareStatement(insertSql);
            for (Ingredient ingredient : ingredients) {
                insertPs.setInt(1, dishId);
                insertPs.setInt(2, ingredient.getId());
                insertPs.addBatch();
            }
            insertPs.executeBatch();
            conn.commit();

            return findById(dishId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
