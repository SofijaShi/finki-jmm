package controllers;

import java.util.Date;

import models.TodoItem;
import play.mvc.Controller;
import play.mvc.results.RenderJson;

public class ToDoController extends Controller {

	public static void all(String format) {
		if (format == null) {

			renderJSON(TodoItem.findAll());
		} else if ("xml".equals(format)) {
			renderXml(TodoItem.findAll());
		} else {
			renderJSON(TodoItem.findAll());
		}
	}

	public static void allXml() {
		renderXml(TodoItem.findAll());
	}

	public static void add(String name, int done, long date) {
		TodoItem item = new TodoItem();
		item.name = name;
		item.done = done == 1;
		item.dueDate = new Date(date);
		item.save();
		renderJSON(item);
	}

}
