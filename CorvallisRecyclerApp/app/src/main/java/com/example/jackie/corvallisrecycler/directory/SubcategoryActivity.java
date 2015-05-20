package com.example.jackie.corvallisrecycler.directory;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.jackie.corvallisrecycler.R;
import com.example.jackie.corvallisrecycler.adapters.CategoryAdapter;
import com.example.jackie.corvallisrecycler.adapters.SubcategoryAdapter;
import com.example.jackie.corvallisrecycler.ui.MainActivity;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SubcategoryActivity extends ActionBarActivity {

    private Subcategory[] mSubcategory;

    @InjectView(R.id.recylcerView2)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(CategoryAdapter.SUBCATEGORY);
        mSubcategory = Arrays.copyOf(parcelables, parcelables.length, Subcategory[].class);

        SubcategoryAdapter adapter = new SubcategoryAdapter(this, mSubcategory);
        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
    }
}
