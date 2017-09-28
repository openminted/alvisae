/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 */
package fr.inra.mig.cdxws.api

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.sql.Timestamp
import java.text.SimpleDateFormat

import net.liftweb.common.{Box,Empty,Failure,Full,Logger,EmptyBox}
import net.liftweb.http._
import net.liftweb.http.auth.{AuthRole,userRoles}
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import net.liftweb.json.Serialization.write
import net.liftweb.util.BasicTypesHelpers._

import org.squeryl.PrimitiveTypeMode._
import fr.inra.mig.cdxws.db.CadixeDB.UsersAutorizations
import fr.inra.mig.cdxws.db._
import fr.inra.mig.cdxws.db.AnnotationSetType._

object RestAPI {
  implicit val formats = Serialization.formats(NoTypeHints)

  def user_json(user : User) = {
    transaction {
      ("id" -> user.id) ~
      ("login" -> user.login) ~
      ("campaigns" -> (user.annotates_in map { c => ("id" -> c.id) ~ ("name" -> c.name) ~ ("guidelines" -> c.guidelinesUrl) }))
    }
  }

  def userProps_json(user : User) = {
    ("properties" -> parse(user.props))
  }

  def user_withauths_json(user : User) = {
    transaction {
      val userCampaigns = user.annotates_in
      val userCampaignIds = userCampaigns map { c => c.id }
      //return info for only campaigns for which the user has at least one authorization
      val userCampaignAuths = from(CadixeDB.user_campaignauthorizations)((uca) =>
        where(uca.user_id === user.id and uca.campaign_id.in { userCampaignIds })
        select(uca)
        orderBy(uca.campaign_id asc, uca.auth_id asc)).toList
      val userByCampaignAuths = userCampaignAuths groupBy (_.campaign_id) map { case (k,v) => k -> (v map (_.auth_id) ) }

      ("id" -> user.id) ~
      ("login" -> user.login) ~
      ("is_admin" -> user.is_admin) ~
      ("is_active" -> user.is_active) ~
      ("campaigns" -> (userCampaigns map { c => ("id" -> c.id) ~ ("name" -> c.name) ~ ("guidelines" -> c.guidelinesUrl) }))  ~
      ("authorizations" ->
       ("global" -> (user.has_authorizations map { a => a.id })) ~
       ("bycampaign" -> (userByCampaignAuths map { case (campaign_id, auth_ids) => (campaign_id.toString -> auth_ids) }))
      )
    }
  }

  def user_allCampaignAuths_json  (user : User) = {
    val allCampaignAuths = CadixeDB.getUserAuthsForAllCampaigns(user.id)

    val userByCampaignAuths = allCampaignAuths groupBy (_._1.id) map { case (k,v) =>
        k -> (v  map ( _._2 match { case Some(uca) => Some(uca.auth_id) case None => None }) ).flatten }

    ("global" -> (user.has_authorizations map { a => a.id })) ~
    ("bycampaign" -> (userByCampaignAuths map { case (campaign_id, auth_ids) => (campaign_id.toString -> auth_ids) }))
  }

  def json_document_list(docs : List[Document]) = {
    transaction {
      ("documents" -> (docs.map { d =>
            ("id" -> d.id) ~
            ("description" -> d.description)  }))
    }
  }

  def user_campaign_json(user : User, campaign : Campaign) = {
    transaction {
      json_document_list(CadixeDB.documents_by_user_and_campaign(user,campaign).toList)
    }
  }

  def campaign_documents_json(campaign : Campaign) = {
    transaction {
      val doc_da = CadixeDB.documents_by_campaign(campaign).toList .groupBy{ _._1 }.map { case (d, ldda) => (d -> ldda.map{ case (d, da) => da }.flatten )}
      (doc_da.map { case (d, das) =>
            ("id" -> d.id) ~
            ("description" -> d.description) ~
            ("user_ids" ->
             (das.map { da => (da.user_id) } ))
        })
    }
  }

  def campaign_annotators_json() = {
    transaction {
      val annotators_by_campaign = CadixeDB.annotators_by_campaign().toList.groupBy(_._1.id).map { case (k,v) => k -> (v.map {_._2} ) }

      annotators_by_campaign.map { case(campaign_id, annotators) =>
          ("campaign_id" -> campaign_id) ~  ("user_ids" ->
                                             (annotators.flatten.map { ca => (ca.user_id) } ))
      }
    }
  }


  def json_of_annotation_set(as : AnnotationSet, invalidatedAnnSetIds : Option[Set[Long]]) = {
    val invalidated = invalidatedAnnSetIds match {
      case Some(invASids) =>
        invASids.contains(as.id)
      case None =>
        CadixeDB.invalidatedAnnotationSet(as.campaign_id, as.user_id, Some(as.id)).headOption.nonEmpty
    }

    val json = 
      ("id" -> as.id) ~
    ("task_id" -> as.task_id) ~
    ("owner" -> as.user_id) ~
    ("type" -> as.`type`.toString) ~
    ("created" -> CadixeDB.dateToString(as.created)) ~
    ("published" -> CadixeDB.dateToString(as.published)) ~
    ("revision" -> as.revision) ~
    ("description" -> as.description) ~
    ("head" -> as.head) ~
    ("text_annotations" -> parse(as.text_annotations)) ~
    ("groups" -> parse(as.groups)) ~
    ("relations" -> parse(as.relations)) ~
    ("invalidated" -> invalidated)
    as.unmatched match { 
      case Some(backRefs) => json ~ ("unmatched" ->  parse(backRefs))
      case None => json
    } 
  }

  def json_summary_of_annotation_set(as : AnnotationSet, invalidatedAnnSetIds : Set[Long]) = {
    ("id" -> as.id) ~
    ("task_id" -> as.task_id) ~
    ("owner" -> as.user_id) ~
    ("type" -> as.`type`.toString) ~
    ("created" -> CadixeDB.dateToString(as.created)) ~
    ("published" -> CadixeDB.dateToString(as.published)) ~
    ("revision" -> as.revision) ~
    ("description" -> as.description) ~
    ("head" -> as.head) ~
    ("text_annotations" -> parse(as.text_annotations).extract[List[TextAnnotation]].length) ~
    ("groups" -> parse(as.groups).extract[List[Group]].length) ~
    ("relations" -> parse(as.relations).extract[List[Relation]].length) ~
    ("invalidated" -> invalidatedAnnSetIds.contains(as.id))
  }

  def retrieveOrcreateUserAnnotationSet(user : User, campaign : Campaign, doc : Document, taskdef : TaskDefinition) = {
    transaction {
      CadixeDB.annotation_sets.where(as => as.doc_id === doc.id and as.user_id === user.id and as.task_id === taskdef.id
                                     and as.campaign_id === campaign.id and as.head === true and as.`type` === UserAnnotation).headOption match {
        case None => {
            val description = user.login + " @" + taskdef.name
            //if no prexisting User's Annotation Set, then create it now
            val as = AnnotationSet(taskdef.id, doc.id,user.id,campaign.id,
                                   write(Nil),write(Nil),write(Nil),
                                   true,0,UserAnnotation, description, CadixeDB.now(), None, None)
            CadixeDB.annotation_sets.insert(as)
            //FIXME remove this useless call (since the AnnotationSet has just been create, it empty thus is not referencing anything)
            CadixeDB.storeAnnotationSetDependencies(as)
            (as, true)
          }
        case Some(as) =>
          (as, false)
      }
    }
  }

  def user_campaign_document_data(as : Option[AnnotationSet], user : User, campaign : Campaign, doc : Document, taskdef : TaskDefinition, forcedAnnotationSetTypes : Set[AnnotationSetType] = Set()) : (List[AnnotationSet], List[AnnotationSet], String) = {
    transaction {
      //1- Automatically loaded AnnotationSet based on their type
      //Formatting AnnotationSet is optional, but if it exists, it must always be sent to client
      //Force loading of AlvisNLP AnnotationSet if it exists
      val otherAnnotationSetTypes = forcedAnnotationSetTypes.union(Set(HtmlAnnotation, AlvisNLPAnnotation))
      val otherAnnotationSets = otherAnnotationSetTypes.foldLeft(List() : List[AnnotationSet])( (accu, asType) => {
          val otherAnnSet =
            CadixeDB.annotation_sets.where(as => as.doc_id === doc.id and as.campaign_id === campaign.id
                                           and as.head === true and as.`type` === asType).toList
          otherAnnSet ++ accu
        })


      //2- Automatically loaded AnnotationSet because they are explicitely referenced by the current AnnotationSet
      //Retrieve referenced AnnotationSets (since Task visibility can not change, referenced AnnotationSet conform to Task visibility parameter)
      val referencedAnnotationSets = as match {
        case Some(as) =>
          CadixeDB.annotationSetLookup(as.id :: Nil).toList
        case None =>
          List()
      }

      val completeAnnSets = otherAnnotationSets ++ referencedAnnotationSets

      //FIXME : Why lookup on the campaign already retrieved?
      val camp = CadixeDB.campaigns.lookup(campaign.id).get

      //3- Other Annotation Sets that may be displayed, they will be loaded on-demand
      val otherVisibleAnnSets = CadixeDB.getPrecedingVisibleAnnotationSets(campaign.id, doc.id, taskdef).toList

      //Since only one single revision of an AnnotationSet per Task can be loaded at a time,
      //so head revision present in visible AnnotationSets list should be removed if there is already a corresponding referenced outdated one,
      val fetchedUsersAnnSet = referencedAnnotationSets.filter{ as => as.`type`.equals(UserAnnotation) }.map{ as => (as.task_id, as.user_id) }.toSet
      val filteredOtherVisibleAnnSets = otherVisibleAnnSets.filter{ as => !fetchedUsersAnnSet.contains((as.task_id, as.user_id)) }
      
      val summarizedAnnSets = (filteredOtherVisibleAnnSets ++ completeAnnSets).distinct


      (completeAnnSets, summarizedAnnSets, camp.schema)
    }
  }

  def user_campaign_document_json(as : Option[AnnotationSet], user : User, campaign : Campaign, doc : Document, taskdef : TaskDefinition, forcedAnnotationSetTypes : Set[AnnotationSetType] = Set()) = {

    val (completeAnnSets, summarizedAnnSets, schema) = user_campaign_document_data(as, user, campaign, doc, taskdef, forcedAnnotationSetTypes)

    val invalidatedByOutdated = CadixeDB.getInvalidatedAnnotationSet(campaign.id, user.id);
    val invalidatedAnnSetIds = invalidatedByOutdated.keys.map{_.id}.toSet
    val outdatedAnnSetIds = invalidatedByOutdated.values.flatten.map{_.id}.toSet
    
    json_document(doc, schema, Some(taskdef), completeAnnSets, summarizedAnnSets, invalidatedAnnSetIds, outdatedAnnSetIds)
  }

  def json_document(doc : Document, schema : String, taskdef : Option[TaskDefinition], completeAnnSets : List[AnnotationSet], summarizedAnnSets : List[AnnotationSet], invalidatedAnnSetIds : Set[Long], outdatedAnnSetIds : Set[Long] = Set()) = {
    val jsCompleteAnnSets = completeAnnSets.map(as => json_of_annotation_set(as, Some(invalidatedAnnSetIds)))
    
    val jsonDoc = ("document" -> (
        ("id" -> doc.id) ~
        ("contents" -> doc.contents) ~
        ("description" -> doc.description) ~
        ("props" -> parse(doc.props)) ~
        ("owner" -> doc.owner))) ~
    ("annotation" -> jsCompleteAnnSets) ~
    ("annotation_sets" -> {
        summarizedAnnSets.map { json_summary_of_annotation_set(_, invalidatedAnnSetIds) }
      }) ~
    ("schema" -> parse(schema)) ~
    ("outdated" -> outdatedAnnSetIds)
    
    taskdef match {
      case Some(taskdef) =>  
        jsonDoc ~
        ("edited_task" -> json_of_task_definition(taskdef))
      case None =>  
        jsonDoc
    }
  }

  //WARN: this portion of code has not been covered by test!!!
  def getDocument(user_id : Long, campaign_id : Long, doc_id : Long, forcedAnnotationSetTypes : Set[AnnotationSetType] = Set()) : Option[JObject] = {
    transaction {
      import CadixeDB._

      //No task id is specified => retrieve doc in readonly mode for the RootTask of the workflow

      // Check that specified user is assigned to specified doc within specified campaign
      from(campaigns,users,documents,document_assignment,task_definitions)((c,u,d,da,td) =>
        where(da.user_id === user_id and da.campaign_id === campaign_id and da.doc_id === doc_id and
              u.id === user_id and c.id === campaign_id and d.id === doc_id and td.name===TaskDefinitionXMLImporter.RootTaskName and td.campaign_id===campaign_id)
        select(c,u,d,td)).headOption match {
        case Some((campaign,user,doc,taskdef)) =>
          Some(user_campaign_document_json(None, user, campaign, doc, taskdef, forcedAnnotationSetTypes))

        case None =>
          None
      }
    }
  }

  def getDocumentDataForTask(user_id : Long, campaign_id : Long, doc_id : Long, task_id : Long, forcedAnnotationSetTypes : Set[AnnotationSetType] = Set()) = {
    transaction {
      import CadixeDB._

      // Check that specified user is assigned to specified doc within specified campaign
      from(campaigns,users,documents,document_assignment,task_definitions)((c,u,d,da,td) =>
        where(da.user_id === user_id and da.campaign_id === campaign_id and da.doc_id === doc_id and
              u.id === user_id and c.id === campaign_id and d.id === doc_id and td.id===task_id and td.campaign_id===campaign_id)
        select(c,u,d,td)).headOption match {
        case Some((campaign,user,doc,taskdef)) =>
          val (userAnnSet, justCreated) = retrieveOrcreateUserAnnotationSet(user, campaign, doc, taskdef)

          //Note 1: In order to conform with the cardinality constraint,
          //        when the document is requested for the first time by a user
          //        for a specific Task, a new empty AnnotationSet will be created.
          //

          // no need to check cardinality for unbounded cardinality Task
          if (justCreated && taskdef.cardinality != -1) {

            //Total number of task instance must not exceed cardinality
            val taskInstances = from(annotation_sets)(as =>
              where(    as.task_id === task_id
                    and as.campaign_id === campaign_id
                    and as.doc_id === doc_id
                    and as.head === true
              )
              select(as)
            )

            if (taskInstances.Count > taskdef.cardinality) {
              //It is assumed here that the current process has caused the cardinality violation;
              //Thus, the newly created AnnotationSet will be removed
              transaction {
                annotation_sets.delete(userAnnSet.id)
              }

              throw new ConflictException("This task could could not be started because of cardinality constraint (" + taskdef.cardinality +  ")")
            }
          }

          Some(userAnnSet, user, campaign, doc, taskdef)
        case None =>
          None
      }
    }
  }

  def getDocumentForTask(user_id : Long, campaign_id : Long, doc_id : Long, task_id : Long, forcedAnnotationSetTypes : Set[AnnotationSetType] = Set()) = {
    transaction {
      import CadixeDB._
      getDocumentDataForTask(user_id, campaign_id, doc_id, task_id, forcedAnnotationSetTypes) match {
        case Some(docData) =>
          val (userAnnSet, user, campaign, doc, taskdef) = docData
          Some(user_campaign_document_json(Some(userAnnSet), user, campaign, doc, taskdef, forcedAnnotationSetTypes))
        case None =>
          None
      }
    }
  }


  def json_of_task_definition(td : TaskDefinition) = {
    ("id" -> td.id) ~
    ("name" -> td.name) ~
    ("cardinality" -> td.cardinality) ~
    ("edittypes" -> td.annotationtypes) ~
    ("precedencelevel" -> td.precedencelevel) ~
    ("visibility" -> td.visibility.toString) ~
    ("precedencies" -> (td.has_directprecedencies map { dep =>  ("task_id" -> dep.predecessor_id) ~  ("reviewing" -> dep.reviewing_dep) ~  ("succeeding" -> dep.succeeding_dep)~  ("typereferencing" -> dep.typereferencing_dep) }))
  }

  case class TaskInstance(
    val task_id : Long,
    val doc_id : Long,
    val user_id : Long,
    var status : TaskStatus.TaskStatus,
    val readonly : Boolean = false,
    val annset_id : Option[Long] = None,
    val created : Option[Timestamp] = None,
    val published : Option[Timestamp] = None,
    var invalidated : Option[Boolean] = None
  )

  def json_task_instance(taskInst : TaskInstance) = {
    val jsonCommon =
      ("task_id" -> taskInst.task_id) ~
    ("doc_id" -> taskInst.doc_id) ~
    ("user_id" -> taskInst.user_id) ~
    ("status" -> taskInst.status.toString)

    val jsonInstance =
      ("readonly" -> taskInst.readonly) ~
    ("invalidated" -> taskInst.invalidated) ~
    ("annset_id" -> taskInst.annset_id) ~
    ("created" -> CadixeDB.dateToString(taskInst.created)) ~
    ("published" -> CadixeDB.dateToString(taskInst.published))

    jsonCommon ~ jsonInstance
  }

  def getTaskInstancesList(user_id : Long, campaign_id : Long, task_id : Option[Long] = None) = {
    import CadixeDB._

    //all possible task on the documents assigned to the current user
    val allTasks = from(document_assignment, task_definitions)((da, td) =>
      where(da.user_id === user_id
            and da.campaign_id === campaign_id
            and td.campaign_id === da.campaign_id      
            and td.id === task_id.?
      )
      select(da, td)
    )

    //all existing instance and Task that could be potentially instanciated
    val allTaskInstances = join(allTasks, annotation_sets.leftOuter){
      case ((da, td), as) =>
        (select(da, td, as)
         orderBy(td.precedencelevel, td.id, da.doc_id, as.map(_.user_id))
         on(td.id === as.map(_.task_id)
            and td.campaign_id === as.map(_.campaign_id)
            and da.doc_id === as.map(_.doc_id)
            and as.map(_.head) === Some(true)))
    }

    //group by task+doc
    val possibleTaskInstance = allTaskInstances.groupBy{ case (docAssign,task, as) =>
        (task.id, task.precedencelevel==0, task.cardinality, docAssign.doc_id)
    }

    val taskInstances = possibleTaskInstance.map { case ((task_id, isRootTask, cardinality, doc_id),datdas) =>

        val anyUserAnnSet = datdas.filter { case (da, td, as) => as.nonEmpty }.map { case (da, td, as) => as.head }

        //Compute task status
        val taskInst = if (anyUserAnnSet.isEmpty) {
          //there is no AnnotationSet at all, and since the minimum cardinality for any task is 1, the current user can start it
          TaskInstance(task_id, doc_id, user_id, TaskStatus.ToDo)

        } else {

          //filter out the AnnotationSet of other users
          val userAnnSet = anyUserAnnSet.filter { as => as.user_id==user_id  }
          if (userAnnSet.isEmpty) {

            //the current user can start this task only if cardinality has not been already reached
            if ((anyUserAnnSet.size < cardinality) || cardinality == -1) {
              TaskInstance(task_id, doc_id, user_id, TaskStatus.ToDo)
            } else {
              TaskInstance(task_id, doc_id, user_id, TaskStatus.Unavailable_Done)
            }

          } else {
            val annSet = userAnnSet.head
            //there is already an AnnotationSet for the user
            val status = if (annSet.published.nonEmpty) {
              TaskStatus.Done
            } else {
              TaskStatus.Pending
            }

            TaskInstance(task_id, doc_id, user_id, status, isRootTask,  Some(annSet.id), Some(annSet.created), annSet.published, None)
          }
        }
        taskInst
    }

    //retrieve task dependencies
    val uniqTasks = allTaskInstances.map{ case (docAssign,task, as) =>  task }.toSet
    val precedindTasks = uniqTasks .map {task => task.id -> task.has_directprecedencies.map{ _.predecessor_id } }.toMap

    //keep the Tasks whose successors or reviewer can be started (i.e. the task already done by any user)
    val finishedTask = taskInstances. map { taskInst =>
      (taskInst.task_id, taskInst.doc_id) -> (TaskStatus.Done.equals(taskInst.status) || TaskStatus.Unavailable_Done.equals(taskInst.status))
    }.toMap
    
    val invalidated = (CadixeDB.getInvalidatedAnnotationSet(campaign_id, user_id)).keys.map{_.id}.toSet
     
    //update task status and Annotation set invalidated status
    for (taskInst <- taskInstances) {
      if (TaskStatus.ToDo.equals(taskInst.status)) {
        //if not all preceding tasks are done, then the task cannot be started yet : change status to Upcoming
        val canNotStartYet = (precedindTasks.get(taskInst.task_id).filter{
            precedindIds =>
            val atLeastOneUnfinished = (precedindIds.filter{
                precedindId =>
                val isNotFinished = finishedTask.get(precedindId, taskInst.doc_id) match {
                  case Some(isFinished) if isFinished => false
                  case _ => true
                    //FIXME : if one unfinished task is found, no need to look at the following : break here!
                }
                isNotFinished
              }).nonEmpty
            atLeastOneUnfinished
          }).nonEmpty

        if (canNotStartYet) {
          taskInst.status = TaskStatus.Upcoming
        }
      }

      if (taskInst.annset_id.nonEmpty && invalidated.contains(taskInst.annset_id.head)) {
        taskInst.invalidated = Some(true)
      }
    }

    val taskStatusToKeep = Set(TaskStatus.Pending, TaskStatus.ToDo, TaskStatus.Done)
    taskInstances.filter(taskInst => taskStatusToKeep.contains(taskInst.status) )
  }
// ---------------------------------------------------------------------------
  def performValidation(as : AnnotationSet, user_id : Long, campaign_id : Long, doc_id : Long, task_id : Long) {
    import fr.inra.mig_bibliome.alvisae.shared.data3.validation.BasicFaultListener
    import fr.inra.mig_bibliome.alvisae.shared.data3.validation.BasicAnnotationSchemaValidator
    import fr.inra.mig_bibliome.alvisae.server.data3.validation.ServerFaultMessages
    import fr.inra.mig_bibliome.alvisae.server.data3.validation.DataStruct.AnnotatedTextImpl

    //
    getDocumentDataForTask(user_id, campaign_id, doc_id, task_id) match {
      case Some(docData) =>
        val (userAnnSet, user, campaign, doc, taskdef) = docData

        val referencedAnnotationSets = CadixeDB.annotationSetLookup(as.id :: Nil).toList
        val allAnnotationSets = referencedAnnotationSets :+ userAnnSet

        val faultLstnr = new BasicFaultListener(new ServerFaultMessages())
        val document = new AnnotatedTextImpl(allAnnotationSets, user, campaign, doc, taskdef)
        BasicAnnotationSchemaValidator.validateAnnotatedText(document, faultLstnr);

        if (!faultLstnr.getMessages().isEmpty) {
          import collection.JavaConversions._
          throw new UnprocessableException("Validation resulted in errors:\n" + faultLstnr.getMessages().toList.mkString("\n"))
        }
      case None =>
        //should not happen
        throw new IllegalArgumentException("Unable to load document to be validated!")
    }
  }

  def performValidation(user_id : Long, campaign_id : Long, doc_id : Long, task_id : Long) {
    transaction{
      CadixeDB.getTaskInstanceHead(user_id, campaign_id, doc_id, task_id)  match {
        case Some(as) =>
          performValidation(as, user_id, campaign_id, doc_id, task_id)
        case None =>
          Console.err.println("No user's AnnotationSet to be validated!")
      }
    }
  }

// ---------------------------------------------------------------------------

  case class UnprocessableException(message : String) extends IllegalArgumentException(message : String)

  case class UnprocessableResponse(message : String) extends LiftResponse with HeaderDefaults {
    def toResponse = InMemoryResponse(message.getBytes("UTF-8"), "Content-Type" -> "text/plain; charset=utf-8" :: headers, cookies, 422)
  }


  case class ConflictException(message : String) extends IllegalArgumentException(message : String)

  case class ConflictResponse(message : String) extends LiftResponse with HeaderDefaults {
    def toResponse = InMemoryResponse(message.getBytes("UTF-8"), "Content-Type" -> "text/plain; charset=utf-8" :: headers, cookies, 409)
  }

// ---------------------------------------------------------------------------
  object user extends RequestVar[Option[User]](None)

  def dispatch : LiftRules.DispatchPF = {

    //Retrieve AlvisAE webService version
    case Req("api" :: "version" :: Nil,_,GetRequest) => {

        () => Full(JsonResponse( "version" -> 2.1) )
      }

      //List Authorizations
    case Req("api" :: "authorizations" :: Nil,_,GetRequest) => {
        user.is match {
          case Some(user) =>
            user.is_admin match {
              //deny listing authorizations to non-admin
              case false =>
                () =>  Full(ResponseWithReason(ForbiddenResponse(), "Only an admin can perform this operation!"))
              case _ =>
                transaction {
                  val scopes = S.param("scopes") match {
                    case Full(scopeLst) if (scopeLst.nonEmpty) =>
                      scopeLst.split(",").toList
                    case _ =>
                      List()
                  }
                  val auths = from(CadixeDB.authorizations)((a) =>
                    where (a.scope.in(scopes).inhibitWhen(scopes.isEmpty))
                    select(a)
                    orderBy(a.id asc)
                  )
                  val jsonResponse = auths map { a =>
                    ("auth_id" -> a.id) ~
                    ("scope" -> a.scope) ~
                    ("description" -> a.description) ~
                    ("campaignrelated" -> a.campaignrelated)
                  }
                  () => Full(JsonResponse(("authorizations" -> jsonResponse)))
                }
            }
          case None =>
            () => Full(BadResponse())
        }
      }

      //List Campaigns
    case Req("api" :: "campaigns" :: Nil,_,GetRequest) => {
        user.is match {
          case Some(user) =>
            user.is_admin match {
              //deny listing campaigns to non-admin
              case false =>
                () =>  Full(ResponseWithReason(ForbiddenResponse(), "Only an admin can perform this operation!"))
              case _ =>
                transaction {
                  val campaigns = from(CadixeDB.campaigns)((c) =>select(c) orderBy(c.id asc))
                  val jsonResponse = campaigns map { c => ("id" -> c.id) ~ ("name" -> c.name) }
                  () => Full(JsonResponse(("campaigns" -> jsonResponse)))
                }
            }
          case None =>
            () => Full(BadResponse())
        }
      }

      //List users registered in AlvisAE
    case Req("api" :: "user" :: Nil,_,GetRequest) => {
        user.is match {
          case Some(user) =>
            user.is_admin match {
              //deny listing users to non-admin
              case false =>
                () =>  Full(ResponseWithReason(ForbiddenResponse(), "Only an admin can perform this operation!"))
              case _ =>
                val withAuthorizations = (S.param("wzauths") ?~ "true").map(toBoolean)==true
                transaction {
                  val users = from(CadixeDB.users)((u) =>select(u) orderBy(u.login asc))
                  val jsonResponse = withAuthorizations match {
                    case true =>
                      users map { u => user_withauths_json(u)}
                    case _ =>
                      users map { u => user_json(u)}
                  }
                  () => Full(JsonResponse(jsonResponse))
                }
            }
          case None =>
            () => Full(BadResponse())
        }
      }

      //Retrieve info about current user
    case Req("api" :: "user" :: "me" :: Nil,_,GetRequest) => {
        user.is match {
          case Some(user) =>
            val withAuthorizations = (S.param("wzauths") ?~ "true").map(toBoolean)==true
            withAuthorizations match {
              case true =>
                () => Full(JsonResponse(user_withauths_json(user)))
              case _ =>
                () => Full(JsonResponse(user_json(user)))
            }
          case None =>
            () => Full(BadResponse())
        }
      }

      //Retrieve Authorization info about another user
    case Req("api" :: "user" :: AsLong(user_id) :: "authorizations" :: Nil,_,GetRequest) => {
        user.is match {
          case Some(user) =>
            user.is_admin match {
              //deny retrieving Authorization info about other users to non-admin
              case false =>
                () =>  Full(ResponseWithReason(ForbiddenResponse(), "Only an admin can perform this operation!"))
              case _ =>
                () => transaction { CadixeDB.users.where(u => u.id === user_id).headOption  match {
                    case None =>
                      Full(ResponseWithReason(NotFoundResponse(), "No User identified by id " + user_id+ " could be found"))
                    case Some(user) =>
                      Full(JsonResponse(user_allCampaignAuths_json(user)))
                  }
                }
            }
          case None =>
            () => Full(BadResponse())
        }
      }

      //Reset Authorizations info of an user
    case req @ Req("api" :: "user" :: AsLong(user_id) :: "authorizations" :: Nil,_,PutRequest) => {
        user.is match {
          case Some(user) =>
            user.is_admin match {
              //deny updating user's Authorizations to non-admin
              case false =>
                () =>  Full(ResponseWithReason(ForbiddenResponse(), "Only an admin can perform this operation!"))
              case _ =>
                () => transaction {
                  CadixeDB.users.where(u => u.id === user_id).headOption  match {
                    case None =>
                      Full(ResponseWithReason(NotFoundResponse(), "No User identified by id " + user_id+ " could be found"))
                    case Some(user) =>

                      jsonBody(req) match {
                        case Full(json) =>
                          try {
                            val auths = json.extract[UsersAutorizations]
                            CadixeDB.setUserAuthsForCampaigns(user_id, auths)

                            Full(JsonResponse(user_allCampaignAuths_json(user)))
                          } catch { case ex : MappingException =>
                              Full(ResponseWithReason(BadResponse(), "Invalid user's Authorizations.\n" + ex.getMessage))
                          }
                        case _ =>
                          Full(ResponseWithReason(BadResponse(), "No or Invalid user's Authorizations"))
                      }
                  }
                }
            }
          case None =>
            () => Full(BadResponse())
        }
      }

      //Create a new user
    case Req("api" :: "user" :: Nil,_,PostRequest) => {
        user.is match {
          case Some(user) =>
            user.is_admin match {
              //deny user creation to non-admin
              case false =>
                () =>  Full(ResponseWithReason(ForbiddenResponse(), "Only an admin can perform this operation!"))
              case _ =>
                () => for(login <- S.param("login").map(_.toString) ?~ "missing login parameter" ~> 400;
                          password <- S.param("passwd").map(_.toString) ?~ "missing passwd parameter" ~> 400;
                          is_admin <- S.param("is_admin").map(_.toBoolean) ?~ "missing is_admin parameter" ~> 400)
                            yield {

                    // val remoteIp = S.containerRequest.map(_.remoteAddress).openOr("localhost")
                    val is_active = S.param("is_active").map(_.toBoolean).openOr(true)

                    transaction {
                      CadixeDB.getUserByLogin(login) match {
                        case Some(previousUser) =>
                          ConflictResponse("Can not create new user because the login name '" + login+ "' is already used")
                        case _ =>
                          val newUser = CadixeDB.addUser(login, password, is_admin, is_active)
                          JsonResponse(user_withauths_json(newUser))
                      }
                    }
                  }
            }
          case None =>
            () => Full(BadResponse())
        }
      }

      //Update an existing user (except password)
    case Req("api" :: "user" :: AsLong(user_id) :: Nil,_,PutRequest) => {
        user.is match {
          case Some(user) =>
            user.is_admin match {
              //deny changing password of other users to non-admin
              case false if user_id != user.id =>
                () =>  Full(ResponseWithReason(ForbiddenResponse(), "Only an admin can perform this operation!"))
              case _ =>
                () => for(login <- S.param("login").map(_.toString) ?~ "missing login parameter" ~> 400;
                          is_admin <- S.param("is_admin").map(_.toBoolean) ?~ "missing is_admin parameter" ~> 400)
                            yield {
                    transaction {
                      CadixeDB.getUserById(user_id) match {
                        case None =>
                          ResponseWithReason(NotFoundResponse(), "No User identified by id " + user_id+ " could be found")
                        case _ =>
                          CadixeDB.getUserByLogin(login) match {
                            case Some(previousUser) if (previousUser.id!=user_id) =>
                              ConflictResponse("Can not apply modification because the login name '" + login+ "' is already used")
                            case _ =>
                              val is_active = S.param("is_active").map(_.toBoolean)
                              import CadixeDB._
                              CadixeDB.users.update(user => {
                                  val w = where(user.id === user_id)
                                  //update is_active column if some value has been provided
                                  is_active match {
                                    case Full(active) =>
                                      w.set(user.login := login,
                                            user.is_admin := is_admin,
                                            user.is_active := active)
                                    case _ =>
                                      w.set(user.login := login,
                                            user.is_admin := is_admin)
                                  }
                                }
                              )
                              val updated = CadixeDB.getUserById(user_id) match { case Some(u) => u ; case _ => null }
                              JsonResponse(user_withauths_json(updated))
                          }
                      }
                    }
                  }
            }
          case None =>
            () => Full(BadResponse())
        }
      }

      //Change user password
      //Note : we are using here the POST method to avoid sending the new password as an URL parameter
    case Req("api" :: "user" :: AsLong(user_id) :: "chpasswd" :: Nil,_,PostRequest) => {
        user.is match {
          case Some(user) =>
            () => for(newPasswd <- S.param("passwd").map(_.toString) ?~ "missing passwd parameter" ~> 400)
              yield {

                user.is_admin match {
                  //deny changing password of other users to non-admin
                  case false if user_id != user.id =>
                    ResponseWithReason(ForbiddenResponse(), "Only an admin can perform this operation!")
                  case _ =>
                    newPasswd.isEmpty match {
                      case true =>
                        ResponseWithReason(BadResponse(), "New password must not be empty")
                      case _ =>
                        transaction {
                          val getUserToChange = (user.id == user_id) match {
                            case true =>
                              Some(user)
                            case _ =>
                              import CadixeDB._
                              val otherUser = from(users)((u) => where(u.id === user_id) select(u)).headOption
                              otherUser
                          }
                          getUserToChange match {
                            case Some(userToChange) =>
                              CadixeDB.changeUserPassword(userToChange, newPasswd)
                              //Console.err.println("Password successfully changed")
                              JsonResponse(user_json(user))
                            case None =>
                              ResponseWithReason(BadResponse(), "Specified user not found")
                          }
                        }
                    }
                }
              }
          case None =>
            () => Full(BadResponse())
        }
      }

      //Retrieve all properties associated to a specific user
    case Req("api" :: "user" :: AsLong(user_id) :: "props" :: Nil,_,GetRequest) => {
        user.is match {
          case Some(user) =>
            () =>
            user.is_admin match {
              case false if user_id != user.id =>
                Full(ResponseWithReason(ForbiddenResponse(), "Only an admin can perform this operation!"))
              case _ =>
                transaction {
                  CadixeDB.users.where(u => u.id === user_id).headOption  match {
                    case None =>
                      Full(ResponseWithReason(NotFoundResponse(), "No User identified by id " + user_id+ " could be found"))
                    case Some(target_user) =>
                      Full(JsonResponse(userProps_json(target_user)))
                  }
                }
            }
          case None =>
            () => Full(BadResponse())
        }
      }

      //Replace all properties of a specific user by the data provided
    case req @ Req("api" :: "user" :: AsLong(user_id) :: "props" :: Nil,_,PutRequest) => {
        user.is match {
          case Some(user) =>
            () =>
            user.is_admin match {
              case false if user_id != user.id =>
                Full(ResponseWithReason(ForbiddenResponse(), "Only an admin can perform this operation!"))
              case _ =>
                jsonBody(req) match {
                  case Full(json) => transaction {
                      CadixeDB.users.where(u => u.id === user_id).headOption  match {
                        case None =>
                          Full(ResponseWithReason(NotFoundResponse(), "No User identified by id " + user_id+ " could be found"))
                        case Some(target_user) =>
                          try {
                            CadixeDB.updateUserProps(target_user, json)
                            Full(JsonResponse(userProps_json(target_user)))
                          } catch {
                            case ex : MappingException =>
                              Full(ResponseWithReason(BadResponse(), "Invalid Properties Map in query"))
                          }
                      }
                    }
                  case _ =>
                    Full(ResponseWithReason(BadResponse(), "Missing Properties Map in query"))
                }
            }
          case None =>
            () => Full(BadResponse())
        }
      }

      //--------------------------------------------------------------------------

      //Retrieve the workflow (=Task definitions) associated to the specified campaign
    case Req("api" :: "campaigns" :: AsLong(campaign_id) :: "workflow" :: Nil,_,GetRequest) => {
        user.is match {
          case Some(user) =>
            () => Full({
                transaction {
                  val campaign = if (user.is_admin) {
                    CadixeDB.getCampaignById(campaign_id)
                  } else {
                    user.annotates_in(campaign_id)
                  }

                  campaign match {
                    case Some(campaign) =>
                      val jsondefs = ("schema" -> parse(campaign.schema)) ~
                      ("taskdefinitions" -> campaign.task_definitions.toList
                       //do not show root task (a.k.a. AlvisAE-preprocessing) to the user
                       .filter( td => td.precedencelevel != 0 )
                       .sortWith( (a, b) => {(a.id < b.id)}).map { td => json_of_task_definition(td)} )
                      JsonResponse(jsondefs)
                    case None =>
                      ResponseWithReason(ForbiddenResponse(), "Only admin or users taking part in this campaign can perform this operation!")
                  }
                }
              })
          case None =>
            () => Full(BadResponse())
        }
      }

      //Retrieve the list of all documents associated to the specified campaign
    case Req("api" :: "campaigns" :: AsLong(campaign_id) :: "documents" :: Nil,_,GetRequest) => {
        user.is match {
          case Some(user) if (user.is_admin) =>
            () => Full({
                transaction {
                  val campaign = CadixeDB.getCampaignById(campaign_id)

                  campaign match {
                    case Some(campaign) =>
                      JsonResponse(campaign_documents_json(campaign))

                    case None =>
                      ResponseWithReason(NotFoundResponse(), "Specified campaign no found")
                  }
                }
              })
          case _ =>
            () => Full(ResponseWithReason(ForbiddenResponse(), "Only admin can perform this operation!"))
        }
      }

      //Retrieve, for each campaign, the list of participating users
    case Req("api" :: "campaigns" :: "annotators" :: Nil,_,GetRequest) => {
        user.is match {
          case Some(user) if (user.is_admin) =>
            () => Full({
                JsonResponse(campaign_annotators_json())
              })
          case _ =>
            () => Full(ResponseWithReason(ForbiddenResponse(), "Only admin can perform this operation!"))
        }
      }

      //Update the set of users participating to the specified campaign
    case Req("api" :: "campaigns" :: AsLong(campaign_id) :: "annotators" :: Nil,_, PutRequest) => {
        user.is match {
          case Some(user) if (user.is_admin) =>
            () => Full({
                try {
                  val (userToAdd, userToDel) = try {
                    ( S.param("add").dmap(Array[Long]())(s => if (s.nonEmpty) { s.split(",").map(_.toLong) } else { Array[Long]() }).toList ,
                     S.param("del").dmap(Array[Long]())(s => if (s.nonEmpty) { s.split(",").map(_.toLong) } else { Array[Long]() }).toList )
                  } catch {
                    case e:Exception =>
                      //FIXME : not really "Unprocessable" in this case, just BadRequest
                      throw new UnprocessableException("Invalid parameters: " + e.getMessage)
                  }

                  if (userToAdd.nonEmpty || userToDel.nonEmpty) {

                    transaction {
                      val campaign = CadixeDB.getCampaignById(campaign_id)

                      campaign match {
                        case Some(campaign) =>
                          transaction {

                            for (user_id <- userToAdd )  {
                              CadixeDB.campaign_annotators.where(ca =>
                                ca.campaign_id === campaign_id and ca.user_id === user_id).headOption match {
                                case None =>
                                  CadixeDB.campaign_annotators.insert(CampaignAnnotator(campaign_id, user_id))
                                case _ =>
                              }
                            }
                            for (user_id <- userToDel )  {
                              CadixeDB.campaign_annotators.deleteWhere(ca => ca.campaign_id === campaign_id and ca.user_id === user_id)
                            }
                          }
                          JsonResponse(campaign_annotators_json())

                        case None =>
                          ResponseWithReason(NotFoundResponse(), "Specified campaign no found")
                      }
                    }
                  } else {
                    ResponseWithReason(BadResponse(), "No annotator specified for add or remove operation")
                  }
                } catch {
                  case e:UnprocessableException =>
                    ResponseWithReason(BadResponse(), e.getMessage)
                }
              })
          case _ =>
            () => Full(ResponseWithReason(ForbiddenResponse(), "Only admin can perform this operation!"))
        }
      }

      //Retrieve all Tasks associated to a specific user within a campaign
    case Req("api" :: "user" :: AsLong(user_id) :: "campaign" :: AsLong(campaign_id) :: "tasks" :: Nil,_,GetRequest) => {
        user.is match {
          case Some(user) =>
            () => Full({
                transaction {

            
                  val campaign = if (user.is_admin) {
                    //admin can get the info even if he is not taking part in the campaign
                    CadixeDB.getCampaignById(campaign_id)
                  } else {
                    if (user.id.equals(user_id)) {
                      //check that the authorized user takes part in the campaign
                      user.annotates_in(campaign_id)
                    } else {
                      //non-admin authorized user can not retrieve info related to other users
                      None
                    }
                  }

                  campaign match {
                    case Some(campaign) =>

                      val taskInstances = getTaskInstancesList(user_id, campaign_id);
                      //generate json
                      val json = taskInstances.toList.sortWith( (a, b) => ( 
                          if (a.status.id==b.status.id) { a.doc_id < b.doc_id } else { a.status.id<b.status.id }
                        )).map  {
                        taskInst => json_task_instance(taskInst)
                      }
                      JsonResponse(json)

                    case None =>
                      ResponseWithReason(ForbiddenResponse(), "Only admin or users taking part in this campaign can perform this operation!")
                  }
                }
              })
          case None =>
            () => Full(BadResponse())
        }
      }

      //List documents associated to one specific user
    case Req("api" :: "user" :: AsLong(user_id) :: "campaign" :: AsLong(campaign_id) :: "documents" :: Nil,_,GetRequest) => {
        user.is match {
          case Some(user) =>
            () => Full({
                transaction {
                  val (quser, campaign) = if (user.is_admin) {
                    //admin can get the info even if he is not taking part in the campaign
                    (CadixeDB.getUserById(user_id), CadixeDB.getCampaignById(campaign_id))
                  } else {
                    if (user.id.equals(user_id)) {
                      //check that the authorized user takes part in the campaign
                      (Some(user), user.annotates_in(campaign_id))
                    } else {
                      //non-admin authorized user can not retrieve info related to other users
                      (None, None)
                    }
                  }
                  //TODO add "viewDocuments" Authorization check here!
                  (quser, campaign) match {
                    case (Some(quser), Some(campaign))  =>
                      JsonResponse(user_campaign_json(quser,campaign))

                    case _ =>
                      ResponseWithReason(ForbiddenResponse(), "Only admin or users taking part in this campaign can perform this operation!")
                  }
                }
              })
          case _ =>
            () => Full(BadResponse())
        }
      }


      //Update documents associated to one specific user
    case Req("api" :: "user" :: AsLong(user_id) :: "campaign" :: AsLong(campaign_id) :: "documents" :: Nil,_, PutRequest) => {
        user.is match {
          //only admin can change the document assignements
          case Some(user) if (user.is_admin) =>
            () => Full({

                try {
                  val (docToAdd, docToDel) = try {
                    ( S.param("add").dmap(Array[Long]())(s => if (s.nonEmpty) { s.split(",").map(_.toLong) } else { Array[Long]() }).toList ,
                     S.param("del").dmap(Array[Long]())(s => if (s.nonEmpty) { s.split(",").map(_.toLong) } else { Array[Long]() }).toList )
                  } catch {
                    case e:Exception =>
                      throw new UnprocessableException("Invalid parameters: " + e.getMessage)
                  }

                  if (docToAdd.nonEmpty || docToDel.nonEmpty) {

                    val (quser, campaign) =
                      transaction {
                        (CadixeDB.getUserById(user_id), CadixeDB.getCampaignById(campaign_id))
                      }
                    (quser, campaign) match {
                      case (Some(quser), Some(campaign))  =>
                        transaction {

                          for (doc_id <- docToAdd )  {
                            CadixeDB.document_assignment.where(da =>
                              da.campaign_id === campaign_id and da.user_id === user_id and da.doc_id === doc_id).headOption match {
                              case None =>
                                CadixeDB.document_assignment.insert(DocumentAssignment(campaign_id, user_id, doc_id))
                              case _ =>
                            }
                          }
                          for (doc_id <- docToDel )  {
                            CadixeDB.document_assignment.deleteWhere(da => da.campaign_id === campaign_id and da.user_id === user_id and da.doc_id === doc_id)
                          }
                        }
                        JsonResponse(user_campaign_json(quser,campaign))

                      case _ =>
                        ResponseWithReason(NotFoundResponse(), "Campaing or user not found")
                    }
                  } else {
                    ResponseWithReason(BadResponse(), "No document specified for add or remove operation")
                  }
                } catch {
                  case e:UnprocessableException =>
                    ResponseWithReason(BadResponse(), e.getMessage)
                }

              })
          case _ =>
            () => Full(ResponseWithReason(ForbiddenResponse(), "Only admin can perform this operation!"))
        }
      }

      //request the specified document associated to the specified Task (= start a new task instance, or carry the task)
    case Req("api" :: "user" :: AsLong(user_id) :: "campaign" :: AsLong(campaign_id) :: "document" :: AsLong(doc_id) :: "task" :: AsLong(task_id) :: Nil,_,GetRequest) => {
        user.is match {
          case Some(user) if (user.id == user_id) =>

            //TODO add "viewDocuments" Authorization check here!
            try {
              getDocumentForTask(user_id, campaign_id, doc_id, task_id) match {
                case Some(jsonDoc) =>
                  () => Full(JsonResponse(jsonDoc))
                case None =>
                  () => Full(ResponseWithReason(BadResponse(), "No Task found for the specified user/campaign/document"))
              }
            } catch {
              case ue:ConflictException =>
                () => Full(ConflictResponse(ue.getMessage))
            }
          case _ =>
            () => Full(ResponseWithReason(ForbiddenResponse(), "Task instance can only be retrieved by its owner"))
        }
      }

      //request the specified document associated to the specified Task (in read/only mode, no new AnnotationSet will be created)
    case Req("api" :: "user" :: AsLong(user_id) :: "campaign" :: AsLong(campaign_id) :: "document" :: AsLong(doc_id) :: "task" :: AsLong(task_id) :: "readonly" :: Nil,_,GetRequest) => {
        user.is match {
          case Some(user) =>
            transaction {
              CadixeDB.getTaskInstanceHead(user_id, campaign_id, doc_id, task_id) match {

                //check that the corresponding head annotation set is published
                case Some(as) =>
                  as.published match {
                    case Some(publicationDate) =>
                      try {
                        getDocumentForTask(user_id, campaign_id, doc_id, task_id) match {
                          case Some(jsonDoc) =>
                            () => Full(JsonResponse(jsonDoc))
                          case None =>
                            () => Full(ResponseWithReason(BadResponse(), "No Task found for the specified user/campaign/document"))
                        }
                      } catch {
                        case ue:ConflictException =>
                          () => Full(ConflictResponse(ue.getMessage))
                      }
                    case None =>
                      () => Full(ResponseWithReason(BadResponse(), "No Published AnnotationSet found for the specified user/campaign/document/task"))
                  }
                case None =>
                  () => Full(ResponseWithReason(BadResponse(), "No AnnotationSet found for the specified user/campaign/document/task"))
              }
            }
          case _ =>
            () => Full(UnauthorizedResponse("AlvisAE"))
        }
      }

      //request the list of documents that could be annotated in the specified Task
    case Req("api" :: "user" :: AsLong(user_id) :: "campaign" :: AsLong(campaign_id) :: "task" :: AsLong(task_id) :: "available" :: Nil,_,GetRequest) => {
        user.is match {
          case Some(user) if (user.id == user_id) =>
            transaction {
              val taskInstances = getTaskInstancesList(user_id, campaign_id, Some(task_id))

              val possibleTasks = taskInstances.filter { taskInst =>
                (TaskStatus.ToDo == taskInst.status || TaskStatus.Pending == taskInst.status || TaskStatus.Done == taskInst.status )
              } .toList.sortWith( (a, b) => (a.status.id < b.status.id))

              if (possibleTasks.nonEmpty) {
                val json = possibleTasks.map  { taskInst => json_task_instance(taskInst) }
                () => Full(JsonResponse(json))
              } else {
                () => Full(ResponseWithReason(NotFoundResponse(), "No other available document found for the specified user/campaign/task"))
              }
            }
          case _ =>
            () => Full(ResponseWithReason(ForbiddenResponse(), "Task instance can only be retrieved by its owner"))
        }
      }

      //request the list of task instances that can be reviewed by the specified Task
    case Req("api" :: "user" :: AsLong(user_id) :: "campaign" :: AsLong(campaign_id) :: "document" :: AsLong(doc_id) :: "task" :: AsLong(task_id) :: "reviewable" :: Nil,_,GetRequest) => {
        user.is match {
          case Some(user) => 
            transaction {
              import CadixeDB._
  
              //check that specified Task is defined
              from(task_definitions)((td) => where(td.campaign_id === campaign_id and td.id === task_id) select(td) 
              ).headOption match {
                case Some(taskDef)  =>
                  
                  //check that specified Task is a review 
                  from(task_precedencies)((tp) => where(tp.successor_id === taskDef.id and tp.direct===true and tp.reviewing_dep===true) select(tp)).toList
                  .map{_.predecessor_id}.headOption match {
                    
                    case Some(reviewed_taskId)  =>

                      //check that specified user is assigned to the document to be reviewed
                      from(document_assignment)((da) => where(da.user_id === user_id and da.campaign_id === campaign_id and da.doc_id === doc_id) select(da) 
                      ).headOption match {
                        case Some(_)  =>
                          
                          //retrieve reviewable Tasks instances
                          val reviewableTaskInstances = from(annotation_sets) ((as) => 
                            where(as.task_id === reviewed_taskId
                                  and as.doc_id === doc_id
                                  and campaign_id === campaign_id
                                  and as.head === true
                                  and as.published.isNotNull)
                            select(as)
                          ).toList. map { as => 
                            
                            TaskInstance(as.task_id, as.doc_id, as.user_id, TaskStatus.Done, false, Some(as.id), Some(as.created), as.published, None) }
                      
                          //generate json
                          val json = reviewableTaskInstances.map  {
                            taskInst => json_task_instance(taskInst)
                          }
                          () => Full(JsonResponse(json))                      
                          
                        case None =>
                          () => Full(ResponseWithReason(ForbiddenResponse(), "The specified User can not review this document"))
                      }
                    case None =>
                      () => Full(ResponseWithReason(BadResponse(), "The specified Task is not a Review"))
                  }
                case _ =>
                  () => Full(ResponseWithReason(BadResponse(), "Unknown Task in the specified Campaign"))
              }
            }
          case _ =>
            () => Full(ResponseWithReason(BadResponse(), "Unknown user"))
        }
      }

      //store a new head revision of the AnnotationSet associated to the specified Task (TaskId is contained in the JSON payload)
    case req @ Req("api" :: "user" :: AsLong(user_id) :: "campaign" :: AsLong(campaign_id) :: "document" :: AsLong(doc_id) :: Nil,_,PutRequest) => {
        jsonBody(req) match {
          case Full(json) => transaction {
              user.is match {
                case Some(user) if (user.id == user_id) =>

                  import CadixeDB._
                  //TODO add "createAnnotations" Authorization check here!

                  try {
                    val task_id = (json \\ "task_id").extract[Long]

                    //FIXME: check that corresponding Task is not the workflow's RootTask which is readonly.


                    // searches for a campaign_id containing doc_id assigned to user_id
                    CadixeDB.getTaskInstanceHead(user_id, campaign_id, doc_id, task_id) match {
                      case Some(as) => {
                          val publicationDate = as.published match {
                            case None =>
                              val saveAndPublish = (S.param("publish") ?~ "true").map(toBoolean)==true
                              if (saveAndPublish) {
                                //perform save and publication on a single operation
                                Some(CadixeDB.now())
                              } else {
                                None
                              }
                            case _ =>
                              //once an annotation set has been published, it will remain published for all its subsequent revisions
                              Some(CadixeDB.now())
                          }
                          val unmatchedStr = json \\ "unmatched"
                          val unmatched =  if (unmatchedStr.children.nonEmpty) { Some(compact(render(unmatchedStr))) } else { None }
                                                  
                          val nas = AnnotationSet(task_id, as.doc_id, as.user_id, as.campaign_id,
                                                  compact(render(json \\ "text_annotations")),
                                                  compact(render(json \\ "groups")),
                                                  compact(render(json \\ "relations")),
                                                  true,
                                                  //FIXME Not safe if concurrent access!!
                                                  as.revision + 1,
                                                  as.`type`, as.description, CadixeDB.now(), publicationDate, unmatched);
                                                  
                          try {
                            transaction {
                              CadixeDB.annotation_sets.insert(nas)
                              CadixeDB.storeAnnotationSetDependencies(nas)
                              as.head = false
                              CadixeDB.annotation_sets.update(as)
                              if (publicationDate.nonEmpty) {
                                //transaction rollback will happen if new AnnotationSet is not valid since validation will end up with an Exception
                                performValidation(nas, user_id, campaign_id, doc_id, task_id)
                              }
                            }
                            () => Full(OkResponse())
                          } catch {
                            case ex : UnprocessableException =>
                              () => Full(UnprocessableResponse(ex.getMessage))
                            case ex : IllegalArgumentException =>
                              () => Full(ResponseWithReason(BadResponse(),  ex.getMessage))
                          }
                        }
                      case None =>
                        () => Full(ResponseWithReason(BadResponse(), "Unexpected situation : there is no previous revision for the specified Task"))
                    }
                  } catch {
                    case _ : MappingException =>
                      () => Full(ResponseWithReason(BadResponse(), "Malformed AnnotationSet, missing taskid"))
                  }
                case _ =>
                  () => Full(ResponseWithReason(ForbiddenResponse(), "AnnotationSet can only be updated by its owner"))
              }
            }
          case _ =>
            () => Full(ResponseWithReason(BadResponse(), "Missing AnnotationSet in query"))
        }
      }

      //get document & task ids by their external id and name
    case Req("api" :: "user" :: AsLong(user_id) :: "campaign" :: AsLong(campaign_id) :: "withexternalid" :: Nil,_,GetRequest) => {
        user.is match {
          case None =>
            () => Full(ResponseWithReason(BadResponse(), "Unknown user"))
            
          case Some(user) => 
        
            transaction {
              import CadixeDB._
              //TODO add "viewDocuments" Authorization check here!

              S.param("documentid") match {
                case Full(documentId) => documentId
                  CadixeDB.getCampaignById(campaign_id) match {
                    case None => 
                      () => Full(ResponseWithReason(BadResponse(), "No such campaign") )

                    case Some(campaign) => 
                      S.param("taskname") match {
                        case Full(taskname) => 
                          CadixeDB.getTaskDefinition(campaign.id, taskname) match {
                            case None => 
                              () => Full(ResponseWithReason(BadResponse(), "No such task in the specified campaign") )

                            case Some(task) => 
                              CadixeDB.getDocumentByExternalId(documentId)match {
                                case None => 
                                  () => Full(ResponseWithReason(BadResponse(), "No such document in the specified campaign") )

                                case Some(document) => 
                                  
                                  from(document_assignment)((da) =>
                                    where(da.user_id === user.id and da.campaign_id === campaign.id and da.doc_id === document.id)
                                    select(da)).headOption match {
                                    case None => 
                                      () => Full(ResponseWithReason(ForbiddenResponse(), "This document is not assigned to the specified user in this campaign") )

                                    case Some(da) => 
                                      val resp =  ("campaign_id" -> campaign.id) ~ ("doc_id" -> document.id) ~  ("task_id" -> task.id)
                                      () => Full(JsonResponse(resp))
                              
                                  }    
                              }    
                          }    
                        case _ => 
                          () => Full(ResponseWithReason(BadResponse(), "A task name must be specified") )
                      }
                  }
                case _ => 
                  () => Full(ResponseWithReason(BadResponse(), "A document id must be specified") )
              }
            }
        }
      }

      //publish the head AnnotationSet corresponding to the specified Task
    case Req("api" :: "user" :: AsLong(user_id) :: "campaign" :: AsLong(campaign_id) :: "document" :: AsLong(doc_id) :: "task" :: AsLong(task_id) :: "publish" :: Nil,_,PostRequest) => {
        transaction {
          user.is match {
            case Some(user) if (user.id == user_id) =>

              import CadixeDB._
              //TODO add "createAnnotations" Authorization check here!

              // searches for a campaign_id containing doc_id assigned to user_id
              CadixeDB.getTaskInstanceHead(user_id, campaign_id, doc_id, task_id)  match {
                case Some(as) => {
                    as.published match {
                      case None =>
                        try {
                          performValidation(as, user_id, campaign_id, doc_id, task_id)

                          as.published = Some(CadixeDB.now())
                          CadixeDB.annotation_sets.update(as)
                          () => Full(OkResponse())
                        } catch {
                          case ex : UnprocessableException =>
                            () => Full(UnprocessableResponse(ex.getMessage))
                          case ex : Exception =>
                            Console.err.println(ex.getMessage)
                            Console.err.println(ex.printStackTrace)
                            () => Full(ResponseWithReason(BadResponse(), ex.getMessage))
                        }
                      case _ =>
                        () => Full(ResponseWithReason(BadResponse(), "Can not publish a task already published"))
                    }
                  }
                case None =>
                  () => Full(ResponseWithReason(ForbiddenResponse(), "Only the user who created the annotations can publish them!"))
              }
            case _ =>
              () => Full(ResponseWithReason(ForbiddenResponse(), "AnnotationSet can only be published by its owner"))
          }
        }
      }

      //Remove all the AnnotationSet revision corresponding to the specified Task
    case Req("api" :: "user" :: AsLong(user_id) :: "campaign" :: AsLong(campaign_id) :: "document" :: AsLong(doc_id) :: "task" :: AsLong(task_id) :: "remove" :: Nil,_,PostRequest) => {
        transaction {
          user.is match {
            case Some(user) if (user.id == user_id) =>

              CadixeDB.getTaskInstanceHead(user_id, campaign_id, doc_id, task_id)  match {
                case Some(as) => {

                    as.published match {
                      case None =>
                        CadixeDB.removeTaskInstanceLine(user_id, campaign_id, doc_id, task_id)
                        () => Full(OkResponse())
                      case _ =>
                        () => Full(ResponseWithReason(BadResponse(), "Can not remove annotations of an already published task"))
                    }
                  }
                case None =>
                  () => Full(ResponseWithReason(ForbiddenResponse(), "Only the user who created the annotations can remove them!"))
              }
            case _ =>
              () => Full(ResponseWithReason(ForbiddenResponse(), "AnnotationSets can only be removed by its owner"))
          }
        }
      }


    case Req("api" :: "annotation" :: Nil,_,GetRequest) => {
        user.is match {
          case Some(user) => transaction {
              //TODO add "viewOthersAnnotations" Authorization check here!

              val ids = S.param("ids").dmap(Array[Long]())(s => s.split(",").map(_.toLong)).toList
              val asets = CadixeDB.annotationSetLookup(ids)

              val res = asets.map(json_of_annotation_set(_, None))
              () => Full(JsonResponse(res))
            }
          case None =>
            () => Full(BadResponse())
        }
      }

      //Compute consolidation block for Review or AnnotationSet comparison
    case Req("api" :: "campaigns" :: AsLong(campaign_id) :: "annotations" :: "diff" :: Nil,_,GetRequest) => {
        user.is match {
          case Some(user) => transaction {
              //TODO add "viewOthersAnnotations" Authorization check here!

              val ids = S.param("ids").dmap(Array[Long]())(s => s.split(",").map(_.toLong)).toSet
              val asets = CadixeDB.annotation_sets.where(as => as.id in ids).toList

              if (asets.isEmpty) {
                () => Full(ResponseWithReason(BadResponse(), "No AnnotationSet id has been specified"))
              } else if (asets.size != ids.size) {
                () => Full(ResponseWithReason(BadResponse(), "Some AnnotationSets could not be found"))
              } else {
                val docIds = asets.map{ _.doc_id}.toSet
                if (docIds.size != 1) {
                  () => Full(ResponseWithReason(BadResponse(), "Compared AnnotationSets should be related to the same document"))
                } else
                {
                  val res = AnnotationSetComparator.compare(asets.toSet)
                  () => Full(JsonResponse(res))
                }
              }
            }
          case None =>
            () => Full(BadResponse())
        }
      }

      //Export all Annotations of a Campaign as zipped CVS files
    case Req("api" :: "campaigns" :: AsLong(campaign_id) :: "annotations" :: "CSV" :: Nil,_,GetRequest) => {
        CadixeDB.createSession().bindToCurrentThread;
        //TODO add "exportAnnotations" Authorization check here!

        val tempDir = Utils.createTempDir()
        val workingDirBaseName = "ExportAlvisAE"
        val workingDir = new File(tempDir.getAbsolutePath + "/" + workingDirBaseName + "/")
        workingDir.mkdir()
        transaction {
          CVSFormatHandler.exportCampaignAnnotationAsCSV(workingDir.getAbsolutePath, campaign_id, true)
        }
        val archiveBaseName = "aae_" + campaign_id + ".zip"
        val archiveAbsoluteName = tempDir.getAbsolutePath + "/" + archiveBaseName
        Utils.createZipFromFolder(workingDir.getAbsolutePath, archiveAbsoluteName, true)

        val stream = new FileInputStream(archiveAbsoluteName)

        () => Full(StreamingResponse(stream, () => stream.close, stream.available, List("Content-Type" -> "application/zip"), Nil, 200))
      }
      
      //Export all Annotations of a Campaign as zipped JSON files
    case Req("api" :: "campaigns" :: AsLong(campaign_id) :: "annotations" :: "JSON" :: Nil,_,GetRequest) => {
        user.is match {
          case Some(user) =>
            if (user.is_admin) {
              CadixeDB.createSession().bindToCurrentThread;
              
              CadixeDB.getCampaignById(campaign_id) match {
                case None => 
                  () => Full(ResponseWithReason(BadResponse(), "No such campaign") )
                case Some(campaign) => 
                  
                  import java.io.IOException
                  import java.io.UnsupportedEncodingException
                  import org.apache.commons.vfs.FileNotFoundException
                  
                  try {

                    val tempDir = Utils.createTempDir()
                    val workingDirBaseName = "ExportAlvisAE"
                    val workingDir = new File(tempDir.getAbsolutePath + "/" + workingDirBaseName + "/")
                    workingDir.mkdir()
                  
                    val schema = campaign.schema
                    import CadixeDB._        

                    //every document associated to the campaign
                    from(documents,campaign_documents) ( (d, cd) =>
                      where (cd.campaign_id === campaign_id and d.id === cd.doc_id).select(d).orderBy(d.id))
                    .map{ document =>

                      val doc_id= document.id
                    
                      //every Head AnnotationSet associated to the document 
                      val annotationSetToExport = from(annotation_sets)((as) =>
                        where(as.doc_id === doc_id and as.campaign_id === campaign_id
                              and as.head === true).select(as).orderBy(as.user_id, as.`type`, as.task_id)).distinct.toList
                
                      val invalidatedNReferencedAnnSets  = getDocumentInvalidatedAnnotationSets(campaign_id, doc_id)
                    
                      //invalidated AnnotationSet Ids associated to the document 
                      val invalidatedAnnSetIds = invalidatedNReferencedAnnSets.map {
                        case (invalidated, invalidating) => invalidated.id
                      }.toSet
                    
                      //non-Head AnnotationSet still referenced by invalidated AnnotationSets
                      val nonHeadReferencedAnnotationSets = invalidatedNReferencedAnnSets.map {
                        case (invalidated, invalidating) => invalidating
                      }.distinct
                
                      val jsonDoc = json_document(document, schema, None, annotationSetToExport ++ nonHeadReferencedAnnotationSets, List(), invalidatedAnnSetIds)  
                
                      //write json to file
                      val docfilename = "aaeDocument_c" + campaign_id + "_d" + doc_id + ".json"
                      val fos = new FileOutputStream(workingDir.getAbsolutePath() + File.separatorChar + docfilename)
                      val out = new OutputStreamWriter(fos, "utf-8")
                    
                      val jsonStr = compact(render(jsonDoc))
                      out.append(jsonStr)
                      out.close()
                    }

                    val archiveBaseName = "aae_" + campaign_id + ".zip"
                    val archiveAbsoluteName = tempDir.getAbsolutePath + "/" + archiveBaseName
                    Utils.createZipFromFolder(workingDir.getAbsolutePath, archiveAbsoluteName, true)
                    val stream = new FileInputStream(archiveAbsoluteName)

                    () => Full(StreamingResponse(stream, () => stream.close, stream.available, List("Content-Type" -> "application/zip"), Nil, 200))
                  
                  } catch  {
                    case ex @ (_ : UnsupportedEncodingException | _ : FileNotFoundException | _ : IOException) =>
                      ex.printStackTrace(Console.err)
                      () => Full(ResponseWithReason(InternalServerErrorResponse(), ex.getMessage()))
                  }
              }
              
            } else {
            
              () => Full(ResponseWithReason(ForbiddenResponse(), "Only admin can perform this operation!"))            
            }
          case None =>
            () => Full(ResponseWithReason(BadResponse(), "Unkown user"))
        }
      }
  }

  def jsonBody(req : Req) =
    req.body.map(bytes => new String(bytes, "UTF-8")) map parse

  def protection : LiftRules.HttpAuthProtectedResourcePF = {
    case Req("api" :: "user" :: _,_,_) => Full(AuthRole("logged"))
    case Req("api" :: "annotation" :: _,_,_) => Full(AuthRole("logged"))
    case Req("api" :: "authorizations" :: _,_,_) => Full(AuthRole("logged"))
    case Req("api" :: "campaigns" :: _,_,_) => Full(AuthRole("logged"))
  }
}
