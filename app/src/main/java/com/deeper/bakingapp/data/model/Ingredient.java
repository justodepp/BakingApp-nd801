package com.deeper.bakingapp.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.deeper.bakingapp.data.db.contentprovider.IngredientContract;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static com.deeper.bakingapp.data.db.contentprovider.IngredientContract.IngredientEntry.COLUMN_ID;
import static com.deeper.bakingapp.data.db.contentprovider.IngredientContract.IngredientEntry.COLUMN_INGREDIENT;
import static com.deeper.bakingapp.data.db.contentprovider.IngredientContract.IngredientEntry.COLUMN_MEASURE;
import static com.deeper.bakingapp.data.db.contentprovider.IngredientContract.IngredientEntry.COLUMN_QUANTITY;
import static com.deeper.bakingapp.data.db.contentprovider.IngredientContract.IngredientEntry.COLUMN_RECIPE_ID;
import static com.deeper.bakingapp.data.db.contentprovider.IngredientContract.IngredientEntry.TABLE_NAME;
import static com.deeper.bakingapp.data.model.Ingredient.ID;
import static com.deeper.bakingapp.data.model.Ingredient.RECIPE_ID;

/**
 * Created by Gianni on 30/03/18.
 */

@Entity(tableName = TABLE_NAME,
        foreignKeys = @ForeignKey(entity = Recipe.class,
                parentColumns = ID,
                childColumns = RECIPE_ID,
                onDelete = ForeignKey.CASCADE))
public class Ingredient implements Parcelable {

    public static final String ID = "id";
    public static final String RECIPE_ID = "recipe_id";
    public static final String QUANTITY = "quantity";
    public static final String MEASURE = "measure";
    public static final String INGREDIENT = "ingredient";

    @SerializedName(ID)
    @PrimaryKey(autoGenerate = true) @ColumnInfo(index = true, name = COLUMN_ID)
    public int id;

    @SerializedName(RECIPE_ID)
    @ColumnInfo(index = true, name = RECIPE_ID)
    private int recipeId;

    @SerializedName(QUANTITY)
    @Expose
    @ColumnInfo(name = COLUMN_QUANTITY)
    private Double quantity;

    @SerializedName(MEASURE)
    @Expose
    @ColumnInfo(name = COLUMN_MEASURE)
    private String measure;

    @SerializedName(INGREDIENT)
    @Expose
    @ColumnInfo(name = COLUMN_INGREDIENT)
    private String ingredient;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public Ingredient() {
    }

    public Ingredient(int id, int recipeId, double quantity, String measure, String ingredient) {
        setId(id);
        setRecipeId(recipeId);
        setQuantity(quantity);
        setMeasure(measure);
        setIngredient(ingredient);
    }

    /**
     * from com.example.android.contentprovidersample
     *
     * Create a new {@link Ingredient} from the specified {@link ContentValues}.
     *
     * @param values A {@link ContentValues}.
     * @return A newly created {@link Ingredient} instance.
     */
    public static Ingredient fromContentValues(ContentValues values) {
        if (values == null) return null;

        final Ingredient ingredient = new Ingredient();

        if (values.containsKey(COLUMN_ID))
            ingredient.setId(values.getAsInteger(COLUMN_ID));
        if (values.containsKey(COLUMN_RECIPE_ID))
            ingredient.setRecipeId(values.getAsInteger(COLUMN_RECIPE_ID));
        if (values.containsKey(COLUMN_QUANTITY))
            ingredient.setQuantity(values.getAsDouble(COLUMN_QUANTITY));
        if (values.containsKey(COLUMN_MEASURE))
            ingredient.setMeasure(values.getAsString(COLUMN_MEASURE));
        if (values.containsKey(COLUMN_INGREDIENT))
            ingredient.setIngredient(values.getAsString(COLUMN_INGREDIENT));

        return ingredient;
    }

    /**
     * from com.example.android.contentprovidersample
     *
     * Create a new {@link Ingredient} from the specified {@link Cursor}.
     *
     * @param cursor A {@link Cursor}.
     * @return A newly created {@link Ingredient} instance.
     */
    public static Ingredient fromCursor(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(IngredientContract.IngredientEntry.COLUMN_ID);
        int recipeIdIndex = cursor.getColumnIndex(IngredientContract.IngredientEntry.COLUMN_RECIPE_ID);
        int quantityIndex = cursor.getColumnIndex(IngredientContract.IngredientEntry.COLUMN_QUANTITY);
        int measureIndex = cursor.getColumnIndex(IngredientContract.IngredientEntry.COLUMN_MEASURE);
        int ingredientIndex = cursor.getColumnIndex(IngredientContract.IngredientEntry.COLUMN_INGREDIENT);

        int ingredientId = cursor.getInt(idIndex);
        int ingredientRecipeId = cursor.getInt(recipeIdIndex);
        float ingredientQuantity = cursor.getFloat(quantityIndex);
        String ingredientMeasure = cursor.getString(measureIndex);
        String ingredient = cursor.getString(ingredientIndex);

        return new Ingredient(ingredientId, ingredientRecipeId, ingredientQuantity, ingredientMeasure, ingredient);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.recipeId);
        dest.writeValue(this.quantity);
        dest.writeString(this.measure);
        dest.writeString(this.ingredient);
    }

    protected Ingredient(Parcel in) {
        this.id = in.readInt();
        this.recipeId = in.readInt();
        this.quantity = (Double) in.readValue(Double.class.getClassLoader());
        this.measure = in.readString();
        this.ingredient = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel source) {
            return new Ingredient(source);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
