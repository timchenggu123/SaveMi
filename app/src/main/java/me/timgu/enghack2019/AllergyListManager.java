package me.timgu.enghack2019;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AllergyListManager {
    private Context mContext;
    public String ALLERGY_LIST_FILE = "me.timgu.allergyList";

    AllergyListManager(Context context){
        mContext = context;
    }

    private SharedPreferences getAllergyList(){
        SharedPreferences allergyList;
        allergyList = mContext.getSharedPreferences(ALLERGY_LIST_FILE, Context.MODE_PRIVATE);
        return allergyList;
    }
    public void add(String item, Integer Severity){
        SharedPreferences.Editor DeckListEditor = getAllergyList().edit();
        DeckListEditor.putInt(item, Severity);
        DeckListEditor.apply();
    }

    public void remove(String item){
        SharedPreferences.Editor DeckListEditor = getAllergyList().edit();
        DeckListEditor.remove(item);
        DeckListEditor.apply();
    }

    public List<String> getAllItemsAsList(){
        Map<String,?> AllergyList;
        AllergyList = getAllergyList().getAll();
        List<String> items = new ArrayList<>(AllergyList.keySet());
        return items;
    }

    public List<?> getAllServerityAsList(){
        Map<String,?> AllergyList;
        AllergyList = getAllergyList().getAll();
        List<?> items = new ArrayList<>(AllergyList.values());
        return items;
    }
}
