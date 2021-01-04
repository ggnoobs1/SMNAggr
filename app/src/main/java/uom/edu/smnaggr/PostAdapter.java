package uom.edu.smnaggr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends ArrayAdapter<PostEntry> {

    private List<PostEntry> post_dataset;
    private final LayoutInflater inflater;
    private final int layoutResource;

    public PostAdapter(@NonNull Context context, int resource, @NonNull List<PostEntry> objects) {
        super(context, resource, objects);
        post_dataset = objects;
        inflater = LayoutInflater.from(context);
        layoutResource = resource;

    }



    public void setPostEntries(@NonNull List<PostEntry> postEntries) {
        post_dataset = postEntries;
        notifyDataSetChanged();
    }

    public PostEntry getPostEntry(int position){
        if(position < post_dataset.size() ){
            return post_dataset.get(position);
        }
        return new PostEntry();
    }

    static class PostViewHolder {

        public TextView titleTextView;
        public TextView authorTextView;
        public TextView descriptionTextView;
        public ImageView imageView;

        public PostViewHolder(View itemView) {
            titleTextView = itemView.findViewById(R.id.titleTxt2);
           // authorTextView = itemView.findViewById(R.id.authorTxt2);
            //descriptionTextView = itemView.findViewById(R.id.descriptionTxt2);
            //imageView = itemView.findViewById(R.id.newsImage2);
        }
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PostAdapter.PostViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(layoutResource, parent, false);
            holder = new PostAdapter.PostViewHolder(convertView);
            convertView.setTag(holder);
        }
        else{
            holder = (PostAdapter.PostViewHolder)convertView.getTag();
        }

        PostEntry postEntry = post_dataset.get(position);

        holder.titleTextView.setText(postEntry.getPost_id());


        return convertView;
    }

    @Override
    public int getCount() {

        return post_dataset.size();
    }
}