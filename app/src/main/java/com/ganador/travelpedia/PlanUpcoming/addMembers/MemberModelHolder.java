package com.ganador.travelpedia.PlanUpcoming.addMembers;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ganador.travelpedia.R;

public class MemberModelHolder extends RecyclerView.ViewHolder{

    View mView;

    TextView memberId;
    CheckBox checkBox;
    CardView memberCard;

    public MemberModelHolder(View itemView) {
        super(itemView);

        mView = itemView;

        checkBox = mView.findViewById(R.id.checkbox);
        memberId = mView.findViewById(R.id.user_name);
        memberCard = mView.findViewById(R.id.member_card);
    }

    public void setMemberId(String id) {
        memberId.setText(id);
    }
}
