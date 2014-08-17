package com.maryamq.codepath.simpletodo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TasksAdapter extends ArrayAdapter<TaskDetails> {
	public TasksAdapter(Context context, ArrayList<TaskDetails> tasks) {
		super(context, R.layout.task_details, tasks);
	}

	static int getBackgroundColor(int priority) {
		switch (priority) {
		case 1:
			return Color.BLUE;
		case 2:
			return Color.YELLOW;
		case 3:
			return Color.RED;
		}
		return Color.WHITE;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		TaskDetails task = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.task_details, parent, false);
		}
		// Lookup view for data population
		TextView tvTask = (TextView) convertView.findViewById(R.id.tvTask);
		TextView tvPriority = (TextView) convertView
				.findViewById(R.id.tvPriority);
		TextView tvDueDate = (TextView) convertView.findViewById(R.id.tvDate);
		// Populate the data into the template view using the data object
		String taskText = task.getTask();

		// If the due date is specified, show it. Color red = due in <=1 day.
		// Green otherwise.
		if (task.hasDate()) {
			Calendar date = task.getDateTime();
			Calendar now = Calendar.getInstance();
			long diff = date.getTimeInMillis() - now.getTimeInMillis();
			long days = TimeUnit.MILLISECONDS.toDays(diff);
			int color = Color.BLUE;
			if (days <= 1) {
				color = Color.RED;
			}
			String dueDate = String.format("Due: %d/%d/%d ",
					date.get(Calendar.YEAR), date.get(Calendar.MONTH),
					date.get(Calendar.DAY_OF_MONTH));
			tvDueDate.setText(dueDate);
			tvDueDate.setTextColor(color);
		} else {
			tvTask.setText(taskText);
			tvDueDate.setText("");
		}

		tvPriority.setBackgroundColor(getBackgroundColor(task.getPriority()));
		// Return the completed view to render on screen
		return convertView;
	}
}
