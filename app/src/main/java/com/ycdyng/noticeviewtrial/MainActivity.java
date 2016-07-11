package com.ycdyng.noticeviewtrial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ycdyng.noticeview.NoticeView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private NoticeView mNoticeView;

    private CustomAdapter mCustomAdapter;

    private Button mShowButton;
    private Button mDismissButton;
    private Button mClearButton;
    private Button mChangeContentButton;
    private Button mChangeLayoutButton;

    private int index;

    private String[] notice = {"杭州地铁4号线南段发生事故 仍有3人失踪", "台风今日中午将登陆福建 浙江多地明后天有暴雨", "网传高考结束离婚率上升 杭州数据显示趋势不明显"};
    private String[] date = {"2016.07.09", "2016.07.09 12:00", "2016.07.09 14:40"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNoticeView = (NoticeView) findViewById(R.id.notice_view);
        mShowButton = (Button) findViewById(R.id.show_button);
        mDismissButton = (Button) findViewById(R.id.dismiss_button);
        mClearButton = (Button) findViewById(R.id.clear_button);
        mChangeContentButton = (Button) findViewById(R.id.change_content_button);
        mChangeLayoutButton = (Button) findViewById(R.id.change_layout_button);

        mShowButton.setOnClickListener(this);
        mDismissButton.setOnClickListener(this);
        mClearButton.setOnClickListener(this);
        mChangeContentButton.setOnClickListener(this);
        mChangeLayoutButton.setOnClickListener(this);

        mCustomAdapter = new CustomAdapter(this, null);
        mNoticeView.setAdapter(mCustomAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.show_button: {
                mNoticeView.show();
                break;
            }
            case R.id.dismiss_button: {
                mNoticeView.dismiss();
                break;
            }
            case R.id.change_content_button: {
                mCustomAdapter.setDateSet(notice[index]);
                mCustomAdapter.notifyDataSetChanged();
                index++;
                if(index == 3) {
                    index = 0;
                }
                break;
            }
            case R.id.clear_button: {
                mNoticeView.clear();
                break;
            }
            case R.id.change_layout_button: {
                NoticeData noticeData = new NoticeData();
                noticeData.setContent(notice[index]);
                noticeData.setDate(date[index]);
                mCustomAdapter.setDateSet(noticeData);
                mCustomAdapter.notifyDataSetChanged();
                index++;
                if(index == 3) {
                    index = 0;
                }
                break;
            }
        }
    }
}
