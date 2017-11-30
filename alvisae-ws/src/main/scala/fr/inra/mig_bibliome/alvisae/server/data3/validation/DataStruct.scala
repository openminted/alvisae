/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig_bibliome.alvisae.server.data3.validation

import scala.reflect.BeanProperty

import fr.inra.mig.cdxws.db
import fr.inra.mig.cdxws.db.AnnotationSchemaHandler


import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationReference
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationSchemaDefinition
import fr.inra.mig_bibliome.alvisae.shared.data3.TextBinding
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.AnnotationGroupDefinition
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.AnnotationTypeDefinition
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropType_ClosedDomain
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropType_FreeText
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropType_TyDIConceptRef
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropType_TyDISemClassRef
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropType_TyDITermRef
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropertiesDefinition
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropertyDefinition
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.PropertyType
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.RelationDefinition
import fr.inra.mig_bibliome.alvisae.shared.data3.validation.TextBindingDefinition
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationBackReference
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationGroup
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationKind
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotatedText
import fr.inra.mig_bibliome.alvisae.shared.data3.Annotation
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationSchemaDefinition
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationSet
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationSetCore
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationSetInfo
import fr.inra.mig_bibliome.alvisae.shared.data3.AnnotationSetType
import fr.inra.mig_bibliome.alvisae.shared.data3.Document
import fr.inra.mig_bibliome.alvisae.shared.data3.Fragment
import fr.inra.mig_bibliome.alvisae.shared.data3.Properties
import fr.inra.mig_bibliome.alvisae.shared.data3.Relation
import fr.inra.mig_bibliome.alvisae.shared.data3.SourceAnnotations
import fr.inra.mig_bibliome.alvisae.shared.data3.TaskDefinition
import fr.inra.mig_bibliome.alvisae.shared.data3.TaskVisibility

/**
 * shallow implementations of the core AlvisAE data structures to allow server-side validation
 */
object DataStruct {

  class PropType_ClosedDomainImpl(val valType : AnnotationSchemaHandler.PropertyValType) extends PropType_ClosedDomain {
    import collection.JavaConversions._
    def getDomainValues = valType.domain.getOrElse(null)
    def getDefaultValue = valType.defaultVal.getOrElse(null)
  }

  class PropType_TyDITermRefImpl(val valType : AnnotationSchemaHandler.PropertyValType) extends PropType_TyDITermRef {
    def getTermRefBaseURL = valType.TyDIRefBaseURL.getOrElse(null)
  }

  class PropType_TyDISemClassRefImpl(val valType : AnnotationSchemaHandler.PropertyValType) extends PropType_TyDISemClassRef {
    def getSemClassRefBaseURL = valType.TyDIRefBaseURL.getOrElse(null)
  }

  class PropType_TyDIConceptRefImpl(val valType : AnnotationSchemaHandler.PropertyValType) extends PropType_TyDIConceptRef {
    def getSemClassRefBaseURL = valType.TyDIRefBaseURL.getOrElse(null)
  }

  class PropertyTypeImpl(val valType : AnnotationSchemaHandler.PropertyValType) extends PropertyType {
    def getTypeName = valType.valTypeName
    def accept(value : String ) = true
    def getAsFreeTextType() : PropType_FreeText =  {
      if (PropType_FreeText.NAME.equals(getTypeName())) {
        this.asInstanceOf[PropType_FreeText]
      } else {
        null
      }
    }
    def getAsClosedDomainType() : PropType_ClosedDomain =  {
      if (PropType_ClosedDomain.NAME.equals(getTypeName())) {
        new PropType_ClosedDomainImpl(valType)
      } else {
        null
      }
    }
    def getAsTyDITermRefType() : PropType_TyDITermRef = {
      if (PropType_TyDITermRef.NAME.equals(getTypeName())) {
        new PropType_TyDITermRefImpl(valType)
      } else {
        null
      }
    }
    def getAsTyDISemClassRefType() : PropType_TyDISemClassRef = {
      if (PropType_TyDISemClassRef.NAME.equals(getTypeName())) {
        new PropType_TyDISemClassRefImpl(valType)
      } else {
        null
      }
    }
    def getAsTyDIConceptRefType() : PropType_TyDIConceptRef = {
      if (PropType_TyDIConceptRef.NAME.equals(getTypeName())) {
        new PropType_TyDIConceptRefImpl(valType)
      } else {
        null
      }
    }
    
    def isTyDIResourceRef() = {
      (   PropType_TyDITermRef.NAME.equals(getTypeName()) 
       || PropType_TyDISemClassRef.NAME.equals(getTypeName()) 
       || PropType_TyDIConceptRef.NAME.equals(getTypeName()))
    }
      
  }

  class PropertyDefinitionImpl(val propDef : AnnotationSchemaHandler.PropertyDefinition) extends PropertyDefinition {
    def getKey = propDef.key

    def isMandatory = propDef.mandatory
    def getMinValues =  propDef.minVal
    def getMaxValues =  propDef.maxVal

    def getValuesType = new PropertyTypeImpl(propDef.valType)
  }

  class PropertiesDefinitionImpl (val propsDef : AnnotationSchemaHandler.PropertiesDefinition) extends PropertiesDefinition {

    def isPropertyKeySupported(key : String) = propsDef.keySet.contains(key)

    def getPropertyDefinitions : java.util.Collection[PropertyDefinition] = {
      import collection.JavaConversions._
      propsDef.values.map { new PropertyDefinitionImpl(_)}.toList
    }
  }

  class TextBindingDefinitionImpl(val textBinding : AnnotationSchemaHandler.TextBinding) extends TextBindingDefinition {
    def getMinFragments = textBinding.minFrag
    def getMaxFragments = textBinding.maxFrag
    def getBoundariesReferenceType = textBinding.boundRef match { case Some(boundRefType) if boundRefType.trim.nonEmpty => boundRefType case _ => null }
    def isCrossingAllowed = textBinding.crossingAllowed
  }

  class AnnotationGroupDefinitionImpl(val groupDefinition : AnnotationSchemaHandler.GroupDefinition) extends AnnotationGroupDefinition {
    def getMinComponents = groupDefinition.minComp
    def getMaxComponents = groupDefinition.maxComp
    def isHomogeneous = groupDefinition.homogeneous

    import collection.JavaConversions._
    def getComponentsTypes = groupDefinition.compType
  }

  class RelationDefinitionImpl(val relationDefinition : AnnotationSchemaHandler.RelationDefinition)  extends RelationDefinition {

    import collection.JavaConversions._
    def getSupportedRoles() = relationDefinition.map{ argDef => argDef.keySet }.flatten.toList

    def getArgumentTypes(role : String ) = {
      relationDefinition.filter{ argDef => argDef.keySet.contains(role) }.flatten.map{ _._2 }.flatten.toList
    }
  }

  class AnnotationTypeDefinitionImpl(val annotationType : AnnotationSchemaHandler.AnnotationType) extends AnnotationTypeDefinition {

    def getType() = annotationType.`type`

    def getAnnotationKind() = AnnotationKind.withId(annotationType.kind)

    def getColor() = annotationType.color

    def getUrl() = annotationType.url.getOrElse(null)

    def getPropertiesDefinition() = annotationType.propDef match {
      case Some(propDef) =>  new PropertiesDefinitionImpl(propDef)
      case _ => new PropertiesDefinitionImpl(AnnotationSchemaHandler.EmptyPropertiesDefinition)
    }

    def getTextBindingDefinition : TextBindingDefinition = new TextBindingDefinitionImpl(annotationType.txtBindingDef.getOrElse(null))
    def getAnnotationGroupDefinition : AnnotationGroupDefinition = new AnnotationGroupDefinitionImpl(annotationType.groupDef.getOrElse(null))
    def getRelationDefinition : RelationDefinition = new RelationDefinitionImpl(annotationType.relationDef.getOrElse(null))
  }

  class AnnotationSchemaDefinitionImpl(val schemaDef : AnnotationSchemaHandler.SchemaDefinition) extends AnnotationSchemaDefinition {


    def getAnnotationTypes : java.util.Collection[String] = {
      import collection.JavaConversions._
      schemaDef.keySet.toList
    }

    def getAnnotationTypes(kind : AnnotationKind) : java.util.List[String] = {
      import collection.JavaConversions._
      schemaDef.filter{ case (typeName, annotationType) =>  annotationType.kind.equals(kind) }.map{ case (typeName, annotationType) => typeName }.toList
    }

    def getAnnotationTypeDefinition(annotationTypeName : String) : AnnotationTypeDefinition = {
      schemaDef.get(annotationTypeName) match {
        case Some(annotationType) => new AnnotationTypeDefinitionImpl(annotationType)
        case _ => null
      }
    }

    def getAnnotationTypeDefinition(kind : AnnotationKind) : java.util.List[AnnotationTypeDefinition] = {
      import collection.JavaConversions._
      schemaDef.values.filter{ annotationType => annotationType.kind.equals(kind) }.map{ new AnnotationTypeDefinitionImpl(_)}.toList
    }


    def getTyDIResourceReferencingTypes : java.util.Map[String, java.util.Map[String, AnnotationSchemaDefinition.TypeUrlEntry]] = {
      import collection.JavaConversions._
      null
      throw new UnsupportedOperationException("Not supported yet.")
    }

  }

  class FragmentImpl(@BeanProperty var start : Int, @BeanProperty var end : Int)  extends Fragment {
    def isSame(other : Fragment) : Boolean = {
      other!=null && getStart()==other.getStart() && getEnd()==other.getEnd();
    }
  }

  class PropertiesImpl(val props : db.Structures.MultivaluedProps)  extends Properties {

    import collection.JavaConversions._
    def getKeys = props.keySet

    def hasKey(key : String) = props.keySet.contains(key)

    def getValues(key : String) = props.get(key)  match { 
      case Some(values) => values
      case _ => null 
    }
    
    def addValue(key : String, value : String) = {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    def removeKey(key : String) = {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    def removeValue(key : String, value : String) = {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    def removeValue(key : String, index : Int) = {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    def replaceValue(key : String, index : Int, value : String) = {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    def replaceAll(properties : Properties ) = {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }


  class TextBindingImpl(val fragList : db.Structures.FragmentsList) extends TextBinding  {

    import collection.JavaConversions._
    def getFragments = fragList.map { frag => new FragmentImpl(frag(0),frag(1)) }
    
    def getSortedFragments = getFragments

    def addFragment(fragment : Fragment )  = {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    def removeFragment(fragment : Fragment )  = {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    def addFragments(fragments : java.util.Collection[Fragment] )  = {
      throw new UnsupportedOperationException("Not supported yet.");
    }
    def removeFragments(fragments : java.util.Collection[Fragment] )  = {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    def setFragments(fragments : java.util.Collection[Fragment] )  = {
      throw new UnsupportedOperationException("Not supported yet.");
    }

  }

  class AnnotationReferenceImpl(val annRef : db.AnnotationReference)  extends AnnotationReference {

    def getAnnotationId = annRef.ann_id

    def getAnnotationSetId = annRef.set_id.head.toInt
  }

  def convAnnRefsList(annRefs : List[db.AnnotationReference]) : java.util.List[AnnotationReference]= {
    import collection.JavaConversions._
    annRefs.map { new AnnotationReferenceImpl(_) }
  }

  def convAnnRefsList(annRefsList :  Option[db.Structures.AnnotationReferencesList]) : java.util.List[AnnotationReference] = {
    annRefsList match {
      case Some(annRefs) =>
        import collection.JavaConversions._
        annRefs.map { new AnnotationReferenceImpl(_) }

      case None =>
        import collection.JavaConversions._
        List()
    }
  }



  class AnnotationGroupImpl(val group : List[db.AnnotationReference]) extends AnnotationGroup {

    import collection.JavaConversions._
    def getComponentRefs = convAnnRefsList(group)

    def addComponent(component :AnnotationReference) = {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    def removeComponent(componentId : String) = {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    def removeComponent(component :AnnotationReference) = {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    def addComponents(component :java.util.Collection[AnnotationReference]) = {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    def removeComponents(component :java.util.Collection[AnnotationReference]) = {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }

  class RelationImpl(val relation : Map[String, db.AnnotationReference]) extends Relation {

    import collection.JavaConversions._

    def getRoles = relation.keys

    def getRolesArguments = {
      relation.map{ case (role, annRef) => (role, new AnnotationReferenceImpl(annRef))}
    }

    def getArgumentRef(role: String) = relation.get(role) match {
      case Some(annRef) =>  new AnnotationReferenceImpl(annRef)
      case None => null
    }

    def setArgument(role : String, argument : AnnotationReference, overwrite : Boolean ) = {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    def getRoles(argument : AnnotationReference) = {
      relation.filter{ case (role, annRef) => annRef.equals(argument) }.map{ case (role, annRef) => role}
    }

    def removeArgument(role : String) = {
      throw new UnsupportedOperationException("Not supported yet.");
    }

  }

  
  class AnnotationBackReferenceImpl()  extends AnnotationBackReference {

    def getAnnotationId = null

    def getAnnotationSetId = null
    
    def getConsolidationStatus = null;
  }
  
  

  class TXTAnnotationImpl(val annotatedText : AnnotatedText, val annotation : db.TextAnnotation)  extends Annotation {

    def getId = annotation.id
    def getAnnotatedText = annotatedText
    def getAnnotationKind = AnnotationKind.TEXT
    def getAnnotationType = annotation.`type`
    def getCreationDate = annotation.created match { case Some(timestamp) => timestamp.toInt; case None  => 0; }
    def getProperties = new PropertiesImpl(annotation.properties)
    def setProperties(props : Properties) = null

    def getAnnotationText(fragmentSeparator : String ) = {
      null
    }

    def getSourceAnnotations = null;
    def setSourceAnnotations(annBackReferences : java.util.List[AnnotationBackReference] ) = null

    def getTextBinding = new TextBindingImpl(annotation.text)
    def getAnnotationGroup = null
    def getRelation = null
  }

  class GRPAnnotationImpl(val annotatedText : AnnotatedText, val annotation : db.Group)  extends Annotation {

    def getId = annotation.id
    def getAnnotatedText = annotatedText
    def getAnnotationKind = AnnotationKind.GROUP
    def getAnnotationType = annotation.`type`
    def getCreationDate = annotation.created match { case Some(timestamp) => timestamp.toInt; case None  => 0; }
    def getProperties = new PropertiesImpl(annotation.properties)
    def setProperties(props : Properties) = null

    def getAnnotationText(fragmentSeparator : String ) = {
      null
    }

    def getSourceAnnotations = null;
    def setSourceAnnotations(annBackReferences : java.util.List[AnnotationBackReference] ) = null

    def getTextBinding = null
    def getAnnotationGroup = new AnnotationGroupImpl(annotation.group)
    def getRelation = null
  }


  class RELAnnotationImpl(val annotatedText : AnnotatedText, val annotation : db.Relation)  extends Annotation {

    def getId = annotation.id
    def getAnnotatedText = annotatedText
    def getAnnotationKind = AnnotationKind.RELATION
    def getAnnotationType = annotation.`type`
    def getCreationDate = annotation.created match { case Some(timestamp) => timestamp.toInt; case None  => 0; }
    def getProperties = new PropertiesImpl(annotation.properties)
    def setProperties(props : Properties) = null

    def getAnnotationText(fragmentSeparator : String ) = {
      null
    }

    def getSourceAnnotations = null;
    def setSourceAnnotations(annBackReferences : java.util.List[AnnotationBackReference] ) = null

    def getTextBinding = null
    def getAnnotationGroup = null
    def getRelation = new RelationImpl(annotation.relation)
  }


  def createAnnotationImpl(annotatedText : AnnotatedText, annotation : Object) : Annotation = {
    annotation match {
      case annotation:db.TextAnnotation  => new TXTAnnotationImpl(annotatedText, annotation)
      case annotation:db.Group  => new GRPAnnotationImpl(annotatedText, annotation)
      case annotation:db.Relation  => new RELAnnotationImpl(annotatedText, annotation)
      case _ => null
    }
  }


  class  DocumentImpl(val doc : db.Document) extends Document {

    def getId = doc.id.toInt
    def getContents = doc.contents

    def setContents( contents : String) = {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    def getDescription = doc.description
    def getOwner = doc.owner.toInt

    def getProperties  = {
      import net.liftweb.json._
      import net.liftweb.json.JsonDSL._
      import net.liftweb.json.NoTypeHints
      implicit val formats = Serialization.formats(NoTypeHints)
      new PropertiesImpl(parse(doc.props).extract[db.Structures.MultivaluedProps])
    }

    def getFragmentsText( fragments : java.util.List[Fragment]) = {
      throw new UnsupportedOperationException("Not supported yet.");
    }

  }

  class AnnotationSetCoreImpl(val annotatedText : AnnotatedText, val as : db.AnnotationSet) extends AnnotationSetCore {

    def getId = as.id.toInt
    def getTaskId = as.task_id.toInt
    def getType = AnnotationSetType.withId(as.`type`.id)
    def getCreationDate = db.CadixeDB.dateToString(as.created)
    def getPublicationDate = db.CadixeDB.dateToString(as.published)
    def getDescription = as.description
    def getRevision = as.revision
    def isHead = as.head
    def getOwner = as.user_id.toInt

    def isInvalidated  = {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }

  class AnnotationSetImpl(annotatedText : AnnotatedText, as : db.AnnotationSet) extends AnnotationSetCoreImpl(annotatedText, as) with AnnotationSet with AnnotationSetInfo  {

    def getTextAnnotationsList : List[TXTAnnotationImpl] = as.get_textannotations.map {annotation => new TXTAnnotationImpl(annotatedText, annotation) }
    def getGroupsList : List[GRPAnnotationImpl] = as.get_groups.map {annotation => new GRPAnnotationImpl(annotatedText, annotation) }
    def getRelationsList : List[RELAnnotationImpl]= as.get_relations.map {annotation => new RELAnnotationImpl(annotatedText, annotation) }

    def getAnnotationsList : List[Annotation] = {
      getTextAnnotationsList ++ getGroupsList ++ getRelationsList
    }
    import collection.JavaConversions._

    def getTextAnnotations = getTextAnnotationsList
    def getGroups = getGroupsList
    def getRelations = getRelationsList

    def getJSON() = {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    def getCSV() = {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    def getNbTextAnnotations = as.get_textannotations.size
    def getNbGroups = as.get_groups.size
    def getNbRelations = as.get_relations.size
    
    def getUnmatchedSourceAnnotations : SourceAnnotations = null
    
    def setUnmatchedSourceAnnotations(annBackReferences : java.util.List[AnnotationBackReference] ) = null

  }

  class TaskDefinitionImpl(val taskdef : db.TaskDefinition) extends TaskDefinition {


    def getId = taskdef.id.toInt

    def getName = taskdef.name

    def getCardinality = taskdef.cardinality

    def getVisibility = TaskVisibility.withId(taskdef.visibility.id)

    def getEditedAnnotationTypes = {
      import net.liftweb.json._
      import net.liftweb.json.JsonDSL._
      import net.liftweb.json.NoTypeHints
      implicit val formats = Serialization.formats(NoTypeHints)
      import collection.JavaConversions._
      parse(taskdef.annotationtypes).extract[Set[String]]
    }

    def getPrecedencelevel = taskdef.precedencelevel

    def getTaskPrecedencies = {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    def getReviewedTask = {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    def getSucceededTasks = {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    def getReferencedAnnotationTypeTasks = {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }

  class AnnotatedTextImpl(val annotationSets : List[db.AnnotationSet], val user : db.User, val campaign : db.Campaign, val doc : db.Document, val taskdef : db.TaskDefinition) extends AnnotatedText {
    
    lazy val schemaDef : AnnotationSchemaHandler.SchemaDefinition = AnnotationSchemaHandler.getSchemaFromJsonStr(campaign.schema)
    lazy val annotationsById = { getAnnotationSets.map { 
        as => as.getAnnotationsList.map {
          ann => (ann.getId -> ann)
        } 
      }.flatten.toMap
    } 

      
    def getDocument = new DocumentImpl(doc)

    def getAnnotationSets : List[AnnotationSetImpl] = {
      annotationSets.map{ as  => new AnnotationSetImpl(this, as) }
    }

    import collection.JavaConversions._
    def getAnnotationSetList : java.util.List[AnnotationSet] = getAnnotationSets

    def getAnnotationSetInfoList : java.util.List[AnnotationSetInfo] = {
      import collection.JavaConversions._
      annotationSets.map{ as  => new AnnotationSetImpl(this, as) }
    }

    def getAnnotationSchema = new AnnotationSchemaDefinitionImpl(schemaDef)

    def getEditedTask = {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    def getOutdatedAnnotationSetIds = {
      throw new UnsupportedOperationException("Not supported yet.");
    }
    
    def scanAnnotations(processor : AnnotatedText.AnnotationProcessor) : Unit = {
      getAnnotationSets.foreach { as =>
        as.getAnnotationsList.foreach {
          annotation => if (! processor.process(as, annotation)) {
            return Unit
          }
        }
      }
    }

    def getJSON : String = {
      throw new UnsupportedOperationException("Not supported yet.");
    }


    def getAnnotations = {
      import collection.JavaConversions._
      (getAnnotationSets.map { as => as.getAnnotationsList }.flatten).toList
    }
    

    def getAnnotations(annotationType : String ) = {
      (getAnnotationSets.map { 
          as => as.getAnnotationsList.filter{
            ann => ann.getAnnotationType.equals(annotationType) 
          } }.flatten).toList
    }

    def getAnnotations(kind : AnnotationKind)  = {
      (getAnnotationSets.map { 
          as => as.getAnnotationsList.filter{
            ann => ann.getAnnotationKind.equals(kind) 
          } }.flatten).toList
    }

    def getAnnotation(annotationId : String) = annotationsById.get(annotationId).getOrElse(null)

  }
} 