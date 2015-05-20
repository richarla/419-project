package com.example.jackie.corvallisrecycler.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jackie.corvallisrecycler.R;
import com.example.jackie.corvallisrecycler.directory.Category;
import com.example.jackie.corvallisrecycler.directory.Subcategory;

/**
 * Created by Jackie on 5/18/2015.
 */
public class SubcategoryAdapter extends RecyclerView.Adapter<SubcategoryAdapter.SubcategoryViewHolder> {

    private Subcategory[] mSubcategory;
    private Context mContext;

    public SubcategoryAdapter(Context context, Subcategory[] subcategory) {
        mSubcategory = subcategory;
        mContext = context;
    }

    @Override
    public SubcategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.subcategory_list_item, viewGroup, false);
        SubcategoryViewHolder viewHolder = new SubcategoryViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SubcategoryAdapter.SubcategoryViewHolder subcategoryViewHolder, int i) {
        subcategoryViewHolder.bindSubcategory(mSubcategory[i]);
    }

    @Override
    public int getItemCount() {
        return mSubcategory.length;
    }

    public class SubcategoryViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        public TextView mName;
        public TextView mId;

        public SubcategoryViewHolder(View itemView) {
            super(itemView);

            mName = (TextView) itemView.findViewById(R.id.nameLabel2);
            mId = (TextView) itemView.findViewById(R.id.idLabel2);

            itemView.setOnClickListener(this);
        }

        public void bindSubcategory(Subcategory subcategory) {
            mName.setText(subcategory.getName());
            mId.setText(subcategory.getId() + "");
        }

        @Override
        public void onClick(View v) {
            String name = mName.getText().toString();
            String id = mId.getText().toString();

            String message = String.format("Subcategory: %s, ID: %s", name, id);
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        }
    }
}