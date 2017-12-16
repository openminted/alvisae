/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2011.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Annotation;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import fr.inra.mig_bibliome.alvisae.client.StanEditorResources;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextHandler;
import fr.inra.mig_bibliome.alvisae.client.data3.AnnotatedTextProcessor;
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind;
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationReference;
import fr.inra.mig_bibliome.alvisae.shared.data3.Fragment;
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.AnnotationTypeDefinition;
import java.util.Collection;

/**
 *
 * @author fpapazian
 */
public abstract class CombinedAnnotationCell extends AbstractCell<Annotation> {

    public static final String REFANNID_ATTRNAME = "aae_refannid";

    public static interface AnnotationCellTemplates extends SafeHtmlTemplates {

        @Template("<span " + REFANNID_ATTRNAME + "='{0}' style='background-color:silver;'>{1}</span> ")
        public SafeHtml annRefSpan(String annotationId, String displayId);

        @Template("<span " + REFANNID_ATTRNAME + "='{0}' title='{0}' style='background-color:silver;'>{1}</span> ")
        public SafeHtml annRefSpan2(String annotationId, SafeHtml safeHtml);

        @Template("<span style='color:silver;'>{0}</span>")
        public SafeHtml grayedSpan(String text);

        @Template("<span title='{0}'>{1}</span>")
        public SafeHtml titledSpan(String title, SafeHtml safeHtml);

        @Template("<span " + REFANNID_ATTRNAME + "='{0}' title='missing referenced Annotation \"{0}\"' style='color:red;'>{1}</span> ")
        public SafeHtml missingAnnRefSpan(String annotationId, String displayId);

        @Template("<span title='missing role \"{0}\"' style='color:red;'>???</span> ")
        public SafeHtml missingRoleSpan(String role);

        @Template("<span style='white-space:nowrap;' title='{0}'>{1}</span>")
        public SafeHtml textDetail(String title, SafeHtml fragments);

        @Template("<div style='display:inline-block;'><div class='{0}' style='display:inline-block;'></div></div>")
        public SafeHtml imgContainer(String style);
    }
    private static final AnnotationCellTemplates TEMPLATES = GWT.create(AnnotationCellTemplates.class);
    private static final SafeHtml TextKindImage = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StanEditorResources.INSTANCE.TextAnnotationIcon()).getHTML());
    private static final SafeHtml GroupKindImage = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StanEditorResources.INSTANCE.GroupAnnotationIcon()).getHTML());
    private static final SafeHtml RelationKindImage = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(StanEditorResources.INSTANCE.RelationAnnotationIcon()).getHTML());

    public static SafeHtml getKindInlinedImage(AnnotationKind kind) {
        SafeHtml image = null;
        switch (kind) {
            case TEXT:
                image = TextKindImage;
                break;
            case GROUP:
                image = GroupKindImage;
                break;
            case RELATION:
                image = RelationKindImage;
                break;
        }
        return image;
    }

    public static void renderKindAsInlinedImg(AnnotationKind kind, SafeHtmlBuilder sb) {
        sb.append(TEMPLATES.titledSpan(kind.toString(), getKindInlinedImage(kind)));
    }

    public static SafeHtml getKindImage(AnnotationKind kind) {
        SafeHtml image = null;
        switch (kind) {
            case TEXT:
                image = TEMPLATES.imgContainer(StanEditorResources.INSTANCE.css().kdText());
                break;
            case GROUP:
                image = TEMPLATES.imgContainer(StanEditorResources.INSTANCE.css().kdGroup());
                break;
            case RELATION:
                image = TEMPLATES.imgContainer(StanEditorResources.INSTANCE.css().kdRelation());
                break;
        }
        return image;
    }

    public static void renderKind(AnnotationKind kind, SafeHtmlBuilder sb) {
        sb.append(TEMPLATES.titledSpan(kind.toString(), getKindImage(kind)));
    }

    public static SafeHtml getTextAsSafeHtml(AnnotatedTextHandler annotatedText, Annotation annotation) {
        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        StringBuilder fragmentsText = new StringBuilder();
        boolean once = false;
        if (annotatedText != null) {
            String leTexte = annotatedText.getAnnotatedText().getDocument().getContents();
            for (Fragment f : annotation.getTextBinding().getSortedFragments()) {
                if (once) {
                    sb.appendHtmlConstant("&brvbar;");
                } else {
                    once = true;
                }
                String fragText = leTexte.substring(f.getStart(), f.getEnd());
                fragmentsText.append(fragText);
                sb.appendHtmlConstant("<b>").appendEscaped(fragText).appendHtmlConstant("</b>");
            }
        }
        return TEMPLATES.textDetail(fragmentsText.toString(), sb.toSafeHtml());
    }

    public static void renderDetail(AnnotatedTextHandler annotatedText, Annotation annotation, boolean textDetailOnly, SafeHtmlBuilder sb) {
        boolean once = false;
        if (annotatedText != null) {

            if (annotation == null) {
                //happens when rendering incomplete relations 
            } else {
                switch (annotation.getAnnotationKind()) {
                    case TEXT:
                        if (!annotatedText.isFormattingAnnotation(annotation.getId())) {
                            sb.append(getTextAsSafeHtml(annotatedText, annotation));
                        }
                        break;
                    case GROUP:
                        if (!textDetailOnly) {
                            sb.append(TEMPLATES.grayedSpan("{ "));
                            for (AnnotationReference a : annotation.getAnnotationGroup().getComponentRefs()) {
                                if (once) {
                                    sb.append(TEMPLATES.grayedSpan(", "));
                                } else {
                                    once = true;
                                }
                                String id = a.getAnnotationId();
                                Annotation refAnn = annotatedText.getAnnotation(id);
                                if (refAnn != null) {
                                    renderKind(refAnn.getAnnotationKind(), sb);
                                    sb.appendHtmlConstant("&nbsp;");
                                    AnnotationSchemaCell.renderType(refAnn.getAnnotationType(), sb);
                                    sb.appendHtmlConstant("&nbsp;");
                                    if (AnnotationKind.TEXT.equals(refAnn.getAnnotationKind())) {
                                        sb.append(TEMPLATES.annRefSpan2(id, SafeHtmlUtils.fromTrustedString(AnnotatedTextProcessor.getAnnotationText(refAnn))));
                                    } else {
                                        sb.append(TEMPLATES.annRefSpan(id, AnnotatedTextProcessor.getBriefId(id)));
                                    }
                                } else {
                                    //missing referenced annotation
                                    GWT.log("Missing referenced Annotation id= " + id + " in Group " + annotation.getId());
                                    sb.append(TEMPLATES.missingAnnRefSpan(id, AnnotatedTextProcessor.getBriefId(id)));
                                }
                            }
                            sb.append(TEMPLATES.grayedSpan(" }"));
                        }
                        break;
                    case RELATION:
                        if (!textDetailOnly) {
                            String annType = annotation.getAnnotationType();
                            AnnotationTypeDefinition annTypeDef = annotatedText.getAnnotatedText().getAnnotationSchema().getAnnotationTypeDefinition(annType);
                            Collection<String> orderedRoles;
                            if (annTypeDef != null) {
                                orderedRoles = annTypeDef.getRelationDefinition().getSupportedRoles();
                            } else {
                                orderedRoles = annotation.getRelation().getRoles();
                            }

                            for (String role : orderedRoles) {
                                if (once) {
                                    sb.append(TEMPLATES.grayedSpan(" + "));
                                } else {
                                    once = true;
                                }
                                sb.appendHtmlConstant("<i>").appendEscaped(role).appendHtmlConstant("</i> ");
                                AnnotationReference ref = annotation.getRelation().getArgumentRef(role);
                                String id = null;
                                Annotation refAnn = null;
                                if (ref != null) {
                                    id = ref.getAnnotationId();
                                    refAnn = annotatedText.getAnnotation(id);
                                }
                                sb.append(TEMPLATES.grayedSpan("( "));
                                if (id == null) {
                                    //missing referenced annotation
                                    GWT.log("Missing Annotation for role= " + role + " in Relation " + annotation.getId());
                                    sb.append(TEMPLATES.missingRoleSpan(role));
                                } else if (refAnn == null) {
                                    //missing referenced annotation
                                    GWT.log("Missing referenced Annotation id= " + id + " in Relation " + annotation.getId());
                                    sb.append(TEMPLATES.missingAnnRefSpan(id, AnnotatedTextProcessor.getBriefId(id)));
                                } else {
                                    renderKind(refAnn.getAnnotationKind(), sb);
                                    sb.appendHtmlConstant("&nbsp;");
                                    AnnotationSchemaCell.renderType(refAnn.getAnnotationType(), sb);
                                    sb.appendHtmlConstant("&nbsp;");
                                    if (AnnotationKind.TEXT.equals(refAnn.getAnnotationKind())) {
                                        sb.append(TEMPLATES.annRefSpan2(id, SafeHtmlUtils.fromTrustedString(AnnotatedTextProcessor.getAnnotationText(refAnn))));
                                    } else {
                                        sb.append(TEMPLATES.annRefSpan(id, AnnotatedTextProcessor.getBriefId(id)));
                                    }
                                }
                                sb.append(TEMPLATES.grayedSpan(" )"));
                            }
                        }
                        break;
                }
            }
        }
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    private AnnotatedTextHandler annotatedText = null;

    public CombinedAnnotationCell(String... consumedEvents) {
        super(consumedEvents);
    }

    public void setDocument(AnnotatedTextHandler annotatedText) {
        this.annotatedText = annotatedText;
    }

    public AnnotatedTextHandler getDocument() {
        return annotatedText;
    }

    @Override
    public abstract void render(Context context, Annotation annotation, SafeHtmlBuilder sb);
}
