package com.babelspeaker.adapter;

import java.util.List;

import com.babelspeaker.data.InitData;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DragGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<Object> mList;
    private int mHoldPosition;
    private boolean mIsChanged = false;
    private boolean mShowItem = false;
    
    private onItemExchangeListener mListener;
    
    public onItemExchangeListener getListener() {
        return mListener;
    }

    public void setOnItemExchangeListener(onItemExchangeListener listener) {
        mListener = listener;
    }

    public interface onItemExchangeListener{
        void onItemExchange(int startID, int endID);
    }

    public DragGridViewAdapter(Context context, List<Object> list) {
        mContext = context;
        mList = list;
    }

    public List<Object> getList() {
        return mList;
    }

    public void setList(List<Object> list) {
        this.mList = list;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public int getHoldPosition() {
        return mHoldPosition;
    }

    public void setHoldPosition(int holdPosition) {
        this.mHoldPosition = holdPosition;
    }

    public boolean isChanged() {
        return mIsChanged;
    }

    public void setIsChanged(boolean isChanged) {
        this.mIsChanged = isChanged;
    }

    public boolean isShowItem() {
        return mShowItem;
    }

    public void setShowItem(boolean showItem) {
        this.mShowItem = showItem;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void exchange(int startPosition, int endPosition) {
        
        if (InitData.DEBUG) System.out.println(startPosition + "--" + endPosition);
        mHoldPosition = endPosition;
        Object startObject = getItem(startPosition);
        System.out.println(startPosition + "========" + endPosition);
        if (InitData.DEBUG) Log.d("ON", "startPostion ==== " + startPosition);
        if (InitData.DEBUG) Log.d("ON", "endPosition ==== " + endPosition);
        if (startPosition < endPosition) {
            mList.add(endPosition + 1, startObject);
            mList.remove(startPosition);
        } else {
            mList.add(endPosition, startObject);
            mList.remove(startPosition + 1);
        }
        mIsChanged = true;
        if (mListener != null) {
            mListener.onItemExchange(startPosition, endPosition);
        }
        notifyDataSetChanged();
        if (InitData.DEBUG) Log.v("CHANGED", mList.toString());
    }

    public void showDropItem(boolean showItem) {
        this.mShowItem = showItem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout layout = new LinearLayout(mContext);
        TextView textView = new TextView(mContext);
        layout.addView(textView);
            convertView = layout;
        
        textView.setText((String)mList.get(position));
        
        if (mIsChanged) {
            if (position == mHoldPosition) {
                if (!mShowItem) {
                    convertView.setVisibility(View.INVISIBLE);
                }
            }
        }
        return convertView;
    }

}
