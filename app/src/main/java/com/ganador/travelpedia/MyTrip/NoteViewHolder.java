package com.ganador.travelpedia.MyTrip;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ganador.travelpedia.R;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    View mView;

    TextView textTitle, textTime, textDate;
    CardView noteCard;

    public NoteViewHolder(View itemView) {
        super(itemView);

        mView = itemView;

        textTitle = mView.findViewById(R.id.note_title);
        textTime = mView.findViewById(R.id.note_time);
        noteCard = mView.findViewById(R.id.note_card);
        textDate = mView.findViewById(R.id.note_date);
    }

    public void setNoteTitle(String title) {
        textTitle.setText(title);
    }
    public void setNoteDate(String date) { textDate.setText(date); }
    public void setNoteTime(String time) {
        textTime.setText(time);
    }

}
