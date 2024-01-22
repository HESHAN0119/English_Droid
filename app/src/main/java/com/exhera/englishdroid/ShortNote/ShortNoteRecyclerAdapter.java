package com.exhera.englishdroid.ShortNote;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.exhera.englishdroid.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShortNoteRecyclerAdapter extends RecyclerView.Adapter<ShortNoteRecyclerAdapter.ViewHolder> {
    private Context context;
    private final List<ShortNote> shortNoteList;
    private ItemClickListener mItemClickListener;
//    FileOutputStream outputStream;


    public ShortNoteRecyclerAdapter(Context context, List<ShortNote> shortNoteList , ItemClickListener itemClickListener ) {
        this.context = context;
        this.shortNoteList = shortNoteList;
        this.mItemClickListener =itemClickListener;
    }

    @NonNull
    @Override
    public ShortNoteRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.shortnote_row, parent,false);

        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ShortNoteRecyclerAdapter.ViewHolder holder, int position) {

        ShortNote shortNote = shortNoteList.get(position);


        holder.title.setText(shortNote.getTitle());
        holder.description.setText(shortNote.getDescription());

        String imageUrl=shortNote.getImageUrl();

        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(shortNote.getTimeAdded()
            .getSeconds()*1000);
        holder.dateAdded.setText(timeAgo);

        holder.dateAdded.setText(timeAgo);

        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(imageUrl);
                Intent intent=new Intent(context,ShortNoteDownload.class);
                intent.putExtra("URL",imageUrl);
                context.startActivity(intent);
                Toast.makeText(context, "Open Preview mode", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "Zoom enable", Toast.LENGTH_SHORT).show();

            }
        });


        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.image_three)
                .into(holder.image);

        holder.itemView.setOnClickListener(view -> {
            mItemClickListener.onItemClick(shortNoteList.get(position));

        });

    }

    @Override
    public int getItemCount() {
        return shortNoteList.size();
    }


    public interface ItemClickListener{

        void onItemClick(ShortNote shortNote);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title,
        description,
        dateAdded;

        public ImageView image;
        public ImageButton shareButton;
        String userId;
        String username;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);

            context= ctx;

            title= itemView.findViewById(R.id.shortNote_title_list);
            description=itemView.findViewById(R.id.shortNote_description_list);
            dateAdded= itemView.findViewById(R.id.shortNote_timestamp_list);
            image=itemView.findViewById(R.id.shortnote_image_list);
            shareButton=itemView.findViewById(R.id.shortNote_row_share_button);



        }
    }
}
