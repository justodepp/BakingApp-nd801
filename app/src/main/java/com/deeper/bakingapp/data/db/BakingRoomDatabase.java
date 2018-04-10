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

    private static final String NAME = "baking_data";

    /** The only instance */
    private static BakingRoomDatabase INSTANCE;

    /**
     * @return The DAO for the table.
     */
    @SuppressWarnings("WeakerAccess")
    public abstract DaoIngredient daoIngredient();
    public abstract DaoStep daoStep();
    public abstract DaoRecipe daoRecipe();

    /**
     * Gets the singleton instance of SampleDatabase.
     *
     * @param context The context.
     * @return The singleton instance of SampleDatabase.
     */
    public static synchronized BakingRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    BakingRoomDatabase.class, NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}

