package me.timgu.enghack2019;

import android.content.Context;

import android.support.annotation.NonNull;

import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.util.List;

public class MainListAdapter extends
        RecyclerView.Adapter<MainListAdapter.ItemViewHolder>{
    private List<String> mLabelList;
    private LayoutInflater mInflater;
    public static final String EXTRA_FILENAME =
            "me.timgu.flashmemorize.extra.FILENAME";
    private Context context;

    public MainListAdapter(Context context, List<String> labelList){
        mInflater = LayoutInflater.from(context); //what the heck does this mean?\
        this.mLabelList= labelList;
        this.context = context;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder
            //implements View.OnClickListener, View.OnLongClickListener
    {

        TextView textItemView;
        public ItemViewHolder(View itemView, MainListAdapter adapter){
            super(itemView);
            textItemView = itemView.findViewById(R.id.main_list_text);
        }

       /* @Override
        public void onClick(View v) {

            int mPosition = getLayoutPosition();

        }
        */
    }
    @NonNull
    @Override
    public MainListAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.main_list_item,parent,false);
        return new ItemViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MainListAdapter.ItemViewHolder itemViewHolder, int i) {
        String current = mLabelList.get(i);
        itemViewHolder.textItemView.setText(current);

    }

    @Override
    public int getItemCount() {
        int count = mLabelList.size();
        return count;
    }

    public void updateList(List<String> labelList){
        mLabelList = labelList;
        this.notifyDataSetChanged();//can be more specific, but this is safest
    }
}
