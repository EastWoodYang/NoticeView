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
