package com.example.demo.repository;


import com.example.demo.entity.*;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IngredientRepository {

    private DataSource dataSource;

    public IngredientRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public Ingredient findById(int id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(
                    """
                       SELECT id, name, price, category FROM ingredient WHERE id = ?
                            """
            );

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Ingredient ingredient = new Ingredient();

                ingredient.setId(rs.getInt("id"));
                ingredient.setName(rs.getString("name"));
                ingredient.setPrice(rs.getDouble("price"));
                ingredient.setCategory(CategoryEnum.valueOf(rs.getString("category")));

                return ingredient;
            }
            throw new RuntimeException("Ingredient.id = "+id+ " not found");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Ingredient> findAll() {
        List<Ingredient> list = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("""
                        SELECT id, name, price, category FROM ingredient
                    """);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Ingredient ingredient = new Ingredient();

                ingredient.setId(rs.getInt("id"));
                ingredient.setName(rs.getString("name"));
                ingredient.setPrice(rs.getDouble("price"));
                ingredient.setCategory(CategoryEnum.valueOf(rs.getString("category")));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<StockMovement> findStockMovements(Integer id) {
        List<StockMovement> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT id, quantity, unit, type, creation_datetime FROM stock_movement WHERE id_ingredient = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                StockMovement sm = new StockMovement();
                sm.setId(rs.getInt("id"));
                sm.setType(MovementTypeEnum.valueOf(rs.getString("type")));
                sm.setCreationDatetime(rs.getTimestamp("creation_datetime").toInstant());
                StockValue sv = new StockValue();
                sv.setQuantity(rs.getDouble("quantity"));
                sv.setUnit(Unit.valueOf(rs.getString("unit")));
                sm.setValue(sv);
                list.add(sm);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public StockValue getStockValue(Instant t, Integer ingredientId) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("""
                SELECT unit,
                    SUM(CASE WHEN type = 'OUT' THEN -quantity ELSE quantity END) AS actual_quantity
                FROM stock_movement
                WHERE id_ingredient = ? AND creation_datetime <= ? AND unit = ?
                GROUP BY unit
                """);
            ps.setInt(1, ingredientId);
            ps.setTimestamp(2, new Timestamp(t.toEpochMilli()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                StockValue sv = new StockValue();
                sv.setUnit(Unit.valueOf(rs.getString("unit")));
                sv.setQuantity(rs.getDouble("actual_quantity"));
                return sv;
            }
            throw new RuntimeException("Ingredient.id=" + ingredientId + " is not found");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
