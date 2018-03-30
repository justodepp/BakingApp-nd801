package com.deeper.bakingapp.data.network.model;

/**
 * Created by Gianni on 30/03/18.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BakingResponse implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ingredients")
    @Expose
    private List<BakingIngredient> ingredients = null;
    @SerializedName("steps")
    @Expose
    private List<BakingStep> steps = null;
    @SerializedName("servings")
    @Expose
    private Integer servings;
    @SerializedName("image")
    @Expose
    private String image;

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

    public List<BakingIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<BakingIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<BakingStep> getSteps() {
        return steps;
    }

    public void setSteps(List<BakingStep> steps) {
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
    }

    public BakingResponse() {
    }

    protected BakingResponse(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.ingredients = in.createTypedArrayList(BakingIngredient.CREATOR);
        this.steps = in.createTypedArrayList(BakingStep.CREATOR);
        this.servings = (Integer) in.readValue(Integer.class.getClassLoader());
        this.image = in.readString();
    }

    public static final Parcelable.Creator<BakingResponse> CREATOR = new Parcelable.Creator<BakingResponse>() {
        @Override
        public BakingResponse createFromParcel(Parcel source) {
            return new BakingResponse(source);
        }

        @Override
        public BakingResponse[] newArray(int size) {
            return new BakingResponse[size];
        }
    };
}
