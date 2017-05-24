package com.roxybakestudio.jacobbakery.helper;

import android.content.ContentValues;

import com.roxybakestudio.jacobbakery.data.RecipeContract;
import com.roxybakestudio.jacobbakery.model.Ingredients;
import com.roxybakestudio.jacobbakery.model.Recipe;
import com.roxybakestudio.jacobbakery.model.Steps;

public class DatabaseValues {

    public static ContentValues[] getRecipesContent(Recipe recipe) {
        ContentValues[] recipeValues = new ContentValues[1];
        ContentValues recipeValue = new ContentValues();

        recipeValue.put(RecipeContract
                .RecipeMain.COLUMN_RECIPE_ID, recipe.getRecipeId());
        recipeValue.put(RecipeContract
                .RecipeMain.COLUMN_NAME, recipe.getName());
        recipeValue.put(RecipeContract
                .RecipeMain.COLUMN_SERVINGS, recipe.getServings());

        recipeValues[0] = recipeValue;
        return recipeValues;
    }

    public static ContentValues[] getIngredientsValues(Recipe recipe, Ingredients ingredients) {
        ContentValues[] ingredientsValues = new ContentValues[1];

        ContentValues ingredientsValue = new ContentValues();

        ingredientsValue.put(RecipeContract
                .RecipeIngredients.COLUMN_RECIPE_ID, recipe.getRecipeId());
        ingredientsValue.put(RecipeContract
                .RecipeIngredients.COLUMN_RECIPE_NAME, recipe.getName());
        ingredientsValue.put(RecipeContract
                .RecipeIngredients.COLUMN_QUANTITY, ingredients.getQuantity());
        ingredientsValue.put(RecipeContract
                .RecipeIngredients.COLUMN_MEASURE, ingredients.getMeasure());
        ingredientsValue.put(RecipeContract
                .RecipeIngredients.COLUMN_INGREDIENT, ingredients.getIngredient());

        ingredientsValues[0] = ingredientsValue;
        return ingredientsValues;
    }

    public static ContentValues[] getStepsValues(Recipe recipe, Steps steps) {
        ContentValues[] stepsValues = new ContentValues[1];

        ContentValues stepsValue = new ContentValues();

        stepsValue.put(RecipeContract
                .RecipeSteps.COLUMN_RECIPE_ID, recipe.getRecipeId());
        stepsValue.put(RecipeContract
                .RecipeSteps.COLUMN_STEP_ID, steps.getStepsId());
        stepsValue.put(RecipeContract
                .RecipeSteps.COLUMN_SHORT_DESCRIPTION, steps.getShortDescription());
        stepsValue.put(RecipeContract
                .RecipeSteps.COLUMN_DESCRIPTION, steps.getDescription());
        stepsValue.put(RecipeContract
                .RecipeSteps.COLUMN_VIDEO, steps.getUrl());

        stepsValues[0] = stepsValue;
        return stepsValues;
    }
}
