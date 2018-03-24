package com.example.michael.latinhelper;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.michael.latinhelper.Sugar.Phrase;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by michael on 2/25/16.
 */
public class RecyclerViewMyAdapter extends RecyclerView.Adapter<RecyclerViewMyAdapter.RemindViewHolder>{

    List<Phrase> data;


    public RecyclerViewMyAdapter(List<Phrase> data){
        this.data = data;
    }





    @Override
    public RemindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_template, parent, false);
        return new RemindViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RemindViewHolder holder, int position) {
        Phrase it = data.get(position);
        holder.UAtextView.setText(it.getUaString());
        holder.LATtextView.setText(it.getLatString());


        if((position % 2)==0){ /** every second cardView is grey */
            holder.cardView.setCardBackgroundColor(Color.parseColor("#D6D6D6"));
        }else {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#ffffff"));

        }
        Log.d(MainActivity.LOG_TAG,"onBind"+position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void animateTo(List<Phrase> models) {
        applyAndAnimateRemovals(models);   // remove not needed cards
        applyAndAnimateAdditions(models);  // add new cards (if you change query)
        applyAndAnimateMovedItems(models); // moving cards

    }
    private void applyAndAnimateRemovals(List<Phrase> newModels) {
        for (int i = data.size() - 1; i >= 0; i--) {
            final Phrase model = data.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Phrase> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Phrase model = newModels.get(i);
            if (!data.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Phrase> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Phrase model = newModels.get(toPosition);
            final int fromPosition = data.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public Phrase removeItem(int position) {
        final Phrase model = data.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Phrase model) {
        data.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Phrase model = data.remove(fromPosition);
        data.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public static class RemindViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView UAtextView;
        TextView LATtextView;

        public RemindViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.cardView);
            UAtextView = (TextView) itemView.findViewById(R.id.ua_txt);
            LATtextView = (TextView) itemView.findViewById(R.id.lat_txt);
        }
    }
}
