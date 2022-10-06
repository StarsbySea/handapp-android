package com.example.text1;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends BaseAdapter {

    private List<Note> dataset;

    private static class NoteViewHolder {

        public TextView text;
        public TextView comment;
        public ImageView image;
        public NoteViewHolder(View itemView) {
            text = itemView.findViewById(R.id.textViewNoteText);
            comment = itemView.findViewById(R.id.textViewNoteComment);
            image = itemView.findViewById(R.id.imageView);
        }
    }

    public NotesAdapter() {
        this.dataset = new ArrayList<>();
    }

    public void setNotes(List<Note> notes) {
        dataset = notes;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NoteViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_note, parent, false);
            holder = new NoteViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (NoteViewHolder) convertView.getTag();
        }

        Note note = getItem(position);
        holder.text.setText(note.getName());
        holder.comment.setText(note.getExplain());
        holder.image.setBackground(note.getImage(false));
        return convertView;
    }

    @Override
    public int getCount() {
        return dataset.size();
    }

    @Override
    public Note getItem(int position) {
        return dataset.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
