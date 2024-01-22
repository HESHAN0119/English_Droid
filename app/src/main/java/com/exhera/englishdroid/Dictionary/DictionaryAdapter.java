package com.exhera.englishdroid.Dictionary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.exhera.englishdroid.R;

import java.util.List;

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.ViewHolder> {
    private Context context;
    private List<Dictionary> dictionaryList;
    private ItemClickListener mItemClickListener;

    public DictionaryAdapter(Context context, List<Dictionary> dictionaryList , ItemClickListener itemClickListener) {
        this.context = context;
        this.dictionaryList = dictionaryList;
        this.mItemClickListener =itemClickListener;
    }

    @NonNull
    @Override
    public DictionaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.dictionary_row,parent,false);

        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull DictionaryAdapter.ViewHolder viewholder, int position) {

        Dictionary dictionary = dictionaryList.get(position);
        viewholder.word.setText(dictionary.getWord());
        viewholder.description.setText(dictionary.getDescription());

        viewholder.itemView.setOnClickListener(view -> {
        mItemClickListener.onItemClick(dictionaryList.get(position));

        });
    }

    @Override
    public int getItemCount() {
        return dictionaryList.size();
    }


    public interface ItemClickListener{

        void onItemClick(Dictionary dictionary);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView word,description;
        String userId;
        String username;

        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            context=ctx;

            word = itemView.findViewById(R.id.word_row);
            description= itemView.findViewById(R.id.description_row);

        }
    }

    public interface OnNoteListener{
        void onNoteClick(int position);


    }
}
