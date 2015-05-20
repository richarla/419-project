package com.example.jackie.corvallisrecycler.directory;

/**
 * Created by Jackie on 5/18/2015.
 */
public class Data {

    private Category[] mCategory;
    private Subcategory[] mSubcategory;

    public Subcategory[] getSubcategory() {
        return mSubcategory;
    }

    public void setSubcategory(Subcategory[] subcategory) {
        mSubcategory = subcategory;
    }

    public Category[] getCategory() {
        return mCategory;
    }

    public void setCategory(Category[] category) {
        mCategory = category;
    }
}
