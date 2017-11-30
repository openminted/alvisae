/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Conso;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.http.client.Response;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import fr.inra.mig_bibliome.alvisae.client.Annotation.AnnotationDetailsUi;
import fr.inra.mig_bibliome.alvisae.client.Config.GlobalStyles;
import fr.inra.mig_bibliome.alvisae.client.Config.History.BasicTaskReviewParams;
import fr.inra.mig_bibliome.alvisae.client.Config.History.PlaceParams.TaskReviewParams;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientBaseGinInjector;
import fr.inra.mig_bibliome.alvisae.client.Conso.AnnSetCompare.ConsolidationHandler.ConsolidationProcessingBlock;
import fr.inra.mig_bibliome.alvisae.client.Document.ConsolidationStatusDisplayer;
import fr.inra.mig_bibliome.alvisae.client.Document.DocumentUi;
import fr.inra.mig_bibliome.alvisae.client.Document.DocumentView;
import fr.inra.mig_bibliome.alvisae.client.Document.RequiresResizeSpy;
import fr.inra.mig_bibliome.alvisae.client.Edit.AnnotationEdit;
import fr.inra.mig_bibliome.alvisae.client.Edit.AnnotationSetReplaceUnmatchedSourceEdit;
import fr.inra.mig_bibliome.alvisae.client.Edit.TextAnnotationCreationEdit;
import fr.inra.mig_bibliome.alvisae.client.Events.EditHappenedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.EditHappenedEventHandler;
import fr.inra.mig_bibliome.alvisae.client.Events.InformationReleasedEvent;
import fr.inra.mig_bibliome.alvisae.client.data.Retrieve.DetailedAsyncResponseHandler;
import fr.inra.mig_bibliome.alvisae.client.data.Retrieve.NetworkActivityDisplayer;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotationBackReferenceImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotationImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotationSetImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.CDWXS_ConsoBlocks_ResponseImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.JsArrayDecorator;
import fr.inra.mig_bibliome.alvisae.client.data3.PropertiesImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.SourceAnnotationsImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.TextBindingImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.validation.ConsolidationBlockImpl;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationBackReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.ConsolidationStatus;
import fr.inra.mig_bibliome.alvisae.shared.data3.Fragment;
import fr.inra.mig_bibliome.alvisae.shared.data3.SpecifiedAnnotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.ConsolidationBlock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

/**
 *
 * @author fpapazian
 */
public class AnnSetCompare extends Composite implements AnnotationSetConsoView, EditHappenedEventHandler {

	public static class ConsolidationHandler {

		public static class ConsolidationProcessingBlock {

			private final int annotationSetId;
			private final ConsolidationBlock block;
			private final Map<String, ConsolidationStatus> sourceStatus = new HashMap<String, ConsolidationStatus>();
			private final Set<String> blockAnnotationIds = new HashSet<String>();
			private final Map<String, ConsolidationStatus> updatedStatus = new HashMap<String, ConsolidationStatus>();
			private final AnnotationKind kind;

			private ConsolidationProcessingBlock(int annotationSetId, ConsolidationBlock block, AnnotationKind kind, AnnotationSetImpl reviewingAnnSet) {
				this.annotationSetId = annotationSetId;
				this.block = block;

				for (List<? extends AnnotationReference> members : block.getMembers()) {
					for (AnnotationReference aRef : members) {
						if (aRef.getAnnotationSetId() == annotationSetId) {
							String annotationId = aRef.getAnnotationId();
							blockAnnotationIds.add(annotationId);
							if (block.isWithoutConflict()) {
								updatedStatus.put(annotationId, ConsolidationStatus.RESOLVED);
							}
						}
					}
				}

				this.kind = kind;
				List<AnnotationImpl> allAnn = new ArrayList<AnnotationImpl>();
				switch (kind) {
					case TEXT:
						allAnn.addAll(reviewingAnnSet.getTextAnnotations());
						break;
					case GROUP:
						allAnn.addAll(reviewingAnnSet.getGroups());
						break;
					case RELATION:
						allAnn.addAll(reviewingAnnSet.getRelations());
						break;
					default:
				}
				//Retrieve Annotation that must not be proposed for merging anymore
				//1- already registered Annotation as source Annnotation of merged Annotation
				Set<String> resolvedSourceAnnIds = getResolvedSourceAnnotationId(allAnn, annotationSetId);
				//if annotations in block are already registered as source Annnotation in reviewed AnnotationSet,
				// then they must not be proposed for merging
				blockAnnotationIds.removeAll(resolvedSourceAnnIds);


				//2- already registered Annotation as unmatched Annnotation of reviewing AnnotationSet
				Set<String> unmatchedSourceAnnIds = getUnmatchedSourceAnnotationId(reviewingAnnSet.getUnmatchedSourceAnnotations(), annotationSetId);
				blockAnnotationIds.removeAll(unmatchedSourceAnnIds);

			}

			public AnnotationKind getKind() {
				return kind;
			}

			private boolean hasSomeResolvableAnnotation() {
				boolean result = false;
				for (ConsolidationStatus status : updatedStatus.values()) {
					if (!ConsolidationStatus.POSTPONED.equals(status)) {
						result = true;
						break;
					}

				}
				return result;
			}

			private void updateAnnotationConsolidationStatus(String annotationId, ConsolidationStatus newStatus) {
				updatedStatus.put(annotationId, newStatus);
			}

			private ConsolidationStatus getAnnotationConsolidationStatus(SpecifiedAnnotation annotation) {
				ConsolidationStatus status = null;
				String annotationId = annotation.getAnnotation().getId();
				if (blockAnnotationIds.contains(annotationId)) {
					status = updatedStatus.get(annotationId);
					if (status == null) {
						status = ConsolidationStatus.POSTPONED;
						updatedStatus.put(annotationId, status);
					}
				}
				return status;
			}

			private List<AnnotationBackReference> getBackReferences() {
				List<AnnotationBackReference> backRefs = new ArrayList<AnnotationBackReference>();
				for (Entry<String, ConsolidationStatus> e : updatedStatus.entrySet()) {
					backRefs.add(AnnotationBackReferenceImpl.create(e.getKey(), annotationSetId, e.getValue()));
				}
				return backRefs;
			}
		}

		private static Set<String> getResolvedSourceAnnotationId(List<AnnotationImpl> annotations, int annotationSetId) {
			Set<String> result = new HashSet<String>();
			for (AnnotationImpl ann : annotations) {
				SourceAnnotationsImpl sourcesAnn = ann.getSourceAnnotations();
				if (sourcesAnn != null) {
					for (AnnotationBackReference abr : sourcesAnn.getAnnotationBackReferences()) {
						if (abr.getAnnotationSetId() == annotationSetId) {
							String annotationId = abr.getAnnotationId();
							ConsolidationStatus annotationStatus = abr.getConsolidationStatus();
							if (ConsolidationStatus.RESOLVED.equals(annotationStatus) || ConsolidationStatus.REJECTED.equals(annotationStatus)) {
								result.add(annotationId);
							}
						}
					}
				}
			}
			return result;
		}

		private static Set<String> getUnmatchedSourceAnnotationId(SourceAnnotationsImpl sourcesAnn, int annotationSetId) {
			Set<String> result = new HashSet<String>();
			if (sourcesAnn != null) {
				for (AnnotationBackReference abr : sourcesAnn.getAnnotationBackReferences()) {
					if (abr.getAnnotationSetId() == annotationSetId) {
						String annotationId = abr.getAnnotationId();
						result.add(annotationId);
					}
				}
			}
			return result;
		}
		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		private List<ConsolidationBlock> consolidationBlocks = null;
		private int internalBlockIndex;
		private Set<Integer> completedBlocksByIndex = new HashSet<Integer>();
		private List<ConsolidationProcessingBlock> processedBlocks = new ArrayList<ConsolidationProcessingBlock>();
		private AnnotatedTextHandler consolidatedDoc = null;
		private AnnotationSetImpl reviewingAnnSet;
		private Map<Integer, AnnotatedTextHandler> reviewedDocByAnnSetId = new HashMap<Integer, AnnotatedTextHandler>();

		private void reset() {
			consolidationBlocks = null;
			internalBlockIndex = - 1;
			processedBlocks.clear();
			reviewedDocByAnnSetId.clear();
			completedBlocksByIndex.clear();
		}

		/**
		 *
		 * @return Id of Annotations (by AnnotationSets) which are already
		 * adjudicated and registered in the AnnotationSet of the review
		 */
		private Map<Integer, Set<String>> getAlreadyResolvedAnnotations() {
			Map<Integer, Set<String>> resolvedAnnotations = new HashMap<Integer, Set<String>>();
			List<AnnotationImpl> reviewingAnnotations = reviewingAnnSet.getAnnotations();
			for (int reviewedAnnSetId : reviewedDocByAnnSetId.keySet()) {
				Set<String> resolvedSourceAnnIds = getResolvedSourceAnnotationId(reviewingAnnotations, reviewedAnnSetId);
				resolvedSourceAnnIds.addAll(getUnmatchedSourceAnnotationId(reviewingAnnSet.getUnmatchedSourceAnnotations(), reviewedAnnSetId));
				resolvedAnnotations.put(reviewedAnnSetId, resolvedSourceAnnIds);
			}
			return resolvedAnnotations;
		}

		/**
		 *
		 * @return Annotations which are not yet adjudicated and registered in
		 * the AnnotationSet of the review
		 */
		public List<AnnotationBackReference> getUnresolvedAnnotation() {

			List<AnnotationBackReference> result = new ArrayList<AnnotationBackReference>();

			//retrieve all resolved Annotations by AnnotationSets
			Map<Integer, Set<String>> resolvedAnnotations = getAlreadyResolvedAnnotations();

			//check for each block if it contains only resolved
			for (int i = 0; i < consolidationBlocks.size(); i++) {
				ConsolidationBlock block = consolidationBlocks.get(i);

				for (List<? extends AnnotationReference> members : block.getMembers()) {

					for (AnnotationReference aRef : members) {
						//FIXME : retrieving resolvedAnnotations could be done only once in the enclosing loop
						int annotationSetId = aRef.getAnnotationSetId();
						Set<String> resolvedSourceAnnIds = resolvedAnnotations.get(annotationSetId);

						String annotationId = aRef.getAnnotationId();
						if (resolvedSourceAnnIds == null) {
							result.add(AnnotationBackReferenceImpl.create(annotationId, annotationSetId, ConsolidationStatus.REJECTED));
						} else if (!resolvedSourceAnnIds.contains(annotationId)) {
							result.add(AnnotationBackReferenceImpl.create(annotationId, annotationSetId, ConsolidationStatus.REJECTED));
						}
					}

				}
			}
			return result;
		}

		public void computeAlreadyCompletedBlocks() {
			if (consolidatedDoc != null && consolidationBlocks != null) {
				completedBlocksByIndex.clear();

				reviewingAnnSet = consolidatedDoc.getEditableUsersAnnotationSet();

				//Important note : collections usage is limited with Javascript Overlay Types,
				//since equals() and hashcode() is defined final in their common ancestor JavascriptObject

				//retrieve all resolved Annotations by AnnotationSets
				Map<Integer, Set<String>> resolvedAnnotations = getAlreadyResolvedAnnotations();

				//check for each block if it contains only resolved
				for (int i = 0; i < consolidationBlocks.size(); i++) {
					ConsolidationBlock block = consolidationBlocks.get(i);

					Set<Integer> annSetWithUnresolvedSourceAnnotation = new HashSet<Integer>();

					membersByAnnSetId:
					for (List<? extends AnnotationReference> members : block.getMembers()) {
						for (AnnotationReference aRef : members) {
							//FIXME : retrieving resolvedAnnotations could be done only once in the enclosing loop
							int annotationSetId = aRef.getAnnotationSetId();
							Set<String> resolvedSourceAnnIds = resolvedAnnotations.get(annotationSetId);

							String annotationId = aRef.getAnnotationId();
							if (resolvedSourceAnnIds == null) {
								annSetWithUnresolvedSourceAnnotation.add(annotationSetId);
								break membersByAnnSetId;
							} else if (!resolvedSourceAnnIds.contains(annotationId)) {
								annSetWithUnresolvedSourceAnnotation.add(annotationSetId);
								break membersByAnnSetId;
							}
						}

					}
					if (annSetWithUnresolvedSourceAnnotation.isEmpty()) {
						completedBlocksByIndex.add(i);
					}
				}
			}
		}

		public void init(ArrayList<ConsolidationBlock> consolidationBlocks, AnnotatedTextHandler consolidatedDoc) {

			this.consolidationBlocks = consolidationBlocks;
			resetBlockIndex();
			this.consolidatedDoc = consolidatedDoc;

			computeAlreadyCompletedBlocks();
			adjustCurrentBlockIndex();
		}

		private void addReviewedDoc(AnnotatedTextHandler reviewedDoc, int userId, int reviewedTaskId) {
			AnnotationSetImpl annSet = reviewedDoc.getAnnotationSetIdForUserTask(userId, reviewedTaskId);
			reviewedDocByAnnSetId.put(annSet.getId(), reviewedDoc);
		}

		public void resetBlockIndex() {
			internalBlockIndex = consolidationBlocks.isEmpty() ? - 1 : 0;
		}

		private Integer getDisplayBlockIndex() {
			Integer result = null;
			if (consolidationBlocks != null) {
				int displayed = 0;
				int i = -1;
				if (consolidationBlocks.isEmpty()) {
					result = displayed + 1;
				} else {

					while ((++i < consolidationBlocks.size())) {
						if (completedBlocksByIndex.contains(i)) {
							continue;
						}
						displayed++;
						if (i >= internalBlockIndex) {
							result = displayed;
							break;
						}
					}
				}
			}
			return result;
		}

		private void adjustCurrentBlockIndex() {
			if (completedBlocksByIndex.contains(internalBlockIndex)) {
				if (hasNextBlock()) {
					nextBlockIndex();
				} else if (hasPrevBlock()) {
					prevBlockIndex();
				} else {
					internalBlockIndex = -1;
				}
			}
		}

		public int getNbBlocks() {
			return consolidationBlocks != null ? consolidationBlocks.size() - completedBlocksByIndex.size() : 0;
		}

		public Integer findNextBlockIndex() {
			Integer result = null;
			if (consolidationBlocks != null) {
				int nextBlockIndex = internalBlockIndex;
				while ((++nextBlockIndex < consolidationBlocks.size())) {
					if (!completedBlocksByIndex.contains(nextBlockIndex)) {
						result = nextBlockIndex;
						break;
					}
				}
			}
			return result;
		}

		public Integer findPrevBlockIndex() {
			Integer result = null;
			if (consolidationBlocks != null) {
				int prevBlockIndex = internalBlockIndex;
				while ((--prevBlockIndex >= 0)) {
					if (!completedBlocksByIndex.contains(prevBlockIndex)) {
						result = prevBlockIndex;
						break;
					}
				}
			}
			return result;
		}

		public boolean hasNextBlock() {
			return findNextBlockIndex() != null;
		}

		public boolean hasPrevBlock() {
			return findPrevBlockIndex() != null;
		}

		public void nextBlockIndex() {
			Integer nextBlockIndex = findNextBlockIndex();
			if (nextBlockIndex != null) {
				internalBlockIndex = nextBlockIndex;
				processedBlocks.clear();
			}
		}

		public void prevBlockIndex() {
			Integer prevBlockIndex = findPrevBlockIndex();
			if (prevBlockIndex != null) {
				internalBlockIndex = prevBlockIndex;
				processedBlocks.clear();
			}
		}

		private ConsolidationProcessingBlock createBlockAtCurrentIndex(int annotationSetId) {
			AnnotatedTextHandler reviewedDoc = reviewedDocByAnnSetId.get(annotationSetId);
			if (consolidationBlocks != null && !consolidationBlocks.isEmpty() && reviewedDoc != null && internalBlockIndex >= 0) {

				//a block contains one single kind of Annotation
				AnnotationKind kind = null;
				ConsolidationBlock block = consolidationBlocks.get(internalBlockIndex);
				blocksLoop:
				for (List<? extends AnnotationReference> members : block.getMembers()) {
					for (AnnotationReference aRef : members) {
						if (aRef.getAnnotationSetId() != annotationSetId) {
							continue blocksLoop;
						}
						Annotation sampleAnnotation = reviewedDoc.getAnnotation(aRef.getAnnotationId());
						kind = sampleAnnotation.getAnnotationKind();
						break blocksLoop;
					}
				}

				if (kind != null) {
					ConsolidationProcessingBlock newBlock = new ConsolidationProcessingBlock(annotationSetId, block, kind, reviewingAnnSet);
					processedBlocks.add(newBlock);
					return newBlock;
				} else {
					return null;
				}
			} else {
				return null;
			}
		}

		private boolean canCurrentBlockBeResolved() {
			boolean result = false;
			for (ConsolidationProcessingBlock block : processedBlocks) {
				if (block.hasSomeResolvableAnnotation()) {
					result = true;;
				}
			}
			return result;
		}

		private boolean isCurrentBlockWithoutConflict() {
			return consolidationBlocks.get(internalBlockIndex).isWithoutConflict();
		}

		private String resolveCurrentBlock() {
			String result = null;
			if (!processedBlocks.isEmpty()) {

				int reusableAnnoationNb = 0;
				List<AnnotationBackReference> toBeMerged = new ArrayList<AnnotationBackReference>();
				List<AnnotationBackReference> rejected = new ArrayList<AnnotationBackReference>();
				for (ConsolidationProcessingBlock block : processedBlocks) {
					for (AnnotationBackReference backRef : block.getBackReferences()) {
						if (ConsolidationStatus.POSTPONED.equals(backRef.getConsolidationStatus())) {
							reusableAnnoationNb++;
						} else if (ConsolidationStatus.REJECTED.equals(backRef.getConsolidationStatus())) {
							rejected.add(backRef);
						} else {
							if (ConsolidationStatus.SPLIT.equals(backRef.getConsolidationStatus())) {
								reusableAnnoationNb++;
							}
							toBeMerged.add(backRef);
						}
					}
				}

				for (AnnotationBackReference b : toBeMerged) {
					GWT.log("Fusion : " + b.getAnnotationId() + " " + b.getConsolidationStatus().toString());
				}

				if (toBeMerged.isEmpty()) {
					if (rejected.isEmpty()) {
						Window.alert("At least one annotation must be Resolved, Split or Rejected to proceed");
					} else {
						//Add the rejected to the already unresolved 
						AnnotationSetImpl usersAnnotationSet = consolidatedDoc.getEditableUsersAnnotationSet();
						List<AnnotationBackReference> unresolved = usersAnnotationSet.getUnmatchedSourceAnnotations() != null ? usersAnnotationSet.getUnmatchedSourceAnnotations().getAnnotationBackReferences() : new ArrayList<AnnotationBackReference>();
						unresolved.addAll(rejected);
						new AnnotationSetReplaceUnmatchedSourceEdit(consolidatedDoc, usersAnnotationSet, unresolved).redo();
					}
				} else {


					//use the first annotation to be merged as template
					AnnotationBackReference templateBackRef = toBeMerged.get(0);
					AnnotatedTextHandler reviewedDoc = reviewedDocByAnnSetId.get(templateBackRef.getAnnotationSetId());
					Annotation templateAnnotation = reviewedDoc.getAnnotation(templateBackRef.getAnnotationId());
					String newAnnotationType = templateAnnotation.getAnnotationType();
					//blocks contains only one Kind of Annotation
					AnnotationKind kind = templateAnnotation.getAnnotationKind();

					// the new annotation will contain back references to RESOLVED, SPLIT and REJECTED annotations
					List<AnnotationBackReference> backRefs = new ArrayList<AnnotationBackReference>();
					backRefs.addAll(toBeMerged);
					backRefs.addAll(rejected);

					String newAnnotationId = consolidatedDoc.getNewAnnotationId();
					switch (kind) {
						case TEXT:

							PropertiesImpl mergedProps = PropertiesImpl.create();
							List<Fragment> mergedAnnotationFragments = new ArrayList<Fragment>();
							for (AnnotationBackReference t : toBeMerged) {
								AnnotatedTextHandler rewDoc = reviewedDocByAnnSetId.get(t.getAnnotationSetId());
								Annotation annotation = rewDoc.getAnnotation(t.getAnnotationId());

								mergedAnnotationFragments.addAll(annotation.getTextBinding().getFragments());
								mergedProps.mergeWith(annotation.getProperties());
							}
							List<Fragment> newAnnotationFragments = TextBindingImpl.mergeOverlappingFragments(mergedAnnotationFragments);

							//FIXME merge properties

							//store the new annotation
							TextAnnotationCreationEdit annCreate = new TextAnnotationCreationEdit(consolidatedDoc, newAnnotationId, newAnnotationType, newAnnotationFragments, mergedProps, backRefs);
							annCreate.redo();
							result = newAnnotationId;

							break;
						case GROUP:
							//FIXME implements

							break;
						case RELATION:
							//FIXME implements

							break;
						default:
					}

					processedBlocks.clear();
				}
			}
			return result;
		}
	}

	public static class BlockConsoStatusDisplayer extends ConsolidationStatusDisplayer {

		private ConsolidationProcessingBlock currentBlock;

		@Override
		public boolean processAnnotation(SpecifiedAnnotation annotation, String color, List<String> markerIds, boolean veiled) {
			if (currentBlock != null) {

				ConsolidationStatus status = currentBlock.getAnnotationConsolidationStatus(annotation);
				if (status != null) {
					super.processAnnotation(annotation, color, markerIds, veiled, status);
				}
			}
			return false;
		}

		@Override
		public boolean processAnnotation(SpecifiedAnnotation annotation, String color, boolean veiled) {
			if (currentBlock != null) {
				ConsolidationStatus status = currentBlock.getAnnotationConsolidationStatus(annotation);
				if (status != null) {
					super.processAnnotation(annotation, color, veiled, status);
				}
			}
			return false;
		}

		private void setProcessedBlock(ConsolidationProcessingBlock block) {
			currentBlock = block;
		}

		@Override
		public void onConsolidationStatusChanged(String annotationId, ConsolidationStatus status) {
			if (currentBlock != null) {
				currentBlock.updateAnnotationConsolidationStatus(annotationId, status);
			}
		}
	}
	private static AnnSetCompareUiBinder uiBinder = GWT.create(AnnSetCompareUiBinder.class);

	interface AnnSetCompareUiBinder extends UiBinder<DockLayoutPanel, AnnSetCompare> {
	}
	//
	private static final StaneClientBaseGinInjector injector = GWT.create(StaneClientBaseGinInjector.class);
	private static final Logger log = Logger.getLogger(AnnSetCompare.class.getName());
	//
	@UiField
	SplitLayoutPanel propsSplitPanel;
	@UiField
	LayoutPanel propsLayoutPanel;
	@UiField
	RequiresResizeSpy propsResizeSpy;
	@UiField
	AnnotationDetailsUi detailsUI1;
	@UiField
	AnnotationDetailsUi detailsUI2;
	@UiField
	AnnotationDetailsUi detailsUIconso;
	@UiField
	SplitLayoutPanel docsSplitPanel;
	@UiField
	LayoutPanel docsLayoutPanel;
	@UiField
	RequiresResizeSpy docsResizeSpy;
	@UiField
	DocumentUi docUI1;
	@UiField
	DocumentUi docUI2;
	@UiField
	DocumentUi docUIconso;
	@UiField
	PushButton prevBlock;
	@UiField
	PushButton nextBlock;
	@UiField
	Label blockNum;
	@UiField
	PushButton merge;
	@UiField
	PushButton autoMerge;
	@UiField
	PushButton clearUnmatched;
	@UiField
	PushButton setUnmatched;
	@UiField
	PushButton save;
	@UiField
	PushButton publish;
	//
	private BasicTaskReviewParams params;
	private AnnotatedTextHandler hnd1;
	private AnnotatedTextHandler hnd2;
	private int annSetId1;
	private int annSetId2;
	private AnnotatedTextHandler hndConso;
	private BlockConsoStatusDisplayer consoStatusDisplayer1 = null;
	private BlockConsoStatusDisplayer consoStatusDisplayer2 = null;
	private ConsolidationHandler consoHandler = new ConsolidationHandler();
	private boolean modified = false;
	private String waterMark;
	private boolean singleAnnotationSet;

	public AnnSetCompare() {
		initWidget(uiBinder.createAndBindUi(this));
		detailsUI1.setReadOnly(true);
		detailsUI2.setReadOnly(true);



		docsResizeSpy.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				propsSplitPanel.setWidgetSize(propsLayoutPanel, event.getHeight());
			}
		});

		propsResizeSpy.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				docsSplitPanel.setWidgetSize(docsLayoutPanel, event.getHeight());
			}
		});

		HandlerRegistration shreg1 = docUI1.addScrollHandler(new ScrollHandler() {
			@Override
			public void onScroll(ScrollEvent event) {
				docUI2.setVerticalScrollPosition(docUI1.getVerticalScrollPosition());
			}
		});

		HandlerRegistration shreg2 = docUI2.addScrollHandler(new ScrollHandler() {
			@Override
			public void onScroll(ScrollEvent event) {
				docUI1.setVerticalScrollPosition(docUI2.getVerticalScrollPosition());
			}
		});

		//Add parametrized global styleSheet
		Element cssElement = GlobalStyles.getInlinedStyleElement();
		cssElement.setId("aae_GlobalDynamicStyles.Block");
		Element oldCssElement = Document.get().getElementById(cssElement.getId());
		if (oldCssElement != null) {
			oldCssElement.removeFromParent();
		}
		RootLayoutPanel.get().getElement().insertFirst(cssElement);

	}

	@Override
	public void proceed() {

		consoHandler.reset();
		denyModification(true);

		Scheduler.get().scheduleDeferred(new Command() {
			@Override
			public void execute() {

				final int user_id1 = params.getReviewedUserIds().get(0);
				final Integer user_id2 = (params.getReviewedUserIds().size() > 1) ? params.getReviewedUserIds().get(1) : null;
				final int reviewedTaskId = params.getReviewedTaskId();

				singleAnnotationSet = (user_id2 == null);

				//Retrieve reviewed documents 1 and 2
				injector.getCoreDataProvider().getAnnotatedDocumentForReview(user_id1, params.getCampaignId(), params.getDocumentId(), reviewedTaskId, new DetailedAsyncResponseHandler<AnnotatedTextImpl>() {
					@Override
					public void onFailure(Response response) {
						int statusCode = response.getStatusCode();
						String responseText = response.getText();
						Window.alert("Error while retrieving Document to be reviewed : " + statusCode + " - " + responseText + "\n"
								+ "(for user=" + user_id1 + ")");
						hnd1 = null;
						docUI1.setDocument((AnnotatedTextHandler) null, new DocumentView.Options(true, true, true, true, false, true, null, null));
					}

					@Override
					public void onSuccess(AnnotatedTextImpl result) {
						hnd1 = AnnotatedTextHandler.createHandler(user_id1, params.getCampaignId(), result);
						docUI1.setDocument(hnd1, new DocumentView.Options(true, true, true, false, false, true, null, null));
						AnnotationSetImpl annSet = hnd1.getAnnotationSetIdForUserTask(user_id1, reviewedTaskId);
						docUI1.setTitleText(annSet.getDescription());
						consoHandler.addReviewedDoc(hnd1, user_id1, reviewedTaskId);
					}
				});

				if (!singleAnnotationSet) {
					injector.getCoreDataProvider().getAnnotatedDocumentForReview(user_id2, params.getCampaignId(), params.getDocumentId(), reviewedTaskId, new DetailedAsyncResponseHandler<AnnotatedTextImpl>() {
						@Override
						public void onFailure(Response response) {
							int statusCode = response.getStatusCode();
							String responseText = response.getText();
							Window.alert("Error while retrieving Document to be reviewed : " + statusCode + " - " + responseText + "\n"
									+ "(for user=" + user_id1 + ")");
							hnd2 = null;
							docUI2.setDocument((AnnotatedTextHandler) null, new DocumentView.Options(true, true, true, true, false, true, null, null));
						}

						@Override
						public void onSuccess(AnnotatedTextImpl result) {
							hnd2 = AnnotatedTextHandler.createHandler(user_id2, params.getCampaignId(), result);
							docUI2.setDocument(hnd2, new DocumentView.Options(true, true, true, false, false, true, null, null));
							AnnotationSetImpl annSet = hnd2.getAnnotationSetIdForUserTask(user_id2, reviewedTaskId);
							docUI2.setTitleText(annSet.getDescription());
							consoHandler.addReviewedDoc(hnd2, user_id2, reviewedTaskId);
						}
					});
				} else {
					hnd2 = null;
					docUI2.setDocument((AnnotatedTextHandler) null, new DocumentView.Options(true, true, true, true, false, true, null, null));
				}


				injector.getCoreDataProvider().getRequestManager().addMilestone(new Command() {
					@Override
					public void execute() {

						if ((hnd1 == null) || (!singleAnnotationSet && hnd2 == null)) {
							Window.alert("One of the reviewed AnnotationSet could not be loaded!");
						} else {


							//start review task
							injector.getCoreDataProvider().getAnnotatedDocument(params.getUserId(), params.getCampaignId(), params.getDocumentId(), params.getTaskId(), new AsyncCallback<AnnotatedTextImpl>() {
								@Override
								public void onFailure(Throwable caught) {
									Window.alert("Document " + params.getUserId() + " was not found in Campaign " + params.getCampaignId() + " for Task " + params.getTaskId() + " !");
									GWT.log(caught.getMessage());
									docUI1.setDocument(hnd1, new DocumentView.Options(true, true, true, true, false, true, null, null));
								}

								@Override
								public void onSuccess(AnnotatedTextImpl result) {
									hndConso = AnnotatedTextHandler.createHandler(params.getUserId(), params.getCampaignId(), result);

									annSetId1 = hnd1.getAnnotationSetIdForUserTask(user_id1, reviewedTaskId).getId();
									if (!singleAnnotationSet) {
										annSetId2 = hnd2.getAnnotationSetIdForUserTask(user_id2, reviewedTaskId).getId();
									}

									docUIconso.setDocument(hndConso, new DocumentView.Options(false, false, false, false, false, true, null, null));
									resetWaterMark();

									detailsUIconso.setRegisteredAnnotatedText(hndConso);

									HashSet<Integer> reviewedAnnSetIds = new HashSet<Integer>();
									reviewedAnnSetIds.add(annSetId1);
									if (!singleAnnotationSet) {
										reviewedAnnSetIds.add(annSetId2);
									}

									injector.getCoreDataProvider().getConsolidationBlocks(params.getCampaignId(), reviewedAnnSetIds, new AsyncCallback<CDWXS_ConsoBlocks_ResponseImpl>() {
										@Override
										public void onFailure(Throwable caught) {
											Window.alert("Unable to diff AnnotationSets !");
											GWT.log(caught.getMessage());
										}

										@Override
										public void onSuccess(CDWXS_ConsoBlocks_ResponseImpl result) {

											ArrayList<ConsolidationBlock> consoBlocks = new ArrayList<ConsolidationBlock>();
											consoBlocks.addAll(new JsArrayDecorator<ConsolidationBlockImpl>(result.getBlocks()));

											detailsUI1.setRegisteredAnnotatedText(hnd1);
											consoStatusDisplayer1 = new BlockConsoStatusDisplayer();
											docUI1.addAnnotationDisplayer(consoStatusDisplayer1);
											docUI1.addOverlayEventsHandler(consoStatusDisplayer1);

											if (!singleAnnotationSet) {
												detailsUI2.setRegisteredAnnotatedText(hnd2);
												consoStatusDisplayer2 = new BlockConsoStatusDisplayer();
												docUI2.addAnnotationDisplayer(consoStatusDisplayer2);
												docUI2.addOverlayEventsHandler(consoStatusDisplayer2);
											}

											initConsolidation(consoBlocks);

										}
									});

								}
							});
						}
					}
				});

			}
		});

	}

	private void initConsolidation(ArrayList<ConsolidationBlock> consoBlocks) {
		consoHandler.init(consoBlocks, hndConso);
		refreshBlockBrowsingButtons();
		displayCurrentBlock();
	}

	private void displayCurrentBlock() {
		ConsolidationProcessingBlock block1 = consoHandler.createBlockAtCurrentIndex(annSetId1);
		consoStatusDisplayer1.setProcessedBlock(block1);
		docUI1.refreshSecondaryDisplayer(consoStatusDisplayer1);

		ConsolidationProcessingBlock block2 = null;
		if (!singleAnnotationSet) {
			block2 = consoHandler.createBlockAtCurrentIndex(annSetId2);
			consoStatusDisplayer2.setProcessedBlock(block2);
			docUI2.refreshSecondaryDisplayer(consoStatusDisplayer2);
		}

		if (block1 != null) {
			List<AnnotationBackReference> backRef = block1.getBackReferences();
			if (backRef != null && !backRef.isEmpty()) {
				String annotationId = backRef.get(0).getAnnotationId();
				docUI1.setScrollPositionAtAnnotation(annotationId);
			}
		} else if (!singleAnnotationSet && (block2 != null)) {
			List<AnnotationBackReference> backRef = backRef = block2.getBackReferences();
			if (backRef != null && !backRef.isEmpty()) {
				String annotationId = backRef.get(0).getAnnotationId();
				docUI2.setScrollPositionAtAnnotation(annotationId);
			}
		}
	}

	private void refreshBlockBrowsingButtons() {
		Integer currentIndex = consoHandler.getDisplayBlockIndex();
		if (currentIndex != null) {
			blockNum.setText(currentIndex + "/" + (consoHandler.getNbBlocks()));
		} else {
			blockNum.setText("");
		}

		boolean someMoreBlocks = consoHandler.getNbBlocks() > 0;
		prevBlock.setEnabled(consoHandler.hasPrevBlock());
		nextBlock.setEnabled(consoHandler.hasNextBlock());
		merge.setEnabled(someMoreBlocks);
		autoMerge.setEnabled(someMoreBlocks);

		boolean someUnmatched = false;
		if (hndConso != null) {
			AnnotationSetImpl usersAnnotationSet = hndConso.getEditableUsersAnnotationSet();
			SourceAnnotationsImpl unmatched = usersAnnotationSet.getUnmatchedSourceAnnotations();
			someUnmatched = unmatched != null && unmatched.getAnnotationBackReferences() != null && !unmatched.getAnnotationBackReferences().isEmpty();
		}
		clearUnmatched.setEnabled(someUnmatched);
		setUnmatched.setEnabled(!someUnmatched && someMoreBlocks);

		save.setEnabled(modified);
		publish.setEnabled(true);
	}

	private void denyModification(boolean deny) {
		prevBlock.setEnabled(!deny);
		nextBlock.setEnabled(!deny);
		merge.setEnabled(!deny);
		autoMerge.setEnabled(!deny);
		clearUnmatched.setEnabled(!deny);
		setUnmatched.setEnabled(!deny);
		save.setEnabled(!deny);
		publish.setEnabled(!deny);
	}

	private void resetWaterMark() {
		waterMark = docUIconso.getUndoManager() != null ? docUIconso.getUndoManager().getWaterMark() : "";
	}
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	private void recomputeCompletedBlocks() {
		consoHandler.computeAlreadyCompletedBlocks();
		consoHandler.adjustCurrentBlockIndex();
	}

	@Override
	public void onEditHappened(EditHappenedEvent event) {
		if (event.getEdit() instanceof AnnotationEdit) {
			AnnotationEdit edit = (AnnotationEdit) event.getEdit();
			if (edit.getAnnotatedTextHandler().getAnnotatedText().equals(hndConso.getAnnotatedText())) {

				//deferred to leave time to the Undo manager to handle the current edit
				Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
					@Override
					public void execute() {
						modified = (!waterMark.equals(docUIconso.getUndoManager().getWaterMark()));
						save.setEnabled(modified);
					}
				});

				recomputeCompletedBlocks();
				refreshBlockBrowsingButtons();
				displayCurrentBlock();
			}
		}
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	@UiHandler("prevBlock")
	void onPrevBlockClicked(ClickEvent event) {
		consoHandler.prevBlockIndex();
		refreshBlockBrowsingButtons();
		displayCurrentBlock();
	}

	@UiHandler("nextBlock")
	void onNextBlockClicked(ClickEvent event) {
		consoHandler.nextBlockIndex();
		refreshBlockBrowsingButtons();
		displayCurrentBlock();
	}

	@UiHandler("merge")
	void onMergeClicked(ClickEvent event) {
		String annotationId = null;
		denyModification(true);
		try {
			if (consoHandler.canCurrentBlockBeResolved()) {
				annotationId = consoHandler.resolveCurrentBlock();
			} else {
				Window.alert("nothing to merge!");
			}
		} finally {
			denyModification(false);
		}
		if (annotationId != null) {
			docUIconso.setScrollPositionAtAnnotation(annotationId);
		}
		refreshBlockBrowsingButtons();
		displayCurrentBlock();
	}

	@UiHandler("autoMerge")
	void onAutoMergeClicked(ClickEvent event) {

		//start from first block...
		consoHandler.resetBlockIndex();
		refreshBlockBrowsingButtons();
		onNextBlockClicked(null);
		onPrevBlockClicked(null);
		denyModification(true);

		Scheduler.get().scheduleIncremental(new Scheduler.RepeatingCommand() {
			int nbProcessed = 0;

			@Override
			public boolean execute() {
				boolean playAgain = false;
				denyModification(true);
				String annotationId = null;
				if (consoHandler.isCurrentBlockWithoutConflict()) {
					annotationId = consoHandler.resolveCurrentBlock();
					playAgain = true;
				} else {
					if (consoHandler.findNextBlockIndex() != null) {
						onNextBlockClicked(null);
						playAgain = true;
					} else {
						//all block without conflict have been processed
						denyModification(false);
						if (nbProcessed > 0) {
							Window.alert(nbProcessed + " block(s) automatically processed");
						} else {
							Window.alert("No block can be automatically processed");
						}
					}
				}
				if (annotationId != null) {
					nbProcessed++;

					docUIconso.setScrollPositionAtAnnotation(annotationId);
					refreshBlockBrowsingButtons();
					displayCurrentBlock();
				}
				return playAgain;
			}
		});
	}

	@UiHandler("clearUnmatched")
	void onClearUnmatchedClicked(ClickEvent event) {
		denyModification(true);
		try {
			AnnotationSetImpl usersAnnotationSet = hndConso.getEditableUsersAnnotationSet();
			SourceAnnotationsImpl unmatched = usersAnnotationSet.getUnmatchedSourceAnnotations();
			if (unmatched.getAnnotationBackReferences() != null && !unmatched.getAnnotationBackReferences().isEmpty()) {
				new AnnotationSetReplaceUnmatchedSourceEdit(hndConso, usersAnnotationSet, null).redo();
			}
		} finally {
			denyModification(false);
		}
	}

	@UiHandler("setUnmatched")
	void onSetUnmatchedClicked(ClickEvent event) {
		denyModification(true);
		try {
			List<AnnotationBackReference> unresolved = consoHandler.getUnresolvedAnnotation();
			AnnotationSetImpl usersAnnotationSet = hndConso.getEditableUsersAnnotationSet();
			new AnnotationSetReplaceUnmatchedSourceEdit(hndConso, usersAnnotationSet, unresolved).redo();
		} finally {
			denyModification(false);
		}
	}

	@UiHandler("publish")
	void onPublishClicked(ClickEvent event) {
		Window.alert("Not implemented yet!");
	}

	@UiHandler("save")
	void onSaveReview(ClickEvent event) {
		AnnotationSetImpl usersAnnotationSet = hndConso.getEditableUsersAnnotationSet();

		DetailedAsyncResponseHandler<JavaScriptObject> callback = new DetailedAsyncResponseHandler<JavaScriptObject>() {
			@Override
			public void onFailure(Response response) {
				int statusCode = response.getStatusCode();
				String responseText = response.getText();
				if (statusCode == 422) {
					Window.alert("Could not save review\n" + responseText);
				} else {
					log.severe("Problem while saving AnnotationSet!");
					Window.alert("Could not save review\n" + responseText);
					//FIXME :escape message
					injector.getMainEventBus().fireEvent(new InformationReleasedEvent("<span style='color:red;'>Problem while saving AnnotationSet!</span>&nbsp;<span>" + responseText + "</span>"));
				}
				denyModification(false);
				refreshBlockBrowsingButtons();
			}

			@Override
			public void onSuccess(JavaScriptObject result) {
				log.info("AnnotationSet Saved!");
				injector.getMainEventBus().fireEvent(new InformationReleasedEvent("Save performed!"));

				modified = false;
				resetWaterMark();
				denyModification(false);
				refreshBlockBrowsingButtons();
			}
		};

		denyModification(true);
		injector.getCoreDataProvider().saveAnnotationSet(params.getUserId(), params.getCampaignId(), params.getDocumentId(), usersAnnotationSet, callback);

	}

	private void reset() {
		this.params = null;

		consoHandler.reset();
		docUI1.setDocument((AnnotatedTextHandler) null, new DocumentView.Options(true, true, true, true, false, true, null, null));
		hnd1 = null;

		docUI2.setDocument((AnnotatedTextHandler) null, new DocumentView.Options(true, true, true, true, false, true, null, null));
		hnd2 = null;

		docUIconso.setDocument((AnnotatedTextHandler) null, new DocumentView.Options(true, true, true, true, false, true, null, null));
		hndConso = null;

		denyModification(true);
	}
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	@Override
	public BasicTaskReviewParams getParams() {
		return params;
	}

	@Override
	public void setParams(TaskReviewParams params) {
		reset();
		this.params = BasicTaskReviewParams.createFromToken(params.createToken());
	}

	@Override
	public void setPresenter(Presenter presenter) {
	}

	@Override
	public boolean canCloseView() {
		return !modified;
	}
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	@Override
	protected void onAttach() {
		super.onAttach();
		EventBus eventBus = injector.getMainEventBus();
		EditHappenedEvent.register(eventBus, this);
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		EditHappenedEvent.unregister(this);
		reset();
	}
}
