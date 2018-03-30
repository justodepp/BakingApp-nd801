package com.deeper.bakingapp.data.network.api;

import com.deeper.bakingapp.data.network.model.BakingResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by paoloc on 26/01/17.
 */

public interface ApiEndpointInterfaces {

    @GET
    Call<BakingResponse> getDesserts();
}
