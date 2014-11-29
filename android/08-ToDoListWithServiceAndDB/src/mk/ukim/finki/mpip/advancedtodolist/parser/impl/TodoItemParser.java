package mk.ukim.finki.mpip.advancedtodolist.parser.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mk.ukim.finki.mpip.advancedtodolist.model.TodoItem;
import mk.ukim.finki.mpip.advancedtodolist.parser.Parser;

public class TodoItemParser implements Parser<List<TodoItem>> {

	@Override
	public List<TodoItem> parse(String content) throws JSONException {
		List<TodoItem> items = new ArrayList<TodoItem>();
		if (content != null) {
			JSONArray jsonItems = new JSONArray(content);

			for (int i = 0; i < jsonItems.length(); i++) {
				JSONObject jObj = (JSONObject) jsonItems.get(i);
				TodoItem item = new TodoItem();
				item.setName(jObj.getString("name"));
				item.setId(jObj.getLong("id"));
				item.setDone(jObj.getBoolean("done"));
				items.add(item);
			}
		}
		return items;
	}

}
