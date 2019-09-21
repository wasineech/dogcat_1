package com.myweb.dogcat;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ShowSearchAdapter extends RecyclerView.Adapter<ShowSearchAdapter.ShowViewHolder> {
    Context context;
    List<RowItem_show> rowItems;

    ShowSearchAdapter(Context context, List<RowItem_show> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    @NonNull
    @Override
    public ShowSearchAdapter.ShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.show_search_listview, null);
        return new ShowSearchAdapter.ShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowSearchAdapter.ShowViewHolder holder, int position) {
        RowItem_show rowItem_show = rowItems.get(position);

        String get_petname = rowItem_show.getPet_name();
        String get_breed = rowItem_show.getPet_breed();
        String get_age = rowItem_show.getPet_age();
        String get_picture = rowItem_show.getPet_profile();

        holder.pet_name.setText(get_petname);
        holder.pet_breed.setText(get_breed);
        holder.pet_age.setText(get_age);
        Picasso.get().load(get_picture).into(holder.pet_profile);
    }


    @Override
    public int getItemCount() {
        return rowItems.size();
    }

    class ShowViewHolder extends RecyclerView.ViewHolder{
        TextView pet_name, pet_breed, pet_age;
        ImageView pet_profile;

        public ShowViewHolder(View itemView){
            super(itemView);

            pet_name = itemView.findViewById(R.id.pet_name);
            pet_breed = itemView.findViewById(R.id.pet_breed);
            pet_age = itemView.findViewById(R.id.pet_age);
            pet_profile = itemView.findViewById(R.id.pet_profile);
        }
    }




}
