/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Widgets;

import com.google.gwt.user.cellview.client.TreeNode;

/**
 *
 * @author fpapazian
 */
public class TreeUtils {
    //class used to recursively visit nodes of a tree

    public static class TreeNodeTraveller {

        public static interface NodeExpression {

            boolean evaluateAndContinue(TreeNode parentNode, int childNodeIndex);
        }

        public static interface NodeExpressionWithResult<T> extends NodeExpression {

            void setResult(T result);

            T getResult();
        }

        public void travelOnOpenedNodes(TreeNode parentNode, NodeExpression nodeExpression) {
            for (int i = 0; i < parentNode.getChildCount(); i++) {
                boolean goDeeper = false;
                try {
                    goDeeper = nodeExpression.evaluateAndContinue(parentNode, i);
                } catch (Exception e) {
                    ;
                }
                if (!goDeeper) {
                    break;
                } else {
                    if (parentNode.isChildOpen(i)) {
                        TreeNode node = parentNode.setChildOpen(i, true);
                        travelOnOpenedNodes(node, nodeExpression);
                    }
                }
            }
        }

        public void travelAndOpenNodes(TreeNode parentNode, NodeExpression nodeExpression) {
            if (parentNode != null) {
                for (int i = 0; i < parentNode.getChildCount(); i++) {
                    boolean goDeeper = false;
                    try {
                        goDeeper = nodeExpression.evaluateAndContinue(parentNode, i);
                    } catch (Exception e) {
                        ;
                    }
                    if (!goDeeper) {
                        break;
                    } else {
                        TreeNode node = parentNode.setChildOpen(i, true);
                        travelAndOpenNodes(node, nodeExpression);
                    }
                }
            }
        }
    }
}
