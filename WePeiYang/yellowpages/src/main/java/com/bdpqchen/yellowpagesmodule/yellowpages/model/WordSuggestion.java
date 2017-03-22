package com.bdpqchen.yellowpagesmodule.yellowpages.model;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Created by chen on 17-2-25.
 */

public class WordSuggestion implements SearchSuggestion {

    public String mName;
    private boolean mIsHistory = false;

    public static final Creator<WordSuggestion> CREATOR = new Creator<WordSuggestion>() {
        @Override
        public WordSuggestion createFromParcel(Parcel in) {
            return new WordSuggestion(in);
        }

        @Override
        public WordSuggestion[] newArray(int size) {
            return new WordSuggestion[size];
        }
    };

    public WordSuggestion(String suggestion) {
        this.mName= suggestion.toLowerCase();
    }

    public WordSuggestion(Parcel in) {
        this.mName = in.readString();
        this.mIsHistory = in.readInt() != 0 ;
    }

    @Override
    public String getBody() {
        return mName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeInt(mIsHistory ? 1 : 0);
    }

    public boolean getIsHistory() {
        return mIsHistory;
    }

    public void setIsHistory(boolean mIsHistory) {
        this.mIsHistory = mIsHistory;
    }
}
