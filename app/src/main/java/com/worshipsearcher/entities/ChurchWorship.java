package com.worshipsearcher.entities;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by MihálySzalai on 2016. 08. 21..
 */
public class ChurchWorship implements Serializable, Comparable<ChurchWorship> {

    private Church church;
    private Worship worship;

    public ChurchWorship(Church church, Worship worship) {
        this.church = church;
        this.worship = worship;
    }

    public Church getChurch() {
        return church;
    }

    public void setChurch(Church church) {
        this.church = church;
    }

    public Worship getWorship() {
        return worship;
    }

    public void setWorship(Worship worship) {
        this.worship = worship;
    }

    @Override
    public int compareTo(ChurchWorship another) {
        String thiscity = this.church.getCity().trim();
        String anotercity = another.church.getCity().trim();
        if (thiscity.compareTo(anotercity) == 0){
            String thisadress = this.church.getAddress().trim();
            String anotheraddress = another.church.getAddress().trim();
            return  thisadress.compareTo(anotheraddress);
        } else {
            return thiscity.compareTo(anotercity);
        }

            //if (this.church.compareTo(another.church)!=0){
            //return this.church.compareTo(another.church);
        //} else {
        //    return this.worship.getTermin().compareTo(another.getWorship().getTermin());
        //}
        //if(this.church.equals(another.church)){
        //    return this.worship.compareTo(another.worship);
        //} else {
            //return 0;//this.church.compareTo(another.church);
        //}
    }
}
