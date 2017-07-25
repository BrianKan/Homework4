package com.example.redfish.newsapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.redfish.newsapp.Models.Contract;
import com.example.redfish.newsapp.Models.NewsItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Redfish on 6/27/2017.
 */

public class newsAdapter extends RecyclerView.Adapter<newsAdapter.ItemHolder>{
    //TODO added cursor, replaced data, to Variables and Constructor
    private Cursor cursor;
    ItemClickListener listener;
    private Context context;
    public static final String TAG="Thisthing";


    public newsAdapter(Cursor cursor, ItemClickListener listener){
        this.cursor = cursor;
        this.listener = listener;
    }
// TODO Added Cursor to ItemClickListener args
    public interface ItemClickListener {
        void onItemClick(Cursor cursor,int clickedItemIndex);
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.item, parent, shouldAttachToParentImmediately);
        ItemHolder holder = new ItemHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bind(position);
    }
//TODO Changed GetitemCount to cursor.getcount
    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
// TODO Added imageview to itemHolder
    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        TextView description;
        TextView date;
        ImageView img;

        ItemHolder(View view){
            super(view);
            title = (TextView)view.findViewById(R.id.title);
            description = (TextView)view.findViewById(R.id.description);
            date = (TextView)view.findViewById(R.id.date);
            img =(ImageView)view.findViewById(R.id.img);
            view.setOnClickListener(this);
        }
// TODO changed bind from data to pull from the SQL Database
        public void bind(int pos){
            cursor.moveToPosition(pos);
            title.setText(cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_TITLE)));
            description.setText(cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_ABSTRACT)));
            date.setText(cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_PUBLISHED_DATE)));
            String url = cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_THUMBURL));
            if(url != null){
                // TODO USing Picasso to display an image inside the view
                Picasso.with(context)
                        .load(url)
                        .placeholder(R.mipmap.ic_launcher_round)
                        .fit()
                        .centerCrop()
                        .into(img);
            }
        }
// TODO Changed OnClick to accept a cursor
        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            listener.onItemClick(cursor, pos);
        }
    }



}
