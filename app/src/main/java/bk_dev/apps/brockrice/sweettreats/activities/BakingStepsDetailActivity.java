package bk_dev.apps.brockrice.sweettreats.activities;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.brockrice.sweettreats.R;

import java.util.ArrayList;
import java.util.List;

import bk_dev.apps.brockrice.sweettreats.model.Recipes;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by brockrice on 12/9/17.
 */

public class BakingStepsDetailActivity extends AppCompatActivity {

    private static final String TAG = BakingStepsDetailActivity.class.getSimpleName();
    //make this a lsit? then pick out a single recipe in the fragment?
    private Recipes.RecipeSteps recipeStep;
    private List<Recipes.RecipeSteps> listSteps;
    private BakingStepsDetailFragment mBakingStepsDetailFragment;
    private static final String TAG_RETAINED_FRAGMENT = "retainedFragment";
    private int mStepIndexPosition;
    private int stepListCount;
    private String recipeName;

    @BindView(R.id.next_button)
    Button nextButton;
    @BindView(R.id.previous_button)
    Button prevButton;
    @BindView(R.id.recipe_detail_steps_container)
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baking_steps_detail);
        Toolbar stepsToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(stepsToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        if (savedInstanceState != null) {
            //Get the saved instance state of the list if the datafrom steps is null
            listSteps = savedInstanceState.getParcelableArrayList("saved_list_steps");
            recipeStep = savedInstanceState.getParcelable("saved_recipe_step");
            recipeName = savedInstanceState.getString("saved_recipeName");
            stepListCount = savedInstanceState.getInt("saved_list_count");
            mStepIndexPosition = savedInstanceState.getInt("saved_current_index");
            //get the reference to the retained fragment otherwise fragment will be added on top of another one
            mBakingStepsDetailFragment = (BakingStepsDetailFragment) getSupportFragmentManager().findFragmentByTag(TAG_RETAINED_FRAGMENT);
        } else {
            //check the bundle to see if it is null and pass in recipeStepList
            //reduce the stepList to strings for each view
            Bundle datafromSteps = getIntent().getExtras();
            if (datafromSteps != null) {
                listSteps = datafromSteps.getParcelableArrayList("listSteps");
                recipeName = getIntent().getStringExtra("recipeName");
                recipeStep = datafromSteps.getParcelable("clickedRecipeStepData");
                stepListCount = getIntent().getIntExtra("stepSize", 0);
                mStepIndexPosition = getIntent().getIntExtra("indexPosition", 0);
                Log.d(TAG, "INDEX POSTION: " + mStepIndexPosition);
            }
            provideFragmentLayout();
        }
        placeRecipeNameInTitle();
        Log.i(TAG, "INT STEP INDEX: " + mStepIndexPosition);
    }
    public void placeRecipeNameInTitle(){
        //Check to ensure recipeStep is not null
        if (recipeStep != null) {
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                try {
                    // Set title for detailed activity
                    getSupportActionBar().setTitle(recipeName);
                    // if Null for recipe name, set default title
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    getSupportActionBar().setTitle("Recipe Steps");
                }
            } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                Toast.makeText(this, "Full Screen", Toast.LENGTH_SHORT).show();
                // Hide status bar
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    try{
                       getSupportActionBar().hide();
                   } catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }
            }
        }


    public void provideFragmentLayout() {
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
        //Get mDescription. mThumbnail and mVideoUrl then set them below
        String mDescription = this.recipeStep.getDescription();
        String mVideoUrl = this.recipeStep.getVideoUrl();
        String mThumbnailUrl = this.recipeStep.getThumbnailPath();
        mStepIndexPosition = this.recipeStep.getStepsId();

        //Set the Index position, steps, videoUrl
        //Then set the detailedStepsFragment detailedView
        mBakingStepsDetailFragment.setStepId(mStepIndexPosition);
        mBakingStepsDetailFragment.setRecipeStep(recipeStep);
        mBakingStepsDetailFragment.setRecipeName(recipeName);
        mBakingStepsDetailFragment.setVideoUrl(mVideoUrl);
        mBakingStepsDetailFragment.setDetailedView(mDescription, mThumbnailUrl, mStepIndexPosition);

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

    //on click, move to next recipe step
    //find id and use getters to load data to views
    @OnClick(R.id.next_button)
    public void nextRecipeStep(View view) {

        //check to see if stepPostion value is less than the list size
        //If so, move to next position
        if (mStepIndexPosition < stepListCount - 1) {
            mStepIndexPosition++;
        } else {
            //reset the recipe steps back to the first step - 0
            mStepIndexPosition = 0;
            Log.d(TAG, "nextRecipeStep: " + mStepIndexPosition);
        }
        recipeStep = listSteps.get(mStepIndexPosition);
        provideFragmentLayout();
        Log.d(TAG, "nextRecipeStep: " + mStepIndexPosition);
    }

    //on click, move to previous recipe step
    //find id and use getters to load data to views
    @OnClick(R.id.previous_button)
    public void lastRecipeStep(View view) {

        //check to see if stepPostion value is less than the list size
        //If so, move to next position
        if (mStepIndexPosition > 0) {
            mStepIndexPosition--;
        } else {
            //reset the recipe steps back to the first step - 0
            mStepIndexPosition = stepListCount - 1;
        }
        recipeStep = listSteps.get(mStepIndexPosition);
        provideFragmentLayout();
        Log.d(TAG, "nextRecipeStep: " + mStepIndexPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //pass movies and sort selection into outState to reference
        //when activity is restored or resumed
        outState.putParcelableArrayList("saved_list_steps", (ArrayList<? extends Parcelable>) listSteps);
        outState.putParcelable("saved_recipe_step", recipeStep);
        outState.putString("saved_recipeName", recipeName);
        outState.putInt("saved_list_count", stepListCount);
        outState.putInt("saved_current_index", mStepIndexPosition);
        Log.d(TAG, "onSaveInstanceState: ");
    }

    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        Log.d(TAG, "onRestoreInstanceState: ");
    }
}

