package com.ycdyng.noticeviewtrial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ycdyng.noticeview.NoticeAdapter;

public class CustomAdapter extends NoticeAdapter {

    private Context mContext;
    private Object mDataSet;
    private LayoutInflater mLayoutInflater;

    public CustomAdapter(Context context, Object dataSet) {
        mContext = context;
        mLayoutInflater =LayoutInflater.from(mContext);
        mDataSet = dataSet;
    }

    @Override
    public Object getDataSet() {
        return mDataSet;
    }

    @Override
    public View getView(View view, Object dataSet) {
        if(dataSet instanceof NoticeData) {
            NoticeData noticeData = (NoticeData) dataSet;
            view = mLayoutInflater.inflate(R.layout.item_notice_content_with_date, null);
            TextView contentTextView = (TextView) view.findViewById(R.id.content_text_view);
            TextView dataTextView = (TextView) view.findViewById(R.id.date_text_view);
            contentTextView.setText(noticeData.getContent());
            dataTextView.setText(noticeData.getDate());
        } else if(dataSet instanceof String) {
            String data = (String) dataSet;
            view = mLayoutInflater.inflate(R.layout.item_notice_content, null);
            TextView contentTextView = (TextView) view.findViewById(R.id.content_text_view);
            contentTextView.setText(data);
        }
        return view;
    }

    @Override
    public void setDateSet(Object dataSet) {
        mDataSet = dataSet;
    }

}
