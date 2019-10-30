package com.dqn.bottomSheet;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

public class MySwitchScrollLinearManager extends LinearLayoutManager {

    private boolean mCanVerticalScroll = true;

    public MySwitchScrollLinearManager(Context context) {
        this(context,LinearLayoutManager.VERTICAL,false);
    }

    public MySwitchScrollLinearManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public MySwitchScrollLinearManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean canScrollVertically() {

        if(mCanVerticalScroll){
            return super.canScrollVertically();
        }else {
            return false;
        }

        //return super.canScrollVertically();
    }

    public void setCanVerticalScroll(boolean b){
        mCanVerticalScroll = b;
    }
}
