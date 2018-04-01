package com.deeper.bakingapp.ui;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.deeper.bakingapp.R;
import com.deeper.bakingapp.data.network.api.ApiEndPointHandler;
import com.deeper.bakingapp.data.network.api.ApiEndpointInterfaces;
import com.deeper.bakingapp.data.network.model.BakingResponse;
import com.deeper.bakingapp.databinding.ActivityMainBinding;
import com.deeper.bakingapp.ui.adapter.RecipeListAdapter;
import com.deeper.bakingapp.utils.Utility;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,
        FragmentBakingRecipeList.OnFragmentInteractionListener {

    private ActivityMainBinding mBinding;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private ArrayList<BakingResponse> recipeList = new ArrayList<>();
    private RecipeListAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //Stetho.initializeWithDefaults(this);
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());

        setSupportActionBar(mBinding.toolbar);
        initUI();
    }

    private void initUI() {
        // Pull to Refresh
        swipeRefreshLayout = findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView = findViewById(R.id.rv_main);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, Utility.getSpan(this)));
        mRecyclerView.setHasFixedSize(true);

        fetchingData();
    }

    private void fetchingData() {
        showProgressBar();

        ApiEndpointInterfaces apiService = ApiEndPointHandler.getApiService(this);
        Call<BakingResponse> responseBaking = apiService.getDesserts();

        responseBaking.enqueue(new Callback<BakingResponse>() {
            @Override
            public void onResponse(Call<BakingResponse> call, Response<BakingResponse> response) {
                if (response.isSuccessful()){
                    recipeList.add(response.body());
                }
            }

            @Override
            public void onFailure(Call<BakingResponse> call, Throwable t) {
                hideProgressBar();

                Snackbar.make(mBinding.coordinatorView, R.string.error_internet_connection,
                        Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onRefresh() {

    }

    private void showProgressBar() {
        swipeRefreshLayout.setRefreshing(true);
    }

    private void hideProgressBar() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
