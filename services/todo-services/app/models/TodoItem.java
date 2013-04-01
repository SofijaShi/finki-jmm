package models;

import java.util.Date;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class TodoItem extends Model {

	public String name;
	public boolean done;
	public Date dueDate;

	@Override
	public String toString() {

		return String.format("[%d] %s (%s)", id, name, done ? "done1"
				: "not done1");
	}

}
