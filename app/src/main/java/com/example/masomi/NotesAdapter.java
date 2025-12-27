package com.example.masomi;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private List<Note> list;
    private OnNoteClickListener listener;
    private Context context;

    public interface OnNoteClickListener {
        void onNoteClick(Note note);
    }

    public NotesAdapter(List<Note> list, OnNoteClickListener listener, Context context) {
        this.list = list;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = list.get(position);
        holder.title.setText(note.title);
        holder.itemView.setOnClickListener(v -> listener.onNoteClick(note));

        holder.itemView.setOnLongClickListener(v -> {
            showOptionsDialog(v.getContext(), note);
            return true;
        });
    }

    private void showOptionsDialog(Context ctx, Note note) {
        String[] options = {"Delete", "Share", "Copy"};
        new AlertDialog.Builder(ctx)
                .setTitle("Options")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            int pos = list.indexOf(note);
                            if (pos >= 0) {
                                new Thread(() -> {
                                    NoteDatabase.getInstance(ctx).noteDao().delete(note);
                                    new Handler(Looper.getMainLooper()).post(() -> {
                                        list.remove(pos);
                                        notifyItemRemoved(pos);
                                        Toast.makeText(ctx, "Deleted", Toast.LENGTH_SHORT).show();
                                    });
                                }).start();
                            }
                            break;
                        case 1:
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_TEXT, (note.title == null ? "" : note.title + "\n") + (note.text == null ? "" : note.text));
                            ctx.startActivity(Intent.createChooser(shareIntent, "Share Note"));
                            break;
                        case 2:
                            ClipboardManager cm = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
                            if (cm != null) {
                                ClipData clip = ClipData.newPlainText("note", note.text == null ? "" : note.text);
                                cm.setPrimaryClip(clip);
                                Toast.makeText(ctx, "Copied", Toast.LENGTH_SHORT).show();
                            }
                            break;
                    }
                })
                .show();
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.noteTitle);
        }
    }
}