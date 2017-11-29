/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig.cdxws.db

import java.io.File
import java.util.Date
import scala.xml.Elem
import AnnotationSetType._

object JSONImporter {
  
  val DOCPROP_FILENAME = "filename";
  val DOCPROP_EXTERNALDOCID = "DocumentID";
  @deprecated
  val DOCPROP_ALVISNLPID = "AlvisNLPID";
  
  // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  case class DocumentExt(id : Long, owner : Long, contents : String, props : Structures.MultivaluedProps, description : String)
  case class AnnotationSetExt(id : Long, owner : Long,
                              text_annotations : List[TextAnnotation], groups : List[Group], relations : List[Relation],
                              head : Boolean, revision : Int, `type` : String,
                              description : String, 
                              timestamp : Option[String], created : Option[String], 
                              task_id : Option[Long] = None, unmatched: Option[Structures.AnnotationBackReferencesList])

  
  case class ImportException(message : String) extends IllegalArgumentException(message : String)
  
  def createCampaign(campaignDescription : String, jsonSchemaFileName : String) : Campaign = {
    createCampaign(campaignDescription, jsonSchemaFileName, null:String, None)
  }

  def createCampaign(campaignDescription : String, jsonSchemaFileName : String, taskDefFileName : String, guidelinesUrl : Option[String]) : Campaign = {
    val taskDefsElem  = (taskDefFileName==null) match {
      case true =>
        TaskDefinitionXMLImporter.DefaultReadOnlyTaskDefinitions 
      case false =>
        scala.xml.XML.loadFile(taskDefFileName)
    }
    createCampaign(campaignDescription, jsonSchemaFileName, taskDefsElem, guidelinesUrl)
  }
  
  def createCampaign(campaignDescription : String, jsonSchemaFileName : String, taskDefsElem : Elem, guidelinesUrl : Option[String]) : Campaign = {

    try {
      
      CadixeDB.getRootUser() match {
        case None => 
          throw new ImportException("SEVERE ERROR - Missing values for Owner! Import aborted.")
        case _ =>
      }

      //retrieve Annotation schema 
      if (jsonSchemaFileName == null || jsonSchemaFileName.trim().isEmpty()) {
        throw new ImportException("Missing annotation schema file! Import aborted.")
      } else {
        val f = new File(jsonSchemaFileName)
        if (!f.exists() || !f.canRead()) {
          throw new ImportException("Can not read annotation schema file! Import aborted.")
        }
      }
              
      val jsSchema = AnnotationSchemaHandler.retrieveSchema(jsonSchemaFileName);
      val schema = {
        import scala.util.parsing.json.JSON._
        import net.liftweb.json.NoTypeHints
        import net.liftweb.json.JsonDSL._
        import net.liftweb.json.Serialization
        import net.liftweb.json._
        import net.liftweb.json.NoTypeHints
        implicit val formats = Serialization.formats(NoTypeHints)
        compact(render(jsSchema))
      }
      if (schema.isEmpty) {
        throw new ImportException("Missing annotation schema! Import aborted.")
      }
      
      //ensure unicity of Campaign description
      CadixeDB.getCampaignByName(campaignDescription) match {
        case Some(c) =>
          throw new ImportException("This description is already used for an existing campaign! Import aborted.")
        case _ =>
      }

      import org.squeryl._
      import org.squeryl.dsl._
      import org.squeryl.PrimitiveTypeMode._
      
      transaction {
        val campaign = Campaign(campaignDescription, schema)
        CadixeDB.campaigns.insert(campaign)
        val extTasks = TaskDefinitionXMLImporter.loadTaskDefinition(campaign.id, taskDefsElem) 
        val taskDefs = TaskDefinitionXMLImporter.storeTaskDefinitions(campaign, extTasks)

        Console.err.println("New Campaign created ("+ campaign.id+ ")")
        campaign
      }

    } catch {
      case ex : ImportException =>
        Console.err.println("Import aborted :")
        ex.printStackTrace(Console.err)
        return null
    }
  }

  /**
   * @param directory the directory where the JSON files are located (any *.json file will be loaded)
   * @param campaign the campaign in which the files will be loaded
   * @param userId the user to which the AnnotationSet of type "User" will be associated
   * @param annSetTaskMapping Set of (TaskName,AnnotationSetTypeName) in order to make the correct association between the AnnotationSet type name found in the json file and the task name from the campaign
   */
  def loadFilesInCampaign(directory : String, campaign : Campaign, user : User, annSetTaskMapping : Set[(String,String)]) : List[Long] = {
    val dir = new File(directory)
    
    if (!dir.isDirectory || ! dir.canRead) {
      throw new ImportException("Invalid or unreadeable specified directory! Loading aborted.")
    }
    val jsondocs = dir.listFiles.filter( { f =>  f.getName.toLowerCase.endsWith(".json") &&  f.isFile } )

    //Add implicit mapping for HTML formatting AnnotationSet
    val completeAnnSetTaskMapping = annSetTaskMapping ++ Set((TaskDefinitionXMLImporter.RootTaskName, "HtmlAnnotation"))
    
    import org.squeryl._
    import org.squeryl.dsl._
    import org.squeryl.PrimitiveTypeMode._
    import CadixeDB._
    
    val taskByName = (from(task_definitions)((td) => where(td.campaign_id===campaign.id) select(td) ). map {
        td=> td.name -> td
      }).toMap
    
    //check that the mapping table contains valid references
    val annSet2Task = completeAnnSetTaskMapping. map  { case (taskName, annSetTypeName) =>
      
        val taskId =  taskByName.get(taskName.trim) match {
          case Some(taskDef) =>
            taskDef.id
          case _ =>
            throw new ImportException("Unknown mapped task ("+ taskName +")! Loading aborted.")
        }
        val annSetType = try {
          AnnotationSetType.withName(annSetTypeName)
        } catch {
          case ex : NoSuchElementException =>      
            throw new ImportException("Unknown mapped annotationSet type ("+ annSetTypeName +")! Loading aborted.")
        }

        (annSetType -> taskId)
    } . toMap
    
    val taskById = taskByName.values.map{ td=> td.id -> td }.toMap
    val importedDoc = jsondocs map {
      f =>
      Console.err.print("\timporting "+ f + " ...")
      val doc = processJSONFile(f, campaign, user, taskById, annSet2Task)
      Console.err.println("\tdone")
      doc
    }
    
    importedDoc.map(_.id).toList
  }
  

  
//  
  def getJsonDocument(filepath : File)  =  {
    
    val source = io.Source.fromFile(filepath)
    val jsonText = source.mkString
    source.close()

    val parsed = net.liftweb.json.JsonParser.parse(jsonText)

    parsed
  }
    
  def getDocumentExtFromFile(filepath : File)  =  {
    import net.liftweb.json._
    import net.liftweb.json.JsonDSL._
    import scala.util.parsing.json.JSON._
    implicit val formats = Serialization.formats(NoTypeHints)
  
    val parsed = getJsonDocument(filepath)
    val jsDoc = parsed \ "document"
    val docExt = jsDoc.extract[DocumentExt]
  
    docExt
  }
  
    
  def getAnnotationSetsExtFromFile(filepath : File)  =  {
    import net.liftweb.json._
    import net.liftweb.json.JsonDSL._
    import scala.util.parsing.json.JSON._
    implicit val formats = Serialization.formats(NoTypeHints)
  
    val parsed = getJsonDocument(filepath)
    val jsAnnSets = parsed \ "annotation"
    val annSets : List[AnnotationSetExt] = jsAnnSets.children.map(_.extract[AnnotationSetExt])
  
    annSets
  }  
//  
  def getDocExternalId(properties: Structures.MultivaluedProps) ={
    properties.get(DOCPROP_EXTERNALDOCID) match {
      case Some(ids) => {
          if (!ids.isEmpty) {
            Option(ids(0));
          } else {
            None;
          }
        }
      case None => None;
    }
  }
  
  def importDocument(filepath : File) : Document = {
    import org.squeryl._
    import org.squeryl.dsl._
    import org.squeryl.PrimitiveTypeMode._

    import net.liftweb.json._
    import net.liftweb.json.JsonDSL._
    import scala.collection.mutable.ListBuffer
    import scala.util.parsing.json.JSON._

    import java.sql.Timestamp
    import java.text.SimpleDateFormat    
    val inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z")

    import CadixeDB._
    import AnnotationSetType._
    
    import net.liftweb.json.Serialization.write
    
    
    //The Owner MUST NOT be an ordinary user
    val ownerId = CadixeDB.getRootUser.head.id
    

    //Import Document part
    val docExt = getDocumentExtFromFile(filepath)

    //add imported file path to the Document properties
    var properties = docExt.props
    properties.get(DOCPROP_FILENAME) match {
      case Some(path) => {
          //prepend new value of filename to the existing ones
          properties -- List(DOCPROP_FILENAME)
          properties += DOCPROP_FILENAME ->  (filepath.getAbsolutePath :: path)
        }
      case None => {
          //create new entry for filename
          properties += DOCPROP_FILENAME -> List(filepath.getAbsolutePath)
        }
    }

    // extract document's External ID
    val externalLDocId = getDocExternalId(properties)
    
    //if no description, use file name
    val docDescr = Option(docExt.description) match {
      case Some(descr) if ! descr.isEmpty => descr 
      case _ => filepath.getName()
    }
  

    //Import HTML AnnotationSet
    val annSets = getAnnotationSetsExtFromFile(filepath)

    val html_annSet = annSets.filter{ 
      annSetExt =>  AnnotationSetType.HtmlAnnotation==AnnotationSetType.withName(annSetExt.`type`) 
    }.map{ 
      //Note: HTML AnnotationSet only contain text bound annotations
      annSetExt =>  write(annSetExt.text_annotations) 
    } .headOption

    //create document record
    //FIXME: comment is unused and always empty
    val doc =  Document(ownerId, write(properties), docExt.contents, "", docDescr, externalLDocId, html_annSet)
    documents.insert(doc)

    Console.err.println("New Document created (id="+ doc.id +")")
    return doc
  }
  

  //associate document to campaign, if not done already
  def assignDocToCampaign(docId : Long, campaignId : Long) : Boolean = {
    import org.squeryl._
    import org.squeryl.dsl._
    import org.squeryl.PrimitiveTypeMode._

    import CadixeDB._
    
    val doc = from(documents)((d) => where(d.id === docId) select(d)).head
    assignDocToCampaign(doc, campaignId)
  }

  def assignDocToCampaign(doc : Document, campaignId : Long) : Boolean = {
    import org.squeryl._
    import org.squeryl.dsl._
    import org.squeryl.PrimitiveTypeMode._

    import net.liftweb.json._
    import net.liftweb.json.JsonDSL._
    import scala.collection.mutable.ListBuffer
    import scala.util.parsing.json.JSON._

    import java.sql.Timestamp
    import java.text.SimpleDateFormat    
    val inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z")

    import CadixeDB._
    
    import net.liftweb.json.Serialization.write
    implicit val formats = Serialization.formats(NoTypeHints)
    
    val ownerId = CadixeDB.getRootUser.head.id
    
    //associate document to campaign, if not done already
    if (from(campaign_documents) (
        (cd) => where(cd.campaign_id === campaignId 
                      and cd.doc_id === doc.id) select(cd)).headOption.isEmpty) {
      campaign_documents.insert(CampaignDocument(campaignId, doc.id, None))
    }
    
    //if no formatting AnnotationSet exists yet for this doc and campaign, create an one 
    //NOTE : at least one AnnotationSet must be associated to RootTask so users can start the tasks that follow.
    if (from(annotation_sets)(
        (as) => where(as.campaign_id === campaignId 
                      and as.doc_id === doc.id 
                      and as.`type`===AnnotationSetType.HtmlAnnotation) 
        select(as)).headOption.isEmpty) {
    
      val emptyAnnotationList = write(List())
      val htmlAnnotations = doc.html_annset match {
        case Some(html_as) =>  html_as
        case None => emptyAnnotationList
      }
    
      //associate HTML AnnotationSet with the workflow root Task
      val rootTaskId = getRootTask(campaignId).head.id
    
      val annSet = AnnotationSet(
        rootTaskId,
        doc.id,
        ownerId,
        campaignId,
        htmlAnnotations,
        emptyAnnotationList,
        emptyAnnotationList,
        true,
        1,
        AnnotationSetType.HtmlAnnotation,
        AnnotationSetType.HtmlAnnotation.toString,
        CadixeDB.now(), 
        Some(CadixeDB.now()),
        None)

      annotation_sets.insert(annSet)
    }    
    true
  }
  
  def assignDocToUserInCampaign(docId : Long, userId : Long, campaignId : Long) : Boolean = {              
    import org.squeryl._
    import org.squeryl.dsl._
    import org.squeryl.PrimitiveTypeMode._

    import CadixeDB._
    
    //assign document to user if not done already
    from(document_assignment)(
      (da) => where(da.campaign_id === campaignId 
                    and da.user_id === userId
                    and da.doc_id === docId) 
      select(da)).headOption match {
              
      case None =>
        CadixeDB.document_assignment.insert(DocumentAssignment(campaignId, userId, docId))
      case _ => 
    }

    //assign campaign to user if not done already
    from(campaign_annotators)(
      (ca) => where(ca.campaign_id === campaignId 
                    and ca.user_id === userId) 
      select(ca)).headOption match {
              
      case None =>
        CadixeDB.campaign_annotators.insert(CampaignAnnotator(campaignId, userId))
      case _ => 
    }
    
    true
  }

  def  importAnnotationSets(campaignId : Long, doc : Document, userMap : Map[Long, User], taskById : Map[Long, TaskDefinition], taskIdMapping : Map[Long, (Long, AnnotationSetType)], annSets : List[AnnotationSetExt], filepath : File) = {
    import org.squeryl._
    import org.squeryl.dsl._
    import org.squeryl.PrimitiveTypeMode._

    import net.liftweb.json._
    import net.liftweb.json.JsonDSL._
    import scala.collection.mutable.ListBuffer
    import scala.util.parsing.json.JSON._

    import java.sql.Timestamp
    import java.text.SimpleDateFormat    
    val inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z")

    import CadixeDB._
    
    import net.liftweb.json.Serialization.write
    implicit val formats = Serialization.formats(NoTypeHints)

    //The Owner MUST NOT be an ordinary user
    val ownerId = CadixeDB.getRootUser.head.id

    assignDocToCampaign(doc.id, campaignId)
        
    //1rst pass - insert Annotation Sets (and generate their internal id) 
    val annSetsMap = annSets
    //do no process HTML Annotation Set
    .filter{annSetExt =>  AnnotationSetType.HtmlAnnotation!=AnnotationSetType.withName(annSetExt.`type`)}
    .map { annSetExt => 
      
      taskIdMapping.get(annSetExt.id) match {
        case None => 
          Console.err.println("No mapped Task/Type provided for AnnotationSet '"+ annSetExt.id +"' in " + filepath.getAbsolutePath + "\tSkipping")
          annSetExt -> None

        case Some((taskId, annotationSetType)) => {

            userMap.get(annSetExt.owner) match {
            
              case None =>
                Console.err.println("No mapped User provided for AnnotationSet '"+ annSetExt.owner+"' in " + filepath.getAbsolutePath + "\tSkipping")
                annSetExt -> None
                
              case Some(actualUser) =>
                val isUserAnnotationSet = annotationSetType == UserAnnotation
                
                val actualUserId = if (isUserAnnotationSet) actualUser.id else ownerId
          
                //if no description, generate one
                val asDescr = Option(annSetExt.description) match {
                  case Some(descr) if descr.nonEmpty => 
                    if (isUserAnnotationSet) {
                      "[imported] " + descr
                    } else { 
                      descr 
                    }
                  case _ => { 
                      if (isUserAnnotationSet) {
                        actualUser.login + " @" + taskById.get(taskId).head.name
                    
                      } else {
                        annotationSetType.toString
                      }
                    }
                }
            
            
                val publicationDate = if (isUserAnnotationSet) { 
                  val isRootTask = taskById.get(taskId).head.precedencelevel==0
                  if (! isRootTask) {
                    //imported User's Annotation set are not marked as published
                    None 
                  } else {
                    //unless Annotation set is associated to the workflow root task; Then it is immediately marked as published
                    Some(CadixeDB.now()) 
                  }
                } else { 
                  //other imported Annotation set are immediately marked as published
                  Some(CadixeDB.now()) 
                }
            
            
             
                //check if there is a previous AnnotationSet with same characteristics
                val previousAS = from(annotation_sets)(
                  (as) => where(as.campaign_id === campaignId 
                                and as.task_id === taskId
                                and as.user_id === actualUserId
                                and as.doc_id === doc.id 
                                and as.`type`===annotationSetType
                                and as.head === true ) 
                  select(as)).headOption
            
                //imported AnnotationSet will be stored as the new head revision
                val revision = if (previousAS.nonEmpty) {
                  previousAS.head.revision + 1
                } else {
                  1
                }
            
            
                //FIXME: the actual annotations are not checked vs Annotation schema !!!
            
                //
                val creationDate = annSetExt.created match {
                  case Some(hdCreated) =>
                    new Timestamp( inputDateFormat.parse(hdCreated).getTime() )
                  case None =>
                    annSetExt.timestamp match {
                      case Some(hdTimestamp) =>
                        new Timestamp( inputDateFormat.parse(hdTimestamp).getTime() )
                      case None =>
                        new Timestamp( new Date().getTime())
                    }   
                }
            
                val annSet = AnnotationSet(
                  taskId,
                  doc.id,
                  actualUserId,
                  campaignId,
                  write(annSetExt.text_annotations),
                  write(annSetExt.groups),
                  write(annSetExt.relations),
                  true,
                  revision,
                  annotationSetType,
                  asDescr,
                  creationDate, 
                  publicationDate,
                  Some(write(annSetExt.unmatched))
                )
            
                annotation_sets.insert(annSet)
            
                //previous AnnotationSet isn't the head revision anymore
                previousAS match {
                  case Some(as) => 
                    as.head = false
                    annotation_sets.update(as)

                    Console.err.println("New revision of AnnotationSet added ["+annSet.id + "] / (t="+taskId+", d="+ doc.id+", u="+ actualUserId+", rev="+ revision +")")
                  case None =>
                    Console.err.println("New AnnotationSet created ["+annSet.id + "] / (t="+taskId+", d="+ doc.id+", u="+ actualUserId+", rev="+ revision +")")
                }  
            
                if (isUserAnnotationSet) {
                  assignDocToUserInCampaign(doc.id, actualUser.id, campaignId)
                }
            
                //store parsed external AnnotationSet and the corresponding AnnotationSet to be able to fix Annotation references
                annSetExt -> Some(annSet)
    
            }
          }
          
      }
    }
                   
    //
    // FIXME : really poor scala style! should someday get rid or var and mutable collection....
    //
    //
    var annSetIdsMap : Map[Long, Long] = Map()
  
    //List of all Annotations (id is globally unique)
    var allAnnotationIds = ListBuffer[String]()
    //List of the referenced Annotation
    var referencedAnnotations = ListBuffer[AnnotationReference]()
    //List of the back referenced Annotation
    var backReferencedAnnotations = ListBuffer[AnnotationBackReference]()

    //2nd pass - fix Annotation references by translating Annotation Set Ids
    val importedAS = annSetsMap.map( { case(annSetExt, annSet) =>
          {
            annSetExt.text_annotations.foreach(txt => {
                allAnnotationIds += txt.id 
                
                if (txt.sources. nonEmpty) for (annRef <- txt.sources.head) {
                  rewriteAnnotationBackRef(annRef, annSetExt.id, annSetIdsMap)
                  backReferencedAnnotations += annRef
                }
                
              }
            )
            annSetExt.groups.foreach(grp => {
                allAnnotationIds += grp.id
                
                if (grp.sources. nonEmpty) for (annRef <- grp.sources.head) {
                  rewriteAnnotationBackRef(annRef, annSetExt.id, annSetIdsMap)
                  backReferencedAnnotations += annRef
                }
                
                grp.group.foreach(
                  { annRef =>  {
                      rewriteAnnotationRef(annRef, annSetExt.id, annSetIdsMap)
                      referencedAnnotations += annRef
                    }
                  }
                )
              }
            )
            annSetExt.relations.foreach(rel => {
                allAnnotationIds += rel.id
                
                if (rel.sources. nonEmpty) for (annRef <- rel.sources.head) {
                  rewriteAnnotationBackRef(annRef, annSetExt.id, annSetIdsMap)
                  backReferencedAnnotations += annRef
                }
                
                rel.relation.foreach(
                  { case(role, annRef) =>  {
                        rewriteAnnotationRef(annRef, annSetExt.id, annSetIdsMap)
                        referencedAnnotations += annRef
                      }
                  }
                )
              }
            )

            annSet match {
              case Some(as) =>
                as.text_annotations = write(annSetExt.text_annotations)
                as.groups = write(annSetExt.groups)
                as.relations = write(annSetExt.relations)
                annotation_sets.update(as)
              case None =>
                
            }
            annSet
          }
      })

    //Display referenced annotation absent from the imported file
    referencedAnnotations.foreach(annRef =>  {
        if (! allAnnotationIds.contains(annRef.ann_id)) {
          Console.err.println("Warning: the referenced annotation (id="+ annRef.ann_id +") is missing from the imported file!")
        }
      })

    importedAS.filter(_.nonEmpty ).map(_.head)

  }
  
  
  @deprecated
  def processJSONFile(filepath : File , campaign : Campaign, user : User, taskById : Map[Long, TaskDefinition], annSet2Task : Map[AnnotationSetType.AnnotationSetType,Long]) = {

    import org.squeryl._
    import org.squeryl.dsl._
    import org.squeryl.PrimitiveTypeMode._

    import net.liftweb.json._
    import net.liftweb.json.JsonDSL._
    import scala.collection.mutable.ListBuffer
    import scala.util.parsing.json.JSON._

    import java.sql.Timestamp
    import java.text.SimpleDateFormat    
    val inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z")

    import CadixeDB._
    import AnnotationSetType._
    
    import net.liftweb.json.Serialization.write
    implicit val formats = Serialization.formats(NoTypeHints)

 
    //The Owner MUST NOT be an ordinary user
    val ownerId = CadixeDB.getRootUser.head.id
    
    val source = io.Source.fromFile(filepath)
    val jsonText = source.mkString
    source.close()

    val parsed = net.liftweb.json.JsonParser.parse(jsonText)

    //Import Document part
    val jsDoc = parsed \ "document"
    val docExt = jsDoc.extract[DocumentExt]

    //add imported file path to the Document properties
    var properties = docExt.props
    properties.get(DOCPROP_FILENAME) match {
      case Some(path) => {
          //prepend new value of filename to the existing ones
          properties -- List(DOCPROP_FILENAME)
          properties += DOCPROP_FILENAME ->  (filepath.getAbsolutePath :: path)
        }
      case None => {
          //create new entry for filename
          properties += DOCPROP_FILENAME -> List(filepath.getAbsolutePath)
        }
    }

    // extract AlvisNLPID
    @deprecated
    val alvisNLPId = properties.get(DOCPROP_ALVISNLPID) match {
      case Some(ids) => {
          if (!ids.isEmpty) {
            Option(ids(0));
          } else {
            None;
          }
        }
      case None => None;
    }

    //if no description, use file name
    val docDescr = Option(docExt.description) match {
      case Some(descr) if ! descr.isEmpty => descr 
      case _ => filepath.getName()
    }
  
    //create document record
    val doc =  Document(ownerId, write(properties), docExt.contents, "", docDescr)
    documents.insert(doc)
   
    //associate document to campaign with optional AlvisNLP_ID
    campaign_documents.insert(CampaignDocument(campaign.id, doc.id, alvisNLPId))


    //Import AnnotationSets
    val jsAnnSets = parsed \ "annotation"
    val annSets : List[AnnotationSetExt] = jsAnnSets.children.map(_.extract[AnnotationSetExt])

    var annSetIdsMap : Map[Long, Long] = Map()
    //1rst pass - insert Annotation Sets (and generate their internal id)
    val annSetsMap = annSets.map { annSetExt =>  

      val annotationSetType =AnnotationSetType.withName(annSetExt.`type`)
      val isUserAnnotationSet = annotationSetType == UserAnnotation
    
      //retrieve the corresponding Task to which the imported AnnoationSet type will be associated
      val taskId = annSet2Task.get(annotationSetType) match {
        case None =>
          throw new ImportException("No mapped Task provided for annotationSet type ("+ annotationSetType.toString +") in " + filepath.getAbsolutePath )
        case Some(taskId)=> 
          taskId
      }

      //if no description, generate one
      val asDescr = Option(annSetExt.description) match {
        case Some(descr) if descr.nonEmpty => 
          if (isUserAnnotationSet) {
            "[imported] " + descr
          } else { 
            descr 
          }
        case _ => { 
            if (isUserAnnotationSet) {
              user.login + " @" + taskById.get(taskId).head.name
            } else {
              annotationSetType.toString
            }
          }
      }
      
      //
      val creationDate = annSetExt.created match {
        case Some(hdCreated) =>
          new Timestamp( inputDateFormat.parse(hdCreated).getTime() )
        case None =>
          annSetExt.timestamp match {
            case Some(hdTimestamp) =>
              new Timestamp( inputDateFormat.parse(hdTimestamp).getTime() )
            case None =>
              new Timestamp( new Date().getTime())
          }   
      }
      
      val publicationDate = if (isUserAnnotationSet) { 
        val isRootTask = taskById.get(taskId).head.precedencelevel==0
        if (! isRootTask) {
          //imported User's Annotation set are not marked as published
          None 
        } else {
          //unless Annotation set is associated to the workflow root task; Then it is immediately marked as published
          Some(CadixeDB.now()) 
        }
      } else { 
        //imported Annotation set are immediately marked as published
        Some(CadixeDB.now()) 
      }
      
      val annSet = AnnotationSet(
        taskId,
        doc.id,
        if (isUserAnnotationSet) user.id else ownerId,
        campaign.id,
        write(annSetExt.text_annotations),
        write(annSetExt.groups),
        write(annSetExt.relations),
        annSetExt.head,
        annSetExt.revision,
        annotationSetType,
        asDescr,
        creationDate, 
        publicationDate,
        None
      )

      annotation_sets.insert(annSet)
      //store parsed external AnnotationSet and the corresponding AnnotationSet to be able to fix Annotation references
       
      annSetExt -> annSet
       
    }

    //if no formatting AnnotationSet is imported, create an empty one to associate with the workflow root Task
    val formattingAnnotationSet = annSetsMap.filter( _._2.`type`==AnnotationSetType.HtmlAnnotation)
    if (formattingAnnotationSet.isEmpty) {
      val annSet = AnnotationSet(
        annSet2Task.get(AnnotationSetType.HtmlAnnotation).head,
        doc.id,
        ownerId,
        campaign.id,
        write(List()),
        write(List()),
        write(List()),
        true,
        1,
        AnnotationSetType.HtmlAnnotation,
        AnnotationSetType.HtmlAnnotation.toString,
        CadixeDB.now(), 
        Some(CadixeDB.now()),
        None)

      annotation_sets.insert(annSet)
    }
    
    
    //
    // FIXME : really poor scala style! should someday get rid or var and mutable collection....
    //
    //
    
    //List of all Annotations (id is globally unique)
    var allAnnotationIds = ListBuffer[String]()
    //List of the referenced Annotation
    var referencedAnnotations = ListBuffer[AnnotationReference]()
    //List of the back referenced Annotation
    var backReferencedAnnotations = ListBuffer[AnnotationBackReference]()

    //2nd pass - fix Annotation references by translating Annotation Set Ids
    annSetsMap.foreach( { case(annSetExt, annSet) =>
          {
            annSetExt.text_annotations.foreach(txt => {
                allAnnotationIds += txt.id 
                
                if (txt.sources. nonEmpty) for (annRef <- txt.sources.head) {
                  rewriteAnnotationBackRef(annRef, annSetExt.id, annSetIdsMap)
                  backReferencedAnnotations += annRef
                }
                
              }
            )
            annSetExt.groups.foreach(grp => {
                allAnnotationIds += grp.id
                
                if (grp.sources. nonEmpty) for (annRef <- grp.sources.head) {
                  rewriteAnnotationBackRef(annRef, annSetExt.id, annSetIdsMap)
                  backReferencedAnnotations += annRef
                }
                
                grp.group.foreach(
                  { annRef =>  {
                      rewriteAnnotationRef(annRef, annSetExt.id, annSetIdsMap)
                      referencedAnnotations += annRef
                    }
                  }
                )
              }
            )
            annSetExt.relations.foreach(rel => {
                allAnnotationIds += rel.id
                
                if (rel.sources. nonEmpty) for (annRef <- rel.sources.head) {
                  rewriteAnnotationBackRef(annRef, annSetExt.id, annSetIdsMap)
                  backReferencedAnnotations += annRef
                }
                
                rel.relation.foreach(
                  { case(role, annRef) =>  {
                        rewriteAnnotationRef(annRef, annSetExt.id, annSetIdsMap)
                        referencedAnnotations += annRef
                      }
                  }
                )
              }
            )

            annSet.text_annotations = write(annSetExt.text_annotations)
            annSet.groups = write(annSetExt.groups)
            annSet.relations = write(annSetExt.relations)
            annotation_sets.update(annSet)
          }
      })

    //Display referenced annotation absent from the imported file
    referencedAnnotations.foreach(annRef =>  {
        if (! allAnnotationIds.contains(annRef.ann_id)) {
          Console.err.println("Warning: the referenced annotation (id="+ annRef.ann_id +") is missing from the imported file!")
        }
      })

    Console.err.println("New Document created (id="+ doc.id +") with "+ annSets.length + " AnnotationSet(s)")
    doc
  }
  
  def rewriteAnnotationRef(reference : AnnotationReference, oldAnnSetId :Long, annSetIdsMap : Map[Long, Long]) = {
    reference.set_id match {
      case Some(setId) => {
          //if the specified Set Id corresponds to the current AnnotationSet, then remove the value
          if (setId == oldAnnSetId) {
            reference.set_id = None
          } else {
            //the specified Set Id corresponds to another AnnotationSet, then the id must be replaced by the new internal Set Id
            annSetIdsMap.get(setId) match {
              case Some(newSetId) =>  reference.set_id = Some(newSetId)
              case _ => //happens only if the imported file contains invalid references :
                //FIXME : abort import loudly, and cancel transaction
                throw new ImportException("Unknown referenced annotationSet ("+ setId +") in Annotation reference!")
            }
          }
        }
      case _ =>
    }
    Unit
  }

  def rewriteAnnotationBackRef(reference : AnnotationBackReference, oldAnnSetId :Long, annSetIdsMap : Map[Long, Long]) = {
    reference.set_id match {
      case Some(setId) => {
          //if the specified Set Id corresponds to the current AnnotationSet, then remove the value
          if (setId == oldAnnSetId) {
            reference.set_id = None
          } else {
            //the specified Set Id corresponds to another AnnotationSet, then the id must be replaced by the new internal Set Id
            annSetIdsMap.get(setId) match {
              case Some(newSetId) =>  reference.set_id = Some(newSetId)
              case _ => //happens only if the imported file contains invalid references :
                //FIXME : abort import loudly, and cancel transaction
                Console.err.println("Warning: Unknown backReferenced annotationSet ("+ setId +") in Annotation backreference!")
            }
          }
        }
      case _ =>
    }
    Unit
  }
 
  def assignDocsToUser(campaign_id : Long, assignees : Set[Long], documentIds : List[Long]  )  {

    import org.squeryl._
    import org.squeryl.dsl._
    import org.squeryl.PrimitiveTypeMode._
    import CadixeDB._
    
    assignees.foreach(
      user_id => {
        //Associate user to campaign if not done already
        from(campaign_annotators)((ca) => where(ca.campaign_id===campaign_id and ca.user_id===user_id) select(ca)).headOption match {
          case None =>
            CadixeDB.campaign_annotators.insert(CampaignAnnotator(campaign_id,user_id))
          case _ =>
        }

        documentIds.foreach(
          doc_id => {
            CadixeDB.document_assignment.insert(DocumentAssignment(campaign_id, user_id, doc_id))
          })
      }
    )
  }
  

}
