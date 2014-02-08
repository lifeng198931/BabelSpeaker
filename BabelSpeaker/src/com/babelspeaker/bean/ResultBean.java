package com.babelspeaker.bean;

public class ResultBean {

    private int mStat;
    private String mData;
    
    public ResultBean() {
        clear();
    }
    
    public void clear() {
        mStat = -1;
        mData = "请求失败";
    }

    public int getStat() {
        return mStat;
    }

    public void setStat(int stat) {
        mStat = stat;
    }

    public String getData() {
        return mData;
    }

    public void setData(String data) {
        mData = data;
    }

    @Override
    public String toString() {
        return "ResultBean [mStat=" + mStat + ", mData=" + mData + "]";
    }
}
