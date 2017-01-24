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

/**
 * Created by chenupt on 2017/1/24.
 */

public class ItemDataHelper {

	public static <T> ItemData<T> wrap(T content, Class<? extends BaseVH> vh) {
		return new ItemData<>(content, vh);
	}

}
