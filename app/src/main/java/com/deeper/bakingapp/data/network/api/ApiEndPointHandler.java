package com.deeper.bakingapp.data.network.api;

import android.content.Context;
import android.util.Log;

import com.deeper.bakingapp.utils.Params;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by paoloc on 26/01/17.
 */

public class ApiEndPointHandler {

    private static final String TAG = "ApiEndPointHandler";
    private static final String API_KEY = "api_key";

    public static ApiEndpointInterfaces getApiService(Context context) {
        return getApiService(context, true, Params.ENDPOINT);
    }

    public static ApiEndpointInterfaces getApiService(Context context, boolean converter) {
        return getApiService(context, converter, Params.ENDPOINT);
    }

    public static ApiEndpointInterfaces getApiService(Context context, boolean useConverter, String ENDPOINT) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder();
                Request request = requestBuilder.build();
                Log.d(TAG, request.toString());
                return chain.proceed(request);

            }
        }).build();

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        if (useConverter) {
            retrofitBuilder
                    .baseUrl(ENDPOINT)
                    .client(okHttpClient);

            Gson gson = new GsonBuilder().registerTypeAdapterFactory(new WorkaroundGson()).create();
            retrofitBuilder.addConverterFactory(GsonConverterFactory.create(gson));
            retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        } else {
            retrofitBuilder
                    .baseUrl(ENDPOINT)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create());
        }

        return retrofitBuilder.build().create(ApiEndpointInterfaces.class);
    }
}
