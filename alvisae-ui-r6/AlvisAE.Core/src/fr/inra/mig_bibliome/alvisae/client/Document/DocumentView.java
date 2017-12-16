/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Document;

import com.google.gwt.dom.client.Element;
import com.google.gwt.json.client.*;
import com.google.gwt.user.client.ui.IsWidget;
import fr.inra.mig_bibliome.alvisae.client.Edit.undo.UndoManager;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.client.data3.DOMRange;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotatedText;
import java.util.List;

/**
 * Interface of the classes that can display document text and the related
 * annotations
 *
 * @author fpapazian
 */
public interface DocumentView extends IsWidget {

	public static class Rect {

		int left;
		int top;
		int width;
		int height;

		public Rect(int left, int top, int width, int height) {
			this.left = left;
			this.top = top;
			this.width = width;
			this.height = height;
		}
	}

	public static class FlankingClips {

		final Rect first;
		final Rect last;

		public FlankingClips(Rect first, Rect last) {
			this.first = first;
			this.last = last == null ? first : last;
		}
	}

	public static class Options {

		private boolean readOnly;
		private boolean hiddenToolbar;
		private boolean collapsedToolbar;
		private boolean hiddenTitlebar;
		private boolean hiddenOccurencebar;
		private boolean veiledRelations;
		private Integer interlineSizeIndex = null;
		private String scrolledAnnotation = null;

		public Options(boolean readOnly, boolean hideToolbar, boolean collapsedToolbar, boolean hideTitlebar, boolean hideOccurencebar, boolean veiledRelations, Integer interlineSizeIndex, String scrolledAnnotation) {
			this.readOnly = readOnly;
			this.hiddenToolbar = hideToolbar;
			this.collapsedToolbar = collapsedToolbar;
			this.hiddenTitlebar = hideTitlebar;
			this.hiddenOccurencebar = hideOccurencebar;
			this.veiledRelations = veiledRelations;
			this.interlineSizeIndex = interlineSizeIndex;
			this.scrolledAnnotation = scrolledAnnotation;
		}

		public Options() {
			this(true, false, false, false, false, true, null, null);
		}

		public Options(Options options) {
			this(options.isReadOnly(), options.isHiddenToolbar(), options.isCollapsedToolbar(), options.isHiddenTitlebar(), options.isHiddenOccurencebar(), options.isVeiledRelations(), options.getInterlineSizeIndex(), options.getScrolledAnnotation());
		}

		public Options(String jsonOptionsStr) {
			this();
			if (jsonOptionsStr != null) {
				JSONValue opt = JSONParser.parseStrict(jsonOptionsStr);
				JSONObject options = opt.isObject();

				if (options != null) {
					for (String optKey : options.keySet()) {
						JSONBoolean optVal = options.get(optKey).isBoolean();
						if (optVal != null) {
							if ("readOnly".equals(optKey)) {
								setReadOnly(optVal.booleanValue());
							} else if ("hiddenToolbar".equals(optKey)) {
								setHideToolbar(optVal.booleanValue());
							} else if ("hiddenTitlebar".equals(optKey)) {
								setHideTitlebar(optVal.booleanValue());
							} else if ("hiddenOccurencebar".equals(optKey)) {
								setHideOccurencebar(optVal.booleanValue());
							} else if ("collapsedToolbar".equals(optKey)) {
								setCollapsedToolbar(optVal.booleanValue());
							} else if ("veiledRelations".equals(optKey)) {
								setVeiledRelations(optVal.booleanValue());
							}
						} else {
							JSONNumber optNum = options.get(optKey).isNumber();
							if (optNum != null) {
								if ("interlineSizeIndex".equals(optKey)) {
									setInterlineSizeIndex(new Double(optNum.doubleValue()).intValue());
								}
							} else {
								JSONString optStr = options.get(optKey).isString();
								if ("scrolledAnnotation".equals(optKey)) {
									setScrolledAnnotation(optStr.stringValue());
								}
							}
						}
					}
				}
			}
		}

		public boolean isCollapsedToolbar() {
			return collapsedToolbar;
		}

		public boolean isHiddenToolbar() {
			return hiddenToolbar;
		}

		public boolean isHiddenTitlebar() {
			return hiddenTitlebar;
		}

		public boolean isHiddenOccurencebar() {
			return hiddenOccurencebar;
		}

		public boolean isReadOnly() {
			return readOnly;
		}

		public boolean isVeiledRelations() {
			return veiledRelations;
		}

		public Integer getInterlineSizeIndex() {
			return interlineSizeIndex;
		}

		public String getScrolledAnnotation() {
			return scrolledAnnotation;
		}

		protected final void setCollapsedToolbar(boolean collapsedToolbar) {
			this.collapsedToolbar = collapsedToolbar;
		}

		protected final void setHideToolbar(boolean hideToolbar) {
			this.hiddenToolbar = hideToolbar;
		}

		protected final void setHideTitlebar(boolean hiddenTitlebar) {
			this.hiddenTitlebar = hiddenTitlebar;
		}

		protected final void setHideOccurencebar(boolean hiddenOccurencebar) {
			this.hiddenOccurencebar = hiddenOccurencebar;
		}

		protected final void setReadOnly(boolean readOnly) {
			this.readOnly = readOnly;
		}

		protected final void setVeiledRelations(boolean veiledRelations) {
			this.veiledRelations = veiledRelations;
		}

		protected final void setInterlineSizeIndex(Integer interlineSizeIndex) {
			this.interlineSizeIndex = interlineSizeIndex;
		}

		protected final void setScrolledAnnotation(String scrolledAnnotation) {
			this.scrolledAnnotation = scrolledAnnotation;
		}
	}

	/**
	 * Set the AnnotatedText displayed by the view
	 *
	 * @param document the document that will be displayed with its associated
	 * annotations
	 * @param readOnly true to disable any Annotation editing
	 */
	public void setDocument(AnnotatedTextHandler document, boolean readOnly);

	public void setDocument(AnnotatedTextHandler document, Options options);

	/**
	 *
	 * @return the AnnotatedText currently displayed by this view
	 */
	public AnnotatedText getDocument();

	public AnnotatedTextHandler getAnnotatedTextHandler();

	public UndoManager getUndoManager();

	/**
	 * Enable or Disable the possibility of creating/remove/modifying annotation
	 * via this view
	 *
	 * @param readOnly true to disable any Annotation editing
	 */
	public void setReadOnly(boolean readOnly);

	/**
	 *
	 * @return true if Annotation editing is disabled
	 */
	public boolean isReadOnly();

	public void createAnchorMarkersFromSelectedRanges();

	public void clearTextSelection(boolean setFocus);

	public void clearAnchorMarkerSelection();

	public void createAnchorMarkersFromRanges(String annotationType, List<DOMRange> ranges);

	public AnnotationDocumentViewMapper getMapper();

	/**
	 * @return true if the point specified by x,y coordinates is inside this
	 * view
	 */
	public boolean isPointInside(int x, int y);

	/**
	 * Reduce the coverage of the main selected Annotation by the specified text
	 * range
	 *
	 * @param ranges
	 */
	public void pruneRangeFromSelectedAnnotation(List<DOMRange> ranges);

	/**
	 * Extend the coverage of the main selected Annotation with the specified
	 * text range
	 *
	 * @param ranges
	 */
	public void extendSelectedAnnotationWithRange(List<DOMRange> ranges);

	/**
	 * @param annotationId
	 * @return The coordinate of the rectangular area occupied by a Text
	 * Annotation. In case of annotation spanning over several line, the area is
	 * the beginning of the annotation on the first line.
	 */
	public Rect getAnnotatedTextClip(String annotationId);

	/**
	 * @param Element representing the Text annotation (span)
	 * @return The coordinate of the rectangular area occupied by a Text
	 * Annotation. In case of annotation spanning over several line, the area is
	 * the beginning of the annotation on the first line.
	 */
	public Rect getAnnotatedTextClip(Element marker);

	/**
	 * Scroll into view the specified annotation
	 *
	 * @param annotationId
	 */
	public void setScrollPositionAtAnnotation(String annotationId);

	/**
	 *
	 * @return the Id of the HTML Element containing the Text of the displayed
	 * document
	 */
	public String getTextContainerId();

	public void setTitleText(String title);
        
        public String getTitleText();
}
