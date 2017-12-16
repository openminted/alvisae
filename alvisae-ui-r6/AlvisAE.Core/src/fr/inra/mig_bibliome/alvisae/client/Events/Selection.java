/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.inra.mig_bibliome.alvisae.client.Events;

import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author fpapazian
 */
public class Selection {

	/**
	 * Detail of an single selected Annotation
	 */
	public static abstract class GenericAnnotationSelection {

		private final Annotation annotation;

		public GenericAnnotationSelection(Annotation annotation) {
			this.annotation = annotation;
		}

		private GenericAnnotationSelection(GenericAnnotationSelection annotationSelection) {
			this(annotationSelection.annotation);
		}

		/**
		 *
		 * @return the selected Annotation
		 */
		public Annotation getAnnotation() {
			return annotation;
		}

		public abstract GenericAnnotationSelection clone();
	}

	/**
	 * Detail of an single selected TEXT Annotation
	 */
	public static class TextAnnotationSelection extends GenericAnnotationSelection {

		private final ArrayList<String> selectedMarkers;

		public TextAnnotationSelection(Annotation annotation) {
			this(annotation, new ArrayList<String>());
		}

		public TextAnnotationSelection(Annotation annotation, ArrayList<String> selectedMarkers) {
			super(annotation);
			this.selectedMarkers = new ArrayList<String>();
			if (selectedMarkers != null) {
				this.selectedMarkers.addAll(selectedMarkers);
			}
		}

		public TextAnnotationSelection(Annotation annotation, String selectedMarker) {
			this(annotation, new ArrayList<String>());
			this.selectedMarkers.add(selectedMarker);
		}

		private TextAnnotationSelection(TextAnnotationSelection annotationSelection) {
			this(annotationSelection.getAnnotation(), annotationSelection.selectedMarkers);
		}

		/**
		 *
		 * @return the list of IDs of the selected Markers
		 */
		public ArrayList<String> getSelectedMarkers() {
			return selectedMarkers;
		}

		/**
		 *
		 * @return The "Main" marker of the selected Annotation i.e. the one
		 * with focus or the one that was actually clicked
		 */
		public String getMainSelectedMarker() {
			return getSelectedMarkers() != null && !getSelectedMarkers().isEmpty() ? getSelectedMarkers().get(0) : null;
		}

		@Override
		public TextAnnotationSelection clone() {
			return new TextAnnotationSelection(this);
		}
	}

	public static class RelationAnnotationSelection extends GenericAnnotationSelection {

		public RelationAnnotationSelection(Annotation annotation) {
			super(annotation);
		}

		public RelationAnnotationSelection(RelationAnnotationSelection annotationSelection) {
			this(annotationSelection.getAnnotation());
		}

		@Override
		public RelationAnnotationSelection clone() {
			return new RelationAnnotationSelection(this);
		}
	}

	public static class GroupAnnotationSelection extends GenericAnnotationSelection {

		public GroupAnnotationSelection(Annotation annotation) {
			super(annotation);
		}

		public GroupAnnotationSelection(GroupAnnotationSelection annotationSelection) {
			this(annotationSelection.getAnnotation());
		}

		@Override
		public GroupAnnotationSelection clone() {
			return new GroupAnnotationSelection(this);
		}
	}

	/**
	 * Detail of an multiple selection of Annotations
	 */
	public static class AnnotationSelections {

		private final ArrayList<GenericAnnotationSelection> selections = new ArrayList<GenericAnnotationSelection>();

		public AnnotationSelections() {
		}

		public AnnotationSelections(AnnotationSelections selectedAnnotations) {
			this();
			appendCopyOfAnnotationSelections(selectedAnnotations);
		}

		/**
		 *
		 * @return list of the selected Annotation
		 */
		public ArrayList<GenericAnnotationSelection> getSelections() {
			return selections;
		}

		/**
		 *
		 * @return true if there is no Annotation selected
		 */
		public boolean isEmpty() {
			return getSelections() == null || getSelections().isEmpty();
		}

		public List<Annotation> getSelectedAnnotations() {
			List<Annotation> selected = new ArrayList<Annotation>();
			for (GenericAnnotationSelection s : getSelections()) {
				selected.add(s.getAnnotation());;
			}
			return selected;
		}

		/**
		 *
		 * @return the main selected Annotation, i.e. the one with the focus, or
		 * the last selected one
		 */
		public Annotation getMainSelectedAnnotation() {
			return !isEmpty() ? getSelections().get(getSelections().size() - 1).getAnnotation() : null;
		}

		public Annotation getMainSelectedTextAnnotation() {
			if (isEmpty()) {
				return null;
			} else {
				for (int i = getSelections().size() - 1; i >= 0; i--) {
					GenericAnnotationSelection s = getSelections().get(i);
					if (s instanceof TextAnnotationSelection) {
						return s.getAnnotation();
					}
				}
				return null;
			}
		}

		public Annotation getMainSelectedRelationAnnotation() {
			if (isEmpty()) {
				return null;
			} else {
				for (int i = getSelections().size() - 1; i >= 0; i--) {
					GenericAnnotationSelection s = getSelections().get(i);
					if (s instanceof RelationAnnotationSelection) {
						return s.getAnnotation();
					}
				}
				return null;
			}
		}

		public Annotation getMainSelectedGroupAnnotation() {
			if (isEmpty()) {
				return null;
			} else {
				for (int i = getSelections().size() - 1; i >= 0; i--) {
					GenericAnnotationSelection s = getSelections().get(i);
					if (s instanceof GroupAnnotationSelection) {
						return s.getAnnotation();
					}
				}
				return null;
			}
		}

		public ArrayList<String> getMainSelectedAnnotationMarkers() {
			GenericAnnotationSelection s;
			if (isEmpty()) {
				return null;
			} else {
				for (int i = getSelections().size() - 1; i >= 0; i--) {
					s = getSelections().get(i);
					if (s instanceof TextAnnotationSelection) {
						return ((TextAnnotationSelection) s).getSelectedMarkers();
					}
				}
				return null;
			}
		}

		public String getMainSelectedMarker() {
			GenericAnnotationSelection s;
			if (isEmpty()) {
				return null;
			} else {
				for (int i = getSelections().size() - 1; i >= 0; i--) {
					s = getSelections().get(i);
					if (s instanceof TextAnnotationSelection) {
						return ((TextAnnotationSelection) s).getMainSelectedMarker();
					}
				}
				return null;
			}
		}

		public List<TextAnnotationSelection> getTextAnnotationSelection() {
			ArrayList<TextAnnotationSelection> result = new ArrayList<TextAnnotationSelection>();
			for (GenericAnnotationSelection s : getSelections()) {
				if (s instanceof TextAnnotationSelection) {
					result.add((TextAnnotationSelection) s);
				}
			}
			return result;
		}

		public List<String> getSelectedTextAnnotationIds() {
			ArrayList<String> result = new ArrayList<String>();
			for (GenericAnnotationSelection s : getSelections()) {
				if (s instanceof TextAnnotationSelection) {
					result.add(s.getAnnotation().getId());
				}
			}
			return result;
		}

		public ArrayList<String> getSelectedRelationAnnotationIds() {
			ArrayList<String> result = new ArrayList<String>();
			for (GenericAnnotationSelection s : getSelections()) {
				if (s instanceof RelationAnnotationSelection) {
					result.add(s.getAnnotation().getId());
				}
			}
			return result;
		}

		public TextAnnotationSelection addAnnotationSelection(Annotation annotation, ArrayList<String> selectedMarkers) {
			TextAnnotationSelection newSelection = null;
			if (!isAnnotationSelected(annotation.getId())) {
				newSelection = new TextAnnotationSelection(annotation, selectedMarkers);
				getSelections().add(newSelection);
			}
			return newSelection;
		}

		public TextAnnotationSelection addAnnotationSelection(Annotation annotation, String mainSelectedMarkerId, ArrayList<String> markerIds) {
			ArrayList<String> selectedMarkers = new ArrayList<String>();
			selectedMarkers.add(mainSelectedMarkerId);
			for (String mId : markerIds) {
				if (!mId.equals(mainSelectedMarkerId)) {
					selectedMarkers.add(mId);
				}
			}
			return addAnnotationSelection(annotation, selectedMarkers);
		}

		public void replaceSelection(AnnotationSelections annotationSelection) {
			clear();
			appendCopyOfAnnotationSelections(annotationSelection);
		}

		private void appendCopyOfAnnotationSelections(AnnotationSelections annotationSelection) {
			if (annotationSelection != null) {
				for (GenericAnnotationSelection selected : annotationSelection.getSelections()) {
					getSelections().add(selected.clone());
				}
			}
		}

		public HashSet<String> getSeletedAnnotationIds() {
			HashSet<String> selectedIds = new HashSet<String>();
			for (GenericAnnotationSelection selectedAnnotation : getSelections()) {
				selectedIds.add(selectedAnnotation.getAnnotation().getId());
			}
			return selectedIds;
		}

		public int getIndexByAnnotationId(String annotationId) {
			int index = 0;
			for (GenericAnnotationSelection selectedAnnotation : getSelections()) {
				if (annotationId.equals(selectedAnnotation.getAnnotation().getId())) {
					return index;
				}
				index++;
			}
			return -1;
		}

		/**
		 *
		 * @return true if the specified Annotation is contained in this
		 * multi-selection
		 */
		public boolean isAnnotationSelected(String annotationId) {
			return getIndexByAnnotationId(annotationId) >= 0;
		}

		/**
		 *
		 * @return delete the specified Annotation from this multi-selection
		 */
		public void removeAnnotationFromSelection(String annotationId) {
			int index = getIndexByAnnotationId(annotationId);
			if (index >= 0) {
				getSelections().remove(index);
			}
		}

		/**
		 * Removes all Annotation from this multi-selection
		 */
		public void clear() {
			getSelections().clear();
		}
	}
}
