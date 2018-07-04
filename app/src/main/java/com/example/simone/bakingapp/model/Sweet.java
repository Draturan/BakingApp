package com.example.simone.bakingapp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Simone on 19/06/2018 for BakingApp project
 */
public class Sweet implements Parcelable {

    private Integer id;
    private String name;
    private ArrayList<Ingredient> ingredients = null;
    private ArrayList<Step> steps = null;
    private Integer servings;
    private String image;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    public final static Parcelable.Creator<Sweet> CREATOR = new Creator<Sweet>() {


        @SuppressWarnings({"unchecked"})
        public Sweet createFromParcel(Parcel in) {
            return new Sweet(in);
        }

        public Sweet[] newArray(int size) {
            return (new Sweet[size]);
        }

    }
            ;

    protected Sweet(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.ingredients = (ArrayList<Ingredient>) in.readArrayList(Ingredient.class.getClassLoader());
        this.steps = (ArrayList<Step>) in.readArrayList(Step.class.getClassLoader());
        this.servings = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
        this.additionalProperties = ((Map<String, Object> ) in.readValue((Map.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Sweet() {
    }

    /**
     *
     * @param ingredients
     * @param id
     * @param servings
     * @param name
     * @param image
     * @param steps
     */
    public Sweet(Integer id, String name, ArrayList<Ingredient> ingredients, ArrayList<Step> steps, Integer servings, String image) {
        super();
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeList(ingredients);
        dest.writeList(steps);
        dest.writeValue(servings);
        dest.writeValue(image);
        dest.writeValue(additionalProperties);
    }

    public int describeContents() {
        return 0;
    }

}
