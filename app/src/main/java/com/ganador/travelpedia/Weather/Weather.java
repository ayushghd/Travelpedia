package com.ganador.travelpedia.Weather;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Weather {

    public Date mdate;
    public String mid;
    public String mtemperature;
    public String mdescription;
    public String micon;
    public void setDate(String dateString) {
        try {
            setDate(new Date(Long.parseLong(dateString) * 1000));
        }
        catch (Exception e) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            try {
                setDate(inputFormat.parse(dateString));
            }
            catch (ParseException e2) {
                setDate(new Date());
                e2.printStackTrace();
            }
        }
    }

    public void setDate(Date date) {
        this.mdate = date;
    }
}
