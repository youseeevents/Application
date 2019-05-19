package com.example.youseeeventsv1;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

class MyAdapter extends android.support.v7.widget.RecyclerView.Adapter {

    private Event[] mDataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView e_name;
        public TextView e_date;
        public TextView e_location;
        public MyViewHolder(@NonNull ConstraintLayout v) {
            super(v);
            view = v;
            e_name = view.findViewById(R.id.event_name_text);
            e_date = view.findViewById(R.id.event_date_text);
            e_location = view.findViewById(R.id.event_location_text);
        }
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Event[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        ConstraintLayout v = (ConstraintLayout)LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if(mDataset[position]!=null) {
            ((MyViewHolder) holder).e_name.setText(mDataset[position].getName());
            ((MyViewHolder) holder).e_date.setText(mDataset[position].getDate());
            ((MyViewHolder) holder).e_location.setText(mDataset[position].getLocation());
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}