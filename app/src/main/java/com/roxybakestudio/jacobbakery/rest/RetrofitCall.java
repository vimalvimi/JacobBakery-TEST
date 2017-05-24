package com.roxybakestudio.jacobbakery.rest;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.roxybakestudio.jacobbakery.data.RecipeContract;
import com.roxybakestudio.jacobbakery.helper.DatabaseValues;
import com.roxybakestudio.jacobbakery.model.Ingredients;
import com.roxybakestudio.jacobbakery.model.Recipe;
import com.roxybakestudio.jacobbakery.model.Steps;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitCall {

    private static final String TAG = "RetrofitCall";

    public static void getRecipes(final Context context) {

        RecipeService recipeService = RecipeService.retrofit.create(RecipeService.class);
        Call<List<Recipe>> listCall = recipeService.getAllRecipes();
        listCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    ContentResolver contentResolver = context.getContentResolver();
                    List<Recipe> recipes = response.body();

                    for (Recipe recipe : recipes) {

                        //Delete Previous Results
                        contentResolver.delete(RecipeContract.RecipeMain
                                .BuildRecipeUriWithId(recipe.getRecipeId()), null,
                                new String[]{String.valueOf(recipe.getRecipeId())});

                        contentResolver.delete(RecipeContract.RecipeIngredients
                                .buildIngredientUriWithId(recipe.getRecipeId()), null,
                                new String[]{String.valueOf(recipe.getRecipeId())});

                        contentResolver.delete(RecipeContract.RecipeSteps
                                .BuildStepUriWithId(recipe.getRecipeId()), null,
                                new String[]{String.valueOf(recipe.getRecipeId())});

                        //Add Recipe
                        ContentValues[] recipeValues = DatabaseValues
                                .getRecipesContent(recipe);

                        if (recipeValues != null && recipeValues.length != 0) {
                            contentResolver
                                    .bulkInsert(RecipeContract.RecipeMain.CONTENT_URI,
                                            recipeValues);
                        }

                        //Add Ingredients
                        List<Ingredients> ingredientsList = recipe.getIngredients();

                        for (Ingredients ingredient : ingredientsList) {
                            ContentValues[] ingredientsValues = DatabaseValues
                                    .getIngredientsValues(recipe, ingredient);

                            if (ingredientsValues != null && ingredientsValues.length != 0) {
                                contentResolver
                                        .bulkInsert(RecipeContract.RecipeIngredients.CONTENT_URI,
                                                ingredientsValues);
                            }
                        }

                        //AddSteps
                        List<Steps> stepsList = recipe.getSteps();

                        for (Steps step : stepsList) {
                            ContentValues[] stepsValues = DatabaseValues
                                    .getStepsValues(recipe, step);

                            if (stepsValues != null && stepsValues.length != 0) {
                                contentResolver
                                        .bulkInsert(RecipeContract.RecipeSteps.CONTENT_URI,
                                                stepsValues);
                            }
                        }
                    }
                } else {
                    Log.d(TAG, "RESPONSE CODE ERROR " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
