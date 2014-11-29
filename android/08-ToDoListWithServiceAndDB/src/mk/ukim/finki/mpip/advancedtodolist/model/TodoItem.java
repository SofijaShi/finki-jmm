package mk.ukim.finki.mpip.advancedtodolist.model;

import java.util.Date;

public class TodoItem extends DatabaseItem {

	private String name;
	private boolean done;
	private Date dueDate;

	public TodoItem() {
	}

	public TodoItem(String name, boolean done, Date dueDate) {
		super();
		this.name = name;
		this.done = done;
		this.dueDate = dueDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

}
