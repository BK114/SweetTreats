package bk_dev.apps.brockrice.sweettreats.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.brockrice.sweettreats.R;

import java.util.ArrayList;
import java.util.List;

import bk_dev.apps.brockrice.sweettreats.model.Recipes;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BakingStepsActivity extends AppCompatActivity {

    private static final String TAG = BakingStepsActivity.class.getSimpleName();
    public List<Recipes.RecipeSteps> recipeStepsList;
    private BakingStepsDetailFragment mBakingStepsDetailFragment;
    private BakingStepsFragment mBakingStepsFragment;
    private static final String TAG_RETAINED_STEPS_FRAGMENT = "retained_list_fragment";
    private static final String TAG_RETAINED_DETAIL_FRAGMENT = "retainedFragment";
    public Recipes recipe;
    private String recipeName;
    private Recipes.RecipeSteps recipeStep;
    private boolean mTwoPane;
    private int mStepIndex;
    private String mVideoUrl;
    private String mThumbnail;
    private String mDescription;

    @BindView(R.id.indredients_button)
    Button buttonIngredients;
    @BindView(R.id.steps_button)
    Button buttonSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baking_steps);
        Toolbar stepsToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(stepsToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //if saved state is not null, collect the Saved State data
        if (savedInstanceState != null) {
            //Get the saved instance state of the list if the datafrom steps is null
            recipeStepsList = savedInstanceState.getParcelableArrayList("list_steps");
            recipeName = savedInstanceState.getString("recipe_name");
            recipe = savedInstanceState.getParcelable("recipe");
            mStepIndex = savedInstanceState.getInt("saved_current_index");
            mBakingStepsDetailFragment = (BakingStepsDetailFragment) getSupportFragmentManager().findFragmentByTag(TAG_RETAINED_DETAIL_FRAGMENT);
            mBakingStepsFragment = (BakingStepsFragment) getSupportFragmentManager().findFragmentByTag(TAG_RETAINED_STEPS_FRAGMENT);
        } else {
            //if the savedState was null then we will get the intentFormInitiator
            //otherwise get the data passed from the RecipeActivity intent
            Bundle datafromIntent = getIntent().getExtras();
            //if the data passed form RecipeActiviy is not null get the data
            if (datafromIntent != null) {
                recipe = datafromIntent.getParcelable("recipe");
                if (this.recipe != null) {
                    recipeStepsList = this.recipe.getSteps();
                    recipeName = this.recipe.getRecipeName();
                }
            }
            //set the list of steps
            provideRecipeListLayout();
            if (findViewById(R.id.baking_steps_detail_linear_layout) != null) {
                provideBakingDetailLayout(mStepIndex);
                buttonSteps.setVisibility(View.GONE);
            }
        }
        placeRecipeNameInTitle();
    }

    public void placeRecipeNameInTitle() {
        //if recipe name is not null, load the recipe name
        if (recipeName != null) {
            try {
                getSupportActionBar().setTitle(recipeName);
            } catch (NullPointerException e) {
                Toast.makeText(this, "No title set", Toast.LENGTH_SHORT).show();
            }
        } else {
            //otherwise set a generic title
            getSupportActionBar().setTitle("Baking Steps");
        }
    }

    public void provideRecipeListLayout() {
        //After data is loaded either from onSavedInstanceState or
        //Recipe Intent Bundle - create a new fragment and set the data for
        //BakingStepsFragmentata for
        //BakingStepsFragment
        BakingStepsFragment stepsFragment = new BakingStepsFragment();
        stepsFragment.setStepsList(recipeStepsList);
        stepsFragment.setRecipe(recipe);
        stepsFragment.setRecipeName(recipeName);
        //begin the fragment transaction amd add the fragment container and
        //stepsFragment to the fragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.recipe_steps_container, stepsFragment)
                .commit();
    }

    //provide the fragment for BakingDetails. We do not want to call
    //on an activity so this will only be called inside provideBakingDetailLayoutOnClick
    //if mTwoPane is false
    public void provideBakingDetailLayout(int position) {
        this.mStepIndex = position;
        //if the layout size is for a tablet then the baking_steps_detail_linear_layout
        //will be called upon. We want to see if baking_steps_detail_linear_layout
        //is null or not. If it is not null then the tablet layout will load
        //then we can add the additional fragment to display the view.
        if (findViewById(R.id.baking_steps_detail_linear_layout) != null) {
            //Buttons for next and previous are attached to the activity so
            //we do not need to set the visibility to gone since the BakingStepsDetailActivity
            //will not be called.
            mTwoPane = true;
            //we will bind the views only in two pane so that we do not get a null
            //pointer exception for smaller devices
            ButterKnife.bind(this);
            if (mTwoPane) {
                buttonSteps.setVisibility(View.GONE);
                Log.i(TAG, "provideBakingDetailLayout: " + mStepIndex);
                //A recipe Step has been clicked and will need to pass in the
                //correct recipe step data to the new fragment
                //Find the retained fragment on Activity restarts
                FragmentManager fragmentManager = getSupportFragmentManager();
                //create a new fragment if null otherwise replace it.
                if (mBakingStepsDetailFragment == null) {
                    mBakingStepsDetailFragment = new BakingStepsDetailFragment();
                    // Add the fragment to its container using a FragmentManager and a Transaction
                    fragmentManager.beginTransaction()
                            .add(R.id.recipe_detail_steps_container, mBakingStepsDetailFragment)
                            .commit();
                    Log.d(TAG, "PROVIDE FRAGMENT ADD");

                } else {
                    mBakingStepsDetailFragment = new BakingStepsDetailFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.recipe_detail_steps_container, mBakingStepsDetailFragment)
                            .commit();
                    Log.d(TAG, "PROVIDE FRAGMENT REPLACE");
                }
                recipeStep = recipeStepsList.get(mStepIndex);
                mVideoUrl = this.recipeStep.getVideoUrl();
                mThumbnail = this.recipeStep.getThumbnailPath();
                mDescription = this.recipeStep.getDescription();

                mBakingStepsDetailFragment.setRecipeName(recipeName);
                mBakingStepsDetailFragment.setVideoUrl(mVideoUrl);
                mBakingStepsDetailFragment.setRecipeName(recipeName);
                mBakingStepsDetailFragment.setDetailedView(mDescription, mThumbnail, mStepIndex);
            }
        } else {
                //we need to track the clicked postion of the recipe steps so we need to create
                //a variable to hold the clickedRecipeStep position
                Recipes.RecipeSteps clickedRecipeStep = recipeStepsList.get(position);
                //we will need to get the recipe name that was clicked
                recipeName = recipe.getRecipeName();
                //since we are going to call a new intent, we will pas in the size of the list
                //so that we can track the next or last position
                int recipeStepsCount = recipeStepsList.size();
                Log.d(TAG, "RECIPE STEP SIZE: " + recipeStepsList.size());
                //we will need to pass a Bundle of recipeSteps with data
                Bundle stepData = new Bundle();
                stepData.putParcelableArrayList("listSteps", (ArrayList<? extends Parcelable>) recipeStepsList);
                Log.d(TAG, "STEPSIZE LIST: " + recipeStepsList);
                //Start a new intent to call the BakingStepsDetailActivity
                Intent stepsIntent = new Intent(this, BakingStepsDetailActivity.class);
                //put data that the activity and fragment will utilize to populate the view
                stepsIntent.putExtras(stepData);
                stepsIntent.putExtra("recipeName", recipeName);
                stepsIntent.putExtra("stepSize", recipeStepsCount);
                stepsIntent.putExtra("indexPosition", position);
                stepsIntent.putExtra("clickedRecipeStepData", clickedRecipeStep);
                Toast.makeText(getApplicationContext(), "Clicked on " + clickedRecipeStep.getShortDescription(),
                        Toast.LENGTH_SHORT).show();
                //Start the next activity
                this.startActivity(stepsIntent);
            }

        }


    //On ingredients clicked, bundle data and pass to IngredientsActivity
    @OnClick(R.id.indredients_button)
    public void showIngredients(View view) {
        if (findViewById(R.id.baking_steps_detail_linear_layout) != null) {
            //Buttons for next and previous are attached to the activity so
            //we do not need to set the visibility to gone since the BakingStepsDetailActivity
            //will not be called.
            mTwoPane = true;
            ButterKnife.bind(this);

            //After data is loaded either from onsavedInstanceState or
            //Recipe Intent Bundle - create a new fragment and set the detailedView data
            IngredientsFragment ingredientsFragment = new IngredientsFragment();
            ingredientsFragment.setRecipeName(recipeName);
            ingredientsFragment.setIngredientsList(recipe.getIngredients());
            //begin the fragment transaction amd add the fragment conatainer and
            //stepsFragment to the fragmentManager
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_steps_container, ingredientsFragment)
                    .commit();
            buttonSteps.setVisibility(View.VISIBLE);
            buttonIngredients.setVisibility(View.GONE);

        } else {
            Bundle ingredientsData = new Bundle();
            ingredientsData.putParcelable("ingredients", recipe);
            Intent recipeIntent = new Intent(getApplicationContext(), IngredientsActivity.class);
            recipeIntent.putExtras(ingredientsData);
            startActivity(recipeIntent);
        }
    }


    @OnClick(R.id.steps_button)
    public void showSteps(View view) {
        //set the button views
        if (findViewById(R.id.baking_steps_detail_linear_layout) != null) {
            //Buttons for next and previous are attached to the activity so
            //we do not need to set the visibility to gone since the BakingStepsDetailActivity
            //will not be called.
            mTwoPane = true;
            ButterKnife.bind(this);

            if (mTwoPane) {
                //After data is loaded either from onSavedInstanceState or
                //Recipe Intent Bundle - create a new fragment and set the data
                BakingStepsFragment backToStepsFragment = new BakingStepsFragment();
                backToStepsFragment.setStepsList(recipeStepsList);
                backToStepsFragment.setRecipe(recipe);
                backToStepsFragment.setRecipeName(recipeName);
                //begin the fragment transaction amd add the fragment conatainer and
                //stepsFragment to the fragmentManager
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.recipe_steps_container, backToStepsFragment)
                        .commit();
                buttonSteps.setVisibility(View.GONE);
                buttonIngredients.setVisibility(View.VISIBLE);

                //if the layout size is for a tablet then the baking_steps_detail_linear_layout
                //will be called upon. We want to see if baking_steps_detail_linear_layout
                //is null or not. If it is not null then the tablet layout will load
                //then we can add the additional fragment to display the view.
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Save the instance state
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //pass recipeStepList and recipe name into outState to reference
        //when activity is restored or resumed
        outState.putParcelableArrayList("list_steps", (ArrayList<? extends Parcelable>) recipeStepsList);
        outState.putString("recipe_name", recipeName);
        outState.putParcelable("recipe", recipe);
        outState.putInt("saved_current_index", mStepIndex);
        Log.d(TAG, "onSaveInstanceState: ");
    }

    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        Log.d(TAG, "onRestoreInstanceState: ");
    }

}


