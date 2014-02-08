package com.babelspeaker.view;

import java.util.Date;

import com.babelspeaker.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PullToRefreshListView extends ListView implements OnScrollListener {
    private final static int RELEASE_To_REFRESH = 0;
    private final static int PULL_To_REFRESH = 1;
    // 正在刷新
    private final static int REFRESHING = 2;
    // 刷新完成
    private final static int DONE = 3;
    private final static int LOADING = 4;

    private final static int RATIO = 3;
    private LayoutInflater mInflater;
    private LinearLayout mHeadView;
    private TextView mTipsTextview;
    private TextView mLastUpdatedTextView;
    private ImageView mArrowImageView;
    private ProgressBar mProgressBar;

    private RotateAnimation mAnimation;
    private RotateAnimation mReverseAnimation;
    private boolean mIsRecored;
    private int mHeadContentWidth;
    private int mHeadContentHeight;
    private int mStartY;
    private int mFirstItemIndex;
    private int mState;
    private boolean mIsBack;
    private OnRefreshListener mRefreshListener;
    private boolean mIsRefreshable;

    int i = 1;

    public PullToRefreshListView(Context context) {
        super(context);
        init(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        // setCacheColorHint(context.getResources().getColor(R.color.transparent));
        mInflater = LayoutInflater.from(context);
        mHeadView = (LinearLayout) mInflater.inflate(
                R.layout.pull_to_refresh_head, null);
        mArrowImageView = (ImageView) mHeadView
                .findViewById(R.id.head_arrowImageView);
        mArrowImageView.setMinimumWidth(70);
        mArrowImageView.setMinimumHeight(50);
        mProgressBar = (ProgressBar) mHeadView
                .findViewById(R.id.head_progressBar);
        mTipsTextview = (TextView) mHeadView
                .findViewById(R.id.head_tipsTextView);
        mLastUpdatedTextView = (TextView) mHeadView
                .findViewById(R.id.head_lastUpdatedTextView);

        measureView(mHeadView);
        mHeadContentHeight = mHeadView.getMeasuredHeight();
        mHeadContentWidth = mHeadView.getMeasuredWidth();
        mHeadView.setPadding(0, -1 * mHeadContentHeight, 0, 0);
        mHeadView.invalidate();
        // Log.v("@@@@@@", "width:" + headContentWidth + " height:"+
        // mHeadContentHeight);
        addHeaderView(mHeadView, null, false);
        setOnScrollListener(this);

        mAnimation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.setDuration(250);
        mAnimation.setFillAfter(true);

        mReverseAnimation = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mReverseAnimation.setInterpolator(new LinearInterpolator());
        mReverseAnimation.setDuration(200);
        mReverseAnimation.setFillAfter(true);

        mState = DONE;
        mIsRefreshable = false;
    }

    public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2,
            int arg3) {
        mFirstItemIndex = firstVisiableItem;
    }

    public void onScrollStateChanged(AbsListView arg0, int arg1) {
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (mIsRefreshable) {
            switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mFirstItemIndex == 0 && !mIsRecored) {
                    mIsRecored = true;
                    mStartY = (int) event.getY();
                    // Log.v("@@@@@@", "ACTION_DOWN 这是第  "+i+++"步" +1 );
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mState != REFRESHING && mState != LOADING) {
                    if (mState == DONE) {
                    }
                    if (mState == PULL_To_REFRESH) {
                        mState = DONE;
                        // Log.v("@@@@@@",
                        // "ACTION_UP PULL_To_REFRESH and changeHeaderViewByState()"
                        // +" 这是第  "+i+++"步前"+2 );
                        changeHeaderViewByState();
                        // Log.v("@@@@@@",
                        // "ACTION_UP PULL_To_REFRESH and changeHeaderViewByState() "
                        // +"这是第  "+i+++"步后"+2 );
                    }
                    if (mState == RELEASE_To_REFRESH) {
                        mState = REFRESHING;
                        // Log.v("@@@@@@",
                        // "ACTION_UP RELEASE_To_REFRESH changeHeaderViewByState() "
                        // +"这是第  "+i+++"步" +3);
                        changeHeaderViewByState();
                        onRefresh();
                        // Log.v("@@@@@@",
                        // "ACTION_UP RELEASE_To_REFRESH changeHeaderViewByState()"
                        // +" 这是第  "+i+++"步" +3);
                    }
                }
                mIsRecored = false;
                mIsBack = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int tempY = (int) event.getY();
                if (!mIsRecored && mFirstItemIndex == 0) {
                    mIsRecored = true;
                    mStartY = tempY;
                    // Log.v("@@@@@@", "ACTION_MOVE 这是第  "+i+++"步" +4);
                }
                if (mState != REFRESHING && mIsRecored && mState != LOADING) {
                    if (mState == RELEASE_To_REFRESH) {
                        setSelection(0);
                        if (((tempY - mStartY) / RATIO < mHeadContentHeight)
                                && (tempY - mStartY) > 0) {
                            mState = PULL_To_REFRESH;
                            changeHeaderViewByState();
                            // Log.v("@@@@@@",
                            // "changeHeaderViewByState() 这是第  "+i+++"步"+5 );
                        } else if (tempY - mStartY <= 0) {
                            mState = DONE;
                            changeHeaderViewByState();
                            // Log.v("@@@@@@",
                            // "ACTION_MOVE RELEASE_To_REFRESH 2  changeHeaderViewByState "
                            // +"这是第  "+i+++"步" +6);
                        }
                    }
                    if (mState == PULL_To_REFRESH) {
                        setSelection(0);
                        if ((tempY - mStartY) / RATIO >= mHeadContentHeight) {
                            mState = RELEASE_To_REFRESH;
                            mIsBack = true;
                            // Log.v("@@@@@@", "changeHeaderViewByState "
                            // +"这是第  "+i+++"步前"+7 );
                            changeHeaderViewByState();
                            // Log.v("@@@@@@", "changeHeaderViewByState "
                            // +"这是第  "+i+++"步后"+7 );
                        } else if (tempY - mStartY <= 0) {
                            mState = DONE;
                            changeHeaderViewByState();
                            // Log.v("@@@@@@",
                            // "ACTION_MOVE changeHeaderViewByState PULL_To_REFRESH 2"
                            // +" 这是第  "+i+++"步" +8);
                        }
                    }
                    if (mState == DONE) {
                        if (tempY - mStartY > 0) {
                            mState = PULL_To_REFRESH;
                            // Log.v("@@@@@@",
                            // "ACTION_MOVE DONE changeHeaderViewByState "
                            // +"这是第  "+i+++"步前" +9);
                            changeHeaderViewByState();
                            // Log.v("@@@@@@",
                            // "ACTION_MOVE DONE changeHeaderViewByState "
                            // +"这是第  "+i+++"步后" +9);
                        }
                    }
                    if (mState == PULL_To_REFRESH) {
                        mHeadView.setPadding(0, -1 * mHeadContentHeight
                                + (tempY - mStartY) / RATIO, 0, 0);
                        // Log.v("@@@@@@", -1 * mHeadContentHeight+(tempY -
                        // mStartY) /
                        // RATIO+"ACTION_MOVE PULL_To_REFRESH 3  这是第  "+i+++"步"+10
                        // );
                    }
                    if (mState == RELEASE_To_REFRESH) {
                        mHeadView.setPadding(0, (tempY - mStartY) / RATIO
                                - mHeadContentHeight, 0, 0);
                        // Log.v("@@@@@@",
                        // "ACTION_MOVE PULL_To_REFRESH 4 这是第  "+i+++"步" +11);
                    }
                }
                break;
            }
        }
        return super.onTouchEvent(event);
    }

    private void changeHeaderViewByState() {
        switch (mState) {
        case RELEASE_To_REFRESH:
            mArrowImageView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mTipsTextview.setVisibility(View.VISIBLE);
            mLastUpdatedTextView.setVisibility(View.VISIBLE);
            mArrowImageView.clearAnimation();
            mArrowImageView.startAnimation(mAnimation);
            mTipsTextview.setText("松开可以刷新");
            // Log.v("@@@@@@", "RELEASE_To_REFRESH 这是第  "+i+++"步"+12 +"请释放 刷新"
            // );
            break;
        case PULL_To_REFRESH:
            mProgressBar.setVisibility(View.GONE);
            mTipsTextview.setVisibility(View.VISIBLE);
            mLastUpdatedTextView.setVisibility(View.VISIBLE);
            mArrowImageView.clearAnimation();
            mArrowImageView.setVisibility(View.VISIBLE);
            if (mIsBack) {
                mIsBack = false;
                mArrowImageView.clearAnimation();
                mArrowImageView.startAnimation(mReverseAnimation);
                mTipsTextview.setText("下拉可以刷新");
            } else {
                mTipsTextview.setText("下拉可以刷新");
            }
            // Log.v("@@@@@@", "PULL_To_REFRESH 这是第  "+i+++"步"
            // +13+"  changeHeaderViewByState()");
            break;
        case REFRESHING:
            mHeadView.setPadding(0, 0, 0, 0);
            mProgressBar.setVisibility(View.VISIBLE);
            mArrowImageView.clearAnimation();
            mArrowImageView.setVisibility(View.GONE);
            mTipsTextview.setText("正在加载...");
            mLastUpdatedTextView.setVisibility(View.VISIBLE);
            // Log.v("@@@@@@", "REFRESHING 这是第  "+i+++"步"
            // +"正在加载中 ...REFRESHING");
            break;
        case DONE:
            mHeadView.setPadding(0, -1 * mHeadContentHeight, 0, 0);
            mProgressBar.setVisibility(View.GONE);
            mArrowImageView.clearAnimation();
            mArrowImageView.setImageResource(R.drawable.ic_pulltorefresh_arrow);
            mTipsTextview.setText("已经加载完毕");
            mLastUpdatedTextView.setVisibility(View.VISIBLE);
            // Log.v("@@@@@@", "DONE 这是第  "+i+++"步" +"已经加载完毕- DONE ");
            break;
        }
    }

    public void setOnRefreshListener(OnRefreshListener refreshListener) {
        this.mRefreshListener = refreshListener;
        mIsRefreshable = true;
    }

    public interface OnRefreshListener {
        public void onRefresh();
    }

    public void onRefreshComplete() {
        mState = DONE;
        mLastUpdatedTextView.setText("上次加载: " + new Date().toLocaleString());
        changeHeaderViewByState();
        // Log.v("@@@@@@", "onRefreshComplete() 被调用。。。");
    }

    private void onRefresh() {
        if (mRefreshListener != null) {
            mRefreshListener.onRefresh();
            // Log.v("@@@@@@", "onRefresh被调用，这是第  "+i+++"步" );
        }
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    public void setAdapter(BaseAdapter adapter) {
        mLastUpdatedTextView.setText("上次加载:"
                + new Date().toLocaleString());
        super.setAdapter(adapter);
    }
}