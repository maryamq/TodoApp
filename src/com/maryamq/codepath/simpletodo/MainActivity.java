package com.maryamq.codepath.simpletodo;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {
	private ArrayList<String> todoItems;
	private ArrayAdapter<String> todoAdapter;
	private ListView lvItems;
	private EditText etNewItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lvItems = (ListView) this.findViewById(R.id.lvItems);
		etNewItem = (EditText) this.findViewById(R.id.etNewItem);
		readItems();
		todoAdapter = new ArrayAdapter<String>(getBaseContext(),
				android.R.layout.simple_list_item_1, todoItems);
		// Attach the adapter
		lvItems.setAdapter(todoAdapter);
		setupListViewListener();
	}

	private void setupListViewListener() {
		lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View item,
					int position, long id) {
				todoItems.remove(position);
				writeItems();
				todoAdapter.notifyDataSetChanged();
				return true;
			}

		});
	}

	private void readItems() {
		File fileDir = getFilesDir();
		File todoFile = new File(fileDir, "todo.txt");
		try {
			todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
		} catch (IOException e) {
			todoItems = new ArrayList<String>();
		}
	}

	private void writeItems() {
		File fileDir = getFilesDir();
		File todoFile = new File(fileDir, "todo.txt");
		try {
			FileUtils.writeLines(todoFile, todoItems);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void onAddedItem(View v) {
		String text = etNewItem.getText().toString();
		etNewItem.setText("");
		todoAdapter.add(text);
		writeItems();
	}

	private void populateArrayItems() {
		// TODO Auto-generated method stub
		todoItems = new ArrayList<String>();
		todoItems.add("Item 1");
		todoItems.add("Item 2");
		todoItems.add("Item 3");
	}
}
