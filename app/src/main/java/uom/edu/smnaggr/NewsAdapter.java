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

public class NewsAdapter extends ArrayAdapter<NewsEntry> {

    private List<NewsEntry> dataset;
    private final LayoutInflater inflater;
    private final int layoutResource;

    public NewsAdapter(@NonNull Context context, int resource, @NonNull List<NewsEntry> objects) {
        super(context, resource, objects);
        dataset = objects;
        inflater = LayoutInflater.from(context);
        layoutResource = resource;
    }

    public NewsEntry getNewsEntry(int position){
        if(position < dataset.size() ){
            return dataset.get(position);
        }
        return new NewsEntry();
    }

    public void setNewsEntries(@NonNull List<NewsEntry> newsEntries) {
        dataset = newsEntries;
        notifyDataSetChanged();
    }

    static class NewsViewHolder {

        public TextView titleTextView;
        public TextView authorTextView;
        public TextView descriptionTextView;
        public ImageView imageView;

        public NewsViewHolder(View itemView) {
            titleTextView = itemView.findViewById(R.id.titleTxt);
            authorTextView = itemView.findViewById(R.id.authorTxt);
            descriptionTextView = itemView.findViewById(R.id.descriptionTxt);
            imageView = itemView.findViewById(R.id.newsImage);
        }
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        NewsViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(layoutResource, parent, false);
            holder = new NewsViewHolder(convertView);
            convertView.setTag(holder);
        }
        else{
            holder = (NewsViewHolder)convertView.getTag();
        }

        NewsEntry newsEntry = dataset.get(position);
        holder.titleTextView.setText(newsEntry.getTitle());
        holder.authorTextView.setText(newsEntry.getAuthor());
        holder.descriptionTextView.setText(Html.fromHtml(newsEntry.getDescription()));
        Picasso.get().load(newsEntry.getUrlToImage()).into(holder.imageView);

        return convertView;
    }

    @Override
    public int getCount() {

        return dataset.size();
    }
}