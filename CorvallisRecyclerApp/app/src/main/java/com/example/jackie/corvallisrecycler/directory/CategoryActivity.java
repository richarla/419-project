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
import com.example.jackie.corvallisrecycler.ui.MainActivity;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CategoryActivity extends ActionBarActivity {

    private Category[] mCategory;

    @InjectView(R.id.recylcerView)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.CATEGORY);
        mCategory = Arrays.copyOf(parcelables, parcelables.length, Category[].class);

        CategoryAdapter adapter = new CategoryAdapter(this, mCategory);
        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);
    }
}
