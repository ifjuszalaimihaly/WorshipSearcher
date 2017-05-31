package com.worshipsearcher.entities;

import java.io.Serializable;

/**
 * Created by Mihaly on 2015.04.24..
 */
public class Worship implements Comparable<Worship>, Serializable{
    private int worshipid;
    private short churchid;
    private String termin;
    private short weekid;
    private String comment;
    private String date;


    public Worship(int worshipid, short churchid, String termin, short weekid, String comment) {
        this.worshipid=worshipid;
        this.churchid = churchid;
        this.termin = termin;
        this.weekid = weekid;
        this.comment = comment;
    }

    /*
    public Worship(int churchid, String termin, int weekid, String comment, String date) {
        this.churchid = churchid;
        this.termin = termin;
        this.weekid = weekid;
        this.comment = comment;
        this.date = date;
    }
    */

    @Override
    public String toString() {
        return "Worship{" +
                "worshipid=" + worshipid +
                ", churchid=" + churchid +
                ", termin='" + termin + '\'' +
                ", weekid=" + weekid +
                ", comment='" + comment + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public int getChurchid() {
        return churchid;
    }

    public int getWorshipid() {
        return worshipid;
    }

    public String getTermin() {
        return termin;
    }

    public int getWeekid() {
        return weekid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment){
        this.comment=comment;
    }

    public String getDate() {
        return date;
    }

    public void setTermin(String termin){
        this.termin=termin;
    }


    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int compareTo(Worship another) {
        if(this.date.equals(another.date)){
            if(!this.termin.equals(another.termin)) {
                if (this.termin.compareTo(another.termin) < 0) {
                    return -1;
                } else {
                    return 1;
                }
            }
            return 0;
        }
        if(this.date.compareTo(another.date)<0){
            return -1;
        } else {
            return 1;
        }
    }
}
