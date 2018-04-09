package com.deeper.bakingapp.data.db;

import android.app.Application;

import com.deeper.bakingapp.data.model.Recipe;

import java.util.List;

public class BakingRepository {
    private DaoRecipe mDaoRecipe;
    private List<Recipe> mAllRecipes;

    private DaoIngredient mDaoIngredient;


    private DaoStep mDaoStep;


    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    BakingRepository(Application application) {
        BakingRoomDatabase db = BakingRoomDatabase.getDatabase(application);
        mDaoRecipe = db.daoRecipe();
        mDaoIngredient = db.daoIngredient();
        mDaoStep = db.daoStep();
        mAllRecipes = mDaoRecipe.getRecipes();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    List<Recipe> getAllRecipes() {
        return mAllRecipes;
    }
}
