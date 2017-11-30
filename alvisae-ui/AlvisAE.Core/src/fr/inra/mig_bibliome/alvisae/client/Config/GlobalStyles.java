/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Config;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;

/**
 * Redefinable global color and associated style for graphical rendering
 * @author fpapazian
 */
public class GlobalStyles {

    public final static String SelectedAttr = "selected";
    public final static String MainSelectedAttr = "mainSelected";
    public final static String VeiledAttr = "veiled";
    public final static String RegularAnnotation = GlobalStyles.Aspect.Regular.getOutlineStyleName();
    public final static String SelectedAnnotation = GlobalStyles.Aspect.Selected.getOutlineStyleName();
    public final static String MainSelectedAnnotation = GlobalStyles.Aspect.MainSelected.getOutlineStyleName();
    public final static String HoveredAnnotation = GlobalStyles.Aspect.Hovered.getOutlineStyleName();
    public final static String HoveredTargetAnnotation = GlobalStyles.Aspect.HoveredTarget.getOutlineStyleName();
    public final static Aspect SelectedRelation = GlobalStyles.Aspect.Selected;
    public final static Aspect MainSelectedRelation = GlobalStyles.Aspect.MainSelected;
    public final static Aspect HoveredRelation = GlobalStyles.Aspect.Hovered;
    public final static Aspect HoveredSelectedRelation = GlobalStyles.Aspect.HoveredSelected;
    public final static String SelectedRow = GlobalStyles.Aspect.Selected.getBackgroundStyleName();
    public final static String MainSelectedRow = GlobalStyles.Aspect.MainSelected.getBackgroundStyleName();
    private final static int interlineSizePx[] = {15, 18, 27, 36, 45, 57, 69, 81};

    public static enum Aspect {

        HoveredSelected("green"),
        HoveredTarget("Lime") {

            @Override
            public String getOutlineStyle() {
                return "." + getOutlineStyleName() + " { outline: " + getColor() + " solid 3px !important;  }\n";
            }
        },
        Regular("red") {

            @Override
            public String getOutlineStyle() {
                return "." + getOutlineStyleName() + "[" + SelectedAttr + "=\"false\"]" + ":not([" + VeiledAttr + "])" + " { outline: " + getColor() + " solid thin; }\n";
            }
        },
        Selected("paleturquoise"),
        MainSelected("turquoise") {

            @Override
            public String getOutlineStyle() {
                return "." + getOutlineStyleName() + " { outline: " + getColor() + " solid 3px; }\n";
            }
        },
        Hovered("yellow") {

            @Override
            public String getOutlineStyle() {
                return "." + getOutlineStyleName() + ":hover[" + SelectedAttr + "=\"false\"] { outline: " + getColor() + " solid 2px; }\n"
                        + "." + getOutlineStyleName() + ":hover[" + SelectedAttr + "=\"true\"] { outline: green solid 2px; }\n";
            }
        },;
        private final String color;
        private final String borderStyleName;
        private final String backgroundStyleName;

        Aspect(String color) {
            this.color = color;
            this.borderStyleName = name() + "_Border";
            this.backgroundStyleName = name() + "_Background";
        }

        public String getColor() {
            return color;
        }

        public String getOutlineStyleName() {
            return borderStyleName;
        }

        public String getOutlineStyle() {
            return "." + getOutlineStyleName() + " { outline: " + getColor() + " solid thin; }\n";
        }

        public String getBackgroundStyleName() {
            return backgroundStyleName;
        }

        public String getBackgroundStyle() {
            return "." + getBackgroundStyleName() + " { background-color: " + getColor() + "; }\n";
        }

        public String getStyleRules() {
            StringBuilder rules = new StringBuilder();
            rules.append(getOutlineStyle());
            rules.append(getBackgroundStyle());
            return rules.toString();
        }
    }

    public static int[] getInterlineSizePx() {
        return interlineSizePx;
    }

    public static String getInterlineStyleName(int index) {
        return "aee_InterLine_" + index;
    }

    public static Element getInlinedStyleElement() {

        StringBuilder sheet = new StringBuilder();
        for (Aspect ctype : Aspect.values()) {
            sheet.append(ctype.getStyleRules());
        }
        sheet.append("\n");
        for (int i = 0; i < interlineSizePx.length; i++) {
            sheet.append(".").append(getInterlineStyleName(i)).append(" { line-height:").append(interlineSizePx[i]).append("px; }\n");
        }
        Element inlinedStyleElement = Document.get().createStyleElement();
        inlinedStyleElement.setAttribute("type", "text/css");
        inlinedStyleElement.setInnerText(sheet.toString());
        return inlinedStyleElement;


    }
}
