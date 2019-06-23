package me.timgu.enghack2019;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AllergyListActivity extends AppCompatActivity implements
        NewAllergyItemDialogueFragment.NewAllergyItemDialogueListener {

    private AllergyAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private AllergyListManager mALM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergy_list);

        mALM = new AllergyListManager(this);

        mRecyclerView = findViewById(R.id.allergy_recyclerView);
        mAdapter = new AllergyAdapter(this, mALM.getAllItemsAsList());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void addItem(View view) {
        DialogFragment dialog = new NewAllergyItemDialogueFragment();
        dialog.show(getSupportFragmentManager(),"NewDeckDialogue");
    }

    @Override
    public void onNewAllergyItemDialoguePositiveClick(DialogFragment dialog, String msg) throws IOException {
        mALM.add(msg,0);
        mAdapter.updateList(mALM.getAllItemsAsList());
        dialog.dismiss();
    }

    @Override
    public void onNewAllergyItemDialogueNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }
}
