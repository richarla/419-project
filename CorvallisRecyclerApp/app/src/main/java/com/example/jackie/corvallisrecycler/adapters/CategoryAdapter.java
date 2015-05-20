package com.example.jackie.corvallisrecycler.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jackie.corvallisrecycler.R;
import com.example.jackie.corvallisrecycler.directory.Category;
import com.example.jackie.corvallisrecycler.directory.CategoryActivity;
import com.example.jackie.corvallisrecycler.directory.Data;
import com.example.jackie.corvallisrecycler.directory.Subcategory;
import com.example.jackie.corvallisrecycler.directory.SubcategoryActivity;
import com.example.jackie.corvallisrecycler.ui.AlertDialogFragment;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.Handler;

import butterknife.OnClick;

/**
 * Created by Jackie on 5/18/2015.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Category[] mCategory;
    private Context mContext;

    private Data mData;

    public static final String TAG = CategoryAdapter.class.getSimpleName();
    public static final String SUBCATEGORY = "SUBCATEGORY";

    public CategoryAdapter(Context context, Category[] category) {
        mCategory = category;
        mContext = context;
    }

    @Override //1, 4, 6, 9
    public CategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.category_list_item, viewGroup, false);
        CategoryViewHolder viewHolder = new CategoryViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.CategoryViewHolder categoryViewHolder, int i) { //3, 7
        categoryViewHolder.bindCategory(mCategory[i]);
    }

    @Override
    public int getItemCount() {
        return mCategory.length;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        public TextView mName;
        public TextView mId;

        public CategoryViewHolder(View itemView) { //2, 5
            super(itemView);

            mName = (TextView) itemView.findViewById(R.id.nameLabel);
            mId = (TextView) itemView.findViewById(R.id.idLabel);

            itemView.setOnClickListener(this);
        }

        public void bindCategory(Category category) {
            mName.setText(category.getName());
            mId.setText(category.getId() + "");
        }

        @Override
        public void onClick(View v) {
            String id = mId.getText().toString();

            //String name = mName.getText().toString();
            //String message = String.format("Category: %s, ID: %s", name, id);
            //Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();

            String apiUrl = "http://www.recycling-app-13.appspot.com/category/" + id;

            getDirectory(apiUrl);
        }
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

                            if (response.isSuccessful()) {
                                Log.v(TAG, jsonData);
                                mData = parseSubcategoryDetail(jsonData);
                                Intent intent = new Intent(mContext, SubcategoryActivity.class); //.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra(SUBCATEGORY, mData.getSubcategory());
                                mContext.startActivity(intent);
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
                Toast.makeText(mContext, "Network is unavailable!",
                        Toast.LENGTH_LONG).show();
            }


        }

        private Data parseSubcategoryDetail(String jsonData) throws JSONException{
            Data data = new Data();

            data.setSubcategory(getSubcategoryDetails(jsonData));

            return data;
        }

        private Subcategory[] getSubcategoryDetails(String jsonData) throws JSONException {

            JSONObject data1 = new JSONObject(jsonData);
            JSONArray data = data1.getJSONArray("subcategories");

            Subcategory[] subs = new Subcategory[data.length()];

            for(int i = 0; i < data.length(); i++){

                //Log.v(TAG, data.length() + "" );
                JSONObject jsonItem = data.getJSONObject(i);
                Subcategory sub = new Subcategory();

                sub.setName(jsonItem.getString("name"));
                sub.setId(jsonItem.getInt("id"));

                subs[i] = sub;
                //Log.v(TAG, sub.getName().toString());
            }
            return subs;
        }

        private boolean isNetworkAvailable() {
            ConnectivityManager manager = (ConnectivityManager)
                    mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            boolean isAvailable = false;
            if (networkInfo != null && networkInfo.isConnected()) {
                isAvailable = true;
            }

            return isAvailable;
        }

        private void alertUserAboutError() {
            AlertDialogFragment dialog = new AlertDialogFragment();
            Log.e(TAG, dialog.toString());
        }
    }
