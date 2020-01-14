package com.ganador.travelpedia.PlanUpcoming.addMembers;

import android.widget.CheckBox;

public class MemberModel {

    public String memberId;
    public CheckBox checkBox;

    public MemberModel() {

    }

    public MemberModel(String noteTitle, CheckBox checkBox) {
        this.memberId = noteTitle;
        this.checkBox = checkBox;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId =  memberId;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }
}
