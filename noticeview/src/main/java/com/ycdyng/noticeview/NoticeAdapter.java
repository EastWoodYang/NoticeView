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
