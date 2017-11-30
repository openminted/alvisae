/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.data;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;
import gwtupload.client.IFileInput.FileInputType;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.Utils;
import gwtupload.client.SingleUploader;

/**
 *
 * @author fpapazian
 */
public class UploadDialog extends DialogBox {

    interface Binder extends UiBinder<Widget, UploadDialog> {
    }
    private static final Binder binder = GWT.create(Binder.class);
    @UiField
    SimplePanel uploadPanel;
    @UiField
    Button uploadButton;
    @UiField
    Button cancelButton;

    public UploadDialog() {

        super(false, true);
        setText("Document Upload...");
        setWidget(binder.createAndBindUi(this));

        setAnimationEnabled(true);
        setGlassEnabled(true);

        SingleUploader defaultUploader = new SingleUploader(FileInputType.BROWSER_INPUT, null, uploadButton);

        defaultUploader.setAutoSubmit(false);
        //defaultUploader.setValidExtensions(new String[] {".html", ".xhtml"});

        uploadPanel.add(defaultUploader);
        defaultUploader.addOnStartUploadHandler(new IUploader.OnStartUploaderHandler() {

            public void onStart(IUploader uploader) {
                uploadButton.setEnabled(false);
                cancelButton.setEnabled(false);
            }
        });

        defaultUploader.addOnFinishUploadHandler(new IUploader.OnFinishUploaderHandler() {

            public void onFinish(IUploader uploader) {

                ResultMessageDialog result;

                if (uploader.getStatus() == Status.SUCCESS) {
                    // The server can send information to the client.
                    // You can parse this information using XML or JSON libraries
                    String resp = uploader.getServerResponse();

                    if (resp != null && !resp.isEmpty()) {

                        Document doc = XMLParser.parse(resp);
                        String size = Utils.getXmlNodeValue(doc, "file-1-size");
                        String type = Utils.getXmlNodeValue(doc, "file-1-type");
                        String id = Utils.getXmlNodeValue(doc, "file-1-id");
                        String supported = Utils.getXmlNodeValue(doc, "file-1-supported");
                        if (Boolean.valueOf(supported)) {
                            GWT.log(type + " is valid file type; ->" + id);
                            result = new ResultMessageDialog(ResultMessageDialog.Ok, "Upload", "The file has been succesfully uploaded.");
                        } else {
                            result = new ResultMessageDialog(ResultMessageDialog.Warn, "Upload", "The file type '" + type + "' is currently not supported.");
                        }
                    } else {
                        result = new ResultMessageDialog(ResultMessageDialog.Error, "Upload", "An error happened while uploading the file (1).");
                    }
                } else {
                    result = new ResultMessageDialog(ResultMessageDialog.Error, "Upload", "An error happened while uploading the file (2).");
                }
                hide();
                result.show();
            }
        });

    }

    @UiHandler("cancelButton")
    void onloginClicked(ClickEvent event) {
        hide();
    }
}
