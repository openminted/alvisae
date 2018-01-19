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

Creating an _Text Annotation_ is fairly intuitive : simply select the words of interest with the mouse pointer, choose the desired annotation _Type_ in the toolbar and click the “Create Text Annotation” button [ ![](images/TextAnnotation_Add.png) ].

### Annotations come in 3 differents _Kinds_

1. ![](images/TextAnnotation_icon.png) _Text Annotations_, also called primary annotation because they are bound to a part of the text

Besides Text Annotations, AlvisAE allows to edit 2 kinds of secondary annotation, which are not bound to text but are referencing other annotations:

2. ![](images/Relation_icon.png) _Relation_ -  which contains a fixed number of arguments, each argument being associated to a named role.

3. ![](images/Group_icon.png) _Group_ - simple set of reference to other annotations (called components)

### ![](images/doIt.png) Creating secondary annotations
Creating secondary annotations is rather simple :
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

![](ExplodedEditorView.png)

The editing view can be divided in the following parts:  
# *Global toolbar*, providing commands to: 
** browse documents [ ![](images/fill-180.png) / ![](images/fill.png) ], 
** return to campaigns & documents list [ ![](images/inbox--arrow.png) ],
** save the edited annotations [ ![](images/disk-black.png) ],
** end the annotation of the current document [ ![](images/receipt-share.png) ],
** menu to sign-out or [[Aae_UserGuide#Password-change|change password]] [ ![](images/user-silhouette.png) ]
# [[Aae_UserGuide#Editor-panel|Editor panel]], which display the document itself and commands to annotate it
# resizeable *Panel-Boundaries*, to organize the space within the browser window 
# [[Aae_UserGuide#Annotation-Table|Annotation table]], displaying annotation in a tabular format
# *Side panel*, used to display and edit Annotation properties
# *Status bar*, displaying system messages and network activity

## Editor panel

## Annotation Tool

## Termino-Ontology Extension

## Miscellaneous

