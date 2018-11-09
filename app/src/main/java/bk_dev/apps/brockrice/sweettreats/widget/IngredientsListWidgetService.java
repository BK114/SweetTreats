package bk_dev.apps.brockrice.sweettreats.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.brockrice.sweettreats.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import bk_dev.apps.brockrice.sweettreats.activities.MainActivity;
import bk_dev.apps.brockrice.sweettreats.model.Recipes;

/**
 * Created by brockrice on 12/15/17.
 */

public class IngredientsListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsListRemoteViewsFactory(this.getApplicationContext());
    }


    public class IngredientsListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context mContext;
        private Recipes recipe = new Recipes();
        private List<Recipes.Ingredients> mIngredientsList;

        public IngredientsListRemoteViewsFactory(Context context) {
            this.mContext = context;
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String json = preferences.getString(MainActivity.SHARED_PREFS_KEY, "");
            if (!json.equals("")) {
                Gson gson = new Gson();
                //we are getting a recipe that was saved in SharedPreferences, so make the TypeToken a Recipe object
                recipe = gson.fromJson(json, new TypeToken<Recipes>() {
                }.getType());
                //get the list of ingredients
                mIngredientsList = recipe.getIngredients();
            }
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            //return the size of the ingredients list that was clicked
            if (mIngredientsList != null) {
                Log.d("COUNT INGREDIENTS", "GET_COUNT: " + mIngredientsList);
                return mIngredientsList.size();
            } else {
                //Only 1 view type will be returned here
                return 0;
            }
        }

        @Override
        public RemoteViews getViewAt(int position) {
            //this method will get the views for the specified layout file
            //set up the remote views for the collection and iterate throught to pupulate the list
            RemoteViews singleView = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
            singleView.setEmptyView(R.id.list_view_widget, R.id.linear_layout_widget_empty);
            if (recipe != null) {
                try {

                    //set the widget views if we have a recipe
                    singleView.setTextViewText(R.id.widget_tv_ingredient, mIngredientsList.get(position).getmIngredient());
                    singleView.setTextViewText(R.id.widget_tv_measure, mIngredientsList.get(position).getmMeasure());
                    //parse the Doule to string
                    singleView.setTextViewText(R.id.widget_tv_quantity, Double.toString(mIngredientsList.get(position).getmQuantity()));
                    Log.d("REMOTE GET VIEW AT", "getViewAt: " + mIngredientsList.get(position).getmIngredient());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            //return the remoteViews
            return singleView;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            //Only 1 view type will be returned here
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}