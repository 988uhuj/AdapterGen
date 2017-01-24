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

package com.chenupt.adaptergen.core;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by chenupt on 2017/1/23.
 */

public abstract class BaseVH<T> {

	protected Context context;
	public ViewHolderWrapper wrapper;
	public View view;
	protected ItemData<T> itemData;

	protected abstract View onCreateView(ViewGroup parent);
	protected abstract void onBindData(ItemData<T> itemData);

	public View getView() {
		return view;
	}

	public final void createView(ViewGroup parent) {
		context = parent.getContext();
		view = onCreateView(parent);
	}

	public final void bindData(ItemData<T> itemData) {
		this.itemData = itemData;
		onBindData(itemData);
	}

	final void bindWrapper(ViewHolderWrapper wrapper) {
		this.wrapper = wrapper;
	}
}
