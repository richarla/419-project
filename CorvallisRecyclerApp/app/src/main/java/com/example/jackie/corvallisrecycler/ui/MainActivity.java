package com.example.jackie.corvallisrecycler.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.example.jackie.corvallisrecycler.directory.Category;
import com.example.jackie.corvallisrecycler.directory.CategoryActivity;
import com.example.jackie.corvallisrecycler.directory.Data;
import com.example.jackie.corvallisrecycler.R;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String CATEGORY = "CATEGORY";

    private Data mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        String apiUrl = "http://www.recycling-app-13.appspot.com/category";

        getDirectory(apiUrl);

        Log.d(TAG, "Main UI code is running!");
    }

    private void getDirectory(String apiUrl) {

        if (isNetworkAvailable()) {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mData = parseCategoryDetail(jsonData);
                        } else {
                            alertUserAboutError();
                        }
                    }
                    catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                    catch (JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        }
        else {
            Toast.makeText(this, getString(R.string.network_unavailable_message),
                    Toast.LENGTH_LONG).show();
        }
    }

    private Data parseCategoryDetail(String jsonData) throws JSONException{
        Data data = new Data();

        data.setCategory(getCategoryDetails(jsonData));

        return data;
    }

    private Category[] getCategoryDetails(String jsonData) throws JSONException {

        JSONArray data = new JSONArray(jsonData);

        Category[] cats = new Category[data.length()];

        for(int i = 0; i < data.length(); i++){
            JSONObject jsonItem = data.getJSONObject(i);
            Category cat = new Category();

            cat.setName(jsonItem.getString("name"));
            cat.setId(jsonItem.getInt("id"));

            cats[i] = cat;
        }
        return cats;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

    @OnClick(R.id.button)
    public void startCategoryActivity(View view){

        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra(CATEGORY, mData.getCategory());
        startActivity(intent);
    }
}