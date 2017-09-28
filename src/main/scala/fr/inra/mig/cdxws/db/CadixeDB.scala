/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig.cdxws.db

import com.mchange.v2.c3p0.ComboPooledDataSource
import com.mchange.v2.c3p0.PooledDataSource
import java.sql.Connection
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date

import org.squeryl._
import org.squeryl.dsl._
import net.liftweb.json._
import java.util.Properties
import net.liftweb.json.JsonDSL._

import net.liftweb.util.Props
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.adapters.H2Adapter
import org.squeryl.adapters.PostgreSqlAdapter

import AnnotationSetType._
import TaskVisibility._

class DbObject extends KeyedEntity[Long] {
  val id : Long = 0
}

object CadixeDB extends org.squeryl.Schema {
  import net.liftweb.json.Serialization.write
  implicit val formats = Serialization.formats(NoTypeHints)

  val fmtdatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z")
  def dateToString(timestamp : Option[ java.sql.Timestamp]) : String = {
    timestamp match {
      case Some(datetime) =>
        dateToString(datetime)
      case None =>
        ""
    }
  }
  def dateToString(timestamp : java.sql.Timestamp) : String = {
    fmtdatetime.format(timestamp)
  }


  //dummy table used to track evolutions of the database model
  val databaseModelVersion = table[DatabaseModelVersion]("databasemodelversion")

  val users = table[User]("user")
  on(users)(user => declare(
      user.login is (unique),
      user.props is(dbType("TEXT"))
    ))

  val authorizations = table[Authorization]("authorization")
  on(authorizations)(authorization => declare(
      authorization.id is (primaryKey)
    ))

  val user_authorizations = manyToManyRelation(users, authorizations, "userauthorization").via[UserAuthorization]((u, a, ua) => (u.id === ua.user_id, ua.auth_id === a.id))

  val campaigns = table[Campaign]("campaign")
  on(campaigns)(c => declare(
      c.schema is(dbType("TEXT")),
      c.name is (unique)
    ))

  //FIXME It is not currently possible to define the relation below with Squeryl (v 0.9.4), so it is handled as a regular table and 3 one-to-manyRelations
  // see http://www.assembla.com/spaces/squeryl/tickets/25-compositekeys-cannot-be-the-binding-expression-for-relations
  val user_campaignauthorizations = table[UserCampaignAuthorization]("usercampaignauthorization")
  val users_usrccampaignauths = oneToManyRelation(users, user_campaignauthorizations).via((u, uca) =>  u.id === uca.user_id)
  val campaigns_usrccampaignauths = oneToManyRelation(campaigns, user_campaignauthorizations).via((c, uca) => c.id === uca.campaign_id)
  val auths_usrccampaignauths = oneToManyRelation(authorizations, user_campaignauthorizations).via((a, uca) =>  a.id === uca.auth_id)

  val documents = table[Document]("document")
  on(documents)(t => declare(
      t.contents is(dbType("TEXT")),
      t.props is(dbType("TEXT")),
      t.html_annset is(dbType("TEXT")),
      t.external_id is(unique,indexed("documentIDX1"))
    ))
  val campaign_annotators = manyToManyRelation(campaigns,users,"campaignannotator").via[CampaignAnnotator]((c,u,ca) => (ca.campaign_id === c.id, u.id === ca.user_id))
  val campaign_documents  = manyToManyRelation(campaigns,documents,"campaigndocument").via[CampaignDocument]((c,d,cd) => (cd.campaign_id === c.id, d.id === cd.doc_id))
  on(campaign_documents)(cd => declare(
      cd.alvisnlp_id is(unique,indexed("campaigndocumentIDX1"))
    ))

  val document_assignment = table[DocumentAssignment]("documentassignment")


  val task_definitions = table[TaskDefinition]("taskdefinition")
  on(task_definitions)(td => declare(
      td.annotationtypes is(dbType("TEXT")),
      columns(td.campaign_id, td.name) are (unique)
    ))

  val campaign_tasks = oneToManyRelation(campaigns, task_definitions).via((c, td) =>  c.id === td.campaign_id)
  val task_precedencies = manyToManyRelation(task_definitions, task_definitions,"taskprecedency").via[TaskPrecedency]((stk, ptk, dep) => (dep.successor_id === stk.id, ptk.id === dep.predecessor_id))

  /**
   * Table of annotation sets. There are implicit invariants:
   * <ul>
   * <li> unique annotationset per user, document, campaign that has head == true and type == UserAnnotation</li>
   * <li> unique annotationset per user, document, campaign (FIXME: qu'est-ce que j'ai voulu dire ?)</li>
   * </ul>
   */
  val annotation_sets = table[AnnotationSet]("annotationset")
  on(annotation_sets)(t => declare(
      t.text_annotations is(dbType("TEXT")),
      t.groups is(dbType("TEXT")),
      t.relations is(dbType("TEXT")),
      t.unmatched is(dbType("TEXT"))
    ))

  val annotationset_task = oneToManyRelation(task_definitions, annotation_sets).via((td, as) => td.id === as.task_id)
  val annotationset_dependencies = manyToManyRelation(annotation_sets,annotation_sets,"annotationsetdependency").via[AnnotationSetDependency]((rt,rd,asd) => (asd.referent_id === rt.id, rd.id === asd.referred_id))

  val annotationset_document = oneToManyRelation(documents,annotation_sets).via((d,as) => d.id === as.doc_id)

  private val ROOT_USERNAME = "aae_root"

  private val AlvisAE_ScopeName = "AlvisAE"
  private val AlvisIR_ScopeName = "AlvisIR"
  private val TyDI_ScopeName = "TyDI"

  object AAEAuthorizationIds  {
    val connect               =  1
    val createCampaign        =  2
    val closeCampaign         =  3
    val addDocument           =  4
    val removeDocument        =  5
    val viewDocuments         =  6
    val createAnnotations     =  7
    val viewOthersAnnotations =  8
    val exportAnnotations     =  9

    val Global = Set(connect, createCampaign)
  }

  object AIRAuthorizationIds  {
    val connect               = 30
    val uploadDocument        = 31
    val accesToPDFDocument    = 32
    val startAlvisAE          = 33
  }

  object TDYAuthorizationIds  {
    val connect               = 60
    val viewOntology          = 61
    val editOntology          = 62
  }


  override def create() = {
    super.create

    //Add predefined Authorizations
    List(
      Authorization(AAEAuthorizationIds.connect, AlvisAE_ScopeName, false, "Connect"),
      Authorization(AAEAuthorizationIds.createCampaign, AlvisAE_ScopeName, false, "Create campaign"),

      //Some Authorizations can be set specifically to individual campaign
      Authorization(AAEAuthorizationIds.closeCampaign, AlvisAE_ScopeName, true, "Close campaign"),
      Authorization(AAEAuthorizationIds.addDocument, AlvisAE_ScopeName, true, "Add document"),
      Authorization(AAEAuthorizationIds.removeDocument, AlvisAE_ScopeName, true, "Remove document"),
      Authorization(AAEAuthorizationIds.viewDocuments, AlvisAE_ScopeName, true, "View documents"),
      Authorization(AAEAuthorizationIds.createAnnotations, AlvisAE_ScopeName, true, "Create annotations"),
      Authorization(AAEAuthorizationIds.viewOthersAnnotations, AlvisAE_ScopeName, true, "View other user's annotations"),
      Authorization(AAEAuthorizationIds.exportAnnotations, AlvisAE_ScopeName, true, "Export annotations"),

      //
      Authorization(AIRAuthorizationIds.connect, AlvisIR_ScopeName, false, "Connect"),
      Authorization(AIRAuthorizationIds.uploadDocument, AlvisIR_ScopeName, false, "Upload document"),
      Authorization(AIRAuthorizationIds.accesToPDFDocument, AlvisIR_ScopeName, false, "Acces to PDF document"),
      Authorization(AIRAuthorizationIds.startAlvisAE, AlvisIR_ScopeName, false, "Start AlvisAE"),
      //
      Authorization(TDYAuthorizationIds.connect, TyDI_ScopeName, false, "Connect"),
      Authorization(TDYAuthorizationIds.viewOntology, TyDI_ScopeName, false, "View Ontology"),
      Authorization(TDYAuthorizationIds.editOntology, TyDI_ScopeName, false, "Edit Ontology")

    ).foreach(a =>  authorizations.insert(a) );

    //create AlvisAE root user
    addUser(ROOT_USERNAME, "Tadmin", true)
  }
  
  object PooledConnectionProvider {
    var pooledDS : Option[ComboPooledDataSource] = None
    
    def getConnection(jdbcUrl : String, user : String, password : String) : Connection = {
      
      def initOrGetPool : PooledDataSource = { 
        this.synchronized { 
          pooledDS match {
            case None =>
              val cpds = new ComboPooledDataSource 
              cpds.setDriverClass("org.h2.Driver") 
              cpds.setJdbcUrl(jdbcUrl) 
              cpds.setUser(user) 
              cpds.setPassword(password)  
              pooledDS = Some(cpds)
              return cpds
            case Some(cpds) =>
              return cpds
          }
        }
      }
    
      initOrGetPool.getConnection()
    }
  }

  def createH2Session(path : String, useConnectionPool : Boolean = false) = {
    val jdbcUrl = "jdbc:h2:" + path

    val session = if (useConnectionPool) {
      Session.create(PooledConnectionProvider.getConnection(jdbcUrl, "foo", "foo"), new H2Adapter)      
    } else {
      Class.forName("org.h2.Driver")
      Session.create(java.sql.DriverManager.getConnection(jdbcUrl, "foo", "foo"), new H2Adapter)
    }  
    
    //session.setLogger(msg => Console.err.println(msg))
    
    session
  }

  def createPGSession(server : String, port : Int,
                      dbname : String, schema : String,
                      user : String, password : String) = {
    Class.forName("org.postgresql.Driver");

    val jdbcUrl = "jdbc:postgresql://" + server + ":" + port.toString + "/" + dbname

    Console.err.println("Creating Connection... " + jdbcUrl)
    val connection = java.sql.DriverManager.getConnection(jdbcUrl, user, password)

    val statement = connection.createStatement
    Console.err.println("Default schema... " + schema)
    statement.execute("SET SEARCH_PATH TO " + schema)
    val session = Session.create(connection, new PostgreSqlAdapter)
    //session.setLogger(msg => Console.err.println(msg))

    session
  }

  def createSession(h2DatabasePath : Option[String] = None) : Session = {
    Props.get("db.type","h2") match {
      case "postgresql" => {
          Props.requireOrDie("db.server", "db.port", "db.dbname", "db.username", "db.password", "db.schema")
          createPGSession(Props.get("db.server", ""),
                          Props.getInt("db.port", 0),
                          Props.get("db.dbname", ""),
                          Props.get("db.schema", ""),
                          Props.get("db.username", ""),
                          Props.get("db.password", ""))
        }
      case _ => 
        val dbpath = h2DatabasePath match {
          case Some(specifiedPath) => specifiedPath
          case _ => Props.get("db.path", "/tmp/AlvisAE_DB")
        }
        //Console.err.println("createSession :" + dbpath)
        createH2Session(dbpath, Props.getBool("db.connectionpool", false))
    }
  }

  def createSession(props : Properties) : Session = {
    props.getProperty("db.type","h2") match {
      case "postgresql" => {
          val required = Set("db.server", "db.port", "db.dbname", "db.username", "db.password", "db.schema")
          val missing = required.foldLeft(List[String]()) {
            (missingSoFar, propName) => props.keySet.contains(propName) match {
              case false => propName :: missingSoFar
              case _ =>  missingSoFar
            }
          }
          missing.toList match {
            case Nil =>
            case bad => throw new Exception("The following required properties are not defined: "+bad.mkString(","))
          }
          createPGSession(props.getProperty("db.server", ""),
                          props.getProperty("db.port", "0").toInt,
                          props.getProperty("db.dbname", ""),
                          props.getProperty("db.schema", ""),
                          props.getProperty("db.username", ""),
                          props.getProperty("db.password", ""))
        }
      case _ => createH2Session(Props.get("db.path","/tmp/cadixedb"), Props.getBool("db.connectionpool", false))
    }
  }

  def initSession(props : Option[Properties] = None) = {
    val session = props match {
      case Some(p) => CadixeDB.createSession(p)
      case None => CadixeDB.createSession(None)
    }
    SessionFactory.concreteFactory = Some( () => session )
    session.bindToCurrentThread;
  }

  /**
   * Returns a timestamp of the current time
   */
  def now()  = new Timestamp((new Date()).getTime)


// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  def documents_by_user_and_campaign(u : User, c : Campaign) = {
    from(documents,document_assignment)((d,da) => where(u.id === da.user_id and c.id === da.campaign_id and
                                                        d.id === da.doc_id)
                                        select(d) orderBy(d.id asc))
  }

  def documents_by_campaign(c : Campaign) = {
    val campaignDocs = from(campaign_documents, documents)(
      (cd, d) =>
      where(cd.campaign_id === c.id
            and d.id === cd.doc_id)
      select(d) orderBy(d.id asc)
    )

    join(campaignDocs, document_assignment.leftOuter)(
      (d, da) =>
      select(d, da)
      orderBy(d.id asc)
      on(da.map(_.campaign_id) === c.id
         and da.map(_.doc_id) === d.id)
    )
  }

  def annotators_by_campaign() = {
    join(campaigns, campaign_annotators.leftOuter)(
      (c, ca) => select(c, ca)
      orderBy(c.id asc, ca.map(_.user_id) asc)
      on(c.id === ca.map(_.campaign_id)))
  }

  def createCampaign(name : String, schema : JValue) = {
    import net.liftweb.json._
    val c = Campaign(name,compact(render(schema)))
    campaigns.insert(c)
    c
  }

  def getCampaignById(campaign_id : Long )  = {
    from(campaigns)((c) => where(c.id === campaign_id) select(c)).headOption
  }

  def getCampaignByName(name : String )  = {
    from(campaigns)((c) => where(c.name === name) select(c)).headOption
  }

  def getDocumentById(document_id : Long )  = {
    from(documents)((d) => where(d.id === document_id) select(d)).headOption
  }

  def getDocumentByExternalId(external_id : String)  = {
    from(documents)((d) => where(d.external_id === Some(external_id)) select(d)).headOption
  }

  def createDocument(user : User, contents : String,
                     props : Map[String,String] = Map(), comment : String = "",
                     description : String = "") = {
    val d = Document(user.id,write(props),contents,comment,description)
    documents.insert(d)
    d
  }

  def getTaskDefinition(task_id : Long )  = {
    from(task_definitions)((t) => where(t.id === task_id) select(t)).headOption
  }

  def getTaskDefinition(campaign_id : Long, task_name : String)  = {
    from(task_definitions)((t) => where(t.campaign_id === campaign_id and t.name === task_name) select(t)).headOption
  }

  def getTaskDefinitionsById(campaign_id : Long)  = {
    (from(task_definitions)((td) => where(td.campaign_id===campaign_id) select(td) )
     .map { td=> td.id -> td  }).toMap
  }

  def addUserAnnotationSet(doc : Document, user : User, campaign : Campaign, task : TaskDefinition,
                           `type` : AnnotationSetType.AnnotationSetType,
                           text_annotations : List[TextAnnotation],
                           relations : List[Relation],
                           groups : List[Group],
                           description : String = "") = {
    val nas = annotation_sets.where(as => as.doc_id === doc.id and as.campaign_id === campaign.id and
                                    as.`type` === `type` and as.head === true).headOption match {
      case None => AnnotationSet(task.id, doc.id,user.id,campaign.id,
                                 write(text_annotations), write(groups), write(relations),
                                 true, 0, `type`, description, now(), None, None)
      case Some(as) => {
          as.head = false
          annotation_sets.update(as)
          AnnotationSet(task.id, doc.id,user.id,campaign.id,
                        write(text_annotations), write(groups), write(relations),
                        true, as.revision + 1, `type`,
                        if(description == "") { as.description } else { description },
                        now(), None, as.unmatched)

        }
    }
    annotation_sets.insert(nas)
    storeAnnotationSetDependencies(nas)
    nas
  }

  def storeAnnotationSetDependencies(referent : AnnotationSet) = {

    val refs = referenceTransitiveClosure(List(referent.id))
    val refDeps = refs.foldLeft(Nil : List[Long])((accu, referred) => if (referred!=referent.id) { referred :: accu } else { accu } )

    val backrefs = referent.getBackReferences
    val backRefDeps =  backrefs.foldLeft(Nil : List[Long])((accu,backRef) => backRef.set_id match { case Some(referred) if (referred!=referent.id) => referred :: accu case _ => accu })

    for (referred <- (refDeps ::: backRefDeps).distinct) {
      annotationset_dependencies.insert(AnnotationSetDependency(referent.id, referred))
    }
  }

  def getTaskInstanceHead(user_id : Long, campaign_id : Long, doc_id : Long, task_id : Long) = {
    from(users,document_assignment,annotation_sets)((u,da,as) =>
      where(da.user_id === user_id and da.campaign_id === campaign_id and da.doc_id === doc_id and
            as.doc_id === doc_id and as.campaign_id === campaign_id and as.user_id === user_id and as.task_id === task_id and as.head === true and
            u.id === user_id)
      select(as)).headOption
  }


  def removeTaskInstanceLine(user_id : Long, campaign_id : Long, doc_id : Long, task_id : Long) = {
    annotation_sets.deleteWhere(as => as.doc_id === doc_id and as.campaign_id === campaign_id and as.user_id === user_id and as.task_id === task_id)
  }

  //Return a list of the AnnotationSet that can read by the specified user while performing the specified Task
  def getPrecedingVisibleAnnotationSets(campaign_id : Long, doc_id : Long, taskdef : TaskDefinition) = {
    from(annotation_sets, task_definitions) ((as, td) =>
      where(as.doc_id === doc_id and as.campaign_id === campaign_id and as.head === true
            and td.campaign_id === as.campaign_id and td.id === as.task_id
            and (
          //AS from reviewed task and AS from tasks editing AnnotationType referenced by the secified Task AnnotationTypes
          (as.published.isNotNull and td.id.in(taskdef.has_directdependency))
          or
          (as.published.isNotNull and td.visibility===TaskVisibility.Public and td.precedencelevel.lt(taskdef.precedencelevel))
          or
          (td.visibility===TaskVisibility.Protected and td.id===taskdef.id and td.cardinality <> 1)
        )
      )
      select (as))
  }

  def invalidatedAnnotationSet(campaign_id : Long, user_id : Long, annset_id : Option[Long] = None) = {
    //if annset_id, it will only check if that specific AnnotationSet is invalidated
    from(annotation_sets, annotationset_dependencies, annotation_sets)((as, asd, rfd_as) =>
      where(as.user_id === user_id and as.campaign_id === campaign_id
            and (as.head === true).inhibitWhen(annset_id.nonEmpty)
            and as.id === annset_id.?
            and asd.referent_id === as.id
            and rfd_as.id === asd.referred_id and rfd_as.head === false)
      select(as, rfd_as) )
  }

  //Return the map (possibly empty) of the invalidated AnnotationSet (key) because they reference non-head AnnotationSets (value)
  def getInvalidatedAnnotationSet(campaign_id : Long, user_id : Long) = {
    val allInvalidated = invalidatedAnnotationSet(campaign_id, user_id).toList

    (allInvalidated.groupBy { case (invalidated, invalidating) => invalidated }.map{
        case (invalidated, tedtings) =>
          val invalidatings = tedtings .map {
            case (invalidated, invalidating) => invalidating }
          invalidated -> invalidatings
      }).toMap
  }

  def getDocumentInvalidatedAnnotationSets(campaign_id : Long, doc_id : Long) = {
    from(annotation_sets, annotationset_dependencies, annotation_sets)((as, asd, rfd_as) =>
      where(as.campaign_id === campaign_id
            and as.doc_id === doc_id
            and as.head === true
            and asd.referent_id === as.id
            and rfd_as.id === asd.referred_id
            and rfd_as.head === false)
      .select(as, rfd_as).orderBy(as.id, rfd_as.id) ).toList
  }

  def getUserById(user_id : Long) : Option[User] = {
    from(users)((u) => where(u.id === user_id) select(u)).headOption
  }

  def getUsersById() = {
    from(users)((u) => select(u)).toList.map { u=> u.id -> u  }.toMap
  }

  def getUserByLogin(login : String) : Option[User] = {
    from(users)((u) => where(u.login === login) select(u)).headOption
  }

  def getRootUser() : Option[User] = {
    getUserByLogin(ROOT_USERNAME)
  }

  def getRootTask(campaignId : Long) : Option[TaskDefinition] = {
    from(task_definitions)((td) => where(td.campaign_id === campaignId) select(td)).headOption
  }

  def addUser(login : String, password : String, is_admin : Boolean, is_active : Boolean = true, props : String = "{}") = {
    val u = User(login, password, is_admin, is_active, props)
    users.insert(u)
    //Grant Connect authorization to any new user
    user_authorizations.insert(UserAuthorization(u.id, AAEAuthorizationIds.connect))
    u
  }

  def changeUserPassword(user : User, new_password : String) = {
    user.password = new_password
    users.update(user)
    user
  }

  def updateUserProps(user : User, new_props : JValue) = {
    user.setPropertiesMap(new_props)
    CadixeDB.users.update(user)
    user
  }

  def removeUserCampaignAuthorization(user_id : Long, campaign_id : Long) {
    user_campaignauthorizations.deleteWhere(uca => uca.user_id === user_id and uca.campaign_id === campaign_id )
  }

  def removeUserCampaignAuthorization(user_id : Long, campaign_id : Long, auth_id : Long) {
    user_campaignauthorizations.deleteWhere(uca => uca.user_id === user_id and uca.campaign_id === campaign_id and uca.auth_id === auth_id )
  }

  def resetUserCampaignAuthorization(user_id : Long, campaign_id : Long, auth_id : Long) {
    removeUserCampaignAuthorization(user_id, campaign_id, auth_id)
    user_campaignauthorizations.insert(UserCampaignAuthorization(user_id, campaign_id, auth_id))
  }

  def addUser2Campaign(user : User, campaign : Campaign) = {
    //add only if user is not already part of the campaign
    campaign_annotators.where(ca => ca.campaign_id === campaign.id and ca.user_id === user.id).headOption match {
      case None =>
        campaign_annotators.insert(CampaignAnnotator(campaign.id,user.id))
      case _ =>
    }

    //FIXME: should use default values stored in UserAuthorization

    //Grant to user the right to views documents and create annotations
    resetUserCampaignAuthorization(user.id, campaign.id, AAEAuthorizationIds.viewDocuments)
    resetUserCampaignAuthorization(user.id, campaign.id, AAEAuthorizationIds.createAnnotations)
  }

  def addDocument2Campaign(doc : Document, campaign : Campaign, alvisnlp_id : Option[String] = None) = {
    //add only if document is not already part of the campaign
    campaign_documents.where(cd => cd.campaign_id === campaign.id and cd.doc_id === doc.id).headOption match {
      case None =>
        campaign_documents.insert(CampaignDocument(campaign.id,doc.id, alvisnlp_id))
      case _ =>
    }
  }

  def assignUser2AllDocuments(user : User, campaign : Campaign) = {
    // FIXME: check that user is already registered in the campaign
    campaign_documents.where(cd => cd.campaign_id === campaign.id).foreach(cd =>
      document_assignment.where(da => da.campaign_id === campaign.id and da.user_id === user.id and da.doc_id === cd.doc_id).headOption match {
        case None =>
          document_assignment.insert(DocumentAssignment(campaign.id,user.id,cd.doc_id))
        case _ =>
      }
    )

    //FIXME: should use default values stored in UserAuthorization

    //Grant to user the right to views documents and create annotations
    resetUserCampaignAuthorization(user.id, campaign.id, AAEAuthorizationIds.viewDocuments)
    resetUserCampaignAuthorization(user.id, campaign.id, AAEAuthorizationIds.createAnnotations)
  }

  //return info for ALL campaigns, includings those for which the user has no authorizations (=>empty array of authorisation)
  def getUserAuthsForAllCampaigns(user_id : Long) = {
    val allCampaignAuths = join(CadixeDB.campaigns, CadixeDB.user_campaignauthorizations.leftOuter)((c,uca) =>
      select(c, uca)
      orderBy(c.id asc)
      on(c.id === uca.map(_.campaign_id) and user_id===uca.map(_.user_id))
    )

    allCampaignAuths.toList
  }

  case class UsersAutorizations(global : List[Long], bycampaign : Map[String,List[Long]])

  //replace the user's Authorisations by the ones supplied
  def setUserAuthsForCampaigns(user_id: Long, newAuths : UsersAutorizations) {
    /*
     user_authorizations.deleteWhere(ua => ua.user_id === user_id)
     for ( auth_id <- newAuths.global) {
     user_authorizations.insert(UserAuthorization(user_id, auth_id))
     }
     */

    for ( (cId, auth_ids) <- newAuths.bycampaign) {
      //FIXME add conversion check
      val campaign_id = cId.toLong
      removeUserCampaignAuthorization(user_id, campaign_id)
      for ( auth_id <- auth_ids) {
        user_campaignauthorizations.insert(UserCampaignAuthorization(user_id, campaign_id, auth_id))
      }
    }
  }
  /*
   * Loads the annotation sets referenced in ids as well as the annotation
   * sets they reference
   * FIXME: this is probably not fat enough => fixpoint of reference relation
   */
  def annotationSetLookup(ids : List[Long]) = {
    val ids_star = referenceTransitiveClosure(ids)
    annotation_sets.where(as => as.id in ids_star)
  }
  /*
   * assumes ids does not contain any duplicates
   */
  private def referenceTransitiveClosure(ids : List[Long]) : List[Long] = {
    val ass = ids.map(annotation_sets.lookup(_).get)
    val refs = ass.foldLeft(ids)((accu,as) => as.references ++ accu)
    val union = (ids ++ refs).distinct
    if(union.length == ids.length) { ids }
    else { referenceTransitiveClosure(union) }
  }
}

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
object Structures {
  type MultivaluedProps = Map[String,List[String]]

  type FragmentsList = List[List[Int]]

  type AnnotationReferencesList = List[AnnotationReference]

  type AnnotationBackReferencesList = List[AnnotationBackReference]
}


case class DatabaseModelVersion(v2_2 : String)

case class User(val login : String, var password : String, is_admin : Boolean, is_active : Boolean, var props : String) extends DbObject {
  lazy val annotates_in = CadixeDB.campaign_annotators.right(this)

  def annotates_in(campaign_id : Long) = {
    from(CadixeDB.campaign_annotators, CadixeDB.campaigns)((ca,c) =>
      where(ca.user_id === this.id and ca.campaign_id === campaign_id and c.id === ca.campaign_id)
      select(c)).headOption
  }

  lazy val has_authorizations = CadixeDB.user_authorizations.left(this)

  implicit val formats = Serialization.formats(NoTypeHints)
  import net.liftweb.json.Serialization.write

  def setPropertiesMap(properties : Map[String, String]) {
    props = write(properties)
  }

  def setPropertiesMap(json : JValue) {
    val properties = json.extract[Map[String, String]]
    setPropertiesMap(properties)
  }

}

case class Authorization(val id : Long, val scope : String, val campaignrelated : Boolean,  val description : String) extends KeyedEntity[Long]
//Global Authorizations given to user (this table also contains the default values for campaign specific Authorizations)
case class UserAuthorization(val user_id : Long, val auth_id : Long) extends KeyedEntity[CompositeKey2[Long,Long]] {
  def id = compositeKey(user_id, auth_id)
}
//Campaign specific Authorizations given to user
case class UserCampaignAuthorization(val user_id : Long, val campaign_id : Long, val auth_id : Long) extends KeyedEntity[CompositeKey3[Long,Long,Long]] {
  def id = compositeKey(user_id, campaign_id, auth_id)
}

case class Campaign(val name : String, val schema : String, val guidelinesUrl : Option[String] = None) extends DbObject {
  lazy val task_definitions = CadixeDB.campaign_tasks.left(this)
}
case class Document(val owner : Long, var props : String, contents : String, comment : String, description : String, external_id : Option[String] = None, html_annset : Option[String] = None) extends DbObject

case class CampaignAnnotator(val campaign_id : Long, val user_id : Long) extends KeyedEntity[CompositeKey2[Long,Long]] {
  def id = compositeKey(campaign_id,user_id)
}

//FIXME alvisnlp_id not used anymore
case class CampaignDocument(val campaign_id : Long, val doc_id : Long, val alvisnlp_id : Option[String]) extends KeyedEntity[CompositeKey2[Long,Long]] {
  def id = compositeKey(campaign_id,doc_id)
}

case class DocumentAssignment(val campaign_id : Long, val user_id : Long, val doc_id : Long) extends KeyedEntity[CompositeKey3[Long,Long,Long]] {
  def id = compositeKey(campaign_id,user_id,doc_id)
}

case class TaskDefinition(val campaign_id : Long,
                          val name : String,
                          val cardinality : Int,
                          val visibility : TaskVisibility.TaskVisibility,
                          val annotationtypes : String,
                          val precedencelevel : Int) extends DbObject {

  //Note : The purpose of this no-args constructor is solely to give an hint to Squeryl about the type of the "visibility" enum column
  def this() = this(0,"",0, TaskVisibility.Public,"",0)

  lazy val has_directprecedencies = CadixeDB.task_precedencies.where(td => td.successor_id === this.id and td.direct===true).toList
  lazy val has_directdependency = CadixeDB.task_precedencies.where(td => td.successor_id === this.id and td.direct===true and (td.reviewing_dep===true or td.typereferencing_dep===true)).map{_.predecessor_id}

  def getEditedAnnotationTypes = annotationtypes.split(",")
}
/*
 * Links of precedence between Tasks defined within the same workflow
 */
case class TaskPrecedency(val successor_id : Long,
                          val predecessor_id : Long,
                          val campaign_id : Long,
                          val direct : Boolean,
                          val reviewing_dep : Boolean,
                          val succeeding_dep : Boolean,
                          val typereferencing_dep : Boolean) extends KeyedEntity[CompositeKey2[Long,Long]] {
  def id = compositeKey(successor_id, predecessor_id)
}

/*
 * A set of annotations with same origin (that is, produced by the same user or agent)
 *
 */
case class AnnotationSet(
  val task_id : Long,
  val doc_id : Long,
  val user_id : Long,
  val campaign_id : Long,
  var text_annotations : String,
  var groups : String,
  var relations : String,
  var head : Boolean, val revision : Int,
  val `type` : AnnotationSetType.AnnotationSetType,
  val description : String,
  val created : Timestamp,
  var published : Option[Timestamp],
  val unmatched : Option[String]         //unmatched source Annotations (for Review AnnotationSets)
) extends DbObject {
  implicit val formats = Serialization.formats(NoTypeHints)

  //Note : The purpose of this no-args constructor is solely to give an hint to Squeryl about the type of the "`type`" column
  // (carefully read : http://squeryl.org/schema-definition.html )
  def this() = this(0,0,0,0,"","","",false,0,UserAnnotation,"",CadixeDB.now(), Some(CadixeDB.now()), None)

  def get_textannotations = parse(text_annotations).extract[List[TextAnnotation]]

  def get_groups = parse(groups).extract[List[Group]]

  def get_relations = parse(relations).extract[List[Relation]]

  def references = {
    val gr_refs = get_groups.foldLeft(Nil : List[Long])((accu,g) => g.references ++ accu)
    get_relations.foldLeft(gr_refs)((accu,r) => r.references ++ accu)
  }

  def getBackReferences = {
    val backrefsT = get_textannotations.foldLeft(Nil : Structures.AnnotationBackReferencesList)((accu, annotation) => annotation.sources match { case Some(backRefs) => backRefs ::: accu case _ => accu } )
    val backrefsTG = get_groups.foldLeft(backrefsT)((accu, annotation) => annotation.sources match { case Some(backRefs) => backRefs ::: accu case _ => accu } )
    val backrefsTGR = get_relations.foldLeft(backrefsTG)((accu, annotation) => annotation.sources match { case Some(backRefs) => backRefs ::: accu case _ => accu } )
    backrefsTGR
  }

}

/*
 * Table containing the explicit AnnotationSet Dependencies, resulting from the fact that Groups & Relations are referencing annotations from other AnnotationSets
 * This information, primarily stored within the JSON of AnnotationSet, is stored here in order to being able to retreive invalidated AnnotationSets via a simple SQL query
 */
case class AnnotationSetDependency(val referent_id : Long, val referred_id : Long) extends KeyedEntity[CompositeKey2[Long,Long]] {
  def id = compositeKey(referent_id, referred_id)
}

case class AnnotationReference(val ann_id : String, var set_id : Option[Long] = None)
//FIXME : should extends Serializer for liftweb to treat 'status' as ConsolidationStatus instead of Int
case class AnnotationBackReference(val ann_id : String, var set_id : Option[Long] = None, val status :Int)

//Note : despite the fact that TextAnnotation, Group and Relation share the
//       same base definition (id, properties, type, kind), it seems that
//       no inheritance or mixin composition can be used here without loosing
//       the advantages of case classes (de/serialization, pattern matching)
//      (as a matter of fact, inheritance of case classes is deprecated as of scala 2.8)

case class TextAnnotation(val id : String,
                          val properties : Structures.MultivaluedProps,
                          val text : Structures.FragmentsList,
                          val `type` : String,
                          val sources : Option[Structures.AnnotationBackReferencesList],
                          val kind : Int = AnnotationKind.TextAnnotation.id,
                          val created : Option[Long] = None) {
  def id_substitution(sigma : Substitution[String]) = TextAnnotation(sigma.subst(id), properties, text, `type`, sources)
}
case class Group(val id : String,
                 val properties : Structures.MultivaluedProps,
                 val `type` : String,
                 val group : Structures.AnnotationReferencesList,
                 val sources : Option[Structures.AnnotationBackReferencesList],
                 val kind : Int = AnnotationKind.GroupAnnotation.id,
                 val created : Option[Long] = None) {
  /*
   * Returns the list of (Long) ids which correspond to annotation sets
   * referenced by this group
   */
  def references : List[Long] = {
    group.foldLeft(Nil : List[Long]){(accu,ar) => ar.set_id match {
        case Some(id) => id :: accu
        case _ => accu
      }
    }
  }
}


case class Relation(val id : String,
                    val properties : Structures.MultivaluedProps,
                    val `type` : String,
                    val relation : Map[String,AnnotationReference],
                    val sources : Option[Structures.AnnotationBackReferencesList],
                    val kind : Int = AnnotationKind.RelationAnnotation.id,
                    val created : Option[Long] = None) {
  def id_substitution(sigma : Substitution[String]) = {
    Relation(sigma.subst(id), properties, `type`, relation.mapValues{
        case AnnotationReference(uuid, ref) => AnnotationReference(sigma.subst(uuid), ref)
      }, sources)
  }

  def references : List[Long] = {
    relation.foldLeft(Nil : List[Long])((accu,kv) => kv._2.set_id match {
        case Some(id) => id :: accu
        case _ => accu
      })
  }
}
