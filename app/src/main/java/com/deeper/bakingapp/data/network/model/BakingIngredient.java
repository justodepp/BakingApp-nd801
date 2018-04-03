package com.deeper.bakingapp.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Gianni on 30/03/18.
 */

public class BakingIngredient implements Parcelable {
    @SerializedName("quantity")
    @Expose
    private Double quantity;
    @SerializedName("measure")
    @Expose
    private String measure;
    @SerializedName("ingredient")
    @Expose
    private String ingredient;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.quantity);
        dest.writeString(this.measure);
        dest.writeString(this.ingredient);
    }

    public BakingIngredient() {
    }

    protected BakingIngredient(Parcel in) {
        this.quantity = (Double) in.readValue(Integer.class.getClassLoader());
        this.measure = in.readString();
        this.ingredient = in.readString();
    }

    public static final Parcelable.Creator<BakingIngredient> CREATOR = new Parcelable.Creator<BakingIngredient>() {
        @Override
        public BakingIngredient createFromParcel(Parcel source) {
            return new BakingIngredient(source);
        }

        @Override
        public BakingIngredient[] newArray(int size) {
            return new BakingIngredient[size];
        }
    };
}
