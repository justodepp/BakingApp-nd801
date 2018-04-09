package com.deeper.bakingapp.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.deeper.bakingapp.data.model.Ingredient;
import com.deeper.bakingapp.data.model.Recipe;
import com.deeper.bakingapp.data.model.Step;

@Database(entities = {Ingredient.class, Recipe.class, Step.class},
        version = 1, exportSchema = false)
public abstract class BakingRoomDatabase extends RoomDatabase {

    public static final String NAME = "baking_data";

    private static BakingRoomDatabase INSTANCE;

    static BakingRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BakingRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BakingRoomDatabase.class, NAME)
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this codelab.
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract DaoIngredient daoIngredient();
    public abstract DaoStep daoStep();
    public abstract DaoRecipe daoRecipe();
}

