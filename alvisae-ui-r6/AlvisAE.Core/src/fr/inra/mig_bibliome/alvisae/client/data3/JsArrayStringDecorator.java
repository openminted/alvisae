package fr.inra.mig_bibliome.alvisae.client.data3;

import com.google.gwt.core.client.JsArrayString;
import java.util.AbstractList;

/**
 * Wrapper for JsArrays that implement read-only Java Collection Framework list.
 * Allows to use JsArray in new Java pretty loops.
 * @author rbossy
 *
 * @param <T>
 */
public class JsArrayStringDecorator extends AbstractList<String> {
	private final JsArrayString jsArray;

	public JsArrayStringDecorator(JsArrayString jsArray) {
		super();
		this.jsArray = jsArray;
	}

	@Override
	public String get(int index) {
		return jsArray.get(index);
	}

	@Override
	public int size() {
		return jsArray.length();
	}
}
