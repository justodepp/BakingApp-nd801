package com.deeper.bakingapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deeper.bakingapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentBakingRecipeList} factory method to
 * create an instance of this fragment.
 */
public class FragmentBakingRecipeList extends Fragment {
    public FragmentBakingRecipeList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_baking_recipe_list, container, false);
    }
}
