/*
 * Copyright 2016 EastWood Yang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ycdyng.noticeview;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

public class NoticeView extends FrameLayout {

    private NoticeSwitcher mNoticeSwitcher;

    public static final int SMOOTH_SCROLL_DURATION_MS = 200;
    public static final int SMOOTH_SCROLL_LONG_DURATION_MS = 325;

    private boolean dismiss = false;

    private Context mContext;

    NoticeAdapter mSwitcherAdapter;
    AdapterDataSetObserver mDataSetObserver;

    private OnNoticeVisibilityListener mOnNoticeVisibilityListener;

    private SmoothScrollRunnable mCurrentSmoothScrollRunnable;
    private Interpolator mScrollAnimationInterpolator;

    private boolean autoShowWhenNotify = false;

    public NoticeView(Context context) {
        super(context);
        init(context, null);
    }

    public NoticeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        mNoticeSwitcher = new NoticeSwitcher(context, attrs);
        addView(mNoticeSwitcher, -1);
    }

    public void setAdapter(NoticeAdapter adapter) {
        if(mNoticeSwitcher != null && adapter != null) {
            if(mSwitcherAdapter != null && mDataSetObserver != null) {
                mSwitcherAdapter.unregisterDataSetObserver(mDataSetObserver);
            }
            mSwitcherAdapter = adapter;
            mDataSetObserver = new AdapterDataSetObserver();
            mSwitcherAdapter.registerDataSetObserver(mDataSetObserver);
            mNoticeSwitcher.setAdapter(mSwitcherAdapter);
        }
    }

    class AdapterDataSetObserver extends DataSetObserver {

        @Override
        public void onChanged() {
            super.onChanged();
            change();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            change();
        }

    }

    private void change() {
        mNoticeSwitcher.getNextView(mNoticeSwitcher.mNoticeAdapter.getDataSet());

        if(autoShowWhenNotify && isDismiss()) {
            show();
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mNoticeSwitcher.showNext();
                }
            }, SMOOTH_SCROLL_DURATION_MS);
        } else {
            mNoticeSwitcher.showNext();
        }
    }

    public void clear() {
        if(isDismiss()) {
            mSwitcherAdapter.setDateSet(null);
            mNoticeSwitcher.getNextView(null);
            mNoticeSwitcher.showNext();
            setVisibility(GONE);
        } else {
            mSwitcherAdapter.setDateSet(null);
            mSwitcherAdapter.notifyDataSetChanged();
        }
    }

    public void show() {
        dismiss = false;
        if(getVisibility() == View.GONE) {
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                setAlpha(0f);
            } else {
                getBackground().setAlpha(0);
            }
            setVisibility(View.VISIBLE);
            post(new Runnable() {
                @Override
                public void run() {
                    scrollTo(0, getHeight());
                    if(Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                        setAlpha(0.95f);
                    } else {
                        getBackground().setAlpha(242);
                    }
                    smoothScrollTo(0, null);
                }
            });
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    smoothScrollTo(0, null);
                }
            });
        }
    }

    public void dismiss() {
        dismiss = true;
        smoothScrollTo(getHeight(), null);
    }

    public boolean isDismiss() {
        return dismiss;
    }

    public void setOnNoticeVisibilityListener(OnNoticeVisibilityListener listener) {
        this.mOnNoticeVisibilityListener = listener;
    }

    protected final void setScroll(int value) {
        scrollTo(0, value);
    }

    protected int getPullToRefreshScrollDuration() {
        return SMOOTH_SCROLL_DURATION_MS;
    }

    protected int getPullToRefreshScrollDurationLonger() {
        return SMOOTH_SCROLL_LONG_DURATION_MS;
    }

    /**
     * Smooth Scroll to position using the default duration of
     * {@value #SMOOTH_SCROLL_DURATION_MS} ms.
     *
     * @param scrollValue - Position to scroll to
     */
    protected final void smoothScrollTo(int scrollValue) {
        smoothScrollTo(scrollValue, getPullToRefreshScrollDuration());
    }

    /**
     * Smooth Scroll to position using the default duration of
     * {@value #SMOOTH_SCROLL_DURATION_MS} ms.
     *
     * @param scrollValue - Position to scroll to
     * @param listener - Listener for scroll
     */
    protected final void smoothScrollTo(int scrollValue, OnSmoothScrollFinishedListener listener) {
        smoothScrollTo(scrollValue, getPullToRefreshScrollDuration(), 0, listener);
    }

    /**
     * Smooth Scroll to position using the longer default duration of
     * {@value #SMOOTH_SCROLL_LONG_DURATION_MS} ms.
     *
     * @param scrollValue - Position to scroll to
     */
    protected final void smoothScrollToLonger(int scrollValue) {
        smoothScrollTo(scrollValue, getPullToRefreshScrollDurationLonger());
    }

    /**
     * Smooth Scroll to position using the specific duration
     *
     * @param scrollValue - Position to scroll to
     * @param duration - Duration of animation in milliseconds
     */
    private final void smoothScrollTo(int scrollValue, long duration) {
        smoothScrollTo(scrollValue, duration, 0, null);
    }

    private final void smoothScrollTo(int newScrollValue, long duration, long delayMillis, OnSmoothScrollFinishedListener listener) {
        if (null != mCurrentSmoothScrollRunnable) {
            mCurrentSmoothScrollRunnable.stop();
        }

        final int oldScrollValue = getScrollY();
//        if(-oldScrollValue == maxHeight) {
//            if(listener != null) {
//                listener.onSmoothScrollFinished();
//            }
//            return;
//        }

        if (oldScrollValue != newScrollValue) {
            if (null == mScrollAnimationInterpolator) {
                // Default interpolator is a Decelerate Interpolator
                mScrollAnimationInterpolator = new DecelerateInterpolator();
            }
            mCurrentSmoothScrollRunnable = new SmoothScrollRunnable(oldScrollValue, newScrollValue, duration, listener);

            if (delayMillis > 0) {
                postDelayed(mCurrentSmoothScrollRunnable, delayMillis);
            } else {
                post(mCurrentSmoothScrollRunnable);
            }
        }
    }

    final class SmoothScrollRunnable implements Runnable {
        private final Interpolator mInterpolator;
        private final int mScrollToY;
        private final int mScrollFromY;
        private final long mDuration;
        private OnSmoothScrollFinishedListener mListener;

        private boolean mContinueRunning = true;
        private long mStartTime = -1;
        private int mCurrentY = -1;

        public SmoothScrollRunnable(int fromY, int toY, long duration, OnSmoothScrollFinishedListener listener) {
            mScrollFromY = fromY;
            mScrollToY = toY;
            mInterpolator = mScrollAnimationInterpolator;
            mDuration = duration;
            mListener = listener;
        }

        @Override
        public void run() {

            /**
             * Only set mStartTime if this is the first time we're starting,
             * else actually calculate the Y delta
             */
            if (mStartTime == -1) {
                mStartTime = System.currentTimeMillis();
            } else {

                /**
                 * We do do all calculations in long to reduce software float
                 * calculations. We use 1000 as it gives us good accuracy and
                 * small rounding errors
                 */
                long normalizedTime = (1000 * (System.currentTimeMillis() - mStartTime)) / mDuration;
                normalizedTime = Math.max(Math.min(normalizedTime, 1000), 0);

                final int deltaY = Math.round((mScrollFromY - mScrollToY)
                        * mInterpolator.getInterpolation(normalizedTime / 1000f));
                mCurrentY = mScrollFromY - deltaY;
                setScroll(mCurrentY);
            }

            // If we're not at the target Y, keep going...
            if (mContinueRunning && mScrollToY != mCurrentY) {
                ViewCompat.postOnAnimation(NoticeView.this, this);
            } else {
//                if (null != mListener) {
//                    mListener.onSmoothScrollFinished();
//                }
                if(mOnNoticeVisibilityListener != null) {
                    mOnNoticeVisibilityListener.onDismiss(isDismiss());
                }
            }
        }

        public void stop() {
            mContinueRunning = false;
            removeCallbacks(this);
        }
    }

    interface OnSmoothScrollFinishedListener {
        void onSmoothScrollFinished();
    }

    public interface OnNoticeVisibilityListener {
        void onDismiss(boolean dismiss);
    }
}
