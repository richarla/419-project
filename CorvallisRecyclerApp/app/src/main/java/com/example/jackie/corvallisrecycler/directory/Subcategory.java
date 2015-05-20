package com.example.jackie.corvallisrecycler.directory;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jackie on 5/18/2015.
 */
public class Subcategory implements Parcelable {

    private String mName;
    private int mId;

    public Subcategory(){}

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeInt(mId);
    }

    private Subcategory(Parcel in){
        mName = in.readString();
        mId = in.readInt();
    }

    public static final Creator<Subcategory> CREATOR = new Creator<Subcategory>() {
        @Override
        public Subcategory createFromParcel(Parcel source) {
            return new Subcategory(source);
        }

        @Override
        public Subcategory[] newArray(int size) {
            return new Subcategory[size];
        }
    };
}
