# Annotation & Annotation Sets

* the _Annotations_ related to a document are organized in _Annotation Sets_. An Annotation belongs to a single Annotation Set
* Annotation Sets are distinguished by their _type_, which usually identify to the processing or the person that produced the corresponding Annotations.
> For instance, an Annotation Set of the automatically recognized NE, and one Annotation Set for each expert that manually annotates the document.

* Annotation Sets can be modified and saved independantly from each others. The unit of data storage (grain) is the Annotation Set.
When the new state of an Annotation Set is saved, a new _revision_ of this Annotation Set is created (hence, the new revision is given a new id).

* The most recent revision of an Annotation Set is the "head" revision. There is only one head revision per type, except for the User's Annotation Set for which there is an head revision for each user.
Only the head revision is modifiable; 

* the Annotation Editor can display several Annotation Sets above the text. For consitency purposes, only a single revision of each Annotation Set can be displayed. 
By default, the head revisions will be displayed, unless another revision is explicitely referenced (see below).

* It is possible to get the chronologicaly ordered list of Annotation Set revisions, since each revision is tagged by its date of creation; 
Annotations keep their ids amongst the Annotation Set revisions; So it is possible to follow Annotation modifications through time.

* A user can only modify the head revision of his/her own Annotation Set.

* Since Group and Relation can reference other Annotations, some specific rules apply to enforce consistency :

  * when a referenced Annotation belongs to the user's Annotation Set, it is an implicit reference to the Annotation within the corresponding Annotation set +revision+. It is not possible to reference Annotation of another revision of the user's Annotation Set

  * when a referenced Annotation belongs to an Annotation Set different from the user's Annotation Set, the corresponding Annotation Set revision id is explicitely recorded along with the Annotation Id.

  * Following references to this specific Annotation Set will be restricted to the same Annotation Set +revision+.

  * If groups or relations from an Annotation Set _A_ references annotations from another Annotation Set _B_, then all references must be in the same revision of _B_. If _B_ is modified after these references are created, the user that edits _A_ will not be able to see the changes. The referenced revision of _B_ is said _in conflict_ with all following revisions of _B_.

  * It is forbidden for an Annotation Set to reference two conflicting revisions of an Annotation Set. This ban applies for the transitive closure of references. Example:

       _B(1)_ references _A(1)_
       _A_ is edited, therefore _A(2)_ is in conflict with _A(1)_
       _C(1)_ references _A(2)_, therefore it is forbidden to reference _B(1)_




