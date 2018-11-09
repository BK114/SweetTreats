package bk_dev.apps.brockrice.sweettreats.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brockrice.sweettreats.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import bk_dev.apps.brockrice.sweettreats.activities.BakingStepsActivity;
import bk_dev.apps.brockrice.sweettreats.activities.MainActivity;
import bk_dev.apps.brockrice.sweettreats.model.Recipes;
import bk_dev.apps.brockrice.sweettreats.widget.RecipeWidgetProvider;


/**
 * Created by brockrice on 12/5/17.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = RecipeAdapter.class.getSimpleName();
    private static final String RECIPE_KEY = "widget_recipes";
    private Context mContext;
    private List<Recipes> mRecipes;
    private LayoutInflater inflater;


    /**
     * this adapter will take Recipe objects and set place the recipe objects into a view holder
     * a ClickListener will be set with @Overridden OnClick method in order to get the position of
     * the recipe card that was clicked and pass intent Extras into the proper fields and then to
     * the RecipeDetailedActivity Class.
     */
    public RecipeAdapter(Context context, List<Recipes> recipe) {
        this.mContext = context;
        this.mRecipes = recipe;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = inflater.inflate(R.layout.recipe_card_view, parent, false);
        return new RecipeViewHolder(rootView);
    }

    /* onBinViewHolder will bind the RecipeViewHolder object to the correct postion within the view.
       * It will also require a call to the provided json file in order to retrieve the correct information
       * for each recipes.
       * Picasso will be used to Fetch images and load them into Views
       */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

          /* Set string to IMAGE_URL_BASE_PATH and get the poster position for each movie object.
         * Pass movie object position along with posterPath request to Picasso along with current context.
         * Load the proper image_url position into the card image_view
         * This method is called for each ViewHolder to bind it to the adapter.
         * This is where we will pass our data to our ViewHolder.
         * a backup image will be a placholder in case no image is available
         */
        // Get current position of item in recyclerview to bind data and assign values from list
        RecipeViewHolder recipeViewHolder = (RecipeViewHolder) holder;
        //Deterimine recipeID and place recipeImage into correct view
        Recipes currentRecipe = mRecipes.get(position);
        //if the current recipe has an image, load appropriate image into imageView
        if (!TextUtils.isEmpty(currentRecipe.getImage())) {
            Picasso.with(mContext).load(currentRecipe.getImage())
                    .placeholder(R.drawable.icecream)
                    .error(R.drawable.icecream)
                    .into(recipeViewHolder.imageView);
        }
        //Otherwise set a drawable pic in its place
        else {
            switch (currentRecipe.getRecipeId()) {

                case 1:
                    Picasso.with(mContext).load(R.drawable.nutella_pie)
                            .placeholder(R.drawable.icecream)
                            .error(R.drawable.icecream)
                            .into(recipeViewHolder.imageView);
                    break;
                case 2:
                    Picasso.with(mContext).load(R.drawable.brownies)
                            .placeholder(R.drawable.icecream)
                            .error(R.drawable.icecream)
                            .into(recipeViewHolder.imageView);
                    break;
                case 3:
                    Picasso.with(mContext).load(R.drawable.yellow_cake)
                            .placeholder(R.drawable.icecream)
                            .error(R.drawable.icecream)
                            .into(recipeViewHolder.imageView);
                    break;
                case 4:
                    Picasso.with(mContext).load(R.drawable.strawberrycheesecake)
                            .placeholder(R.drawable.icecream)
                            .error(R.drawable.icecream)
                            .into(recipeViewHolder.imageView);
                    break;
                default:
                    Picasso.with(mContext).load(R.drawable.icecream)
                            .placeholder(R.drawable.icecream)
                            .error(R.drawable.icecream)
                            .into(recipeViewHolder.imageView);
                    break;
            }
        }


        recipeViewHolder.recipeName.setText(currentRecipe.mRecipeName);
        recipeViewHolder.servingSize.setText(currentRecipe.mServings);
        Log.d(TAG, "onBindViewHolder: " + recipeViewHolder);

    }

    @Override
    public int getItemCount() {

        return mRecipes.size();
    }

    @Override
    public int getItemViewType(final int position) {
        return position;
    }


    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imageView;
        public final TextView recipeName;
        public final TextView servingSize;

        public RecipeViewHolder(final View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.recipe_image_view);
            recipeName = itemView.findViewById(R.id.recipe_name);
            servingSize = itemView.findViewById(R.id.text_view_serving_quantity);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Recipes clickedRecipe = mRecipes.get(position);

                        Bundle stepsData = new Bundle();
                        stepsData.putParcelable("recipe", clickedRecipe);
                        Intent recipeIntent = new Intent(mContext, BakingStepsActivity.class);
                        recipeIntent.putExtras(stepsData);
                        Toast.makeText(mContext, "Clicked on " +
                                clickedRecipe.getRecipeName(), Toast.LENGTH_SHORT).show();
                        mContext.startActivity(recipeIntent);

                        //Add recipe to widget by utilizing Gson dependency and
                        //storing the JSON String for the clickedRecipe
                        Gson gson = new Gson();
                        String json = gson.toJson(clickedRecipe);
                        //SharedPreferences will allow us to store data using the Editor class with a sharedPrefs Key
                        //We will be able to retrieve the key in the onDataSetChanged method for the IngredientsListWIdgetService class
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(MainActivity.SHARED_PREFS_KEY, json).apply();
                        //create a widget for the clickedRecipe that will send a Broadcast to the widget provider
                        Intent widgetIntent = new Intent(mContext.getApplicationContext(), RecipeWidgetProvider.class);
                        widgetIntent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                        widgetIntent.putExtra(RECIPE_KEY, clickedRecipe);
                        mContext.sendBroadcast(widgetIntent);
                        Log.d(TAG, "BROADCASTED RECIPE: " + widgetIntent);

                    }
                }
            });
        }
    }
}
