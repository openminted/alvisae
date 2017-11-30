/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010.
 *
 */

/**
 *
 * @author fpapazian
 */

/* 
 * Wrapper for DocumentUI component
*/
function StaneDocumentUI(parentNodeId, withScrollpanel){
	if (undefined == withScrollpanel) {
		withScrollpanel = false;
	}
    this.docUIref = window._Stane_createDocumentUI(parentNodeId, withScrollpanel);
	this.docLoadedEventHandlers = [];

    this.loadDocument = function() {
        window._Stane_loadDocument(this.docUIref);
    };

    this.setDocument = function(jsonDoc, jsonOptions) {
        return window._Stane_setDocument(this.docUIref, jsonDoc, jsonOptions);
    };

    this.getDocument = function() {
        return window._Stane_getDocument(this.docUIref);
    };
	
	this.getDocumentHeight = function() {
		return window._Stane_getDocumentHeight(this.docUIref);
	};

	this.registerDocLoadedEventHandler = function(nativeHandler) {
		var handler = window._Stane_registerDocLoadedEventHandler(this.docUIref, nativeHandler);
		this.docLoadedEventHandlers.push(handler);
	};

	this.unregisterDocLoadedEventHandlers = function() {
		this.docLoadedEventHandlers.forEach(function(handler) {
			window._Stane_unregisterDocLoadedEventHandler(handler);
		});
		this.docLoadedEventHandlers.length = 0;
	};
}


function RawRange(){
    this.startContainer=null;
    this.startNode=null;
    this.startOffset=null;
    this.endContainer=null;
    this.getEndNode=null;
    this.endOffset=null;
    this.text=null;
    this.commonAncestorContainer=null;
}

var backupSelection = null;

function _getSelectedRawRanges() {

    backupSelection = new Array();
    result = new Array();
    if (document.selection) {
        //NOT TESTED
        // IE selections
        throw ("IE support not implemented yet!!");

        var selection = document.selection;
        if (selection.type=='Text') {
            var ranges = selection.createRangeCollection();

            for (i=0; i<ranges.length; i++) {
                var range = ranges.item(i);


                try {
                    //englobing element
                    var parentElt = range.parentElement();

                    var rawRange=new RawRange();
                    rawRange.startNode = parentElt;
                    rawRange.startOffset = 0;
                    rawRange.endNode = parentElt;
                    rawRange.endOffset = range.text.length;
                    rawRange.commonAncestorContainer = parentElt;
                    rawRange.text = range.text;
                    result.push(rawRange);


                }
                catch (e) {
                    if ((e.name=='ReferenceError') || (e.name=='SyntaxError')) {
                        throw e;
                    }
                    return false;
                }

            }

        }
    /////
    } else {

        // Mozilla selections / W3C Ranges
        // - Tested on FF3.6/Linux
        // - Seems to work on Chrome 7/Linux
        try {

            var selection = window.getSelection();
            if (! selection.isCollapsed) {

                for (i=0; i<selection.rangeCount; i++) {
                    var range = selection.getRangeAt(i);
                    backupSelection.push(range);

                    var rawRange=new RawRange();
                    rawRange.startNode = range.startContainer;
                    rawRange.startOffset = range.startOffset;
                    rawRange.endNode = range.endContainer;
                    rawRange.endOffset = range.endOffset;
                    rawRange.commonAncestorContainer = range.commonAncestorContainer;
                    result.push(rawRange);
                }
            }

        }
        catch(e){
            if ((e.name=='ReferenceError') || (e.name=='SyntaxError')) {
                throw e;
            }
            return false;
        }
    }
    return result;
}

function AnnotationMarkerIpml(){
    this.containerId=null;
    this.node=null;
    this.startOffset=null;
    this.endOffset=null;
    this.text=null;
}


// -----------------------------------------------------------------------------


