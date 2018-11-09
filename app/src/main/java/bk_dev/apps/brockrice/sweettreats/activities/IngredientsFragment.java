package bk_dev.apps.brockrice.sweettreats.activities;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.brockrice.sweettreats.R;

import java.util.ArrayList;
import java.util.List;

import bk_dev.apps.brockrice.sweettreats.adapter.IngredientAdapter;
import bk_dev.apps.brockrice.sweettreats.model.Recipes;

/**
 * A placeholder fragment containing a simple view.
 */
public class IngredientsFragment extends Fragment {

    // Tag for logging
    private static final String TAG = "IngredientStepsFragment";
    // Variables to store a list of steps resources and the stepsId of the step that this fragment displays
    List<Recipes.Ingredients> ingredientsList;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    IngredientAdapter adapter;
    Recipes recipe;
    private String recipeName;
    private static final String INGREDIENTS_LIST = "ingredients";

    public IngredientsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Load the saved state (the list of images and list index) if there is one
        if (savedInstanceState != null) {
            ingredientsList = savedInstanceState.getParcelableArrayList(INGREDIENTS_LIST);
        }
        // Inflate the fragment-baking-steps layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view_ingredients);
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new IngredientAdapter(getActivity(), ingredientsList);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        return rootView;

    }

    public void setIngredientsList(List<Recipes.Ingredients> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelableArrayList(INGREDIENTS_LIST, (ArrayList<? extends Parcelable>) ingredientsList);
    }

}