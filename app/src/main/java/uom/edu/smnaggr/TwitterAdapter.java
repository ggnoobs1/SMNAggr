package uom.edu.smnaggr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Context;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import java.util.List;

public class TwitterAdapter extends ArrayAdapter<TwitterEntry> {

    private List<TwitterEntry> dataset;
    private final LayoutInflater inflater;
    private final int layoutResource;

    //TODO: na allaksoyme tis theseis kai to megethos twn textview sto activity_tweet_adapter
    // treksto mia fora sta
    // trends, kai mia fora sto search gia na deis pws fainontai prwta

    public TwitterAdapter(@NonNull Context context, int resource, @NonNull List<TwitterEntry> objects) {
        super(context, resource, objects);
        dataset = objects;
        inflater = LayoutInflater.from(context);
        layoutResource = resource;
    }


    public TwitterEntry getTweetsEntry(int position){
        if(position < dataset.size() ){
            return dataset.get(position);
        }
        return new TwitterEntry();
    }

    public void setTwitterEntries(@NonNull List<TwitterEntry> twEntries) {
        dataset = twEntries;
        notifyDataSetChanged();
    }



    public TwitterEntry getTwitterEntry(int position){
        if(position < dataset.size() ){
            return dataset.get(position);
        }
        return new TwitterEntry();
    }

    static class TwitterViewHolder {

        public TextView nameTextView;
        public TextView urlTextView;
        public TextView queryTextView;
        public TextView tweet_volumeTextView;

        public TwitterViewHolder(View itemView) {
            nameTextView = itemView.findViewById(R.id.nameTxt);
            urlTextView = itemView.findViewById(R.id.urlTxt);
            queryTextView = itemView.findViewById(R.id.queryTxt);
            tweet_volumeTextView = itemView.findViewById(R.id.tweet_volumeTxt);
        }
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TwitterViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(layoutResource, parent, false);
            holder = new TwitterViewHolder(convertView);
            convertView.setTag(holder);
        }
        else{
            holder = (TwitterViewHolder)convertView.getTag();
        }

        TwitterEntry twEntry = dataset.get(position);
        holder.nameTextView.setText(twEntry.getName());
        holder.urlTextView.setText(twEntry.getUrl());
        holder.queryTextView.setText(twEntry.getQuery());
        holder.tweet_volumeTextView.setText(twEntry.getTweet_volume());


        return convertView;
    }

    @Override
    public int getCount() {

        return dataset.size();
    }

}
