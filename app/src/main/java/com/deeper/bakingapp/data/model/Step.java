package com.deeper.bakingapp.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static android.arch.persistence.room.ForeignKey.CASCADE;
import static com.deeper.bakingapp.data.db.contentprovider.StepContract.StepEntry.COLUMN_DESCRIPTION;
import static com.deeper.bakingapp.data.db.contentprovider.StepContract.StepEntry.COLUMN_ID;
import static com.deeper.bakingapp.data.db.contentprovider.StepContract.StepEntry.COLUMN_RECIPE_ID;
import static com.deeper.bakingapp.data.db.contentprovider.StepContract.StepEntry.COLUMN_SHORT_DESCRIPTION;
import static com.deeper.bakingapp.data.db.contentprovider.StepContract.StepEntry.COLUMN_STEP_ID;
import static com.deeper.bakingapp.data.db.contentprovider.StepContract.StepEntry.COLUMN_THUMBNAIL_URL;
import static com.deeper.bakingapp.data.db.contentprovider.StepContract.StepEntry.COLUMN_VIDEO_URL;
import static com.deeper.bakingapp.data.db.contentprovider.StepContract.StepEntry.TABLE_NAME;

/**
 * Created by Gianni on 30/03/18.
 */

@Entity(tableName = TABLE_NAME,
        foreignKeys = @ForeignKey(entity = Recipe.class,
                parentColumns = "id",
                childColumns = COLUMN_RECIPE_ID,
                onDelete = CASCADE))
public class Step implements Parcelable {

    public static final String STEP_ID = "id";
    public static final String RECIPE_ID = "recipe_id";
    public static final String SHORT_DESCRIPTION = "shortDescription";
    public static final String DESCRIPTION = "description";
    public static final String VIDEO_URL = "videoURL";
    public static final String THUMBNAIL_URL = "thumbnailURL";

    @SerializedName(COLUMN_ID)
    @PrimaryKey(autoGenerate = true) @ColumnInfo(index = true, name = COLUMN_ID)
    public int id;

    @SerializedName(STEP_ID)
    @Expose
    private Integer stepId;

    @SerializedName(RECIPE_ID)
    @ColumnInfo(index = true, name = COLUMN_RECIPE_ID)
    private int recipeId;

    @SerializedName(SHORT_DESCRIPTION)
    @Expose
    private String shortDescription;

    @SerializedName(DESCRIPTION)
    @Expose
    private String description;

    @SerializedName(VIDEO_URL)
    @Expose
    private String videoURL;

    @SerializedName(THUMBNAIL_URL)
    @Expose
    private String thumbnailURL;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getStepId() {
        return stepId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public Step() {
    }

    /**
     * from com.example.android.contentprovidersample
     *
     * Create a new {@link Step} from the specified {@link ContentValues}.
     *
     * @param values A {@link ContentValues}.
     * @return A newly created {@link Step} instance.
     */
    public static Step fromContentValues(ContentValues values) {
        if (values == null) return null;

        final Step step = new Step();

        if (values.containsKey(COLUMN_ID))
            step.setId(values.getAsInteger(COLUMN_ID));
        if (values.containsKey(COLUMN_STEP_ID))
            step.setStepId(values.getAsInteger(COLUMN_STEP_ID));
        if (values.containsKey(COLUMN_RECIPE_ID))
            step.setRecipeId(values.getAsInteger(COLUMN_RECIPE_ID));
        if (values.containsKey(COLUMN_DESCRIPTION))
            step.setDescription(values.getAsString(COLUMN_DESCRIPTION));
        if (values.containsKey(COLUMN_SHORT_DESCRIPTION))
            step.setShortDescription(values.getAsString(COLUMN_SHORT_DESCRIPTION));
        if (values.containsKey(COLUMN_VIDEO_URL))
            step.setVideoURL(values.getAsString(COLUMN_VIDEO_URL));
        if (values.containsKey(COLUMN_THUMBNAIL_URL))
            step.setThumbnailURL(values.getAsString(COLUMN_THUMBNAIL_URL));

        return step;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeValue(this.stepId);
        dest.writeInt(this.recipeId);
        dest.writeString(this.shortDescription);
        dest.writeString(this.description);
        dest.writeString(this.videoURL);
        dest.writeString(this.thumbnailURL);
    }

    protected Step(Parcel in) {
        this.id = in.readInt();
        this.stepId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.recipeId = in.readInt();
        this.shortDescription = in.readString();
        this.description = in.readString();
        this.videoURL = in.readString();
        this.thumbnailURL = in.readString();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel source) {
            return new Step(source);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };
}
