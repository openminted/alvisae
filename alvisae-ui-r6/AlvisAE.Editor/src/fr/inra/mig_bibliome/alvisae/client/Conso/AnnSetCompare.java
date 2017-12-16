/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012-2014.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Conso;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.http.client.Response;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
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
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import fr.inra.mig_bibliome.alvisae.client.AlvisaeCore;
import fr.inra.mig_bibliome.alvisae.client.Annotation.AnnotationDetailsUi;
import fr.inra.mig_bibliome.alvisae.client.Annotation.AnnotationTable;
import fr.inra.mig_bibliome.alvisae.client.Config.ApplicationOptions.PersistedOptionHandler;
import fr.inra.mig_bibliome.alvisae.client.Config.GlobalStyles;
import fr.inra.mig_bibliome.alvisae.client.Config.History.BasicTaskReviewParams;
import fr.inra.mig_bibliome.alvisae.client.Config.History.PlaceParams.TaskReviewParams;
import fr.inra.mig_bibliome.alvisae.client.Config.ShortCutToActionTypeMapper;
import fr.inra.mig_bibliome.alvisae.client.Config.StaneClientBaseGinInjector;
import fr.inra.mig_bibliome.alvisae.client.Document.Blinker;
import fr.inra.mig_bibliome.alvisae.client.Document.ConsolidationStatusDisplayer;
import fr.inra.mig_bibliome.alvisae.client.Document.DocumentUi;
import fr.inra.mig_bibliome.alvisae.client.Document.DocumentView;
import fr.inra.mig_bibliome.alvisae.client.Document.RequiresResizeSpy;
import fr.inra.mig_bibliome.alvisae.client.Edit.AnnotationCreationEdit;
import fr.inra.mig_bibliome.alvisae.client.Edit.AnnotationDeletionEdit;
import fr.inra.mig_bibliome.alvisae.client.Edit.AnnotationEdit;
import fr.inra.mig_bibliome.alvisae.client.Edit.AnnotationGroupCreationEdit;
import fr.inra.mig_bibliome.alvisae.client.Edit.AnnotationRelationCreationEdit;
import fr.inra.mig_bibliome.alvisae.client.Edit.AnnotationSetReplaceUnmatchedSourceEdit;
import fr.inra.mig_bibliome.alvisae.client.Edit.TextAnnotationCreationEdit;
import fr.inra.mig_bibliome.alvisae.client.Events.AnnotationSelectionChangedEventHandler;
import fr.inra.mig_bibliome.alvisae.client.Events.EditHappenedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.EditHappenedEventHandler;
import fr.inra.mig_bibliome.alvisae.client.Events.GenericAnnotationSelectionChangedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.GenericAnnotationSelectionEmptiedEvent;
import fr.inra.mig_bibliome.alvisae.client.Events.InformationReleasedEvent;
import fr.inra.mig_bibliome.alvisae.client.data.Retrieve.DetailedAsyncResponseHandler;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotationBackReferenceImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotationImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotationSetImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.CDWXS_ConsoBlocks_ResponseImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.JsArrayDecorator;
import fr.inra.mig_bibliome.alvisae.client.data3.PropertiesImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.SetSafeAnnotationReference;
import fr.inra.mig_bibliome.alvisae.client.data3.SourceAnnotationsImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.TextBindingImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.validation.ConsolidableAnnotationImpl;
import fr.inra.mig_bibliome.alvisae.client.data3.validation.ConsolidationBlockImpl;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationBackReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.ConsolidationStatus;
import fr.inra.mig_bibliome.alvisae.shared.data3.Fragment;
import fr.inra.mig_bibliome.alvisae.shared.data3.SpecifiedAnnotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.ConsolidableAnnotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.ConsolidationBlock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * UI used to perform the consolidation of (up to) 2 annotations set into a
 * reviewed AnnotationSet
 *
 * @author fpapazian
 */
public class AnnSetCompare extends Composite implements AnnotationSetConsoView, EditHappenedEventHandler, AnnotationSelectionChangedEventHandler {

    public static class ConsolidationHandler {

        //Part of a ConsolidationBlock related to a single AnnotationSet
        public static class ConsolidationProcessingSubBlock {

            private final int annotationSetId;
            private final ConsolidationBlock block;
            private final Map<String, ConsolidationStatus> sourceStatus = new HashMap<String, ConsolidationStatus>();
            private final Set<String> blockAnnotationIds = new HashSet<String>();
            private final Map<String, ConsolidationStatus> updatedStatus = new HashMap<String, ConsolidationStatus>();

            private ConsolidationProcessingSubBlock(int annotationSetId, ConsolidationBlock block, AnnotationSetImpl reviewingAnnSet) {
                this.annotationSetId = annotationSetId;
                this.block = block;
                //if a block is without conflict, then the status can be initialized to Resolved so it is easier for the user to adjudicate
                for (AnnotationReference aRef : block.getMembersByAnnotationSet(annotationSetId)) {
                    if (aRef.getAnnotationSetId() == annotationSetId) {
                        String annotationId = aRef.getAnnotationId();
                        blockAnnotationIds.add(annotationId);
                        if (block.isWithoutConflict()) {
                            updatedStatus.put(annotationId, ConsolidationStatus.RESOLVED);
                        }
                    }
                }
                //Retrieve Annotation that must NOT be proposed for merging anymore (from 2 sources)
                //1- already registered Annotation as source Annnotation of merged Annotation
                //
                List<AnnotationImpl> allAnn = new ArrayList<AnnotationImpl>();
                switch (block.getAnnotationKind()) {
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
                Set<String> resolvedSourceAnnIds = getResolvedSourceAnnotationIds(allAnn, annotationSetId);
                //if annotations in block are already registered as source Annnotation in reviewed AnnotationSet,
                // then they must not be proposed for merging
                blockAnnotationIds.removeAll(resolvedSourceAnnIds);
                //2- already registered Annotation as unmatched Annnotation of reviewing AnnotationSet
                Set<String> unmatchedSourceAnnIds = getSourceAnnotationIdForAnnSet(reviewingAnnSet.getUnmatchedSourceAnnotations(), annotationSetId);
                blockAnnotationIds.removeAll(unmatchedSourceAnnIds);
            }

            /**
             * @return true if an adjudicated Annotation can be derived from
             * this block
             */
            public boolean hasSomeResolvableAnnotation() {
                boolean result = false;
                for (ConsolidationStatus status : updatedStatus.values()) {
                    if (!ConsolidationStatus.POSTPONED.equals(status)) {
                        result = true;
                        break;
                    }
                }
                return result;
            }

            public void updateAnnotationConsolidationStatus(String annotationId, ConsolidationStatus newStatus) {
                updatedStatus.put(annotationId, newStatus);
            }

            public AnnotationKind getKind() {
                return block.getAnnotationKind();
            }

            public ConsolidationBlock getConsolidationBlock() {
                return block;
            }

            public ConsolidationStatus getAnnotationConsolidationStatus(SpecifiedAnnotation annotation) {
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

            public List<AnnotationBackReference> getBackReferences() {
                List<AnnotationBackReference> backRefs = new ArrayList<AnnotationBackReference>();
                for (Map.Entry<String, ConsolidationStatus> e : updatedStatus.entrySet()) {
                    String annotationId = e.getKey();
                    if (blockAnnotationIds.contains(annotationId)) {
                        backRefs.add(AnnotationBackReferenceImpl.create(e.getKey(), annotationSetId, e.getValue()));
                    }
                }
                return backRefs;
            }
        }
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

        /**
         *
         * @return Ids of Annotations, belonging to the specified Annotation
         * Set, that are definitely resolved
         */
        private static Set<String> getResolvedSourceAnnotationIds(List<AnnotationImpl> annotations, int annotationSetId) {
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

        /**
         * @returns Ids of Annotations belonging to the specified Annotation Set
         */
        private static Set<String> getSourceAnnotationIdForAnnSet(SourceAnnotationsImpl sourcesAnn, int annotationSetId) {
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
        private List<ConsolidationBlock> firstLevelBlocks = null;
        private List<ConsolidationBlock> consolidationBlocks = null;
        private List<ConsolidableAnnotation> higherOrderAnns = null;
        private int internalBlockIndex;
        private Set<Integer> completedBlocksByIndex = new HashSet<Integer>();
        private List<ConsolidationProcessingSubBlock> currentlyProcessedBlocks = new ArrayList<ConsolidationProcessingSubBlock>();
        private AnnotatedTextHandler consolidatedDoc = null;
        private AnnotationSetImpl reviewingAnnSet;
        private Map<Integer, AnnotatedTextHandler> reviewedDocByAnnSetId = new HashMap<Integer, AnnotatedTextHandler>();
        //for each AnnotationSetId, the map of derived Annotation Id(s) indexed by their source Annotation (only for source Annotation that are referenced as components/arguments)
        private Map<Integer, Map<String, List<String>>> compArgDerivedAnnotations = new HashMap<Integer, Map<String, List<String>>>();
        // Id of Annotations (by AnnotationSets) which are already adjudicated and registered in the AnnotationSet of the review
        private Map<Integer, Set<String>> resolvedAnnotations = new HashMap<Integer, Set<String>>();

        void reset() {
            consolidationBlocks = null;
            internalBlockIndex = - 1;
            currentlyProcessedBlocks.clear();
            reviewedDocByAnnSetId.clear();
            completedBlocksByIndex.clear();
            compArgDerivedAnnotations.clear();
        }

        private Map<Integer, Set<String>> refreshAlreadyResolvedAnnotations() {
            initResolvedAnnotationMaps();

            resolvedAnnotations.clear();
            //retreive unmatched annotations stored in the Review AnnotationSet
            SourceAnnotationsImpl unmatchedAnnotations = reviewingAnnSet.getUnmatchedSourceAnnotations();
            //retreive all Annotations belonging to the Review AnnotationSet
            List<AnnotationImpl> reviewingAnnotations = reviewingAnnSet.getAnnotations();
            for (int reviewedAnnSetId : reviewedDocByAnnSetId.keySet()) {
                Set<String> resolvedSourceAnnIds = new HashSet<String>();
                //extract from the review Annotation BackReferences' the resolved annotations belonging to the current reviewed AnnotationSet
                resolvedSourceAnnIds.addAll(getResolvedSourceAnnotationIds(reviewingAnnotations, reviewedAnnSetId));
                //Amongst the unmatched annotations, keep those belonging to the current reviewed AnnotationSet 
                resolvedSourceAnnIds.addAll(getSourceAnnotationIdForAnnSet(unmatchedAnnotations, reviewedAnnSetId));

                resolvedAnnotations.put(reviewedAnnSetId, resolvedSourceAnnIds);


                //for each resolved Annotations record the derived AnnotationIds 
                Map<String, List<String>> byAnnSetId = compArgDerivedAnnotations.get(reviewedAnnSetId);
                for (AnnotationImpl ann : reviewingAnnotations) {
                    SourceAnnotationsImpl sourcesAnn = ann.getSourceAnnotations();
                    if (sourcesAnn != null) {
                        String derivedId = ann.getId();
                        for (AnnotationBackReference abr : sourcesAnn.getAnnotationBackReferences()) {
                            if (abr.getAnnotationSetId() == reviewedAnnSetId) {
                                String sourceAnnId = abr.getAnnotationId();
                                ConsolidationStatus annotationStatus = abr.getConsolidationStatus();
                                if (ConsolidationStatus.RESOLVED.equals(annotationStatus) || ConsolidationStatus.SPLIT.equals(annotationStatus)) {
                                    if (byAnnSetId.containsKey(sourceAnnId)) {
                                        byAnnSetId.get(sourceAnnId).add(derivedId);
                                    }
                                }
                            }
                        }
                    }
                }


            }
            return resolvedAnnotations;
        }

        private boolean isResolved(AnnotationReference aRef) {
            return isResolved(aRef.getAnnotationSetId(), aRef.getAnnotationId());
        }

        private boolean isResolved(Integer annotationSetId, String annotationId) {
            Set<String> resolvedSourceAnnIds = resolvedAnnotations.get(annotationSetId);
            return (resolvedSourceAnnIds != null) && resolvedSourceAnnIds.contains(annotationId);
        }

        /**
         *
         * @return Annotations which are not yet adjudicated and registered in
         * the AnnotationSet of the review
         */
        public List<AnnotationBackReference> getAllUnresolvedReviewedAnnotationAsRejectList() {
            List<AnnotationBackReference> result = new ArrayList<AnnotationBackReference>();

            //Inspect each block to check and retrieve unresolved members
            for (ConsolidationBlock block : consolidationBlocks) {
                for (List<? extends AnnotationReference> members : block.getMembers()) {
                    for (AnnotationReference aRef : members) {
                        if (!isResolved(aRef)) {
                            int annotationSetId = aRef.getAnnotationSetId();
                            String annotationId = aRef.getAnnotationId();
                            result.add(AnnotationBackReferenceImpl.create(annotationId, annotationSetId, ConsolidationStatus.REJECTED));
                        }
                    }
                }
            }
            return result;
        }

        private void initResolvedAnnotationMaps() {
            compArgDerivedAnnotations.clear();
            for (ConsolidableAnnotation hoAnnotation : higherOrderAnns) {
                Integer annSetId = hoAnnotation.getAnnotationRef().getAnnotationSetId();
                if (!compArgDerivedAnnotations.containsKey(annSetId)) {
                    compArgDerivedAnnotations.put(annSetId, new HashMap<String, List<String>>());
                }
                Map<String, List<String>> byAnnSetId = compArgDerivedAnnotations.get(annSetId);
                for (String compArgId : hoAnnotation.getReferencedAnnotationIds()) {
                    byAnnSetId.put(compArgId, new ArrayList<String>());
                }
            }
        }

        static class SubBlockAlternative {

            final AnnotationReference annRef;
            final String annotationType;
            final List<String> argDerivedIds;

            public SubBlockAlternative(SetSafeAnnotationReference annRef, String annotationType, List<String> argDerivedIds) {
                this.annRef = annRef;
                this.annotationType = annotationType;
                this.argDerivedIds = argDerivedIds;
            }

            public AnnotationReference getAnnRef() {
                return annRef;
            }

            public String getAnnotationType() {
                return annotationType;
            }

            public List<String> getArgDerivedIds() {
                return argDerivedIds;
            }
        }

        private void generateBlockForHigherOrderAnnotations() {

            //1rst level key: group type. 2nd level key: ref of component derived annotation. value: Set of Group ref that should be joined in a block
            Map<String, Map<AnnotationReference, Set<AnnotationReference>>> matchingGroups = new HashMap<String, Map<AnnotationReference, Set<AnnotationReference>>>();
            Map<Set<String>, List<SubBlockAlternative>> matchingRelations = new HashMap<Set<String>, List<SubBlockAlternative>>();

            List<HighOrderConsolidationBlock> highorderBlocks = new ArrayList<HighOrderConsolidationBlock>();
            Map<AnnotationReference, Integer> highOrderAnnEndPos = new HashMap<AnnotationReference, Integer>();



            highOrderAnnotationLoop:
            for (ConsolidableAnnotation hoAnnotation : higherOrderAnns) {
                Integer annSetId = hoAnnotation.getAnnotationRef().getAnnotationSetId();

                if (isResolved(annSetId, hoAnnotation.getAnnotationRef().getAnnotationId())) {
                    //the current Group/Relation annotation has already been resolved

                    //create a dummy completed block for the resolved annoation
                    //Note: no need that completed block structure correspond to the actual one, as long as all resolved higerOrder annotations are part of one of these blocks 
                    highorderBlocks.add(new HighOrderConsolidationBlock(hoAnnotation.getAdjudicationLevel(), hoAnnotation.getAnnotationKind(), new SetSafeAnnotationReference(hoAnnotation.getAnnotationRef())));
                    continue highOrderAnnotationLoop;
                }

                for (String compArgId : hoAnnotation.getReferencedAnnotationIds()) {
                    if (!isResolved(annSetId, compArgId)) {
                        //at least one component/argument is not yet resolved, the current Group/Relation annotation can not be resolved
                        continue highOrderAnnotationLoop;
                    }
                }

                //FIXME: incomplete implementation : does not handle components/arguments which are outside reviewed AnnnotationSet or a Group/Relation
                AnnotatedTextHandler reviewedDoc = reviewedDocByAnnSetId.get(annSetId);
                int endPos = 0;
                for (String compArgId : hoAnnotation.getReferencedAnnotationIds()) {
                    Annotation compArg = reviewedDoc.getAnnotation(compArgId);
                    if (AnnotationKind.TEXT.equals(compArg.getAnnotationKind())) {
                        List<Fragment> frags = compArg.getTextBinding().getSortedFragments();
                        endPos = Math.max(endPos, frags.get(frags.size() - 1).getEnd());
                    }
                }
                highOrderAnnEndPos.put(new SetSafeAnnotationReference(hoAnnotation.getAnnotationRef()), endPos);

                //FIXME : before displaying block of adjudication level L, maybe we should wait for all Annotations over level L-1 to be resolved.
                //
                Map<String, List<String>> byAnnSetId = compArgDerivedAnnotations.get(annSetId);

                if (AnnotationKind.GROUP.equals(hoAnnotation.getAnnotationKind())) {
                    //Rule to join Groups in same block: all Groups, of the same type, sharing at least one component 

                    Annotation hoAnn = reviewedDoc.getAnnotation(hoAnnotation.getAnnotationRef().getAnnotationId());
                    String groupType = hoAnn.getAnnotationType();

                    for (String componentId : hoAnnotation.getReferencedAnnotationIds()) {
                        Map<AnnotationReference, Set<AnnotationReference>> groupsByComponent;
                        if (matchingGroups.containsKey(groupType)) {
                            groupsByComponent = matchingGroups.get(groupType);

                        } else {
                            groupsByComponent = new HashMap<AnnotationReference, Set<AnnotationReference>>();
                            matchingGroups.put(groupType, groupsByComponent);
                        }

                        List<String> derived = byAnnSetId.get(componentId);
                        for (String derivedId : derived) {
                            AnnotationReference compRef = new SetSafeAnnotationReference(derivedId, annSetId);
                            Set<AnnotationReference> groupsForComponent;
                            if (!groupsByComponent.containsKey(compRef)) {
                                groupsForComponent = new HashSet<AnnotationReference>();
                                groupsByComponent.put(compRef, groupsForComponent);
                            } else {
                                groupsForComponent = groupsByComponent.get(compRef);
                            }
                            groupsForComponent.add(new SetSafeAnnotationReference(hoAnnotation.getAnnotationRef()));
                        }
                    }

                } else if (AnnotationKind.RELATION.equals(hoAnnotation.getAnnotationKind())) {
                    //Rule to join Relations in same block: all Relations, whatever their type, that share the same set of arguments, even if they have different roles

                    Annotation hoAnn = reviewedDoc.getAnnotation(hoAnnotation.getAnnotationRef().getAnnotationId());
                    String relType = hoAnn.getAnnotationType();

                    //because any arguments can be derived into several distinct Annotation, every combination must be evaluated to find potential matching Relations
                    List<List<String>> allDerivedByArgument = new ArrayList<List<String>>();
                    int productSize = 1;
                    for (String argumentId : hoAnnotation.getReferencedAnnotationIds()) {
                        List<String> derived = byAnnSetId.get(argumentId);
                        if (!derived.isEmpty()) {
                            allDerivedByArgument.add(derived);
                            productSize *= derived.size();
                        }
                    }

                    List<List<String>> allArgCombinations = new ArrayList<List<String>>();
                    for (int possibleSetIdx = 0; possibleSetIdx < productSize; possibleSetIdx++) {
                        allArgCombinations.add(new ArrayList<String>());
                    }
                    for (List<String> derived : allDerivedByArgument) {
                        int argumentDerivedNum = derived.size();
                        for (int possibleSetIdx = 0; possibleSetIdx < productSize; possibleSetIdx++) {
                            allArgCombinations.get(possibleSetIdx).add(derived.get(possibleSetIdx % argumentDerivedNum));
                        }
                    }

                    //put arguments combination in the relation matching map to find potential block
                    for (List<String> permutation : allArgCombinations) {
                        Set<String> combination = new HashSet<String>(permutation);
                        List<SubBlockAlternative> forCombination;
                        if (!matchingRelations.containsKey(combination)) {
                            forCombination = new ArrayList<SubBlockAlternative>();
                            matchingRelations.put(combination, forCombination);
                        } else {
                            forCombination = matchingRelations.get(combination);
                        }
                        forCombination.add(new SubBlockAlternative(new SetSafeAnnotationReference(hoAnnotation.getAnnotationRef()), relType, permutation));
                    }
                }

            }

            //Generate Group blocks
            for (Map<AnnotationReference, Set<AnnotationReference>> groupsByComponent : matchingGroups.values()) {
                Set<AnnotationReference> alreadyPartOfBlockGroup = new HashSet<AnnotationReference>();

                List<Set<AnnotationReference>> memberSetsBySize = new ArrayList<Set<AnnotationReference>>(groupsByComponent.values());
                Collections.sort(memberSetsBySize, new Comparator<Set<AnnotationReference>>() {
                    @Override
                    public int compare(Set<AnnotationReference> o1, Set<AnnotationReference> o2) {
                        return Integer.valueOf(o2.size()).compareTo(Integer.valueOf(o1.size()));
                    }
                });

                for (Set<AnnotationReference> blockMembers : memberSetsBySize) {
                    HashSet<AnnotationReference> unusedGroups = new HashSet<AnnotationReference>(blockMembers);
                    unusedGroups.removeAll(alreadyPartOfBlockGroup);
                    int endPos = 0;
                    for (AnnotationReference member : unusedGroups) {
                        endPos = Math.max(endPos, highOrderAnnEndPos.get(member));
                    }
                    if (!unusedGroups.isEmpty()) {
                        highorderBlocks.add(new HighOrderConsolidationBlock(2, AnnotationKind.GROUP, endPos, endPos, unusedGroups, false, null));
                        alreadyPartOfBlockGroup.addAll(unusedGroups);
                    }
                }
            }


            //FIXME : one relation should be in one single block

            //Generate Relation blocks
            for (Map.Entry<Set<String>, List<SubBlockAlternative>> e : matchingRelations.entrySet()) {
                Set<AnnotationReference> blockMembers = new HashSet<AnnotationReference>();
                List<SubBlockAlternative> subBlocks = e.getValue();
                Set<String> derivedIds = e.getKey();

                //Rule for non-conflicting Relation block :
                // * a member exists in every all reviewed AnnotationSet
                // * all members share same Annotation Type
                // * all members share same role/arguments derived annotations
                boolean oneRelPerReviewedAnnSet = subBlocks.size() == reviewedDocByAnnSetId.size();
                boolean uniqType = true;
                boolean sameDerivedIds = true;

                int endPos = 0;
                for (SubBlockAlternative sb : subBlocks) {
                    AnnotationReference member = sb.getAnnRef();
                    blockMembers.add(member);

                    endPos = Math.max(endPos, highOrderAnnEndPos.get(member));

                    if (!sb.getAnnotationType().equals(subBlocks.get(0).getAnnotationType())) {
                        uniqType = false;
                    }

                    for (int i = 0; i < sb.getArgDerivedIds().size(); i++) {
                        if (!sb.getArgDerivedIds().get(i).equals(subBlocks.get(0).getArgDerivedIds().get(i))) {
                            sameDerivedIds = false;
                            break;
                        }
                    }

                }

                boolean noConflict = oneRelPerReviewedAnnSet && uniqType && sameDerivedIds;

                highorderBlocks.add(new HighOrderConsolidationBlock(2, AnnotationKind.RELATION, endPos, endPos, blockMembers, noConflict, derivedIds));
            }

            //sort highOrder blocks depending on their components/arguments position
            Collections.sort(highorderBlocks, HighOrderConsolidationBlock.comparator);
            //Block are sorted by kind :  Primary Annotation blocks, then Groups, then Relations 
            List<HighOrderConsolidationBlock> groupCBlocks = new ArrayList<HighOrderConsolidationBlock>();
            List<HighOrderConsolidationBlock> relationCBlocks = new ArrayList<HighOrderConsolidationBlock>();
            for (HighOrderConsolidationBlock hoBlock : highorderBlocks) {
                if (AnnotationKind.GROUP.equals(hoBlock.getAnnotationKind())) {
                    groupCBlocks.add(hoBlock);

                } else {
                    relationCBlocks.add(hoBlock);
                }
            }
            consolidationBlocks.addAll(groupCBlocks);
            consolidationBlocks.addAll(relationCBlocks);
        }

        private void computeAlreadyCompletedBlocks() {
            if (consolidatedDoc != null && firstLevelBlocks != null) {
                consolidationBlocks = new ArrayList<ConsolidationBlock>(firstLevelBlocks);

                completedBlocksByIndex.clear();

                //update data structure containing resolved Annotations
                refreshAlreadyResolvedAnnotations();

                //
                generateBlockForHigherOrderAnnotations();

                //Important note : collections usage is limited with Javascript Overlay Types,
                //since equals() and hashcode() is defined final in their common ancestor JavascriptObject

                //Inspect each block to check if it only contains resolved members
                for (int i = 0; i < consolidationBlocks.size(); i++) {
                    ConsolidationBlock block = consolidationBlocks.get(i);
                    Set<Integer> annSetWithUnresolvedSourceAnnotation = new HashSet<Integer>();
                    membersByAnnSetId:
                    for (List<? extends AnnotationReference> members : block.getMembers()) {
                        for (AnnotationReference aRef : members) {
                            if (!isResolved(aRef)) {
                                int annotationSetId = aRef.getAnnotationSetId();
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

        void refreshBlocks() {
            computeAlreadyCompletedBlocks();
            if (consolidationBlocks.isEmpty()) {
                internalBlockIndex = - 1;
            }
            adjustCurrentBlockIndex();
        }

        public void init(List<ConsolidationBlock> firstLevelBlocks, List<ConsolidableAnnotation> higherOrderAnns, AnnotatedTextHandler consolidatedDoc) {
            this.firstLevelBlocks = firstLevelBlocks;
            this.higherOrderAnns = higherOrderAnns;
            this.consolidatedDoc = consolidatedDoc;
            this.reviewingAnnSet = consolidatedDoc.getEditableUsersAnnotationSet();
            internalBlockIndex = 0;
            refreshBlocks();
        }

        public void addReviewedDoc(AnnotatedTextHandler reviewedDoc, int userId, int reviewedTaskId) {
            AnnotationSetImpl annSet = reviewedDoc.getAnnotationSetIdForUserTask(userId, reviewedTaskId);
            reviewedDocByAnnSetId.put(annSet.getId(), reviewedDoc);
        }

        public AnnotationSetImpl getReviewAnnotationSet() {
            return reviewingAnnSet;
        }

        void resetBlockIndex() {
            internalBlockIndex = consolidationBlocks.isEmpty() ? - 1 : 0;
        }

        Integer getDisplayBlockIndex() {
            Integer result = null;
            if (consolidationBlocks != null) {
                int displayed = 0;
                int i = -1;
                if (consolidationBlocks.isEmpty()) {
                    result = displayed + 1;
                } else {
                    while (++i < consolidationBlocks.size()) {
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
            if (internalBlockIndex >= consolidationBlocks.size()) {
                internalBlockIndex = consolidationBlocks.size() - 1;
            }
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
                boolean overflown = false;
                int nextBlockIndex = internalBlockIndex;
                while (++nextBlockIndex < consolidationBlocks.size() || !overflown) {
                    //circular loop
                    if (nextBlockIndex >= consolidationBlocks.size()) {
                        nextBlockIndex = 0;
                        overflown = true;
                    }
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
                boolean overflown = false;
                int prevBlockIndex = internalBlockIndex;
                while (--prevBlockIndex >= 0 || !overflown) {
                    //circular loop
                    if (prevBlockIndex < 0) {
                        prevBlockIndex = consolidationBlocks.size() - 1;
                        overflown = true;
                    }
                    if (!completedBlocksByIndex.contains(prevBlockIndex)) {
                        result = prevBlockIndex;
                        break;
                    }
                }
            }
            return result;
        }

        public boolean gotoNextBlockForAnnotation(Annotation annotation) {
            AnnotationKind annotationKind = annotation.getAnnotationKind();
            String annotationId = annotation.getId();
            Integer annSetId = null;
            for (Map.Entry<Integer, AnnotatedTextHandler> e : reviewedDocByAnnSetId.entrySet()) {
                if (e.getValue().getAnnotation(annotationId) != null) {
                    annSetId = e.getKey();
                    break;
                }
            }
            Integer newIndex = null;
            if (annSetId != null) {
                blockLoop:
                for (int idx = 0; idx < consolidationBlocks.size(); idx++) {
                    if (completedBlocksByIndex.contains(idx)) {
                        continue;
                    }
                    ConsolidationBlock block = consolidationBlocks.get(idx);
                    if (!block.getAnnotationKind().equals(annotationKind)) {
                        continue;
                    }
                    for (AnnotationReference member : block.getMembersByAnnotationSet(annSetId)) {
                        if (annotationId.equals(member.getAnnotationId())) {
                            newIndex = idx;
                            break blockLoop;
                        }
                    }
                }
                if (newIndex != null) {
                    internalBlockIndex = newIndex;
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
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
                currentlyProcessedBlocks.clear();
            }
        }

        public void prevBlockIndex() {
            Integer prevBlockIndex = findPrevBlockIndex();
            if (prevBlockIndex != null) {
                internalBlockIndex = prevBlockIndex;
                currentlyProcessedBlocks.clear();
            }
        }

        void clearSubBlocks() {
            currentlyProcessedBlocks.clear();
        }

        ConsolidationProcessingSubBlock createSubBlockAtCurrentIndex(int annotationSetId) {
            AnnotatedTextHandler reviewedDoc = reviewedDocByAnnSetId.get(annotationSetId);
            if (consolidationBlocks != null && !consolidationBlocks.isEmpty() && reviewedDoc != null && internalBlockIndex >= 0) {
                ConsolidationBlock block = consolidationBlocks.get(internalBlockIndex);

                //check that the block contains members belonging to the specified Annotation Set
                if (!block.getMembersByAnnotationSet(annotationSetId).isEmpty()) {
                    ConsolidationProcessingSubBlock newBlock = new ConsolidationProcessingSubBlock(annotationSetId, block, reviewingAnnSet);
                    currentlyProcessedBlocks.add(newBlock);
                    return newBlock;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }

        public boolean canCurrentBlockBeResolved() {
            boolean result = false;
            for (ConsolidationProcessingSubBlock block : currentlyProcessedBlocks) {
                if (block.hasSomeResolvableAnnotation()) {
                    result = true;
                }
            }
            return result;
        }

        boolean isCurrentBlockWithoutConflict() {
            return consolidationBlocks.get(internalBlockIndex).isWithoutConflict();
        }

        String resolveCurrentBlock() {
            String result = null;
            if (!currentlyProcessedBlocks.isEmpty()) {
                AnnotationKind kind = null;
                ConsolidationBlock consoblock = null;
                int reusableAnnotationNb = 0;
                List<AnnotationBackReference> toBeMerged = new ArrayList<AnnotationBackReference>();
                List<AnnotationBackReference> rejected = new ArrayList<AnnotationBackReference>();
                //sort out members of the block by the status set by the user
                for (ConsolidationProcessingSubBlock block : currentlyProcessedBlocks) {
                    //note: blocks contains only one Kind of Annotation
                    if (kind == null) {
                        kind = block.getKind();
                        consoblock = block.getConsolidationBlock();
                    }
                    AlvisaeCore.firebugLog("---- " + block.toString());
                    for (AnnotationBackReference backRef : block.getBackReferences()) {
                        AlvisaeCore.firebugLog("InMergedBlock : " + block.annotationSetId + " . " + backRef.getAnnotationId() + " " + backRef.getConsolidationStatus().toString());

                        if (ConsolidationStatus.POSTPONED.equals(backRef.getConsolidationStatus())) {
                            reusableAnnotationNb++;
                        } else if (ConsolidationStatus.REJECTED.equals(backRef.getConsolidationStatus())) {
                            rejected.add(backRef);
                        } else {
                            if (ConsolidationStatus.SPLIT.equals(backRef.getConsolidationStatus())) {
                                reusableAnnotationNb++;
                            }
                            toBeMerged.add(backRef);
                        }
                    }
                }
                AlvisaeCore.firebugLog("---- ");


                if (toBeMerged.isEmpty()) {
                    //Case 1: No derived Annotation will be created as the result of the adjudication
                    if (rejected.isEmpty()) {
                        Window.alert("At least one annotation must be Resolved, Split or Rejected to proceed");
                    } else {
                        //Append the backreference of the rejected to the AnnotationSet's list of unmatched
                        AnnotationSetImpl usersAnnotationSet = getReviewAnnotationSet();
                        List<AnnotationBackReference> unresolved = usersAnnotationSet.getUnmatchedSourceAnnotations() != null ? usersAnnotationSet.getUnmatchedSourceAnnotations().getAnnotationBackReferences() : new ArrayList<AnnotationBackReference>();
                        unresolved.addAll(rejected);
                        new AnnotationSetReplaceUnmatchedSourceEdit(consolidatedDoc, usersAnnotationSet, unresolved).redo();
                    }
                } else {
                    //Case 2: One derived Annotation will be created as the result of the adjudication
                    //use the first annotation to be merged as template
                    AnnotationBackReference templateBackRef = toBeMerged.get(0);
                    AnnotatedTextHandler reviewedDoc = reviewedDocByAnnSetId.get(templateBackRef.getAnnotationSetId());
                    Annotation templateAnnotation = reviewedDoc.getAnnotation(templateBackRef.getAnnotationId());
                    String newAnnotationType = templateAnnotation.getAnnotationType();
                    // the new annotation will contain back references to RESOLVED, SPLIT and REJECTED annotations
                    List<AnnotationBackReference> backRefs = new ArrayList<AnnotationBackReference>();
                    backRefs.addAll(toBeMerged);
                    backRefs.addAll(rejected);

                    PropertiesImpl mergedProps = PropertiesImpl.create();
                    switch (kind) {
                        case TEXT:
                            //the resulting Annotation is the union of of merged annotations fragments
                            List<Fragment> mergedAnnotationFragments = new ArrayList<Fragment>();
                            for (AnnotationBackReference t : toBeMerged) {
                                AnnotatedTextHandler rewDoc = reviewedDocByAnnSetId.get(t.getAnnotationSetId());
                                Annotation annotation = rewDoc.getAnnotation(t.getAnnotationId());
                                mergedAnnotationFragments.addAll(annotation.getTextBinding().getFragments());
                                mergedProps.mergeWith(annotation.getProperties());
                            }
                            List<Fragment> newAnnotationFragments = TextBindingImpl.mergeOverlappingFragments(mergedAnnotationFragments);
                            //store the new annotation
                            String newAnnotationId = consolidatedDoc.getNewAnnotationId();
                            TextAnnotationCreationEdit annCreate = new TextAnnotationCreationEdit(consolidatedDoc, newAnnotationId, newAnnotationType, newAnnotationFragments, mergedProps, backRefs);
                            annCreate.redo();
                            result = newAnnotationId;
                            break;
                        case GROUP:
                            //the resulting Annotation is the union of merged annotations components (merged Annotation are themselves resulting of an adjudication)

                            Set<AnnotationReference> components = new HashSet<AnnotationReference>();
                            for (AnnotationBackReference merged : toBeMerged) {
                                Integer annSetId = merged.getAnnotationSetId();
                                AnnotatedTextHandler rewDoc = reviewedDocByAnnSetId.get(annSetId);
                                Annotation annotation = rewDoc.getAnnotation(merged.getAnnotationId());

                                for (AnnotationReference compRef : annotation.getAnnotationGroup().getComponentRefs()) {
                                    if (compRef.getAnnotationSetId() == null || compRef.getAnnotationSetId() == annSetId) {
                                        // replace components belonging to the reviewed Annotation set by their derived annotation reference 
                                        //in case of several derived annotations, just take the first one
                                        List<String> derivedAnnotationIds = compArgDerivedAnnotations.get(annSetId).get(compRef.getAnnotationId());
                                        if (!derivedAnnotationIds.isEmpty()) {
                                            String derivedId = derivedAnnotationIds.get(0);
                                            components.add(new SetSafeAnnotationReference(derivedId));
                                        } else {
                                            //no derived annotation means that the source annotation was rejected
                                        }

                                    } else {
                                        //no change needed since component does not belong to a reviewed AnnotationSet
                                        components.add(new SetSafeAnnotationReference(compRef));
                                    }

                                }
                                mergedProps.mergeWith(annotation.getProperties());
                            }

                            //store the new Group
                            AnnotationGroupCreationEdit groupCreate = new AnnotationGroupCreationEdit(consolidatedDoc, newAnnotationType, new ArrayList<AnnotationReference>(components), mergedProps, backRefs);
                            groupCreate.redo();
                            result = groupCreate.getCreatedGroup().getId();

                            break;
                        case RELATION:
                            Set<String> selectedDerivedIds = null;
                            if (consoblock instanceof HighOrderConsolidationBlock) {
                                selectedDerivedIds = ((HighOrderConsolidationBlock) consoblock).getDerivedIds();
                            }
                            //Since all merged Relation have same argument list, just use the first relation as template for argument role map, 
                            Integer templateRelAnnSetId = null;
                            Annotation templateRelAnnotation = null;
                            for (AnnotationBackReference merged : toBeMerged) {
                                AnnotatedTextHandler rewDoc = reviewedDocByAnnSetId.get(merged.getAnnotationSetId());
                                Annotation annotation = rewDoc.getAnnotation(merged.getAnnotationId());
                                if (templateRelAnnotation == null) {
                                    templateRelAnnotation = annotation;
                                    templateRelAnnSetId = merged.getAnnotationSetId();
                                }
                                mergedProps.mergeWith(annotation.getProperties());
                            }
                            Map<String, AnnotationReference> argRoleMap = new HashMap<String, AnnotationReference>();
                            for (Map.Entry<String, AnnotationReference> ra : templateRelAnnotation.getRelation().getRolesArguments().entrySet()) {
                                AnnotationReference argRef = ra.getValue();

                                if (argRef.getAnnotationSetId() == null || argRef.getAnnotationSetId() == templateRelAnnSetId) {
                                    // replace arguments belonging to the reviewed Annotation set by their derived annotation reference 
                                    //in case of several derived annotations, just take the first one
                                    List<String> derivedAnnotationIds = compArgDerivedAnnotations.get(templateRelAnnSetId).get(argRef.getAnnotationId());
                                    if (!derivedAnnotationIds.isEmpty()) {
                                        String derivedId = null;
                                        //find the actual derived annotation id to use  
                                        if (selectedDerivedIds != null && !selectedDerivedIds.isEmpty()) {
                                            for (String i : derivedAnnotationIds) {
                                                if (selectedDerivedIds.contains(i)) {
                                                    derivedId = i;
                                                    break;
                                                }
                                            }
                                        } else {
                                            derivedId = derivedAnnotationIds.get(0);
                                        }
                                        argRoleMap.put(ra.getKey(), new SetSafeAnnotationReference(derivedId));
                                    } else {
                                        //no derived annotation means that the source annotation was rejected
                                    }

                                } else {
                                    //no change needed since argument does not belong to a reviewed AnnotationSet
                                    argRoleMap.put(ra.getKey(), new SetSafeAnnotationReference(argRef));
                                }
                            }

                            //store the new relation
                            AnnotationRelationCreationEdit relCreate = new AnnotationRelationCreationEdit(consolidatedDoc, newAnnotationType, argRoleMap, mergedProps, backRefs);
                            relCreate.redo();
                            result = relCreate.getCreatedRelation().getId();
                            break;
                        default:
                    }
                }
                currentlyProcessedBlocks.clear();
            }
            return result;
        }
    }

    /**
     * Class in charge of displaying the widget used to change the consolidation
     * status of Annotations to be adjudicated
     */
    public static class BlockConsoStatusDisplayer extends ConsolidationStatusDisplayer {

        private ConsolidationHandler.ConsolidationProcessingSubBlock currentBlock;

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

        private void setProcessedBlock(ConsolidationHandler.ConsolidationProcessingSubBlock block) {
            currentBlock = block;
        }

        @Override
        public void onConsolidationStatusChanged(String annotationId, ConsolidationStatus status) {
            if (currentBlock != null) {
                currentBlock.updateAnnotationConsolidationStatus(annotationId, status);
            }
        }
    }
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    private static AnnSetCompareUiBinder uiBinder = GWT.create(AnnSetCompareUiBinder.class);

    interface AnnSetCompareUiBinder extends UiBinder<DockLayoutPanel, AnnSetCompare> {
    }
    //
    private static final StaneClientBaseGinInjector injector = GWT.create(StaneClientBaseGinInjector.class);
    private static final Logger log = Logger.getLogger(AnnSetCompare.class.getName());
    //
    @UiField
    SplitLayoutPanel mainHorizontalSplitPanel;
    @UiField
    SplitLayoutPanel mainVerticalSplitPanel;
    @UiField
    SplitLayoutPanel propsSplitPanel;
    @UiField
    LayoutPanel propsLayoutPanel;
    @UiField
    LayoutPanel prop1LayoutPanel;
    @UiField
    LayoutPanel prop2LayoutPanel;
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
    LayoutPanel doc1LayoutPanel;
    @UiField
    LayoutPanel doc2LayoutPanel;
    @UiField
    RequiresResizeSpy docsResizeSpy;
    @UiField
    DocumentUi docUI1;
    @UiField
    DocumentUi docUI2;
    @UiField
    DocumentUi docUIconso;
    @UiField
    PushButton locateBlock;
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
    @UiField
    ToggleButton syncScroll;
    @UiField
    MenuItem annSetMenuItem;
    @UiField
    AnnotationTable annotationTable;
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
    private final PersistedOptionHandler reviewPanelHeightHnd = new PersistedOptionHandler("review.reviewpanelheight");
    private final PersistedOptionHandler propPanelWidthHnd = new PersistedOptionHandler("review.proppanelwidth");
    //
    private Annotation lastSelectAnnotation;

    public AnnSetCompare() {
        initWidget(uiBinder.createAndBindUi(this));

        mainHorizontalSplitPanel.addDomHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                ShortCutToActionTypeMapper.ShortCutTriggeredActionType shortcut = ShortCutToActionTypeMapper.getHappenedActionType(event.getCharCode(), event.getNativeEvent().getKeyCode(), event.isControlKeyDown(), event.isAltKeyDown(), event.isMetaKeyDown(), event.isShiftKeyDown());
                if (shortcut != null) {
                    switch (shortcut) {
                        case CONSOLIDATEANNOTATION:
                            onMergeClicked(null);
                            break;
                        default:
                            break;
                    }
                }
            }
        }, KeyPressEvent.getType());

        //restore panel sizes as set by the user                
        Scheduler.get().scheduleDeferred(new Command() {
            @Override
            public void execute() {
                {
                    Integer reviewPanelHeight = reviewPanelHeightHnd.getValue();
                    if (reviewPanelHeight != null) {
                        reviewPanelHeight = Math.min(reviewPanelHeight, propsSplitPanel.getElement().getClientHeight() - 20);
                        propsSplitPanel.setWidgetSize(propsLayoutPanel, reviewPanelHeight);
                        docsSplitPanel.setWidgetSize(docsLayoutPanel, reviewPanelHeight);
                    }
                }
                {
                    Integer propPanelWidth = propPanelWidthHnd.getValue();
                    if (propPanelWidth != null) {
                        propPanelWidth = Math.min(propPanelWidth, mainVerticalSplitPanel.getElement().getClientWidth() - 20);
                        mainVerticalSplitPanel.setWidgetSize(propsSplitPanel, propPanelWidth);
                    }
                }
            }
        });



        detailsUI1.setReadOnly(true);
        detailsUI2.setReadOnly(true);



        docsResizeSpy.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                propsSplitPanel.setWidgetSize(propsLayoutPanel, event.getHeight());
                reviewPanelHeightHnd.persistValue(event.getHeight());
            }
        });

        propsResizeSpy.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                docsSplitPanel.setWidgetSize(docsLayoutPanel, event.getHeight());
                reviewPanelHeightHnd.persistValue(event.getHeight());
                propPanelWidthHnd.persistValue(event.getWidth());
            }
        });

        HandlerRegistration shreg1 = docUI1.addScrollHandler(new ScrollHandler() {
            @Override
            public void onScroll(ScrollEvent event) {
                docUI2.setVerticalScrollPosition(docUI1.getVerticalScrollPosition());
                if (syncScroll.isDown()) {
                    docUIconso.setVerticalScrollPosition(docUI1.getVerticalScrollPosition());
                }
            }
        });

        HandlerRegistration shreg2 = docUI2.addScrollHandler(new ScrollHandler() {
            @Override
            public void onScroll(ScrollEvent event) {
                docUI1.setVerticalScrollPosition(docUI2.getVerticalScrollPosition());
                if (syncScroll.isDown()) {
                    docUI2.setVerticalScrollPosition(docUI2.getVerticalScrollPosition());
                }
            }
        });


        HandlerRegistration shregConso = docUIconso.addScrollHandler(new ScrollHandler() {
            @Override
            public void onScroll(ScrollEvent event) {
                if (syncScroll.isDown()) {
                    docUI1.setVerticalScrollPosition(docUIconso.getVerticalScrollPosition());
                }
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

                if (singleAnnotationSet) {
                    //show only panels related to the one and only doc
                    propsLayoutPanel.getWidgetContainerElement(prop1LayoutPanel).getStyle().setHeight(100, Style.Unit.PCT);
                    propsLayoutPanel.getWidgetContainerElement(prop2LayoutPanel).getStyle().setDisplay(Style.Display.NONE);
                    docsLayoutPanel.getWidgetContainerElement(doc1LayoutPanel).getStyle().setHeight(100, Style.Unit.PCT);
                    docsLayoutPanel.getWidgetContainerElement(doc2LayoutPanel).getStyle().setDisplay(Style.Display.NONE);
                }

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
                        docUI1.setDocument(hnd1, new DocumentView.Options(true, true, true, false, false, false, null, null));
                        AnnotationSetImpl annSet = hnd1.getAnnotationSetIdForUserTask(user_id1, reviewedTaskId);
                        docUI1.setTitleText(annSet.getDescription());
                        detailsUI1.setRegisteredAnnotatedText(hnd1);
                        consoStatusDisplayer1 = new BlockConsoStatusDisplayer();
                        docUI1.addAnnotationDisplayer(consoStatusDisplayer1);
                        docUI1.addOverlayEventsHandler(consoStatusDisplayer1);
                        annSetId1 = hnd1.getAnnotationSetIdForUserTask(user_id1, reviewedTaskId).getId();

                        consoHandler.addReviewedDoc(hnd1, user_id1, reviewedTaskId);
                        detailsUIconso.addSourceAnnotatedText(hnd1);
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
                            docUI2.setDocument(hnd2, new DocumentView.Options(true, true, true, false, false, false, null, null));
                            AnnotationSetImpl annSet = hnd2.getAnnotationSetIdForUserTask(user_id2, reviewedTaskId);
                            docUI2.setTitleText(annSet.getDescription());
                            detailsUI2.setRegisteredAnnotatedText(hnd2);
                            consoStatusDisplayer2 = new BlockConsoStatusDisplayer();
                            docUI2.addAnnotationDisplayer(consoStatusDisplayer2);
                            docUI2.addOverlayEventsHandler(consoStatusDisplayer2);
                            annSetId2 = hnd2.getAnnotationSetIdForUserTask(user_id2, reviewedTaskId).getId();

                            consoHandler.addReviewedDoc(hnd2, user_id2, reviewedTaskId);
                            detailsUIconso.addSourceAnnotatedText(hnd2);
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

                            //
                            startReviewTask(null, null);

                        }
                    }
                });

            }
        });

    }

    private void startReviewTask(final Command runOnSuccess, final Command runOnFailure) {
        injector.getCoreDataProvider().getAnnotatedDocument(params.getUserId(), params.getCampaignId(), params.getDocumentId(), params.getTaskId(), new AsyncCallback<AnnotatedTextImpl>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Document " + params.getUserId() + " was not found in Campaign " + params.getCampaignId() + " for Task " + params.getTaskId() + " !");
                GWT.log(caught.getMessage());
                if (runOnFailure != null) {
                    runOnFailure.execute();
                }
            }

            @Override
            public void onSuccess(AnnotatedTextImpl result) {

                ///////////////:::::
                hndConso = AnnotatedTextHandler.createHandler(params.getUserId(), params.getCampaignId(), result);
                docUIconso.setDocument(hndConso, new DocumentView.Options(false, false, false, false, false, false, null, null));
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
                        if (runOnFailure != null) {
                            runOnFailure.execute();
                        }
                    }

                    @Override
                    public void onSuccess(CDWXS_ConsoBlocks_ResponseImpl result) {
                        ArrayList<ConsolidationBlock> consoBlocks = new ArrayList<ConsolidationBlock>();
                        consoBlocks.addAll(new JsArrayDecorator<ConsolidationBlockImpl>(result.getBlocks()));
                        ArrayList<ConsolidableAnnotation> horderAnnotations = new ArrayList<ConsolidableAnnotation>();
                        horderAnnotations.addAll(new JsArrayDecorator<ConsolidableAnnotationImpl>(result.getHigherOrderAnnotations()));
                        initConsolidation(consoBlocks, horderAnnotations);
                        //
                        initAnnotationSourceMenu();
                        if (runOnSuccess != null) {
                            runOnSuccess.execute();
                        }
                    }
                });

            }
        });
    }

    private void initAnnotationSourceMenu() {
        MenuBar annSetMenuBar = new MenuBar(true);

        annSetMenuBar.addItem(new MenuItem(SafeHtmlUtils.fromString(docUI1.getTitleText()), new Command() {
            @Override
            public void execute() {
                annotationTable.setRegisteredAnnotatedText(hnd1);
            }
        }));
        if (!singleAnnotationSet) {
            annSetMenuBar.addItem(new MenuItem(SafeHtmlUtils.fromString(docUI2.getTitleText()), new Command() {
                @Override
                public void execute() {
                    annotationTable.setRegisteredAnnotatedText(hnd2);
                }
            }));
        }
        annSetMenuBar.addItem(new MenuItem(SafeHtmlUtils.fromString(docUIconso.getTitleText()),
                new Command() {
            @Override
            public void execute() {
                annotationTable.setRegisteredAnnotatedText(hndConso);
            }
        }));

        annSetMenuItem.setSubMenu(annSetMenuBar);
    }

    private void initConsolidation(List<ConsolidationBlock> consoBlocks, List<ConsolidableAnnotation> higherOrderAnns) {
        consoHandler.init(consoBlocks, higherOrderAnns, hndConso);
        refreshBlockBrowsingButtons();
        displayCurrentBlock();
    }

    private void displayCurrentBlock() {
        consoStatusDisplayer1.setProcessedBlock(null);
        consoStatusDisplayer2.setProcessedBlock(null);
        consoHandler.clearSubBlocks();

        ConsolidationHandler.ConsolidationProcessingSubBlock block1 = consoHandler.createSubBlockAtCurrentIndex(annSetId1);
        consoStatusDisplayer1.setProcessedBlock(block1);
        docUI1.refreshSecondaryDisplayer(consoStatusDisplayer1);

        ConsolidationHandler.ConsolidationProcessingSubBlock block2 = null;
        if (!singleAnnotationSet) {
            block2 = consoHandler.createSubBlockAtCurrentIndex(annSetId2);
            consoStatusDisplayer2.setProcessedBlock(block2);
            docUI2.refreshSecondaryDisplayer(consoStatusDisplayer2);
        }

        //scroll document panels to the first Annotation of the current block
        if (block1 != null) {
            List<AnnotationBackReference> backRef = block1.getBackReferences();
            if (backRef != null && !backRef.isEmpty()) {
                String annotationId = backRef.get(0).getAnnotationId();
                docUI1.setScrollPositionAtAnnotation(annotationId);
            }
        } else if (!singleAnnotationSet && (block2 != null)) {
            List<AnnotationBackReference> backRef = block2.getBackReferences();
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
            AnnotationSetImpl usersAnnotationSet = consoHandler.getReviewAnnotationSet();
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

    @Override
    public void onEditHappened(EditHappenedEvent event) {
        AnnotationEdit eventEdit = event.getEdit();
        if (eventEdit instanceof AnnotationCreationEdit || eventEdit instanceof AnnotationDeletionEdit) {
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

                consoHandler.refreshBlocks();
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
            int nbProcessedSinceOverflow = -1;

            @Override
            public boolean execute() {
                if (1 == consoHandler.getDisplayBlockIndex()) {
                    //no block resolved in the last complete loop, hence none can be resolved anymore
                    if (nbProcessedSinceOverflow == 0) {
                        return false;
                    }
                    nbProcessedSinceOverflow = 0;
                }

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
                    nbProcessedSinceOverflow++;

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
            AnnotationSetImpl usersAnnotationSet = consoHandler.getReviewAnnotationSet();
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
            List<AnnotationBackReference> unresolved = consoHandler.getAllUnresolvedReviewedAnnotationAsRejectList();
            AnnotationSetImpl usersAnnotationSet = consoHandler.getReviewAnnotationSet();
            new AnnotationSetReplaceUnmatchedSourceEdit(hndConso, usersAnnotationSet, unresolved).redo();
        } finally {
            denyModification(false);
        }
    }

    @UiHandler("publish")
    void onPublishClicked(ClickEvent event) {
        saveReview(true);
    }

    @UiHandler("save")
    void onSaveReview(ClickEvent event) {
        saveReview(false);
    }

    private void saveReview(boolean publish) {

        AnnotationSetImpl usersAnnotationSet = consoHandler.getReviewAnnotationSet();

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

                //reload latest revision of document and restart review
                startReviewTask(new Command() {
                    @Override
                    public void execute() {
                        modified = false;
                        denyModification(false);
                        refreshBlockBrowsingButtons();
                        injector.getMainEventBus().fireEvent(new InformationReleasedEvent("Save performed!"));
                    }
                }, new Command() {
                    @Override
                    public void execute() {
                        denyModification(false);
                        refreshBlockBrowsingButtons();
                    }
                });
            }
        };

        denyModification(true);
        if (publish) {
            injector.getCoreDataProvider().saveAndPublishAnnotationSet(params.getUserId(), params.getCampaignId(), params.getDocumentId(), usersAnnotationSet, callback);
        } else {
            injector.getCoreDataProvider().saveAnnotationSet(params.getUserId(), params.getCampaignId(), params.getDocumentId(), usersAnnotationSet, callback);
        }
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
    public void onAnnotationSelectionChanged(GenericAnnotationSelectionChangedEvent event) {
        if (event instanceof GenericAnnotationSelectionEmptiedEvent) {
            lastSelectAnnotation = null;
        } else {
            lastSelectAnnotation = event.getMainSelectedAnnotation() != null ? event.getMainSelectedAnnotation() : null;
        }
        locateBlock.setEnabled(lastSelectAnnotation != null);
    }

    @UiHandler("locateBlock")
    void onLocateBlockClicked(ClickEvent event) {
        if (lastSelectAnnotation != null) {
            locateBlock.setEnabled(false);
            if (consoHandler.gotoNextBlockForAnnotation(lastSelectAnnotation)) {
                refreshBlockBrowsingButtons();
                displayCurrentBlock();
            } else {
                new Blinker(locateBlock, new int[]{120, 40, 100, 40, 100, 100}).start();
            }
            locateBlock.setEnabled(true);
        }
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
        GenericAnnotationSelectionChangedEvent.register(eventBus, this);
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        EditHappenedEvent.unregister(this);
        GenericAnnotationSelectionChangedEvent.unregister(this);
        reset();
    }
}
