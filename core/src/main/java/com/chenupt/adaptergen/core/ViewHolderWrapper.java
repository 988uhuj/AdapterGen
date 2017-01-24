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

import android.support.v7.widget.RecyclerView;

/**
 * Created by chenupt on 2017/1/23.
 */

public class ViewHolderWrapper<T> extends RecyclerView.ViewHolder{

	private BaseVH vh;

	public ViewHolderWrapper(BaseVH baseVH) {
		super(baseVH.getView());
		this.vh = baseVH;
		baseVH.bindWrapper(this);
	}

	public BaseVH getVH() {
		return vh;
	}

	public void bindData(ItemData<T> itemData) {
		vh.bindData(itemData);
	}
}
