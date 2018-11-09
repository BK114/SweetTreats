package bk_dev.apps.brockrice.sweettreats.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.brockrice.sweettreats.R;

import java.util.List;

import bk_dev.apps.brockrice.sweettreats.model.Recipes;


public class IngredientsActivity extends AppCompatActivity {
    private static final String TAG = IngredientsActivity.class.getSimpleName();
    private List<Recipes.Ingredients> ingredientsList;
    private String INGREDIENTS_KEY = "ingredients_key";
    private String RECIPE_NAME_KEY = "rv_key";
    public Recipes recipe;
    private String recipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        Toolbar toolbar = findViewById(R.id.toolbar_ingredients);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (savedInstanceState != null) {
            recipe = savedInstanceState.getParcelable(INGREDIENTS_KEY);
            recipeName = savedInstanceState.getString(RECIPE_NAME_KEY);
            if (this.recipe != null) {
                ingredientsList = this.recipe.getIngredients();
                recipeName = this.recipe.getRecipeName();
                if (recipeName == null) {
                    getSupportActionBar().setTitle("Ingredients");
                } else {
                    getSupportActionBar().setTitle(recipeName);
                }
            }
        }else {
            Bundle datafromIntent = getIntent().getExtras();
            if (datafromIntent != null) {
                //if the savedState was null then we will get the intentFormInitiator
                recipe = datafromIntent.getParcelable("ingredients");
            }
            if (this.recipe != null) {
                ingredientsList = this.recipe.getIngredients();
                recipeName = this.recipe.getRecipeName();
                if (recipeName == null) {
                    getSupportActionBar().setTitle("Ingredients");
                } else {
                    getSupportActionBar().setTitle(recipeName);
                }
            }
            IngredientsFragment stepsFragment = new IngredientsFragment();
            stepsFragment.setIngredientsList(ingredientsList);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_ingredients_container, stepsFragment)
                    .commit();
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(INGREDIENTS_KEY, recipe);
        outState.putString(RECIPE_NAME_KEY, recipeName);
        //set recyclerView state using getLayoutManager (for scrolling position)

    }
}
