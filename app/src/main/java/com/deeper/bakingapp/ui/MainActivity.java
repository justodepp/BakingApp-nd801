package com.deeper.bakingapp.ui;

import android.content.Intent;
import android.content.res.Configuration;
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
import com.deeper.bakingapp.data.network.model.BakingStep;
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
        RecipeListAdapter.RecipeClickListener, FragmentRecipeDetailsList.OnStepClicked {

    private static final String RECIPE_LIST_KEY = "recipe_list";
    private static final String KEY_FIRST_VISIBLE_ITEM_POS = "first_visible_item_pos";
    private static final String SINGLE_RECIPE_KEY = "single_recipe";

    private ActivityMainBinding mBinding;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private TextView mTextError;
    private int firstVisibleItemPosition = -1;

    private ArrayList<BakingResponse> recipeList = new ArrayList<>();
    private RecipeListAdapter recipeAdapter;

    private BakingResponse mRecipe;

    private FragmentRecipeDetailsList fragmentRecipeDetailsList;

    private boolean mLandscape;
    private boolean mIsTablet;

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

        mIsTablet = getResources().getBoolean(R.bool.isTablet);
        mLandscape = getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE;

        initUI();
        gettingData(savedInstanceState);
    }

    private void gettingData(Bundle savedInstanceState) {
        if (Utility.isDeviceOnline(this)) {
            if (savedInstanceState != null) {
                hideProgressBar();

                mRecipe = savedInstanceState.getParcelable(SINGLE_RECIPE_KEY);
                recipeList = savedInstanceState.getParcelableArrayList(RECIPE_LIST_KEY);

                if (recipeList == null)
                    onRefresh();
                else if (recipeList.size() > 0) {
                    if (savedInstanceState.containsKey(KEY_FIRST_VISIBLE_ITEM_POS))
                        firstVisibleItemPosition = savedInstanceState.getInt(KEY_FIRST_VISIBLE_ITEM_POS);
                    else
                        firstVisibleItemPosition = 0;
                    mRecyclerView.smoothScrollToPosition(firstVisibleItemPosition);

                    RecipeListAdapter adapter = (RecipeListAdapter) mRecyclerView.getAdapter();
                    if (adapter == null) {
                        adapter = new RecipeListAdapter(MainActivity.this, recipeList, MainActivity.this);
                        mRecyclerView.setAdapter(adapter);
                    }

                    adapter.setList(recipeList);
                    adapter.notifyDataSetChanged();
                }
            } else {
                fetchingData();
            }
        } else {
            showErrorMessage();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (recipeList != null)
            outState.putParcelableArrayList(RECIPE_LIST_KEY, new ArrayList<>(recipeList));
        if (mRecipe != null)
            outState.putParcelable(SINGLE_RECIPE_KEY, mRecipe);

        outState.putInt(KEY_FIRST_VISIBLE_ITEM_POS, firstVisibleItemPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        gettingData(savedInstanceState);
    }

    private void initUI() {
        // Pull to Refresh
        swipeRefreshLayout = findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(this);

        final GridLayoutManager layoutManager = new GridLayoutManager(this, Utility.getSpan(this));
        mRecyclerView = findViewById(R.id.rv_main);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            }
        });

        mTextError = findViewById(R.id.error_no_connection);

        if (mIsTablet && mLandscape) {
            fragmentRecipeDetailsList = (FragmentRecipeDetailsList) getSupportFragmentManager()
                    .findFragmentById(R.id.recipe_detail_fragment);

            setDetailsPanel();
        }
    }

    private void setDetailsPanel() {
        if (mRecipe == null) {
            mBinding.labelRecipeNotSelected.setVisibility(View.VISIBLE);
            findViewById(R.id.recipe_detail_fragment).setVisibility(View.GONE);
        } else {
            mBinding.labelRecipeNotSelected.setVisibility(View.GONE);
            findViewById(R.id.recipe_detail_fragment).setVisibility(View.VISIBLE);

            fragmentRecipeDetailsList.updateRecipe(mRecipe);
        }
    }

    private void fetchingData() {
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
    }

    @Override
    public void onRefresh() {
        if (recipeList.size() != 0)
            recipeList.clear();
        fetchingData();
    }

    private void showProgressBar() {
        swipeRefreshLayout.setRefreshing(true);
    }

    private void hideProgressBar() {
        swipeRefreshLayout.setRefreshing(false);
    }

    private void showErrorMessage() {
        hideProgressBar();
        mRecyclerView.setVisibility(View.GONE);
        mTextError.setVisibility(View.VISIBLE);
    }

    private void showData() {
        hideProgressBar();
        mRecyclerView.setVisibility(View.VISIBLE);
        mTextError.setVisibility(View.GONE);
    }

    @Override
    public void onClickRecipeItem(BakingResponse recipe) {
        mRecipe = recipe;

        if (mIsTablet && mLandscape) {
            setDetailsPanel();
        } else {
            Intent intent = new Intent(this, RecipeDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("recipe", recipe);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onClickedStep(int position, BakingStep step) {

    }
}
