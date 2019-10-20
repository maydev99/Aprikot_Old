package com.bombadu.aprikot;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.RecyclerHolder> {

    private List<ListItem> listData;
    private LayoutInflater inflater;
    private ItemClickCallback itemClickCallback;

    interface ItemClickCallback {
        void onItemClick(int p);
    }

    void setItemClickCallback(final ItemClickCallback inItemClickCallback) {
        this.itemClickCallback = inItemClickCallback;
    }

    NewsAdapter(List<ListItem> listData, Context c) {
        this.inflater = LayoutInflater.from(c);
        this.listData = listData;
    }

    @NonNull
    @Override
    public NewsAdapter.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.news_card_layout, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.RecyclerHolder holder, int position) {
        ListItem item = listData.get(position);
        holder.authorTextView.setText(item.getAuthorText());
        holder.titleTexView.setText(item.getTitleText());
        holder.descriptionTextView.setText(item.getDescriptionText());
        Uri uri = Uri.parse(item.getImageUrlText());
        holder.cardImageView.setImageResource(item.getImageImage());
        Picasso.get().load(uri).into(holder.cardImageView);

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView authorTextView;
        private TextView descriptionTextView;
        private TextView titleTexView;
        private ImageView cardImageView;
        private View cardContainer;


        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);

            authorTextView = itemView.findViewById(R.id.byLine_text_view);
            descriptionTextView = itemView.findViewById(R.id.description_text_view);
            titleTexView = itemView.findViewById(R.id.title_text_view);
            cardImageView = itemView.findViewById(R.id.news_card_image_view);
            cardContainer = itemView.findViewById(R.id.news_card_container);
            cardContainer.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            itemClickCallback.onItemClick(getAdapterPosition());
        }
    }

}

