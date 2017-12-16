/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010.
 *
 */

package fr.inra.mig_bibliome.alvisae.client.About;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * About dialog box to show credits and copyrights
 * @author fpapazian
 */
public class AboutStaneDialog extends DialogBox {

  interface Binder extends UiBinder<Widget, AboutStaneDialog> { }
  private static final Binder binder = GWT.create(Binder.class);

  @UiField Button closeButton;

  public AboutStaneDialog() {
    setText("About...");
    setWidget(binder.createAndBindUi(this));

    setAnimationEnabled(true);
    setGlassEnabled(true);
  }

  @Override
  protected void onPreviewNativeEvent(NativePreviewEvent preview) {
    super.onPreviewNativeEvent(preview);

    NativeEvent evt = preview.getNativeEvent();
    if (evt.getType().equals("keydown")) {
      // Use the popup's key preview hooks to close the dialog when either
      // enter or escape is pressed.
      switch (evt.getKeyCode()) {
        case KeyCodes.KEY_ENTER:
        case KeyCodes.KEY_ESCAPE:
          hide();
          break;
      }
    }
  }

  @UiHandler("closeButton")
  void onSignOutClicked(ClickEvent event) {
    hide();
  }
}
