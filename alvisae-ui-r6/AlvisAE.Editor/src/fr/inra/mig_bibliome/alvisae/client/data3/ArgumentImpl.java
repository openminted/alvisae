/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data3;

import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.Relation;

/**
 *
 * @author fpapazian
 */
public class ArgumentImpl implements Relation.Argument {

    private final String role;
    private final Annotation argument;

    public ArgumentImpl(String role, Annotation argument) {
        this.role = role;
        this.argument = argument;
    }

    @Override
    public String getRole() {
        return role;
    }

    @Override
    public Annotation getArgument() {
        return argument;
    }
}
