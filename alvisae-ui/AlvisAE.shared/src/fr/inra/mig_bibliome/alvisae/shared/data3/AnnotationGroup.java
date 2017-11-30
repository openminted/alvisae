/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.shared.data3;

import java.util.Collection;

public interface AnnotationGroup {

    /**
     * Return all components of this group.
     * The returned value is read-only.
     * Will not return null.
     * @return
     */
    Collection<AnnotationReference> getComponentRefs();

    /**
     * Add a component to this group.
     * @param component
     * @throws NullPointerException if component is null
     */
    void addComponent(AnnotationReference component);

    /**
     * Remove the component with the specified identifier from this group.
     * @param componentId
     * @throws NullPointerException if componentId is null
     */
    void removeComponent(String componentId);

    /**
     * Remove the specified annotation from this group.
     * @param component
     * @throws NullPointerException if component is null
     */
    void removeComponent(AnnotationReference component);

    /**
     * Add all components from the specified collection to this group.
     * @param components
     * @throws NullPointerException if components is null
     */
    void addComponents(Collection<AnnotationReference> components);

    /**
     * Remove all components from the specified collection from this group.
     * @param components
     * @throws NullPointerException if components is null
     */
    void removeComponents(Collection<AnnotationReference> components);
}
