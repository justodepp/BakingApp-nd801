package com.deeper.bakingapp.data.model;

/**
 * Created by Gianni on 30/03/18.
 */

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.deeper.bakingapp.data.db.contentprovider.RecipeContract;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.deeper.bakingapp.data.db.contentprovider.RecipeContract.RecipeEntry.COLUMN_ID;
import static com.deeper.bakingapp.data.db.contentprovider.RecipeContract.RecipeEntry.COLUMN_IMAGE;
import static com.deeper.bakingapp.data.db.contentprovider.RecipeContract.RecipeEntry.COLUMN_NAME;
import static com.deeper.bakingapp.data.db.contentprovider.RecipeContract.RecipeEntry.COLUMN_SERVINGS;
import static com.deeper.bakingapp.data.db.contentprovider.RecipeContract.RecipeEntry.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public class Recipe implements Parcelable {

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String INGREDIENTS = "ingredients";
    public static final String STEPS = "steps";
    public static final String SERVINGS = "servings";
    public static final String IMAGE = "image";
    public static final String FAVOURITE = "favourite";

    @SerializedName(ID)
    @Expose
    @PrimaryKey
    @ColumnInfo(index = true, name = COLUMN_ID)
    private Integer id;

    @SerializedName(NAME)
    @Expose
    @ColumnInfo(index = true, name = COLUMN_NAME)
    private String name;

    @SerializedName(INGREDIENTS)
    @Expose
    @Ignore
    private List<Ingredient> ingredients = null;

    @SerializedName(STEPS)
    @Expose
    @Ignore
    private List<Step> steps = null;

    @SerializedName(SERVINGS)
    @Expose
    @ColumnInfo(name = COLUMN_SERVINGS)
    private Integer servings;

    @SerializedName(IMAGE)
    @Expose
    @ColumnInfo(name = COLUMN_IMAGE)
    private String image;

    @SerializedName(FAVOURITE)
    @Ignore
    private boolean favourite;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean getFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    @Override
    public String toString() {
        return getName();
    }

    public boolean displayEquals(Object obj) {
        if (! (obj instanceof Recipe)) return false;

        Recipe other = (Recipe) obj;
        return this.getName().equals(other.getName());
    }

    public String printIngredients() {
        if (getIngredients() == null) return null;

        StringBuilder result = new StringBuilder();
        for (Ingredient ingredient : getIngredients())
            result.append(ingredient.getIngredient()).append(", ");
        result.replace(result.length() - 3, result.length() -1, "");

        return result.toString();
    }

    public Recipe() {
    }

    public Recipe(int id, String name, int servings, String image) {
        setId(id);
        setName(name);
        setIngredients(new ArrayList<Ingredient>());
        setSteps(new ArrayList<Step>());
        setServings(servings);
        setImage(image);
    }

    public Recipe(int id, String name, List<Ingredient> ingredients, List<Step> steps,
                  int servings, String image) {
        setId(id);
        setName(name);
        setIngredients(ingredients);
        setSteps(steps);
        setServings(servings);
        setImage(image);
    }

    /**
     * from com.example.android.contentprovidersample
     *
     * Create a new {@link Recipe} from the specified {@link ContentValues}.
     *
     * @param values A {@link ContentValues}.
     * @return A newly created {@link Recipe} instance.
     */
    public static Recipe fromContentValues(ContentValues values) {
        if (values == null) return null;

        final Recipe recipe = new Recipe();

        if (values.containsKey(COLUMN_ID))
            recipe.setId(values.getAsInteger(COLUMN_ID));
        if (values.containsKey(COLUMN_NAME))
            recipe.setName(values.getAsString(COLUMN_NAME));
        if (values.containsKey(COLUMN_SERVINGS))
            recipe.setServings(values.getAsInteger(COLUMN_SERVINGS));
        if (values.containsKey(COLUMN_IMAGE))
            recipe.setImage(values.getAsString(COLUMN_IMAGE));

        return recipe;
    }

    /**
     * from com.example.android.contentprovidersample
     *
     * Create a new {@link Recipe} from the specified {@link Cursor}.
     *
     * @param cursor A {@link Cursor}.
     * @return A newly created {@link Recipe} instance.
     */
    public static Recipe fromCursor(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_ID);
        int nameIndex = cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_NAME);
        int servingsIndex = cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_SERVINGS);
        int imageIndex = cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_IMAGE);

        int recipeId = cursor.getInt(idIndex);
        String recipeName = cursor.getString(nameIndex);
        int recipeServings = cursor.getInt(servingsIndex);
        String recipeImage = cursor.getString(imageIndex);

        return new Recipe(recipeId, recipeName, recipeServings, recipeImage);
    }

    public static Recipe fromJsonString(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<Recipe>() {}.getType();
        return gson.fromJson(json, type);
    }

    public String toJsonString() {
        Gson gson = new Gson();
        Type type = new TypeToken<Recipe>() {}.getType();
        return gson.toJson(this, type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeTypedList(this.ingredients);
        dest.writeTypedList(this.steps);
        dest.writeValue(this.servings);
        dest.writeString(this.image);
        dest.writeByte(this.favourite ? (byte) 1 : (byte) 0);
    }

    protected Recipe(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        this.steps = in.createTypedArrayList(Step.CREATOR);
        this.servings = (Integer) in.readValue(Integer.class.getClassLoader());
        this.image = in.readString();
        this.favourite = in.readByte() != 0;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
