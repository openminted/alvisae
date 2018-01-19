# AlvisAE User Guide

## Introduction
AlvisAE is a tool designed to seamlessly perform the annotation of textual documents.
Because it is a Web Application, no installation is needed to start using AlvisAE : Indeed, AlvisAE runs on any platform providing a modern web browser (such as Firefox, Safari, Chrome).

![](images/AlvisAE_globalDocView.png)

## Brief description
AlvisAE has been designed to facilitates the annotation of textual documents, with the goal of extracting knowledge. To do so, AlvisAE displays the text of the document, that can optionally includes some simple formatting; 
Annotations are superimposed to the text, in a way that do not impair the readability. 
Each Annotations belongs to a _Type_, and is displayed according to this _Type_ (the list of _Types_ and their associated colors are specified in the _Annotation Schema_).  

### ![](images/doIt.png) Quick start: creating a _Text Annotation_

Creating an _Text Annotation_ is fairly intuitive: simply select the words of interest with the mouse pointer, choose the desired annotation _Type_ in the toolbar and click the “Create Text Annotation” button [ ![](images/TextAnnotation_Add.png) ].

### Annotations come in 3 differents _Kinds_

1. ![](images/TextAnnotation_icon.png) _Text Annotations_, also called primary annotation because they are bound to a part of the text

Besides Text Annotations, AlvisAE allows to edit 2 kinds of secondary annotation, which are not bound to text but are referencing other annotations:

2. ![](images/Relation_icon.png) _Relation_ -  which contains a fixed number of arguments, each argument being associated to a named role.

3. ![](images/Group_icon.png) _Group_ - simple set of reference to other annotations (called components)

### ![](images/doIt.png) Creating secondary annotations

Creating secondary annotations is rather simple:

* A Relation between 2 Text Annotations is created by dragging the first annotation and dropping it onto the other one (drag the icon that appears at the top left corner of the annotation ![](images/magnet-blue.png) ); 
* For more complex Relations (i.e. concerning more than 2 primary annotations, or some secondary annotation), you will need to select the arguments to be included in the relation from the Annotation Table, then clicking the "Create Relation" button [ ![](images/Relation_Add.png) ].
* Creating Groups is also performed by selecting the components in the Annotation Table, then clicking the "Create Group" button [ ![](images/Group_Add.png) ].

## Tour of AlvisAE

### start using AlvisAE

you can experiment AlvisAE with the online demo at [AlvisAE Demo](https://bibliome.jouy.inra.fr/alvisae/demo/AlvisAE/)

### Athentification
since each user can edit its own annotation, you will need to authenticate before doing any modification.

![](images/information-balloon.png) If you are trying the online demo, login name and password are both : @foo@

![](images/signInDialog.png)

![](lightning.png AlvisAE will remember your login name for next time !)

### Campaign and document selection

Once authenticated, AlvisAE will display the list of the available Campaigns. 
Each campaign contains a set of documents that can be annotated according to the Annotation schema specific to the campaign.
 
When selecting a specific campaign in the first table, the list of the corresponding documents will be displayed in the second table.
Double-click on the desired document line to start annotating it (or click the "Annotate selected document" button [ ![](images/fill.png) ])
  
![](images/CampaignsDocuments.png)

### Editing view

The selected document is displayed in the editing view:

![](images/ExplodedEditorView.png)

The editing view can be divided in the following parts:
  
1. **Global toolbar**, providing commands to: 
	* browse documents [ ![](images/fill-180.png) / ![](images/fill.png) ], 
	* return to campaigns & documents list [ ![](images/inbox--arrow.png) ],
	* save the edited annotations [ ![](images/disk-black.png) ],
	* end the annotation of the current document [ ![](images/receipt-share.png) ],
	* menu to sign-out or [Password-change]...? [[Aae_UserGuide#Password-change|change password]] [ ![](images/user-silhouette.png) ]
2. [[Aae_UserGuide#Editor-panel|Editor panel]], which display the document itself and commands to annotate it
3. resizeable **Panel-Boundaries**, to organize the space within the browser window 
4. [[Aae_UserGuide#Annotation-Table|Annotation table]], displaying annotation in a tabular format
5. **Side panel**, used to display and edit Annotation properties
6. **Status bar**, displaying system messages and network activity

## Editor panel
### Title Bar

![](lightning.png) double-clicking on the title bar will maximize the document panel (double-click on it again to restore normal view) 

![](information-balloon.png) when the title of the document is underlined, it contains an hyperlink to the source of the document (typically the PDF version of the document) 

### ToolBar

the toolbar contains all the commands used to edit annotations: 
* ![](images/_edit-vertical-alignment-top.png) increase text interline size,
* ![](images/_edit-vertical-alignment.png) decrease interline size,

* ![](images/arrow-curve-180-left.png) undo last edit (shortcut [ @Ctrl & Z@ ]),
* ![](images/arrow-curve.png) redo last undo-ed edit (shortcut [ @Ctrl & Y@ ]),


* choose annotation _Type_ from the drop-down list,

* ![](images/TextAnnotation_Add.png) create a new Text Annotation with the currently selected text portion (shortcut [ @A@ ]),
* ![](images/tag--minus.png) remove the currently selected Text Annotation (shortcut [ @Del@ ]),

* ![](images/ui-text-field--plus.png) add the selected text to the selected Text Annotation,
* ![](images/ui-text-field--minus.png) remove the selected text from the selected Text Annotation,

* ![](images/Group_Add.png) create a Group with the selected annotations,
* ![](images/jar--minus.png) remove the selected Group,
* ![](images/jar--pencil.png) edit the selected Group (add/remove components),

* ![](images/Relation_Add.png) create a Relation with the selected annotations,
* ![](images/layer-shape-line--minus.png) remove the selected Relation,
* ![](images/layer-shape-line--pencil.png) edit the selected Relation (change type and/or arguments),

* ![](images/layer-shape-text.png) / ![](images/layer-shape-curve.png) switch between Text selection and secondary annotation selection mode (shortcut [ @V@ ]),

* ![](images/map.png) display the current Annotation Schema,
* ![](images/clipboard-task.png) validate the current document,

* select the displayed Annotation Sets


![](images/lightning.png) The toolbar can be collapsed by clicking on the small triangular icon on the top left side [ ![](images/expandCollapseHandle.png) ]


### Document panel

This is the central panel of AlvisAE, which displays the text of the document, enriched with specific graphical elements to represent the different kind of Annotations.

![](images/information-balloon.png) When a document is loaded, Relations are hidden by default to preserve a good text readability.
![](images/lightning.png) when the mouse pointer fly over a Text Annotation, all Relations involving the hovered Text Annotation are temporarily revealed.
(The visibility status of each annotation can be individually set from the [[Aae_UserGuide#Annotation-Table|Annotation table]]).

#### Text Annotations

+Graphical representation+

Text Annotation are displayed as colored boxes, whose background color is set according to the Annotation Type.
The border color has also some specific meaning:
* red : default color of unselected annotation,
* yellow : currently hovered, unselected annotation,
* cyan : color of selected annotation(s),
* dark green : currently hovered, selected annotation,
* lime green : currently hovered annotation during a drag and drop operation

The border is always displayed, even if an annotation is hidden because it includes another annotation. So, the thickness of the border is a good indication of the number of included annotations.
![](images/aae_includedAnnotations.png)

![](images/lightning.png) Pressing the @Shift@ key while moving the mouse pointer over included annotations will popup the exploded list of annotations present at the mouse position, which is useful to select the right annotation (this list is automatically opened when dropping during a drag and drop operation intended to create a Relation).
![](images/aae_includedAnnotationsExploded.png)


+Edition+

* Click and drag to select a fragment of text, then click the “Create Text Annotation” button [ ![](images/TextAnnotation_Add.png) ] or use the keyboard shortcut @A@
![](images/lightning.png) It is possible to create discontinuous annotation by selecting several distinct fragments of text with the @Ctrl@ key pressed (your web browser must support discontinuous text selection)


* margin : warning icons....

>>>>>*TODO : add details here*

#### Relations

+Graphical representation+

Relation are figured by horizontal and vertical line segments linking each argument to a lozenge kernel, whose background color is set according to the Annotation Type.

#### Groups

+Graphical representation+

Groups are figured by diagonal line segments linking each component to a circled kernel, whose background color is set according to the Annotation Type.



h3. Occurrence bar

This is a vertical bar (located on right side of the document panel) where each Text Annotation is figured by a little horizontal line marker :
* the vertical position of the marker on the bar is proportional to the annotation location inside the document,
* the marker's color correspond to its Annotation Type.

![](images/lightning.png) double-clicking on a marker will scroll the document to view the corresponding text annotation.

![](images/information-balloon.png) The occurrence bar presents a global summary of all Text Annotations of the current document, in terms of localization, density, and is especially useful for areas of the document currently not scrolled into view.


### keyboard shortcuts

>>>>>*TODO : add details here*

<pre>

</pre>

## Annotation Table

This table presents in a tabular format all annotations of the current document.

![](images/aae_AnnotationTable.png)

Each annotation is displayed as a single line in the table which contains the following columns:

* unique **identifier** of the annotation
![](images/lightning.png) double-clicking on an id will scroll the document to view the corresponding annotation.
* **Annotation Set** to which the annotation belongs (named after the user name, or the software agent, who produced it).
* **Kind** of the annotation ( ![](images/TextAnnotation_icon.png) / ![](images/Relation_icon.png) / ![](images/Group_icon.png) )
* **Type**, figured by its color and name (depending of the Annotation Schema of the current document)
* **Details** : the fragment(s) of text covered for Text Annotation, or a specific flat representation for Relation and Group.
![](images/lightning.png) in the flat representation for Relation and Group: double-clicking on the grayed part representing a referenced annotation will change the selection to this specific annotation.
* optional **Term handle** [ ![](images/term-white.png) ], used for enriching an external Terminology/Ontology resource (see [[Aae_UserGuide#Termino-Ontology-Extension|Terminology/Ontology extension]])
* **Visibility** status, allowing to toggle (by a simple click on the icon) the visibility of the corresponding annotation in the document panel
![](images/lightning.png) if several lines are selected in the table, the visibility will be changed for all of them.
![](images/information-balloon.png) when loading a document, Relations are hidden by default, hence maintaining a good text readability.


The content of the table can be reordered by clicking on the column header (first click for ascending order, second click for descending order).
![](images/lightning.png) when ordering by Kind, the Text Annotation are presented in the order of the text flow.

There is a toolbar (collapsed by default) above the table header that can by expanded by clicking on the small triangular icon on the top left side [ ![](images/collapseExpandHandle.png) ] : it contains a menu allowing to select which Annotation Set(s) is displayed in the table.

<pre>

</pre>

## Termino-Ontology Extension

This extension can display an external Termino/Ontology resource stored in TyDI system.
The Classes hierarchy can be modified, and the resource can be enriched with terms or classes occurring in the document currently annotated.

![](images/aae_TdyExtension.png)

### Extension Activation

Text Annotations of a certain Type can be linked to external Termino/Ontology resource stored in TyDI system.
This option is specified within the Annotation schema; when it is enabled, the Termino-Ontology Extension will display in the left side panel.

### Authentication

Since the data displayed by this extension are managed by a system distinct from the Annotation system, you will be requested to authenticate to access the  Termino/Ontology resource.
The extension will remember your credentials, and will automatically submit it next time you'll need to access the Termino/Ontology resource.

![](images/lightning.png) It is still possible to change the credentials used to access the Termino/Ontology resource by clicking to the logout button [ ![](images/signOut.png) ].

### Browsing the Terminology/Ontology

* The middle panel displays the Terminology/Ontology as a tree which is loaded on demand when levels are expanded.
* The bottom panel of the extension presents the Terms belonging to the currently selected class.

### Searching within the Terminology/Ontology

![](images/doIt.png) The top toolbar of the extension can be used to search Classes within the Terminology/Ontology:

* enter a string pattern in the text field provided : ![](images/aae_TdyExtension_search.png)
![](images/information-balloon.png) the wild card character @%@ matches any string (0 or more characters)
* click the search button [ ![](images/go-down.png) ],
* The matching classes are presented in a popup list :
	* selecting a result line will trigger the opening of the tree for the path from the root node to the selected class.

![](images/lightning.png) the search result list is kept until next search, and it can be displayed again by clicking on the button on the left side of the pattern field [ ![](images/ui-accordion.png) ]

### Changing Classes hierarchy

![](images/doIt.png) The tree structure can be easily amended by simple drag and drag operations:

* To change the parent of a given class, simply drag the icon of that class and drop it on the class chosen as new parent.
* Instead of replacing the current parent class, it is possible to add a new parent by holding the Shift key when dropping the class.
* Finally, the link to a parent is cut by drop the class to the root of the tree.

![](images/information-balloon.png) Of course, the system ensure that no cycle appear in the DAG of the Terminology/Ontology as a result of these structure modifications.
A warning message will appear in the message notification area below the toolbar


### Enriching the Terminology/Ontology

Terms or concepts occurring in the document currently annotated can be used to enrich the Terminology/Ontology, once again by simple drag and drop operations initiated from the [[Aae_UserGuide#Annotation-Table|Annotation table]].

Indeed, any Annotation can be dragged by its corresponding circled T symbol [ ![](images/term-white.png) ] that appear in the table if the corresponding Type has been enabled to reference a Term or Class resource, and dropped on the Terminology/Ontology tree.
![](images/doIt.png) The actual modification of Terminology/Ontology will depend of the target class where the Annotated term is dropped:
* dropping onto an existing class will add the term as a synonym of the target class,
* dropping onto the root of the tree will create a new class with the term as canonical representative.
In both case, a new term will be created beforehand if it does not already exists in TyDI's database.

![](images/information-balloon.png) When the modification is confirmed, the source Annotation is automatically supplied with a property containing the Term or Class resource identifier.

![](images/information-balloon.png) Depending on the case, a drag and drop operation could lead to different possible modifications (adding Terms and/or Classes, merging Classes, etc.), and the final decision of which one to achieve belongs to the user.
After that a drop operation occurs, AlvisAE extension performs multiple checks and will possibly propose alternative ulterior actions along with some information which may not be obvious.

A list of message will popup in the message notification area below the toolbar ![](images/aae_MultipleAction.png)
![](images/lightning.png) You can choose one action from all the alternative operations proposed, or even cancel the modification.

<pre>

</pre>

## Miscellaneous

### Password change [Password-change]

Use the sub-menu "Change password" [ ![](images/key-solid.png) ] in the global toolbar of the [[Aae_UserGuide#Campaign-and-Document-selection|Campaign and Document selection view]].

A dialog box will appear allowing to type the new password. It must be entered a second time for verification purposes.

![](images/changePassword.png)

![](images/information-balloon.png) The change button is enabled when the two entered passwords are consistent.


### Users Management

To access Users Management screen, use the sub-menu "Manage users [ ![](images/users.png) ] in the global toolbar of the [[Aae_UserGuide#Campaign-and-Document-selection|Campaign and Document selection view]].

>>>>>*TODO : add screen capture here*

* The table in the top part of the screen lists all users existing in the current AlvisAE instance,
* The table in the bottom part display Authorizations for the currently selected user (one line per campaign),

Modifications can be performed directly in the tables.

>>>>>*TODO : add details here*

![](images/information-balloon.png) Managing user is a feature restricted to users with the Admin status enabled



