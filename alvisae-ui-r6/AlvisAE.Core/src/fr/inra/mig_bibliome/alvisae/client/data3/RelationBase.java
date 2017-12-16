/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2014.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationReference;
import java.util.Map;

/**
 *
 * @author fpapazian
 */
public class RelationBase {

    private String type;
    private Map<String, AnnotationReference> argumentRoleMap;

    public RelationBase(String type, Map<String, AnnotationReference> argumentRoleMap) {
        this.type = type;
        this.argumentRoleMap = argumentRoleMap;
    }

    public String getType() {
        return type;
    }

    public Map<String, AnnotationReference> getArgumentRoleMap() {
        return argumentRoleMap;
    }
}
