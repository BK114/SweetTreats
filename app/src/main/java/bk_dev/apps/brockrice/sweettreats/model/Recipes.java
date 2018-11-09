package bk_dev.apps.brockrice.sweettreats.model;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brockrice on 12/5/17.
 */

public class Recipes implements Parcelable {
        /*
         * Retrieve data from JSON returned by baking app json file
         * Ensure field names are the same as those in The baking json file
         * we can use our own names within program.
         * Serialize each JSON parameter and match with Recipe variables
         */

    private int mRecipeId;
    public String mServings;
    public String mRecipeName;
    private String mImage;
    private List<RecipeSteps> steps;
    private List<Ingredients> ingredients;

    public Recipes() {

    }

    public Recipes(long id, String recipeName, String servings, String image,
                   List<RecipeSteps> steps, List<Ingredients> ingredients) {
    }

    public int getRecipeId() {
        return mRecipeId;
    }

    public void setRecipeId(int recipeId) {
        this.mRecipeId = recipeId;
    }

    public String getServings() {
        return mServings;
    }

    public void setServings(String servings) {
        this.mServings = servings;
    }

    public String getRecipeName() {
        return mRecipeName;
    }

    public void setRecipeName(String recipeName) {
        this.mRecipeName = recipeName;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        this.mImage = image;
    }


    public List<RecipeSteps> getSteps() {
        return steps;
    }

    public void setSteps(List<RecipeSteps> steps) {
        this.steps = steps;
    }

    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    public Recipes(Parcel in) {
        mRecipeId = in.readInt();
        mServings = in.readString();
        mRecipeName = in.readString();
        mImage = in.readString();
        if (in.readByte() == 0x01) {
            steps = new ArrayList<RecipeSteps>();
            in.readList(steps, RecipeSteps.class.getClassLoader());
        } else {
            steps = null;
        }
        if (in.readByte() == 0x01) {
            ingredients = new ArrayList<Ingredients>();
            in.readList(ingredients, Ingredients.class.getClassLoader());
        } else {
            ingredients = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mRecipeId);
        dest.writeString(mServings);
        dest.writeString(mRecipeName);
        dest.writeString(mImage);
        if (steps == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(steps);
        }
        if (ingredients == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(ingredients);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Recipes> CREATOR = new Parcelable.Creator<Recipes>() {
        @Override
        public Recipes createFromParcel(Parcel in) {
            return new Recipes(in);
        }

        @Override
        public Recipes[] newArray(int size) {
            return new Recipes[size];
        }
    };

    public static class RecipeSteps implements Parcelable {

        int stepsId;
        String shortDescription;
        String description;
        String videoUrl;
        String thumbnailPath;

        public RecipeSteps() {

        }

        public RecipeSteps(int stepsId, String shortDescription, String description,
                           String videoUrl, String thumbNailUrl) {
        }

        public int getStepsId() {
            return stepsId;
        }

        public void setStepsId(int stepsId) {
            this.stepsId = stepsId;
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

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getThumbnailPath() {
            return thumbnailPath;
        }

        public void setThumbnailPath(String thumbnailPath) {
            this.thumbnailPath = thumbnailPath;
        }

        protected RecipeSteps(Parcel in) {
            stepsId = in.readInt();
            shortDescription = in.readString();
            description = in.readString();
            videoUrl = in.readString();
            thumbnailPath = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(stepsId);
            dest.writeString(shortDescription);
            dest.writeString(description);
            dest.writeString(videoUrl);
            dest.writeString(thumbnailPath);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<RecipeSteps> CREATOR = new Parcelable.Creator<RecipeSteps>() {
            @Override
            public RecipeSteps createFromParcel(Parcel in) {
                return new RecipeSteps(in);
            }

            @Override
            public RecipeSteps[] newArray(int size) {
                return new RecipeSteps[size];
            }
        };
    }

    public static class Ingredients implements Parcelable {

        private double mQuantity;
        private String mMeasure;
        private String mIngredient;

        public Ingredients(double quantity, String measure, String ingredient) {
            this.mQuantity = quantity;
            this.mMeasure = measure;
            this.mIngredient = ingredient;
        }

        public Ingredients() {

        }
        public double getmQuantity() {
            return mQuantity;
        }

        public void setmQuantity(double mQuantity) {
            this.mQuantity = mQuantity;
        }

        public String getmMeasure() {
            return mMeasure;
        }

        public void setmMeasure(String mMeasure) {
            this.mMeasure = mMeasure;
        }

        public String getmIngredient() {
            return mIngredient;
        }

        public void setmIngredient(String mIngredient) {
            this.mIngredient = mIngredient;
        }


        public Ingredients(Parcel in) {
            mQuantity = in.readDouble();
            mMeasure = in.readString();
            mIngredient = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(mQuantity);
            dest.writeString(mMeasure);
            dest.writeString(mIngredient);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Ingredients> CREATOR = new Parcelable.Creator<Ingredients>() {
            @Override
            public Ingredients createFromParcel(Parcel in) {
                return new Ingredients(in);
            }

            @Override
            public Ingredients[] newArray(int size) {
                return new Ingredients[size];
            }
        };
    }
}