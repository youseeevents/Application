package com.example.youseeeventsv1;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class MyAdapter extends android.support.v7.widget.RecyclerView.Adapter {

    private ArrayList<Event> mDataset;
    private OnItemClickListener listener;
    private OnItemLongClickListener listenerLong;

    /**
     * Constructor
     */
    public MyAdapter(ArrayList<Event> myDataset, OnItemClickListener listener) {
        mDataset = myDataset;
        this.listener = listener;
        this.listenerLong = null;
    }
    public MyAdapter(ArrayList<Event> myDataset, OnItemClickListener listener, OnItemLongClickListener listenerLong) {
        mDataset = myDataset;
        this.listener = listener;
        this.listenerLong = listenerLong;
    }

    public interface OnItemClickListener {
        void onItemClick(Event v);
    }
    public interface OnItemLongClickListener {
        boolean onItemLongClick(Event v);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView e_name;
        public TextView e_date;
        public TextView e_location;
        public LinearLayout organizer_buttons;
        public Button delete_button;
        public Button edit_button;
        public Button cancel_button;

        public MyViewHolder(@NonNull ConstraintLayout v) {
            super(v);
            view = v;
            e_name = view.findViewById(R.id.event_name_text);
            e_date = view.findViewById(R.id.event_date_text);
            e_location = view.findViewById(R.id.event_location_text);

            organizer_buttons = view.findViewById(R.id.org_buttons);
            delete_button = view.findViewById(R.id.delete_button);
            edit_button = view.findViewById(R.id.edit_button);
            cancel_button = view.findViewById(R.id.cancel_button);

            delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            edit_button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                }
            });

            cancel_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    organizer_buttons.setVisibility(View.GONE);
                }
            });
        }

        public void bind(final Event item, final OnItemClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    System.out.println("Short click");
                    listener.onItemClick(item);
                }
            });
        }
        public void bind(final Event item, final OnItemLongClickListener listenerLong){
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    organizer_buttons.setVisibility(View.VISIBLE);
                    return listenerLong.onItemLongClick(item);
                }
            });
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        ConstraintLayout v = (ConstraintLayout)LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_object_event, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if(mDataset.get(position)!=null) {
            String name = mDataset.get(position).getName();
            String date = mDataset.get(position).getDate_readable();
            String location = mDataset.get(position).getLocation();
            if(name.length() > 34){
                name = name.substring(0, 30) + "...";
            }
            if(date.length() > 34){
                date = date.substring(0, 30) + "...";
            }
            if(location.length() > 34){
                location = location.substring(0, 30) + "...";
            }
            ((MyViewHolder) holder).e_name.setText(name);
            ((MyViewHolder) holder).e_date.setText(date);
            ((MyViewHolder) holder).e_location.setText(location);
        }
        ((MyViewHolder)holder).bind(mDataset.get(position), listener);
        if(listenerLong != null)
            ((MyViewHolder)holder).bind(mDataset.get(position), listenerLong);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void swap(ArrayList<Event> datas)
    {
        mDataset.clear();
        mDataset.addAll(datas);
        notifyDataSetChanged();
    }

    public void append(ArrayList<Event> data){
        mDataset.addAll(data);
        Collections.sort(mDataset, new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                if(o1 == null){
                    return -1;
                }
                if(o2 == null){
                    return 1;
                }
                if(o1.getDate() == null){
                    return -1;
                }
                if(o2.getDate() == null){
                    return 1;
                }
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        notifyDataSetChanged();
    }

}