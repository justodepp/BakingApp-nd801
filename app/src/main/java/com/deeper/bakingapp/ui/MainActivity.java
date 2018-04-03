package com.deeper.bakingapp.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.deeper.bakingapp.BuildConfig;
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
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,
        RecipeListAdapter.RecipeClickListener {

    private ActivityMainBinding mBinding;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private TextView mTextError;

    private ArrayList<BakingResponse> recipeList = new ArrayList<>();
    private RecipeListAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

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

        mTextError = findViewById(R.id.error_no_connection);

        fetchingData();
    }

    private void fetchingData() {
        if(Utility.isDeviceOnline(this)) {
            showProgressBar();

            ApiEndpointInterfaces apiService = ApiEndPointHandler.getApiService(this, false);
            Call<BakingResponse[]> responseBaking = apiService.getDesserts();

            responseBaking.enqueue(new Callback<BakingResponse[]>() {
                @Override
                public void onResponse(Call<BakingResponse[]> call, Response<BakingResponse[]> response) {
                    if (response.isSuccessful()) {
                        for (int i = 0; i < response.body().length; i++) {
                            recipeList.add(response.body()[i]);
                        }

                        recipeAdapter = new RecipeListAdapter(MainActivity.this, recipeList, MainActivity.this);
                        mRecyclerView.setAdapter(recipeAdapter);

                        showData();
                    }
                }

                @Override
                public void onFailure(Call<BakingResponse[]> call, Throwable t) {
                    hideProgressBar();

                    Snackbar.make(mBinding.coordinatorView, R.string.error_internet_connection,
                            Snackbar.LENGTH_LONG).show();
                }
            });
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onRefresh() {
        if(recipeList.size() != 0)
            recipeList.clear();
        fetchingData();
    }

    private void showProgressBar() {
        swipeRefreshLayout.setRefreshing(true);
    }

    private void hideProgressBar() {
        swipeRefreshLayout.setRefreshing(false);
    }

    private void showErrorMessage(){
        hideProgressBar();
        mRecyclerView.setVisibility(View.GONE);
        mTextError.setVisibility(View.VISIBLE);
    }

    private void showData(){
        hideProgressBar();
        mRecyclerView.setVisibility(View.VISIBLE);
        mTextError.setVisibility(View.GONE);
    }

    @Override
    public void onClickRecipeItem(BakingResponse recipe) {
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("recipe", recipe);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
