package com.roxybakestudio.jacobbakery.rest;

import com.roxybakestudio.jacobbakery.helper.Constants;
import com.roxybakestudio.jacobbakery.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface RecipeService {

    @GET("baking.json")
    Call<List<Recipe>> getAllRecipes();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
