package com.example.simone.bakingapp.model;


import java.util.HashMap;
import java.util.Map;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Simone on 19/06/2018 for BakingApp project
 */
public class Ingredient implements Parcelable {

    private long quantity;
    private String measure;
    private String ingredient;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    public final static Parcelable.Creator<Ingredient> CREATOR = new Creator<Ingredient>() {


        @SuppressWarnings({"unchecked"})
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        public Ingredient[] newArray(int size) {
            return (new Ingredient[size]);
        }

    }
            ;

    protected Ingredient(Parcel in) {
        this.quantity = ((long) in.readValue((long.class.getClassLoader())));
        this.measure = ((String) in.readValue((String.class.getClassLoader())));
        this.ingredient = ((String) in.readValue((String.class.getClassLoader())));
        this.additionalProperties = ((Map<String, Object> ) in.readValue((Map.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Ingredient() {
    }

    /**
     *
     * @param measure
     * @param ingredient
     * @param quantity
     */
    public Ingredient(Integer quantity, String measure, String ingredient) {
        super();
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(quantity);
        dest.writeValue(measure);
        dest.writeValue(ingredient);
        dest.writeValue(additionalProperties);
    }

    public int describeContents() {
        return 0;
    }

}
