/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2014.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Conso;

import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.ConsolidationBlock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Consolidation Block for the adjudication of Groups and Relations
 *
 * @author fpapazian
 */
public class HighOrderConsolidationBlock implements ConsolidationBlock {

    public static final Comparator<ConsolidationBlock> comparator = new Comparator<ConsolidationBlock>() {
        @Override
        public int compare(ConsolidationBlock o1, ConsolidationBlock o2) {
            return Integer.valueOf(o1.getEnd()).compareTo(Integer.valueOf(o2.getEnd()));

        }
    };
    private final int level;
    private final AnnotationKind kind;
    private final int start;
    private final int end;
    private final Map<Integer, List<AnnotationReference>> membersById = new HashMap<Integer, List<AnnotationReference>>();
    private boolean noconflict = false;
    private final Set<String> derivedIds = new HashSet<String>();

    private HighOrderConsolidationBlock(int adjudicationLevel, AnnotationKind kind, int start, int end) {
        this.level = adjudicationLevel;
        this.kind = kind;
        this.start = start;
        this.end = end;
    }

    public HighOrderConsolidationBlock(int adjudicationLevel, AnnotationKind kind, int start, int end, Collection<AnnotationReference> members, boolean noconflict, Set<String> derivedIds) {
        this(adjudicationLevel, kind, start, end);
        addMembers(members);
        setWithoutConflict(noconflict);
        if (derivedIds != null) {
            this.derivedIds.addAll(derivedIds);
        }
    }

    public HighOrderConsolidationBlock(int adjudicationLevel, AnnotationKind annotationKind, AnnotationReference annotationRef) {
        this(adjudicationLevel, annotationKind, 0, 0);
        ArrayList<AnnotationReference> member = new ArrayList<AnnotationReference>();
        member.add(annotationRef);
        addMembers(member);
    }

    private void addMembers(Collection<AnnotationReference> members) {
        for (AnnotationReference memberRef : members) {
            int annSetId = memberRef.getAnnotationSetId();
            if (!membersById.containsKey(annSetId)) {
                membersById.put(annSetId, new ArrayList<AnnotationReference>());
            }
            membersById.get(annSetId).add(memberRef);
        }
    }

    @Override
    public int getAdjudicationLevel() {
        return level;
    }

    @Override
    public AnnotationKind getAnnotationKind() {
        return kind;
    }

    @Override
    public int getStart() {
        return start;
    }

    @Override
    public int getEnd() {
        return end;
    }

    @Override
    public boolean isWithoutConflict() {
        return noconflict;
    }

    public void setWithoutConflict(boolean noconflict) {
        this.noconflict = noconflict;
    }

    @Override
    public List<List<? extends AnnotationReference>> getMembers() {
        List<List<? extends AnnotationReference>> result = new ArrayList<List<? extends AnnotationReference>>();
        result.addAll(membersById.values());
        return result;
    }

    @Override
    public List<? extends AnnotationReference> getMembersByAnnotationSet(int annotationSetId) {
        if (membersById.containsKey(annotationSetId)) {
            return membersById.get(annotationSetId);
        } else {
            return new ArrayList<AnnotationReference>();
        }
    }

    public Set<String> getDerivedIds() {
        return derivedIds;
    }
}
