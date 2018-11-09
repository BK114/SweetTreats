package bk_dev.apps.brockrice.sweettreats.adapter;

import android.content.Context;
import android.renderscript.Sampler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.brockrice.sweettreats.R;

import java.util.Collections;
import java.util.List;

import bk_dev.apps.brockrice.sweettreats.model.Recipes;

/**
 * Created by brockrice on 12/8/17.
 */

public class IngredientAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String TAG = IngredientAdapter.class.getSimpleName();
    public Context mContext;
    public List<Recipes.Ingredients> mIngredients;
    public LayoutInflater inflater;
    public Recipes.Ingredients currentIngredient;
    /**
     * this adapter will take Recipe objects and set place the recipe objects into a view holder
     * a ClickListener will be set with @Overridden OnClick method in order to get the position of
     * the recipe card that was clicked and pass intent Extras into the proper fields and then to
     * the RecipeDetailedActivity Class.
     */
    public IngredientAdapter(Context context, List<Recipes.Ingredients> ingredients) {
        this.mContext = context;
        this.mIngredients = ingredients;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = inflater.inflate(R.layout.ingredients_list_item, parent, false);
        return new IngredentsViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        IngredentsViewHolder ingredentsViewHolder = (IngredentsViewHolder) holder;
        currentIngredient = mIngredients.get(position);
        ingredentsViewHolder.recipeMeasure.setText(currentIngredient.getmMeasure());
        ingredentsViewHolder.recipeQuantity.setText(String.valueOf(currentIngredient.getmQuantity()));
        ingredentsViewHolder.recipeIngredient.setText(currentIngredient.getmIngredient());
        Log.d(TAG, "onBindViewHolder: " + ingredentsViewHolder);
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    public class IngredentsViewHolder extends RecyclerView.ViewHolder {

        public final TextView recipeQuantity;
        public final TextView recipeMeasure;
        public final TextView recipeIngredient;

        public IngredentsViewHolder(final View itemView) {
            super(itemView);
            recipeQuantity = itemView.findViewById(R.id.tv_ingredient_quantity);
            recipeMeasure = itemView.findViewById(R.id.tv_measure);
            recipeIngredient = itemView.findViewById(R.id.tv_ingredient);

        }
    }
}
