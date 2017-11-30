package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import java.util.AbstractList;

/**
 * Wrapper for JsArrays that implement read-only Java Collection Framework list.
 * Allows to use JsArray in new Java pretty loops.
 * @author rbossy
 *
 * @param <T>
 */
public class JsArrayDecorator<T extends JavaScriptObject> extends AbstractList<T> {
	private final JsArray<T> jsArray;

	public JsArrayDecorator(JsArray<T> jsArray) {
		super();
		this.jsArray = jsArray;
	}

	@Override
	public T get(int index) {
		return jsArray.get(index);
	}

	@Override
	public int size() {
		return jsArray.length();
	}
}
