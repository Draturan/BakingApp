package com.example.simone.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore;
import android.widget.RemoteViews;

import com.example.simone.bakingapp.model.Ingredient;
import com.example.simone.bakingapp.model.Sweet;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class SweetWidgetProvider extends AppWidgetProvider {

    private String sweetName;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String sweetName, String ingredientsList) {


        CharSequence widgetText = context.getString(R.string.appwidget_text);
        if (sweetName != null){
            sweetName = sweetName + widgetText;
        }else{
            sweetName = "SWEEN" + widgetText;
        }


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.sweet_widget_provider);
        views.setTextViewText(R.id.appwidget_text, sweetName);
        if (ingredientsList == null){
            ingredientsList = context.getText(R.string.appwidget_ingredients).toString();
        }
        views.setTextViewText(R.id.appwidget_ingredients, ingredientsList);

        // Create a Pending Intent to bring the user to main activity when click on widget
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_rl, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // setting null variable
        String name = null;
        String list = null;
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, name, list);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        // checking if there is an intent
        if (intent.hasExtra(MainActivity.WIDGET_SWEET_KEY)){
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            Sweet sweet = intent.getParcelableExtra(MainActivity.WIDGET_SWEET_KEY);
            int ids[] = intent.getIntArrayExtra(MainActivity.WIDGET_IDS_KEY);
            String action = intent.getAction();
            // checking if the action correspond to an update and if the data is not null
            if (action.equals(appWidgetManager.ACTION_APPWIDGET_UPDATE) && sweet != null){
                sweetName = sweet.getName();
                ArrayList<Ingredient> ingredients = sweet.getIngredients();
                // Adding the name of the sweet

                // cycling all the data to send to the widget in update
                StringBuilder ingredientsListBuilder = new StringBuilder();
                for (int i = 0; i < ingredients.size(); i++){
                    ingredientsListBuilder.append(ingredients.get(i).getIngredient()).append("\n");
                }
                //updating all the instance of the widget
                for (int i = 0; i< ids.length; i++){
                    updateAppWidget(context, appWidgetManager, ids[i], sweetName, ingredientsListBuilder.toString());
                }

            }
        }
    }
}

