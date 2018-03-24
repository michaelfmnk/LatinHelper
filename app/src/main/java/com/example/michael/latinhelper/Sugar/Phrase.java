package com.example.michael.latinhelper.Sugar;

import com.example.michael.latinhelper.RecyclerViewMyAdapter;
import com.orm.SugarRecord;

import java.util.Comparator;

/**
 * Created by michael on 2/25/16.
 */
public class Phrase extends SugarRecord {

    public String uastring;
    public String latstring;

    public Phrase(String uaString, String latString) {
        this.uastring = uaString;
        this.latstring = latString;
    }

    public Phrase() {
    }

    public String getUaString() {
        return uastring;
    }

    public String getLatString() {
        return latstring;
    }


}
