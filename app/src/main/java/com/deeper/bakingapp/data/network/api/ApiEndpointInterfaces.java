package com.deeper.bakingapp.data.network.api;

import com.deeper.bakingapp.data.model.Recipe;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by paoloc on 26/01/17.
 */

public interface ApiEndpointInterfaces {

    @GET("baking.json")
    Call<Recipe[]> getDesserts();
}
