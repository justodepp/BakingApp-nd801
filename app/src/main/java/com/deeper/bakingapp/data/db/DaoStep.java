package com.deeper.bakingapp.data.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import com.deeper.bakingapp.data.model.Step;

import java.util.List;

@Dao
public interface DaoStep {
    @Insert
    long addStep(Step entry);

    @Insert
    void addSteps(List<Step> entries);

    @Query("DELETE FROM steps WHERE _id = :id")
    int deleteStep(int id);

    @Query("DELETE FROM steps WHERE recipe_id = :recipe_id")
    int deleteRecipeSteps(int recipe_id);

    @Query("SELECT * FROM steps WHERE recipe_id = :recipe_id")
    List<Step> getRecipeSteps(int recipe_id);

    @Query("SELECT * FROM steps WHERE recipe_id = :recipe_id")
    Cursor getCursorRecipeSteps(int recipe_id);

    @Query("SELECT * FROM steps WHERE _id = :id")
    Step getStep(int id);

    @Query("SELECT COUNT(_id) FROM steps WHERE recipe_id = :recipe_id")
    int getRecipeStepsCount(int recipe_id);
}
