package com.worshipsearcher.entities;

import java.io.Serializable;

/**
 * Created by Mihaly on 2015.04.24..
 */
public class Week implements Serializable {
    private short weekid;
    private String type;
    public Week(short id, String type) {
        this.weekid = id;
        this.type = type;
    }
    @Override
    public String toString() {
        return "Week [id=" + weekid + ", type=" + type + "]";
    }
    public short getWeekid() {
        return weekid;
    }
    public String getType() {
        return type;
    }




}
