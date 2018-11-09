package bk_dev.apps.brockrice.sweettreats.activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.brockrice.sweettreats.R;

import java.util.ArrayList;
import java.util.List;

import bk_dev.apps.brockrice.sweettreats.adapter.StepsAdapter;
import bk_dev.apps.brockrice.sweettreats.model.Recipes;

/**
 * A simple {@link Fragment} subclass.
 */
public class BakingStepsFragment extends Fragment {

    static final String RECYCLER_VIEW_STATE_KEY = "recycler_list";
    private static final String TAG = "BakingStepsFragment";
    private static final String RECIPE_STEPS = "recipe_steps";
    private static final String RECIPES_KEY = "recipe_key";
    // Variables to store a list of steps resources and the stepsId of the step that this fragment displays
    List<Recipes.RecipeSteps> stepsList;
    RecyclerView recyclerView;
    StepsAdapter adapter;
    String recipeName;
    Recipes recipe;
    boolean mTwoPane;

    public BakingStepsFragment() {
        // Required empty public constructor
    }

    /**
     * Inflates the fragment layout file and sets the correct resource for the image to display
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the fragment-baking-steps layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_baking_steps, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view_steps);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setHasFixedSize(true);
        //set the adapter
        // Load the saved state (the list of images and list index) if there is one
        if (savedInstanceState != null) {
            //get recyclerview scroll location
            stepsList = savedInstanceState.getParcelableArrayList(RECIPE_STEPS);
            recipe = savedInstanceState.getParcelable(RECIPES_KEY);
            Parcelable savedParcelable = savedInstanceState.getParcelable(RECYCLER_VIEW_STATE_KEY);
            Log.d(TAG, "STATE SAVED: " + savedParcelable);
            recyclerView.getLayoutManager().onRestoreInstanceState(savedParcelable);
        }
        adapter = new StepsAdapter(getActivity(), stepsList, recipe, mTwoPane);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return rootView;
    }

    public void setRecipe(Recipes recipe) {
        this.recipe = recipe;
    }

    public void setStepsList(List<Recipes.RecipeSteps> stepsList) {
        this.stepsList = stepsList;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPE_STEPS, (ArrayList<? extends Parcelable>) stepsList);
        //set recyclerView state using getLayoutManager (for scrolling position)
        outState.putParcelable(RECIPES_KEY, recipe);
        outState.putParcelable(RECYCLER_VIEW_STATE_KEY, recyclerView.getLayoutManager().onSaveInstanceState());

        Log.d(TAG, "onSaveInstanceState: " + recyclerView.getLayoutManager().onSaveInstanceState());
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle resumeBundle = new Bundle();
        resumeBundle.getParcelable(RECYCLER_VIEW_STATE_KEY);
        recyclerView.getLayoutManager().onRestoreInstanceState(resumeBundle);
        Log.d(TAG, "RESUMED STATE: " + recyclerView);
    }

}
