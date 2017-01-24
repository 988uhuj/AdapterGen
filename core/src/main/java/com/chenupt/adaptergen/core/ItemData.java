package com.chenupt.adaptergen.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenupt on 2017/1/23.
 */

public class ItemData<T> {

	public long id;
	public T content;
	public Class<? extends BaseVH> vh;
	public Map<String, Object> attrs;

	ItemData(T content, Class<? extends BaseVH> vh) {
		this.content = content;
		this.vh = vh;
	}

	public ItemData<T> addAttr(String key, Object value){
		if(attrs == null){
			attrs = new HashMap<>();
		}
		attrs.put(key, value);
		return this;
	}

	public void attach(List<ItemData> list) {
		list.add(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ItemData<?> itemData = (ItemData<?>) o;

		if (content != null ? !content.equals(itemData.content) : itemData.content != null)
			return false;
		if (vh != null ? !vh.equals(itemData.vh) : itemData.vh != null) return false;
		return attrs != null ? attrs.equals(itemData.attrs) : itemData.attrs == null;

	}

	@Override
	public int hashCode() {
		int result = content != null ? content.hashCode() : 0;
		result = 31 * result + (vh != null ? vh.hashCode() : 0);
		result = 31 * result + (attrs != null ? attrs.hashCode() : 0);
		return result;
	}
}
