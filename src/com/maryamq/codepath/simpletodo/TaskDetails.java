package com.maryamq.codepath.simpletodo;

import java.util.ArrayList;
import java.util.Calendar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TaskDetails {
	private int priority;
	private String task;
	private Calendar dateTime = Calendar.getInstance();
	private boolean hasDate;

	public TaskDetails() {
		hasDate = false;
		// TODO(maryam): Change this once time feature is available.
		clearTimeFromCalendar(dateTime);
	}

	public TaskDetails(String json) {
		try {
			JSONObject object = new JSONObject(json);
			this.task = object.getString("task");
			this.priority = object.getInt("priority");
			this.hasDate = object.getBoolean("hasDate");
			this.dateTime.set(Calendar.YEAR, object.getInt("year"));
			dateTime.set(Calendar.MONTH, object.getInt("month"));
			dateTime.set(Calendar.DAY_OF_MONTH, object.getInt("day"));

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<TaskDetails> fromJson(ArrayList<String> jsonTasks) {
		ArrayList<TaskDetails> tasks = new ArrayList<TaskDetails>();
		for (String jsonString : jsonTasks) {
			tasks.add(new TaskDetails(jsonString));
		}
		return tasks;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public boolean hasDate() {
		return this.hasDate;
	}

	public void setHasDate(boolean value) {
		this.hasDate = value;
	}

	public static void clearTimeFromCalendar(Calendar c) {
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
	}

	public JSONObject asJsonObject() throws JSONException {
		JSONObject ob = new JSONObject();
		ob.put("task", this.getTask());
		ob.put("priority", this.getPriority());
		ob.put("hasDate", this.hasDate);
		ob.put("day", this.dateTime.get(Calendar.DAY_OF_MONTH));
		ob.put("month", this.dateTime.get(Calendar.MONTH));
		ob.put("year", this.dateTime.get(Calendar.YEAR));
		return ob;
	}

	@Override
	public String toString() {
		try {
			return asJsonObject().toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "ERROR: " + super.toString();
	}

	public Calendar getDateTime() {
		return dateTime;
	}

	public void setDateTime(Calendar dateTime) {
		this.dateTime = dateTime;
	}

}
