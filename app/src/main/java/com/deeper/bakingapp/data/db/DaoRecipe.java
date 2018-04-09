package com.deeper.bakingapp.data.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import com.deeper.bakingapp.data.model.Recipe;

import java.util.List;

@Dao
public interface DaoRecipe {
    @Insert
    long addRecipe(Recipe entry);

    @Insert
    void addRecipes(List<Recipe> entries);

    @Query("DELETE FROM recipes WHERE id = :id")
    int deleteRecipe(int id);

    @Query("SELECT * FROM recipes")
    List<Recipe> getRecipes();

    @Query("SELECT * FROM recipes")
    Cursor getCursorRecipes();

    @Query("SELECT * FROM recipes WHERE id = :id")
    Recipe getRecipe(int id);

    @Query("SELECT * FROM recipes WHERE id = :id")
    Cursor getCursorRecipe(int id);

    @Query("SELECT COUNT(id) FROM recipes")
    int getRecipesCount();
}
