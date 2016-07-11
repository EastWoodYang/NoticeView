package com.ycdyng.noticeview;

import android.view.View;

public abstract class NoticeAdapter extends BaseAdapter {

    public abstract Object getDataSet();

    public abstract View getView(View view, Object dataSet);

    public abstract void setDateSet(Object dataSet);

    public int getInAnimation()  {
        return android.R.anim.fade_in;
    }

    public int getOutAnimation()  {
        return android.R.anim.fade_out;
    }

    public int getDefaultEmptyLayout() {
        return R.layout.default_empty_notice_layout;
    }

}
