/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010.
 *
 */
package fr.inra.mig_bibliome.alvisae.client.Config;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Window.Navigator;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A class that associate Action types to redefinable keyboard shortcuts
 * @author fpapazian
 */
public class ShortCutToActionTypeMapper {

    private static final boolean isMacOs = Navigator.getPlatform().contains("Mac");

    public static boolean isMacOs() {
        return isMacOs;
    }

    public static ShortCutTriggeredActionType getHappenedActionType(char charCode, int keyCode, boolean controlKeyDown, boolean altKeyDown, boolean metaKeyDown, boolean shiftKeyDown) {
        for (ShortCutTriggeredActionType shortCutEvent : ShortCutTriggeredActionType.values()) {
            if (shortCutEvent.happened(charCode, keyCode, controlKeyDown, altKeyDown, metaKeyDown, shiftKeyDown)) {
                return shortCutEvent;
            }
        }
        return null;
    }
    private static final KeyboardShortCut[] undo = {new KeyboardShortCut('z', null, !isMacOs(), false, isMacOs(), null), new KeyboardShortCut('Z', null, !isMacOs(), false, isMacOs(), null)};
    private static final KeyboardShortCut[] redo = {new KeyboardShortCut('y', null, !isMacOs(), false, isMacOs(), null), new KeyboardShortCut('Y', null, !isMacOs(), false, isMacOs(), null)};
    private static final KeyboardShortCut[] createAnnotation = {new KeyboardShortCut('a', null, false, false, false, null), new KeyboardShortCut('A', null, false, false, false, null), new KeyboardShortCut((char)0, 45, false, false, false, false)};
    private static final KeyboardShortCut[] removeAnnotation = {new KeyboardShortCut('s', null, false, false, false, null), new KeyboardShortCut('S', null, false, false, false, null), new KeyboardShortCut((char)0, KeyCodes.KEY_DELETE, false, false, false, false)};
    private static final KeyboardShortCut[] increaseLineSize = {new KeyboardShortCut('+', null, false, false, false, true)};
    private static final KeyboardShortCut[] decreaseLineSize = {new KeyboardShortCut('-', null, false, false, false, true)};
    private static final KeyboardShortCut[] toggleSelectionMode = {new KeyboardShortCut('v', null, false, false, false, null),new KeyboardShortCut('V', null, false, false, false, null)};
    private static final KeyboardShortCut[] consolidateAnnotation = {new KeyboardShortCut('c', null, false, false, false, null), new KeyboardShortCut('C', null, false, false, false, null), new KeyboardShortCut((char)0, 45, false, false, false, false)};
    
    /**
     * Application defined Action types with their associated keyboard shortcuts
     */
    public static enum ShortCutTriggeredActionType {
        UNDO(undo),
        REDO(redo),
        CREATEANNOTATION(createAnnotation),
        REMOVEANNOTATION(removeAnnotation),
        INCREASELINESIZE(increaseLineSize),
        DEREASELINESIZE(decreaseLineSize),
        TOGGLESELECTONMODE(toggleSelectionMode),
        CONSOLIDATEANNOTATION(consolidateAnnotation),
        ;

        private ArrayList<KeyboardShortCut> shortcuts = new ArrayList<KeyboardShortCut>();

        ShortCutTriggeredActionType(KeyboardShortCut[] shortcuts) {
            this.shortcuts.addAll(Arrays.asList(shortcuts));
        }

        public ShortCutTriggeredActionType addShortCut(char charCode, Integer keyCode, Boolean controlKey, Boolean altKey, Boolean metaKey, Boolean shiftKey) {
            shortcuts.add(new KeyboardShortCut(charCode, keyCode, controlKey, altKey, metaKey, shiftKey));
            return this;
        }

        public boolean happened(char charCode, Integer keyCode, Boolean controlKey, Boolean altKey, Boolean metaKey, Boolean shiftKey) {
            for (KeyboardShortCut shortcut : shortcuts) {
                if (shortcut.happened(charCode, keyCode, controlKey, altKey, metaKey, shiftKey)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * key combination defining a Keyboard Shortcut.
     */
    public static class KeyboardShortCut {

        private final Integer charCode;
        private final Integer keyCode;
        private final Boolean controlKey;
        private final Boolean altKey;
        private final Boolean metaKey;
        private final Boolean shiftKey;

        /**
         *
         * @param charCode the key character used for this shortcut
         * @param controlKeyPressed define whether Control key must on/off for this shortcut (null to ignore Control key)
         * @param altKeyPressed define whether Alt key must on/off for this shortcut (null to ignore Alt key)
         * @param metaKeyPressed define whether Meta key must on/off for this shortcut (null to ignore Meta key)
         * @param shiftKeyPressed define whether Shift key must on/off for this shortcut (null to ignore Shift key)
         */
        public KeyboardShortCut(char charCode, Integer keyCode, Boolean controlKeyPressed, Boolean altKeyPressed, Boolean metaKeyPressed, Boolean shiftKeyPressed) {
            this.charCode = (int) charCode;
            this.keyCode = keyCode;
            this.controlKey = controlKeyPressed;
            this.altKey = altKeyPressed;
            this.metaKey = metaKeyPressed;
            this.shiftKey = shiftKeyPressed;
        }

        public boolean happened(char charCode, int keyCode, Boolean controlKey, Boolean altKey, Boolean metaKey, Boolean shiftKey) {
            return (this.charCode == 0 || this.charCode == charCode)
                    && (this.keyCode == null || this.keyCode == keyCode)                    
                    && (this.controlKey == null || this.controlKey.equals(controlKey))
                    && (this.altKey == null || this.altKey.equals(altKey))
                    && (this.metaKey == null || this.metaKey.equals(metaKey))
                    && (this.shiftKey == null || this.shiftKey.equals(shiftKey));
        }
    }
}
