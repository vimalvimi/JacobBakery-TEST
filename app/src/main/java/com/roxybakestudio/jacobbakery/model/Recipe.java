package com.roxybakestudio.jacobbakery.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable {

    @Expose
    private int id;
    @Expose
    private String name;
    @Expose
    private List<Ingredients> ingredients;
    @Expose
    private List<Steps> steps;
    @Expose
    private int servings;

    public Recipe(int id, String name, int servings) {
        this.id = id;
        this.name = name;
        this.servings = servings;
    }

    public Recipe(int id, String name, List<Ingredients> ingredients,
                  List<Steps> steps, int servings) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
    }

    public int getRecipeId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public List<Steps> getSteps() {
        return steps;
    }

    public int getServings() {
        return servings;
    }
}
