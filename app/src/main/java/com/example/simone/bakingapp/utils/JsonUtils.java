package com.example.simone.bakingapp.utils;

import com.example.simone.bakingapp.model.Ingredient;
import com.example.simone.bakingapp.model.Step;
import com.example.simone.bakingapp.model.Sweet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Simone on 20/06/2018 for BakingApp project
 */
public class JsonUtils {

    private final String p_id = "id";
    private final String p_name = "name";
    private final String p_ingredients = "ingredients";
    private final String p_steps = "steps";
    private final String p_servings = "servings";
    private final String p_image = "image";

    private final String p_Step_id = "id";
    private final String p_Step_shortDescription = "shortDescription";
    private final String p_Step_description = "description";
    private final String p_Step_videoURL = "videoURL";
    private final String p_Step_thumbnailURL = "thumbnailURL";

    private final String p_Ingredient_quantity = "quantity";
    private final String p_Ingredient_measure = "measure";
    private final String p_Ingredient_ingredient = "ingredient";

    public ArrayList<Sweet> parseAnswerJson(String jsonDiscover) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonDiscover);
        ArrayList<Sweet> movieArrayList = new ArrayList<>();
        // parsing data retrieved and putting in the ArrayList as a List of Movie objects
        for (int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Sweet sweet = new Sweet();
            sweet.setId(jsonObject.optInt(p_id));
            sweet.setName(jsonObject.optString(p_name));
            sweet.setIngredients(parseAnswerIngredientsJson(jsonObject.optString(p_ingredients)));
            sweet.setSteps(parseAnswerStepJson(jsonObject.optString(p_steps)));
            sweet.setServings(jsonObject.optInt(p_servings));
            sweet.setImage(jsonObject.optString(p_image));

            movieArrayList.add(sweet);
        }
        return movieArrayList;
    }

    public ArrayList<Step> parseAnswerStepJson (String jsonTrailers) throws JSONException{

        JSONArray jsonArray = new JSONArray(jsonTrailers);
        ArrayList<Step> trailersArrayList = new ArrayList<>();

        // parsing data retrieved and putting in the ArrayList as a List of Movie objects
        for (int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Step step = new Step();
            step.setId(jsonObject.optInt(p_Step_id));
            step.setShortDescription(jsonObject.optString(p_Step_shortDescription));
            step.setDescription(jsonObject.optString(p_Step_description));
            step.setVideoURL(jsonObject.optString(p_Step_videoURL));
            step.setThumbnailURL(jsonObject.optString(p_Step_thumbnailURL));

            trailersArrayList.add(step);
        }
        return trailersArrayList;
    }

    public ArrayList<Ingredient> parseAnswerIngredientsJson (String jsonReviews) throws JSONException {

        JSONArray jsonArray = new JSONArray(jsonReviews);
        ArrayList<Ingredient> reviewsArrayList = new ArrayList<>();

        // parsing data retrieved and putting in the ArrayList as a List of Movie objects
        for (int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Ingredient ingredient = new Ingredient();
            ingredient.setQuantity(jsonObject.optLong(p_Ingredient_quantity));
            ingredient.setMeasure(jsonObject.optString(p_Ingredient_measure));
            ingredient.setIngredient(jsonObject.optString(p_Ingredient_ingredient));

            reviewsArrayList.add(ingredient);
        }
        return reviewsArrayList;
    }
}