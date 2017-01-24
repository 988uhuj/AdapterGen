package com.chenupt.adaptergen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chenupt.adaptergen.core.ItemData;
import com.chenupt.adaptergen.core.ItemDataHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	private RecyclerView recyclerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

		// TestAdapter is generated when building
		TestAdapter adapter = new TestAdapter();
		List<ItemData> dataList = new ArrayList<>();
		for (int i = 0; i < 30; i++) {
			// Use ItemDataHelper to create a ItemData for RecyclerView Item.
			ItemDataHelper.wrap("ITEM " + i, TestItem1VH.class).attach(dataList);
			// Another ItemData which VH is different
			ItemDataHelper.wrap(i, TestItem2VH.class).attach(dataList);
		}
		adapter.dataList = dataList;

		recyclerView.setAdapter(adapter);
	}
}
