
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
import android.widget.Toast;

import com.chenupt.adaptergen.annotation.Adapter;
import com.chenupt.adaptergen.core.BaseVH;
import com.chenupt.adaptergen.core.ItemData;

/**
 * Created by chenupt on 2017/1/24.
 */

@Adapter(name = "TestAdapter")
public class TestItem2VH extends BaseVH<Integer>{

	private static final String TAG = "TestItem2VH";

	private TextView tvContent;

	@Override
	protected View onCreateView(ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.view_holder_item2, parent, false);
		tvContent = (TextView) view.findViewById(R.id.tvContent);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "content: " + itemData.content, Toast.LENGTH_SHORT).show();
			}
		});
		return view;
	}

	@Override
	protected void onBindData(ItemData<Integer> itemData) {
		Log.d(TAG, "onBindData: " + itemData.content);
	}
}
