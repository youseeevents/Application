package com.example.youseeeventsv1;

import android.media.Image;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

class MyAdapter extends android.support.v7.widget.RecyclerView.Adapter {

    private ArrayList<Event> mDataset;
    private OnItemClickListener listener;
    private OnItemLongClickListener listenerLong;

    /**
     * Constructor with click listener
     */
    public MyAdapter(ArrayList<Event> myDataset, OnItemClickListener listener) {
        mDataset = myDataset;
        this.listener = listener;
        this.listenerLong = null;
    }

    /**
     * Constructor with click and long click listener
     */
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
        public TextView e_popularity;
        public ImageView e_image;
        public LinearLayout organizer_buttons;
        public Button delete_button;
        public Button edit_button;
        public Button cancel_button;
        private boolean pushed_up = false;

        public MyViewHolder(@NonNull ConstraintLayout v) {
            super(v);
            view = v;
            e_name = view.findViewById(R.id.event_name_text);
            e_date = view.findViewById(R.id.event_date_text);
            e_location = view.findViewById(R.id.event_location_text);
            e_popularity = view.findViewById(R.id.event_popularity_text);
            e_image = view.findViewById(R.id.event_img);

            organizer_buttons = view.findViewById(R.id.org_buttons);
            delete_button = view.findViewById(R.id.delete_button);
            edit_button = view.findViewById(R.id.edit_button);
            cancel_button = view.findViewById(R.id.cancel_button);
        }

        public void bind(final Event item, final OnItemClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    listener.onItemClick(item);
                }
            });
        }
        public void bind(final Event item, final OnItemLongClickListener listenerLong){
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    //slide_views_from_left(v, organizer_buttons);
                    if(!pushed_up) {
                        view.animate().translationX(view.getX() + organizer_buttons.getWidth()).start();
                        pushed_up = true;
                    }
                    // Delete Button Functionality
                    delete_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Events")
                                    .child(item.getEventId())
                                    .removeValue();
                            String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Users")
                                    .child(username)
                                    .child("created_events")
                                    .child(item.getEventId())
                                    .removeValue();
                            view.animate().translationX(view.getX() - organizer_buttons.getWidth()).start();
                            pushed_up = false;
                            itemView.setVisibility(View.GONE);
                        }
                    });
                    // Edit Button Functionality
                    edit_button.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            Intent event_info_intent = new Intent(v.getContext(), EditEventActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("Event", item);
                            event_info_intent.putExtras(bundle);
                            v.getContext().startActivity(event_info_intent);
                        }
                    });
                    // Cancel Button Functionality
                    cancel_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //slide_views_from_right(v, organizer_buttons);
                            view.animate().translationX(view.getX() - organizer_buttons.getWidth()).start();
                            pushed_up = false;
                        }
                    });
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
            String tag = mDataset.get(position).getTag();
            int pop_count = mDataset.get(position).getCounterGoing();
            String popularity;
            if(pop_count == 1){
                popularity = mDataset.get(position).getCounterGoing() + " person is interested!";
            }
            else {
                popularity = mDataset.get(position).getCounterGoing() + " people are interested!";
            }
            if(name.length() > 42){
                name = name.substring(0, 39) + "...";
            }
            if(date.length() > 42){
                date = date.substring(0, 39) + "...";
            }
            if(location.length() > 42){
                location = location.substring(0, 39) + "...";
            }
            if(popularity.length() > 42){
                popularity = popularity.substring(0, 39) + "...";
            }
            ((MyViewHolder) holder).e_name.setText(name);
            ((MyViewHolder) holder).e_date.setText(date);
            ((MyViewHolder) holder).e_location.setText(location);
            ((MyViewHolder) holder).e_popularity.setText(popularity);
            switch(tag){
                case "arts & culture":
                    ((MyViewHolder) holder).e_image.setImageResource(R.drawable.art_icon);
                    break;
                case "fitness & well-being":
                    ((MyViewHolder) holder).e_image.setImageResource(R.drawable.fitness_icon);
                    break;
                case "athletics":
                    ((MyViewHolder) holder).e_image.setImageResource(R.drawable.ic_directions_run_black_24dp);
                    break;
                case "seminars & info-sessions":
                    ((MyViewHolder) holder).e_image.setImageResource(R.drawable.seminar_icon);
                    break;
                case "community":
                    ((MyViewHolder) holder).e_image.setImageResource(R.drawable.ic_group_24dp);
                    break;
                case "weekend event":
                    ((MyViewHolder) holder).e_image.setImageResource(R.drawable.weekend_icon);
                    break;
            }
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