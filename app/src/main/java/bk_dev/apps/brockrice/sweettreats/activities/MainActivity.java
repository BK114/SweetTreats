package bk_dev.apps.brockrice.sweettreats.activities;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.brockrice.sweettreats.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import bk_dev.apps.brockrice.sweettreats.adapter.RecipeAdapter;
import bk_dev.apps.brockrice.sweettreats.model.Recipes;
import butterknife.BindView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String SHARED_PREFS_KEY = "SHARED_PREFS_KEY";
    private ArrayList<Recipes> recipesArrayList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Sweet Treats");
        //create a new ArrayList<> to hold recipes
        //cast layout id recycler_view_main into Recyclerview
        recyclerView = findViewById(R.id.recycler_view_main);
        recyclerView.setHasFixedSize(true);
        new getRecipes().execute();
        // Set Layout manager to populate recyclerView for smaller devices
        if (findViewById(R.id.phone_main_linear_layout) != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            //get the current context of this recyclerView and adjust amount of columns
            // based on screen orientation
        } else {
            setGridOrientation();
        }
    }

    public void setGridOrientation() {
        //determine if the gridlayout is triggered and set views
        if (findViewById(R.id.grid_layout_main_activity) != null) {
            //set the gridlayout to a span of 2 or 3 depending on orientation
            if (recyclerView.getResources().getConfiguration()
                    .orientation == Configuration.ORIENTATION_PORTRAIT) {
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            } else if (recyclerView.getResources().getConfiguration()
                    .orientation == Configuration.ORIENTATION_LANDSCAPE) {
                recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            }
        }
    }

    public class getRecipes extends AsyncTask<String, String, List<Recipes>> {
        private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

        @Override
        protected List<Recipes> doInBackground(String... params) {
            //set jsonString to null, this will be used to store json data
            try {
                //new URL in order to create the connection and call the json data
                URL url = new URL(BASE_URL);
                //open connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //instantiate a new InputStream to get the data from online by passing in urlConnection
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                //use buffered input streams to read json data.
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                //the buffer is a space allocated into memory for bytes in this case
                //store the buffered input into jsonString variable named builder
                StringBuilder builder = new StringBuilder();

                //input string will be labeled jsonString
                String jsonString;
                while ((jsonString = reader.readLine()) != null) {
                    builder.append(jsonString);
                }
                //Create new JSONArray for recipes
                JSONArray recipeArray = new JSONArray(builder.toString());

                try {
                    //iterate through the json data and add data to recipesArrayList
                    //as the loop continues
                    recipesArrayList = new ArrayList<>();
                    for (int i = 0; i < recipeArray.length(); i++) {
                        JSONObject jsonRecipeObject = recipeArray.getJSONObject(i);
                        //Create a new Recipes object in order to save each iteration of recipe data
                        //store key, values into variables within each recipe object
                        Recipes recipes = new Recipes();
                        recipes.setRecipeId(jsonRecipeObject.getInt("id"));
                        recipes.setRecipeName(jsonRecipeObject.getString("name"));
                        recipes.setServings(jsonRecipeObject.getString("servings"));
                        try {
                            recipes.setImage(jsonRecipeObject.getString("image"));
                        } catch (NullPointerException e) {
                            jsonRecipeObject.isNull("image");
                        }
                        //Create a new list of ingredients that will be stored from for loop
                        List<Recipes.Ingredients> ingredientsList = new ArrayList<>();
                        //iterate through the array of ingredients
                        for (int j = 0; j < jsonRecipeObject.getJSONArray("ingredients").length(); j++) {
                            //Create a new Json object by referencing the original object and then the
                            //nested array ingredients
                            JSONObject ingredientsObject =
                                    jsonRecipeObject.getJSONArray("ingredients").getJSONObject(j);
                            //Create a new ingredients object that will allow the loop to store a
                            //single ingreadient object into the array list each time through the loop
                            Recipes.Ingredients ingredients = new Recipes.Ingredients();
                            //Set each item from the array
                            ingredients.setmQuantity(ingredientsObject.getDouble("quantity"));
                            ingredients.setmMeasure(ingredientsObject.getString("measure"));
                            ingredients.setmIngredient(ingredientsObject.getString("ingredient"));
                            recipes.setIngredients(recipes.getIngredients());
                            //add ingredients to the created ingredientsList from above;
                            ingredientsList.add(ingredients);
                        }
                        //new we must set the ingredientsList to Recipes
                        recipes.setIngredients(ingredientsList);

                        //Create a new list of Steps that will be stored from the next for loop
                        List<Recipes.RecipeSteps> stepsList = new ArrayList<>();
                        //iterate through the array of steps
                        for (int k = 0; k < jsonRecipeObject.getJSONArray("steps").length(); k++) {
                            //Create a new Json object by referencing the original object and then the
                            //nested array steps
                            JSONObject stepsObject =
                                    jsonRecipeObject.getJSONArray("steps").getJSONObject(k);
                            //Create a new ingredients object that will allow the loop to store a
                            //single ingreadient object into the array list each time through the loop
                            Recipes.RecipeSteps steps = new Recipes.RecipeSteps();
                            //Set each item from the array
                            steps.setStepsId(stepsObject.getInt("id"));
                            steps.setShortDescription(stepsObject.getString("shortDescription"));
                            steps.setDescription(stepsObject.getString("description"));
                            steps.setVideoUrl(stepsObject.getString("videoURL"));
                            steps.setThumbnailPath(stepsObject.getString("thumbnailURL"));
                            //add steps to the created stepsList from above;
                            stepsList.add(steps);
                        }
                        recipes.setSteps(stepsList);
                        //add the final recipes object to the recipesArrayList
                        recipesArrayList.add(recipes);
                        Log.d(TAG, "After Final Add: " + recipes.getRecipeName());
                    }
                    urlConnection.disconnect();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //urlConnection.disconnect();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return recipesArrayList;
        }

        @Override
        protected void onPostExecute(List<Recipes> result) {
            super.onPostExecute(result);
            if (recipesArrayList != null && recipesArrayList.size() - 1 != 0) {
                //Setup and Handover data to recyclerview
                RecipeAdapter adapter = new RecipeAdapter(getApplicationContext(), result);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
