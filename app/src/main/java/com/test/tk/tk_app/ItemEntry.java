package com.test.tk.tk_app;

/**
 * Created by P16099 on 2015-09-18.
 */
public class ItemEntry {
    private String mId;
    private int mAge;

    public ItemEntry(String _mId, int _mAge){
        this.mId= _mId;
        this.mAge= _mAge;
    }

    public String getMemId() {
        return mId;
    }

    public int getMemAge(){
        return mAge;
    }
}
