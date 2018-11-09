package bk_dev.apps.brockrice.sweettreats.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brockrice.sweettreats.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bk_dev.apps.brockrice.sweettreats.activities.BakingStepsActivity;
import bk_dev.apps.brockrice.sweettreats.activities.BakingStepsDetailActivity;
import bk_dev.apps.brockrice.sweettreats.activities.BakingStepsDetailFragment;
import bk_dev.apps.brockrice.sweettreats.model.Recipes;

/**
 * Created by brockrice on 12/7/17.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {

    private static final String TAG = StepsAdapter.class.getSimpleName();
    private Context mContext;
    private String recipeName;
    private Recipes recipe;
    private List<Recipes.RecipeSteps> mRecipeSteps;
    private LayoutInflater inflater;
    private boolean mTwoPane;


    /**
     * this adapter will take Recipe objects and set place the recipe objects into a view holder
     * a ClickListener will be set with @Overridden OnClick method in order to get the position of
     * the recipe card that was clicked and pass intent Extras into the proper fields and then to
     * the RecipeDetailedActivity Class.
     */
    public StepsAdapter(Context context, List<Recipes.RecipeSteps> recipeSteps, Recipes recipe, boolean twoPane) {
        this.mContext = context;
        this.mRecipeSteps = recipeSteps;
        this.recipe = recipe;
        this.mTwoPane = twoPane;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public StepsAdapter.StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = inflater.inflate(R.layout.baking_steps_list_item, parent, false);
        return new StepsViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(StepsAdapter.StepsViewHolder holder, int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        mRecipeSteps.get(position);
        holder.recipeDescription.setText(mRecipeSteps.get(position).getShortDescription());
        Log.d(TAG, "onBindViewHolder: " + holder);

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + recipeName);
        return mRecipeSteps.size();
    }

    @Override
    public int getItemViewType(final int position) {
        return position;
    }


    public class StepsViewHolder extends RecyclerView.ViewHolder {

        public TextView recipeDescription;

        public StepsViewHolder(final View itemView) {
            super(itemView);

            recipeDescription = itemView.findViewById(R.id.tv_shortDescription);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //check to see if this click originated in the BakingStepsActivity

                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (mContext instanceof BakingStepsActivity) {
                            BakingStepsActivity bakingStepsActivity = ((BakingStepsActivity) mContext);
                           if (!mTwoPane) {
                               mRecipeSteps = recipe.getSteps();
                               recipeName = recipe.getRecipeName();
                               Bundle stepData = new Bundle();
                               stepData.putString("recipeName", recipeName);
                           }
                            Log.i(TAG, "ONCLICK MRECIPESTEPS: " + position);
                            //we need to determine if the click was an instance of BakingStepsActivity.
                            // if it was we need to call to that activity to handle the layout
                            //this is becuase the activity will either call upon a new activity in
                            //smaller device use cases or it will create mutliple fragments within one
                            // activity for larger devices with a min screen size of 600dp
                            //we can callback to the BakingStepsActivity to provide a layout for us
                            //by using the position of the clicked position
                            bakingStepsActivity.provideBakingDetailLayout(getAdapterPosition());
                        }
                    }
                }
            });
        }
    }
}