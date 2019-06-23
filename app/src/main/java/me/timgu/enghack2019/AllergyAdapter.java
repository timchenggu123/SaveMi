package me.timgu.enghack2019;

import android.content.Context;

import android.support.annotation.NonNull;

import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class AllergyAdapter extends
        RecyclerView.Adapter<AllergyAdapter.ItemViewHolder>{
    private List<String> mAllergyList;
    private LayoutInflater mInflater;
    public static final String EXTRA_FILENAME =
            "me.timgu.flashmemorize.extra.FILENAME";
    private Context context;
    private AllergyListManager mALM;

    public AllergyAdapter(Context context, List<String> AllergyList){
        mInflater = LayoutInflater.from(context); //what the heck does this mean?\
        mALM = new AllergyListManager(context);
        this.mAllergyList= AllergyList;
        this.context = context;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        public final Button mDeleteButton;
        public final TextView textItemView;

        public ItemViewHolder(View itemView){
            super(itemView);
            textItemView = itemView.findViewById(R.id.main_list_text);
            mDeleteButton = itemView.findViewById(R.id.allergy_list_item_delete);
            mDeleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int mPosition = getLayoutPosition();
            if (v.getId() == R.id.allergy_list_item_delete){
                mALM.remove(mAllergyList.get(mPosition));
                updateList(mALM.getAllItemsAsList());
            }
        }
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.allergy_list_item,parent,false);
        return new ItemViewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        String current = mAllergyList.get(i);
        itemViewHolder.textItemView.setText(current);
    }

    @Override
    public int getItemCount() {
        int count = mAllergyList.size();
        return count;
    }

    public void updateList(List<String> AllergyList){
        mAllergyList = AllergyList;
        this.notifyDataSetChanged();//can be more specific, but this is safest
    }
}
