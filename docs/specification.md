# Annotations

An annotation is an enrichment of the document text.

## Annotation type

Each annotation has a single type.
The Annotation type is identified by its name (string).

## Annotation properties

* Each annotation has a set of properties.
* Properties are a key/values mapping.
* Keys are strings.
* Values are consistent lists of anything: boolean, string, number, list, etc.

## Annotation kind

An annotation can be of one of three kinds:

### Text Annotations

Text annotations are bound to the document text:
* a text annotation may have 1 to several fragments
* each fragment has a start and end position
* positions are zero-based indexes of unicode characters (not bytes)
* the start position is the index of the first character of the fragment
* the end position is the index of the character following the last character of the fragment (end - start = length)

### Group Annotations

Groups are lists of annotations. Component annotations may be of any kind.

### Relation Annotations

Relations are lists of key/value pairs. Keys, or _roles_, are strings, values, or _arguments_,  are Annotations of any kind.

# Schema

A _schema_ is a specification of the annotation for a campaig ([schema BNF definition](https://github.com/openminted/alvisae/edit/master/docs/annotation_schema_bnf_definition.md)).
A schema must list all allowed annotation types and for each type:
* the kind of the annotations of this type: all annotations of a given type have the same kind
* the list of allowed properties keys, and for each key:
** either the property is mandatory
** the domain of the property XXX

For text annotation types, the schema must also specify:
* the number of fragments (1 for continuous annotations)

For groups, the schema must also specify:
* the allowed types for component annotations
* either all components should be of the same type
* the cardinality range

For relations, the schema must also specify:
* the list of role
* the allowed types for each argument

## Type dependency

For a give schema, a type _Ta_ is said to depend on type _Tb_ if:
* _Ta_ has kind group, and the allowed component types include _Tb_, *or*
* _Ta_ has kind relation, and the allowed type for one of the arguments include _Tb_

## Static checking of a schema

1. if _Ta_ depends on _Tb_, then _Tb_ must be specified in the schema
2. no circular dependencies (transitive closure)

# Tasks and Workflow

A _Task_ is a unit step in the annotation process. A _Workflow_ defines all tasks for a campaign (see "dtd":https://migale.jouy.inra.fr/redmine/projects/cdxws/repository/entry/trunk/lift-prototype/alvisae-workflow.dtd). For each task, the workflow must define:
* the name of the task
* the cardinality of the task: how many concurrent annotations for each document are required 
* the visibility of the task:
	* restricted: annotation sets of this task are only visible to its owner, and, when it is published, to users that perform a task that depends on it
	* protected: annotation sets of this task are only visible to its owner, and to users that perform parallel annotation on the same document and task (for cardinality greater than 1)
	* public: annotation sets of this task are only visible to its owner, and, when it is published, to any user
* the types of the annotations that are edited (created / modified / deleted) by the task
* in case the task includes the review of another task, the name of the reviewed task
* in case the task must be performed after another task, the name of the preceding task

## Task dependency

For a given schema and a given workflow, a task _Ca_ is said to depend on task _Cb_ if:
1. _Ca_ is a review of _Cb_, *or*
2. _Ca_ must be performed after _Cb_, *or*
3. one of the annotation types in _Ca_ depend on one of the annotation types in _Cb_ *and* that _Cb_ is not reviewed by another task


## Static checking of a workflow

1. if _Ca_ depends on _Cb_, then _Cb_ must be specified as a task in the workflow
2. all annotation types edited in a task must be specified in the schema
3. all annotation types in the schema must be edited at least in one task in the workflow
4. if a task has a cardinality greater than 1, then it must be reviewed by another task
5. a task can be reviewed by at most a single task
6. if an annotation type occurs in two tasks, then one must be the review of the other (transitive closure) _QUESTION: either two tasks can edit the same annotation type, this would alternative workflows for each document_
7. if a task _Ca_ is the review of _Cb_, then all types edited in _Cb_ must be edited in _Ca_
8. no circular dependencies (transitive closure)

## Notes

No task can depend on a task with cardinality > 1. Annotations in Annotation Sets of tasks that will be reviewed cannot be referenced. Annotations in Annotation Sets of tasks with cardinality > 1 cannot be referenced.

## Examples

### Biotopes

<pre>
task: preannotation
    users: AlvisNLP
    cardinality: 1
    types: Bacteria, Host
    review-of: none

task: annotation
    users: RB, JJ, CN, WG, ZR
    cardinality: 2
    types: all
    review-of: preannotation

task: conciliation
    users: RB, JJ, CN, WG, ZR
    cardinality: 1
    types: all
    review-of: annotation
</pre>

### Genic interactions

<pre>
task: annotation
    users: PV
    cardinality: 1
    types: all
    review-of: none

task: verification
    users: RB, PB
    cardinality: 1
    types: all
    review-of: annotation
</pre>

# Annotation Sets

The _annotation set_ is the unit of storage. An annotation set has:
* a document
* a task
* an user

Annotation sets are under version control: an annotation set revision represents the state of advancement of an user for a given task and document. The most recent revision of an annotation set is the annotation set _head_.

Each annotation pertains to a single annotation set.

## Annotation Set dependency

An annotation set _Sa_ is said to depend on another annotation set _Sb_ if:
* _Sa_ and _Sb_ have the same document, *and*
* the task of _Sa_ depends on the task of _Sb_

## Annotation references

An annotation reference is either a group component or a relation argument. The group or relation is called the _referent_, and the compound or argument is called _refered_.
The reference contains:
1. the identifier of the refered
2. the identifier of the annotation set to which the refered pertains
3. the revision number of this annotation set visible by the user when the referent was created

(2) and (3) are optional iff the referent and the referred pertain to the same annotation set.

This means that annotation references are revision-specific. If the annotation set of the refered is modified after the reference was created, then the referent is still referencing the old revision.

For all referent in an annotation set, a single revision of a given annotation set can be referenced.

## Notification of obsolescence

If an annotation set has a new revision, then:
* the user must be notified when opening and saving an annotation set that depends on it directly (no transitive closure)
* the user must be able to require an update: the client operates a diff between the annotation set revision referenced and the head, it also gives the user the means to update the annotation references, or update the annotation copies in the case of a review dependency, in his own annotation set.

## Starting review task annotation set

The initial version of a review task must be done with a diff of all concurrent revised annotation sets. The client provides the means to the reviewer to create a new annotation set by chosing between concurrent annotation sets. If the cardinality of the reviewed task is 1, then the initial version of the review is a copy of the reviewed annotation set. The annotations in the review annotation set must keep an annotation reference to the copied annotation.


