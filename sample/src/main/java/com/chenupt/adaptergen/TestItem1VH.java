/*
 * Copyright 2017 chenupt
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

package com.chenupt.adaptergen;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chenupt.adaptergen.annotation.Adapter;
import com.chenupt.adaptergen.core.BaseVH;
import com.chenupt.adaptergen.core.ItemData;

/**
 * Created by chenupt on 2017/1/24.
 *
 * RecyclerView item.
 */

@Adapter(name = "TestAdapter")
public class TestItem1VH extends BaseVH<String>{

	private static final String TAG = "TestItem1VH";

	private TextView tvContent;

	@Override
	protected View onCreateView(ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.view_holder_item1, parent, false);
		tvContent = (TextView) view.findViewById(R.id.tvContent);
		return view;
	}

	@Override
	protected void onBindData(ItemData<String> itemData) {
		Log.d(TAG, "onBindData: " + itemData.content);
		tvContent.setText(itemData.content);
	}
}
