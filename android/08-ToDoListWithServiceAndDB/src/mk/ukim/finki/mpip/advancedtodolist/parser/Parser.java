package mk.ukim.finki.mpip.advancedtodolist.parser;

import org.json.JSONException;

public interface Parser<T> {

	public T parse(String content) throws JSONException;
}
