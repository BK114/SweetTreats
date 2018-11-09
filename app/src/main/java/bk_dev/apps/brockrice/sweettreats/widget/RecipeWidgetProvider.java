package bk_dev.apps.brockrice.sweettreats.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.brockrice.sweettreats.R;

import bk_dev.apps.brockrice.sweettreats.activities.MainActivity;
import bk_dev.apps.brockrice.sweettreats.model.Recipes;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {
    private Recipes recipe;
    private static final String RECIPE_KEY = "widget_recipes";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        //Create an intnet to launch main acitivity when cupcake widget is clicked
        Intent intent = new Intent(context, MainActivity.class);
        //wrapt the intent with a pending intent to link remote view
        //that will laucnch once the cupcake drawable is clicked
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        //widgets only allow click handlers to launch pending intents
        views.setOnClickPendingIntent(R.id.button_add_recipe, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            //updateWidget for calling main activity if add recipe button pressed
            //Build the intent to call the service
            Intent serviceIntent = new Intent(context, IngredientsListWidgetService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            // Update the widgets via the service
            context.startService(serviceIntent);
            //update each widget with the remote adapter
            Intent intent = new Intent(context, IngredientsListWidgetService.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            // Add a random integer to stop the Intent being ignored.  This is needed for some API levels due to intent caching
            intent.putExtra("Random", Math.random() * 1000);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
            //set the empty adapter for when there is no recipe selected
            remoteViews.setEmptyView(R.id.linear_layout_widget_fill, R.id.linear_layout_widget_empty);
            remoteViews.setRemoteAdapter(R.id.list_view_widget, intent);
            if (recipe != null) {
                remoteViews.setViewVisibility(R.id.linear_layout_widget_fill, VISIBLE);
                remoteViews.setOnClickFillInIntent(R.id.widget_ll_item, intent);
                remoteViews.setTextViewText(R.id.widget_recipe_name, recipe.getRecipeName());
                Log.d("RECIPE NAME UPDATE", "NAME: " + recipe.getRecipeName());

            }

            //updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
            // Instruct the widget manager to update the widget
            updateAppWidget(context, appWidgetManager, appWidgetId);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_view_widget);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        //check to see if the intent has extras
        recipe = intent.getParcelableExtra(RECIPE_KEY);
        try {
            //try to recieve the data. catch a NullPointerException
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
            ComponentName thisWidget = new ComponentName(context.getApplicationContext(), RecipeWidgetProvider.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            onUpdate(context, appWidgetManager, appWidgetIds);
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(context, "Cannot find recipe", Toast.LENGTH_SHORT).show();
        }
    }
}

