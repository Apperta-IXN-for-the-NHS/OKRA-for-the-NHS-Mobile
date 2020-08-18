package com.emis.emismobile.knowledge;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emis.emismobile.R;

import java.util.List;

public class KnowledgeAdapter extends RecyclerView.Adapter<KnowledgeAdapter.ViewHolder> {

    private List<Article> mArticles;

    public KnowledgeAdapter(List<Article> articles) {
        mArticles = articles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View knowledgeView = inflater.inflate(R.layout.item_knowledge, parent, false);

        return new ViewHolder(knowledgeView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article article = mArticles.get(position);

        TextView titleTextView = holder.titleTextView;
        titleTextView.setText(article.getTitle());
        TextView dateTextView = holder.dateTextView;
        dateTextView.setText(article.getDate());
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView dateTextView;

        public ViewHolder(final View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.article_title);
            dateTextView = (TextView) itemView.findViewById(R.id.article_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemOnClick();
                }
            });
        }

        public void itemOnClick() {
            int pos = getAdapterPosition();

            if (pos != RecyclerView.NO_POSITION) {
                Article clickedArticle = mArticles.get(pos);
                Intent intent = new Intent(itemView.getContext(), DisplayArticleActivity.class);
                String articleId = clickedArticle.getId();
                intent.putExtra("article_id", articleId);
                itemView.getContext().startActivity(intent);
            }
        }
    }
}
