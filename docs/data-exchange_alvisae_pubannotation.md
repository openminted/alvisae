# Data-Exchange solution between AlvisAE and PubAnnotation

The idea is to make it interoperable AlvisAE and PubAnnotation by implementing a solution to import documents/projects from PubAnnotation and store annotated document to PubAnnotation. Manual annotations are precious, they produced with domain experts during laborious annotation campaign. Our motivation is to ease creation and sharing of annotations produced on AlvisAE with PubAnnotation.

## Prototype
The prototype will enable to
* import documents and projects available on PubAnnotation
* export annotated documents to pubAnnotation
The end users will use an interface to access contents from PubAnnotation. They will also be able to share annotated documents to PubAnnotation.

## Result during Blah4
We have done the following tasks
* study the data representations of PubAnnotation according to AlvisAE and have done mappings between the relevent contents
* implement REST requests to get PubAnnotation compatible document from AlvisAE and set pubAnnotation compatible document to AlvisAE
