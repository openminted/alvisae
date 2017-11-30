/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Task;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import fr.inra.mig_bibliome.alvisae.client.StaneResources;
import fr.inra.mig_bibliome.alvisae.client.data3.TaskInstanceImpl;

/**
 *
 * @author fpapazian
 */
public abstract class ImageButtonCell extends AbstractCell<TaskInstanceImpl> {
    private static final String ImageButtonCellClassName = "aae-ImageButtonCell";

    public static interface ImageButtonCellTemplates extends SafeHtmlTemplates {
        @SafeHtmlTemplates.Template(value = "<div title='{1}' class='" + ImageButtonCellClassName + " {2}'>{0}</div>")
        public SafeHtml button(SafeHtml image, String title, String classStyle);
    }
    public static final ImageButtonCellTemplates TEMPLATES = GWT.create(ImageButtonCellTemplates.class);
    //
    private final SafeHtml image;
    private final String title;

    public ImageButtonCell(ImageResource image, String title) {
        super(new String[]{"click"});
        this.image = SafeHtmlUtils.fromSafeConstant(AbstractImagePrototype.create(image).getHTML());
        this.title = title;
    }

    public abstract boolean displayImageButton(TaskInstanceImpl value);

    public abstract void onClicked(TaskInstanceImpl value);

    @Override
    public void onBrowserEvent(Context context, Element parent, TaskInstanceImpl value, NativeEvent event, ValueUpdater<TaskInstanceImpl> valueUpdater) {
        super.onBrowserEvent(context, parent, value, event, valueUpdater);
        String eventType = event.getType();
        if ("click".equals(eventType)) {
            EventTarget evtTarget = event.getEventTarget();
            Element targetElement = evtTarget.cast();
            if (targetElement.getClassName().startsWith(ImageButtonCellClassName) || targetElement.getParentElement().getClassName().startsWith(ImageButtonCellClassName)) {
                onClicked(value);
            }
        }
    }

    @Override
    public void render(Context context, TaskInstanceImpl value, SafeHtmlBuilder sb) {
        if (displayImageButton(value)) {
            sb.append(TEMPLATES.button(image, title, StaneResources.INSTANCE.style().MiniPopButton()));
        }
    }
    
}
