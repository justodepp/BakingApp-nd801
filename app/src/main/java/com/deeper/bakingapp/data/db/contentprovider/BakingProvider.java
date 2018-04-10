package com.deeper.bakingapp.data.db.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.deeper.bakingapp.data.db.BakingRoomDatabase;
import com.deeper.bakingapp.data.model.Ingredient;
import com.deeper.bakingapp.data.model.Recipe;
import com.deeper.bakingapp.data.model.Step;

import java.util.ArrayList;

/**
 * some code are provided from com.example.android.contentprovidersample
 *
 * A {@link ContentProvider} based on a Room database.
 *
 * <p>Note that you don't need to implement a ContentProvider unless you want to expose the data
 * outside your process or your application already uses a ContentProvider.</p>
 */
public class BakingProvider extends ContentProvider {

    /** The authority of this content provider. */
    public static final String CONTENT_AUTHORITY = "com.deeper.bakingapp.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final int CODE_RECIPES = 100;
    private static final int CODE_RECIPE_WITH_ID = 101;

    private static final int CODE_INGREDIENTS = 200;
    private static final int CODE_INGREDIENT_WITH_ID = 201;

    private static final int CODE_STEPS = 300;
    private static final int CODE_STEP_WITH_ID = 301;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(CONTENT_AUTHORITY, RecipeContract.RecipeEntry.TABLE_NAME, CODE_RECIPES);
        matcher.addURI(CONTENT_AUTHORITY, RecipeContract.RecipeEntry.TABLE_NAME + "/#", CODE_RECIPE_WITH_ID);

        matcher.addURI(CONTENT_AUTHORITY, IngredientContract.IngredientEntry.TABLE_NAME, CODE_INGREDIENTS);
        matcher.addURI(CONTENT_AUTHORITY, IngredientContract.IngredientEntry.TABLE_NAME + "/#", CODE_INGREDIENT_WITH_ID);

        matcher.addURI(CONTENT_AUTHORITY, StepContract.StepEntry.TABLE_NAME, CODE_STEPS);
        matcher.addURI(CONTENT_AUTHORITY, StepContract.StepEntry.TABLE_NAME + "/#", CODE_STEP_WITH_ID);

        return matcher;
    }

    private BakingRoomDatabase bakingDatabase;

    @Override
    public boolean onCreate() {
        if (bakingDatabase == null) bakingDatabase = BakingRoomDatabase.getDatabase(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        final Context context = getContext();
        if (context == null) return null;

        Cursor cursor;
        String id;
        switch (sUriMatcher.match(uri)) {
            case CODE_RECIPES:
            cursor = bakingDatabase.daoRecipe().getCursorRecipes();
            cursor.setNotificationUri(context.getContentResolver(), uri);
            break;

            case CODE_INGREDIENTS:
            id = uri.getQueryParameter(IngredientContract.IngredientEntry.COLUMN_RECIPE_ID);
            cursor = bakingDatabase.daoIngredient()
                    .getCursorRecipeIngredients(Integer.valueOf(id));
            cursor.setNotificationUri(context.getContentResolver(), uri);
            break;

            case CODE_STEPS:
            id = uri.getQueryParameter(StepContract.StepEntry.COLUMN_RECIPE_ID);
            cursor = bakingDatabase.daoStep()
                    .getCursorRecipeSteps(Integer.valueOf(id));
            cursor.setNotificationUri(context.getContentResolver(), uri);
            break;

            default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final Context context = getContext();
        if (context == null) {
            return null;
        }

        long id;
        switch (sUriMatcher.match(uri)) {
            case CODE_RECIPE_WITH_ID:
                id = bakingDatabase.daoRecipe()
                        .addRecipe(Recipe.fromContentValues(contentValues));
                context.getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            case CODE_INGREDIENT_WITH_ID:
                id = bakingDatabase.daoIngredient()
                        .addIngredient(Ingredient.fromContentValues(contentValues));
                context.getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            case CODE_STEP_WITH_ID:
                id = bakingDatabase.daoStep()
                        .addStep(Step.fromContentValues(contentValues));
                context.getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] valuesArray) {
        final Context context = getContext();
        if (context == null) {
            return 0;
        }

        switch (sUriMatcher.match(uri)) {
            case CODE_INGREDIENTS:
                final ArrayList<Ingredient> ingredients = new ArrayList<>();
                for (ContentValues ingredient : valuesArray)
                    ingredients.add(Ingredient.fromContentValues(ingredient));

                bakingDatabase.daoIngredient().addIngredients(ingredients);
                return valuesArray.length;
            case CODE_STEPS:
                final ArrayList<Step> steps = new ArrayList<>();
                for (ContentValues step : valuesArray)
                    steps.add(Step.fromContentValues(step));

                bakingDatabase.daoStep().addSteps(steps);
                return valuesArray.length;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final Context context = getContext();
        if (context == null) return 0;

        int count;
        String id;
        switch (sUriMatcher.match(uri)) {
            case CODE_RECIPES:
                count = bakingDatabase.daoRecipe()
                        .deleteRecipe((int) ContentUris.parseId(uri));
                context.getContentResolver().notifyChange(uri, null);
                return count;
            case CODE_INGREDIENTS:
                id = uri.getQueryParameter(IngredientContract.IngredientEntry.COLUMN_RECIPE_ID);
                count = bakingDatabase.daoIngredient()
                        .deleteRecipeIngredients(Integer.parseInt(id));
                context.getContentResolver().notifyChange(uri, null);
                return count;
            case CODE_STEPS:
                id = uri.getQueryParameter(StepContract.StepEntry.COLUMN_RECIPE_ID);
                count = bakingDatabase.daoStep()
                        .deleteRecipeSteps(Integer.parseInt(id));
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
