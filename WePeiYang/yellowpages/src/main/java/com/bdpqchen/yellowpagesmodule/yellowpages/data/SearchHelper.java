package com.bdpqchen.yellowpagesmodule.yellowpages.data;

import com.bdpqchen.yellowpagesmodule.yellowpages.model.History;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.SearchResult;
import com.bdpqchen.yellowpagesmodule.yellowpages.model.WordSuggestion;

import java.util.ArrayList;
import java.util.List;

public class SearchHelper {

    public interface OnFindWordListener {
        void onResults(List<SearchResult> results);
    }

    public static void findWord(String query, String lastQuery, final OnFindWordListener listener) {
        List<SearchResult> results = DataManager.findWord(query, lastQuery);
        if (listener != null && null != results) {
            listener.onResults(results);
        }
    }

    public static List<WordSuggestion> getHistory(int count) {
        List<History> histories = DataManager.getHistory(count);
        List<WordSuggestion> wordSuggestions = new ArrayList<>();
        if (null != histories && histories.size() > 0) {
            for (int i = 0; i < histories.size(); i++) {
                WordSuggestion wordSuggestion = new WordSuggestion(histories.get(i).getText());
                wordSuggestions.add(wordSuggestion);
            }
        }
        return wordSuggestions;
    }

    public interface OnFindSuggestionsListener {
        void onResults(List<WordSuggestion> results);
    }

    public static void findSuggestions(final String query, final int limit, final OnFindSuggestionsListener listener) {
        List<SearchResult> resultList = DataManager.findSuggestions(query, limit);
        List<WordSuggestion> wordSuggestions = new ArrayList<>();
        if (null != resultList && resultList.size() > 0) {
            for (int i = 0; i < resultList.size(); i++) {
                WordSuggestion wordSuggestion = new WordSuggestion(resultList.get(i).name);
                wordSuggestions.add(wordSuggestion);
            }
        }
        if (listener != null) {
            listener.onResults(wordSuggestions);
        }

    }


}