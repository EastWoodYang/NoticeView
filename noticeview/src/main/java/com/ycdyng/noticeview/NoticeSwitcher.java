package com.ycdyng.noticeview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ViewAnimator;

public class NoticeSwitcher extends ViewAnimator {

    private Context mContext;
    NoticeAdapter mNoticeAdapter;

    public NoticeSwitcher(Context context) {
        super(context);
        init(context, null);
    }

    public NoticeSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        setMeasureAllChildren(false);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException if this switcher already contains two children
     */
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() >= 2) {
            throw new IllegalStateException("Can't add more than 2 views to a ViewSwitcher");
        }
        super.addView(child, index, params);
    }

    /**
     * Returns the next view to be displayed.
     *
     * @return the view that will be displayed after the next views flip.
     */
    public View getNextView(Object dateSet) {
        if(getChildCount() == 0) {
            return obtainView(0, dateSet);
        } else if(getChildCount() == 1) {
            return obtainView(1, dateSet);
        } else {
            int which = getDisplayedChild() == 0 ? 1 : 0;
            removeView(getChildAt(which));
            return obtainView(which, dateSet);
        }
    }

    private View obtainView(int position, Object dateSet) {
        View child = null;
        if(dateSet == null) {
            child = getDefaultEmptyView();
        } else {
            child = mNoticeAdapter.getView(null, dateSet);
            if(child == null) {
                child = getDefaultEmptyView();
            }
        }
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        if (lp == null) {
            lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }
        addView(child, position, lp);
        return child;
    }

    private View getDefaultEmptyView() {
        if(mNoticeAdapter.getDefaultEmptyLayout() == 0) {

        }
        return LayoutInflater.from(mContext).inflate(mNoticeAdapter.getDefaultEmptyLayout(), this, false);
    }

    public void setAdapter(NoticeAdapter adapter) {
        mNoticeAdapter = adapter;
        getNextView(mNoticeAdapter.getDataSet());

        setInAnimation(AnimationUtils.loadAnimation(getContext(), mNoticeAdapter.getInAnimation()));
        setOutAnimation(AnimationUtils.loadAnimation(getContext(), mNoticeAdapter.getOutAnimation()));
    }



}
