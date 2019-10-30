package com.dqn.bottomSheet;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * author: dqn
 */

public class MyRecyclerView extends RecyclerView {

    private int spanCount;

    private boolean isScreenW = false;
    private boolean isScreenH = false;
    private int mScreenWidth;
    private float mW_is_Screen = 0.0f;

    public MyRecyclerView(Context context) {
        this(context, null);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        //获取自定义值
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable
                .MyRecyclerView, defStyle, 0);

        if (typedArray != null) {
            spanCount = typedArray.getInt(R.styleable.MyRecyclerView_spanCount, 0);


            if (typedArray.hasValue(R.styleable.MyRecyclerView_myRecycleViw_isScreenW)) {
                mScreenWidth = getResources().getDisplayMetrics().widthPixels;
                isScreenW = typedArray.getBoolean(R.styleable.MyRecyclerView_myRecycleViw_isScreenW, false);
                mW_is_Screen = typedArray.getFloat(R.styleable.MyRecyclerView_myRecycleViw_w_is_screen, 0.0f);
            }


            typedArray.recycle();
        }
        setSpanCount(spanCount);


    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        if(isScreenW){
            //是否是参照手机屏幕宽度
            int width = (int) (mScreenWidth * mW_is_Screen);
            widthSpec = MeasureSpec.makeMeasureSpec(width,MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthSpec, heightSpec);

    }

    public int getSpanCount() {
        return spanCount;
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
        setLayoutManagerType();
    }

    private void setLayoutManagerType() {
        LayoutManager layoutManager = null;
        if (spanCount == 0) {
            //竖向滑动
            layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
                    false);
        } else if (spanCount == 1) {
            //横向滑动
            layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,
                    false);
        } else if (spanCount <= 10) {
            //
            layoutManager = new GridLayoutManager(getContext(), getSpanCount());

        } else if (spanCount > 10) {

            layoutManager = new GridLayoutManager(getContext(), getSpanCount() % 10,
                    GridLayoutManager.HORIZONTAL, false);
        }

        setLayoutManager(layoutManager);
        setItemAnimator(new DefaultItemAnimator());
    }


}
