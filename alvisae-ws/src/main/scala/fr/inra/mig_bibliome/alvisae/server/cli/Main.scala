/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2010-2013.
 *
 */
package fr.inra.mig_bibliome.alvisae.server.cli

import fr.inra.mig.cdxws.api.ConfigResourceLocator
import fr.inra.mig.cdxws.db.AnnotationSet
import fr.inra.mig.cdxws.db.Campaign
import fr.inra.mig.cdxws.db.Document
import fr.inra.mig.cdxws.db.JSONImporter.DocumentExt
import fr.inra.mig.cdxws.db.User
import java.io.File

object Main {

  object StrOption extends Enumeration {

    type Names = Value
    val propsFileName = Value("propsFileName")
    val campaignName = Value("campaignName")
    val schemaDefinitionFile = Value("schema")
    val workflowDefinitionFile  = Value("workflow")
    val guidelinesUrl  = Value("guidelines")
    val docsDirectory  = Value("docsInputDir")
    val taskName  = Value("taskName")
    val userName  = Value("userName")
    val annotationSetFile  = Value("annotationSetFile")
    val docExternalId = Value("docExternalId")

    val password  = Value("password")
    val outDirectory  = Value("outputDir")
    val outFormat = Value("format")

    val taskList  = Value("taskList")
    val userList  = Value("userList")
  }

  object IntOption extends Enumeration {

    type Names = Value
    val annotationSetId = Value("annotationSetId")
    val docInternalId = Value("docInternalId")
    val campaignId  = Value("campaignId")
    val taskId  = Value("taskId")
    val userId  = Value("userId")
  }


  case class CommandSelector(
    nbCommand : Int = 0,
    verbose : Boolean = false,
    //command flags
    createUser : Boolean = false,
    createCampaign : Boolean = false,
    addDocuments : Boolean = false,
    importAnnotations : Boolean = false,
    assignDocument : Boolean = false,
    exportCampaign : Boolean = false,
    //options
    strOptions : Map[StrOption.Names, String] = Map(),
    intOptions : Map[IntOption.Names, Int] = Map()
    //
  )

  object OutFormat extends Enumeration {
    type OutFormat = Value
    val CSV = Value(0,"CSV")
    val JSON = Value(1,"JSON")
  }


  def main(args: Array[String]) {

    val jarName = new java.io.File(this.getClass.getProtectionDomain().getCodeSource().getLocation().getPath()).getName()
    val jarVersion = "0.1"

    val parserCmd = new scopt.immutable.OptionParser[CommandSelector] (jarName, false) {
      def options = Seq(
        flag("create-campaign", "Create a campaign")  {
          (c: CommandSelector) => c.copy(nbCommand=c.nbCommand+1, createCampaign=true) },

        flag("add-documents", "Add documents")  {
          (c: CommandSelector) => c.copy(nbCommand=c.nbCommand+1, addDocuments=true) },

        flag("import-annotations", "Import annotations")  {
          (c: CommandSelector) => c.copy(nbCommand=c.nbCommand+1, importAnnotations=true) },

        flag("assign-document", "assign document to a campaign")  {
          (c: CommandSelector) => c.copy(nbCommand=c.nbCommand+1, assignDocument=true) },

        flag("create-user", "Create a user")  {
          (c: CommandSelector) => c.copy(nbCommand=c.nbCommand+1, createUser=true) },

        flag("export-campaign", "Export a campaign as a zip archive")  {
          (c: CommandSelector) => c.copy(nbCommand=c.nbCommand+1, exportCampaign=true) },

        opt("p", StrOption.propsFileName.toString, "properties file containing connection parameters") {
          (v: String, c: CommandSelector) => c.copy(strOptions = c.strOptions + (StrOption.propsFileName -> v.trim)) },

        //option specific to campaign creation
        opt("c", StrOption.campaignName.toString, "campaign name") {
          (v: String, c: CommandSelector) => c.copy(strOptions = c.strOptions + (StrOption.campaignName -> v.trim)) },

        opt("w", StrOption.workflowDefinitionFile.toString, "XML workflow definition file") {
          (v: String, c: CommandSelector) => c.copy(strOptions = c.strOptions + (StrOption.workflowDefinitionFile -> v.trim)) },

        opt("s", StrOption.schemaDefinitionFile.toString, "JSON schema definition file") {
          (v: String, c: CommandSelector) => c.copy(strOptions = c.strOptions + (StrOption.schemaDefinitionFile -> v.trim)) },

        opt(StrOption.guidelinesUrl.toString, "URL pointing to the annotation guidelines of the campaign") {
          (v: String, c: CommandSelector) => c.copy(strOptions = c.strOptions + (StrOption.guidelinesUrl -> v.trim)) },

        //option specific to document loading & annotations import
        opt("d", StrOption.docsDirectory.toString, "JSON files input directory") {
          (v: String, c: CommandSelector) => c.copy(strOptions = c.strOptions + (StrOption.docsDirectory -> v.trim)) },

        //option specific to annotation set importing
        opt(StrOption.annotationSetFile.toString, "JSON file containing the Annotation Set to be imported") {
          (v: String, c: CommandSelector) => c.copy(strOptions = c.strOptions + (StrOption.annotationSetFile -> v.trim)) },
        intOpt(IntOption.annotationSetId.toString, "Id of the Annotation Set to be imported") {
          (v: Int, c: CommandSelector) => c.copy(intOptions = c.intOptions + (IntOption.annotationSetId -> v)) },
        intOpt(IntOption.docInternalId.toString, "internal Id of Document corresponding to the imported Annotation Set") {
          (v: Int, c: CommandSelector) => c.copy(intOptions = c.intOptions + (IntOption.docInternalId -> v)) },
        opt(StrOption.docExternalId.toString, "External Id of Document corresponding to the imported Annotation Set") {
          (v: String, c: CommandSelector) => c.copy(strOptions = c.strOptions + (StrOption.docExternalId -> v.trim)) },
        intOpt(IntOption.campaignId.toString, " Id of the Campaign where to import the Annotation Set") {
          (v: Int, c: CommandSelector) => c.copy(intOptions = c.intOptions + (IntOption.campaignId -> v)) },
        intOpt(IntOption.taskId.toString, " Id of the Task where to import the Annotation Set") {
          (v: Int, c: CommandSelector) => c.copy(intOptions = c.intOptions + (IntOption.taskId -> v)) },
        opt(StrOption.taskName.toString , " Name of the Task where to import the Annotation Set") {
          (v: String, c: CommandSelector) => c.copy(strOptions = c.strOptions + (StrOption.taskName -> v.trim)) },
        intOpt(IntOption.userId.toString, "Id of the User to be associated with the imported Annotation Set") {
          (v: Int, c: CommandSelector) => c.copy(intOptions = c.intOptions + (IntOption.userId -> v)) },
        opt(StrOption.userName.toString, "Name of the User to be associated with the imported Annotation Set") {
          (v: String, c: CommandSelector) => c.copy(strOptions = c.strOptions + (StrOption.userName -> v.trim)) },
        
        opt(StrOption.taskList.toString, "CSV file containing Task names to associate to task ids of the imported Annotation Set") {
          (v: String, c: CommandSelector) => c.copy(strOptions = c.strOptions + (StrOption.taskList -> v.trim)) },
        opt(StrOption.userList.toString, "CSV file containing User names to associate to owner ids of the imported Annotation Set") {
          (v: String, c: CommandSelector) => c.copy(strOptions = c.strOptions + (StrOption.userList -> v.trim)) },
        
        
        opt(StrOption.password.toString, "user's password") {
          (v: String, c: CommandSelector) => c.copy(strOptions = c.strOptions + (StrOption.password -> v.trim)) },

        opt("o", StrOption.outDirectory.toString, "output directory") {
          (v: String, c: CommandSelector) => c.copy(strOptions = c.strOptions + (StrOption.outDirectory -> v.trim)) },
        opt("f", StrOption.outFormat.toString, "output format [(csv)|json]") {
          (v: String, c: CommandSelector) => c.copy(strOptions = c.strOptions + (StrOption.outFormat -> v.trim)) },

        flag("v", "verbose")  {
          (c: CommandSelector) => c.copy(verbose=true) }
      ) }

    val selectedCommand = parserCmd.parse(args, CommandSelector()) map { cmdSelector =>

      if (cmdSelector.nbCommand!=1) {
        Console.err.println("One, and only one command must be specified!")
        parserCmd.showUsage
        exit(1)
      } else {

        val propsFileName = cmdSelector.strOptions.get(StrOption.propsFileName) match {
          case Some(fileName) =>  fileName
          case None =>
            Console.err.println("Missing connection parameters")
            parserCmd.showUsage
            exit(1)
        }
        val props = ConfigResourceLocator.getAndCheckConfigFile("", cmdSelector.verbose, propsFileName)
        if (props==null) {
          parserCmd.showUsage
          exit(1)
        } else
          // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

        if (cmdSelector.createCampaign) {

          val newCampaign = createCampaign(props, cmdSelector)
          if (newCampaign==null) {
            Console.err.println("campaign creation aborted!")
            exit(1)
          } else {
            Console.err.println("campaign created")
            Console.out.println(newCampaign.id + "\t\"" + newCampaign.name + "\"")
            exit(0)
          }


        } else
          // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        if (cmdSelector.addDocuments) {

          val addedDocs = addDocuments(props, cmdSelector)
          if (addedDocs.isEmpty) {
            Console.err.println("No document added")
            exit(1)
          } else {
            Console.err.println(addedDocs.size +  " document(s) added")
            addedDocs.foreach{ case(path, doc) =>
                Console.out.print(doc.id + "\t\"" + path.toString + "\"\t")
                if (doc.external_id.nonEmpty) {
                  Console.out.println("\"" +  doc.external_id.head +"\"" );
                } else {
                  Console.out.println();
                }
            }
            exit(0)

          }

        } else if (cmdSelector.importAnnotations) {

          val importedAS =
            cmdSelector.strOptions.get(StrOption.docsDirectory) match {
              case Some(dirPath) =>
                importAnnotationSets(props, cmdSelector)
              case None =>
                importAnnotations(props, cmdSelector)

            }

          if (importedAS.isEmpty) {
            Console.err.println("No annotation Set imported")
            exit(1)
          } else {
            Console.err.println("AnnotationSet sucessfully imported")
            exit(0)

          }

        } else if (cmdSelector.assignDocument) {

          if (assignDocToCampaign(props, cmdSelector)) {
            Console.err.println("Document sucessfully assigned")
            exit(0)
          } else {
            Console.err.println("No document could be assigned!")
            exit(1)

          }

        } else if (cmdSelector.createUser) {

          val createdUser = createUser(props, cmdSelector)
          if (createdUser==null) {
            Console.err.println("user creation aborted!")
            exit(1)
          } else {
            Console.err.println("user created")
            Console.out.println(createdUser.id + "\t\"" + createdUser.login + "\"")
            exit(0)
          }
        } else if (cmdSelector.exportCampaign) {

          val archivePath = exportCampaign(props, cmdSelector)
          if (archivePath==null) {
            Console.err.println("export aborted!")
            exit(1)
          } else {
            Console.err.println("export performed")
            Console.out.println(archivePath)
            exit(0)
          }
        }

      }

    } getOrElse {
      // arguments are bad, usage message will have been displayed
      exit(1)
    }

  }

  def createCampaign(props : java.util.Properties, cmdSelector : CommandSelector) : Campaign = {
    import fr.inra.mig.cdxws.db._
    import org.squeryl.PrimitiveTypeMode._

    val campaignDescription = cmdSelector.strOptions.get(StrOption.campaignName) match {
      case Some(description) =>
        description
      case None =>
        Console.err.println("A Campaign must be specified")
        return null
    }
    val schemaDefinitionFile = cmdSelector.strOptions.get(StrOption.schemaDefinitionFile) match {
      case Some(fileName) =>
        fileName
      case None =>
        Console.err.println("A shema must be specified")
        return null
    }
    val guidelinesUrl = cmdSelector.strOptions.get(StrOption.guidelinesUrl)

    CadixeDB.initSession(Some(props))
    cmdSelector.strOptions.get(StrOption.workflowDefinitionFile)
    match {
      case Some(workflowDefinitionFile) =>
        JSONImporter.createCampaign(campaignDescription, schemaDefinitionFile, workflowDefinitionFile, guidelinesUrl)
      case None =>
        JSONImporter.createCampaign(campaignDescription, schemaDefinitionFile, TaskDefinitionXMLImporter.DefaultTaskDefinitions, guidelinesUrl)
    }
  }

  def addDocuments(props : java.util.Properties, cmdSelector : CommandSelector) : List[(java.io.File, Document)] = {
    import java.io.File
    import org.squeryl.PrimitiveTypeMode._
    import fr.inra.mig.cdxws.db._
    import AnnotationSetType._

    cmdSelector.strOptions.get(StrOption.docsDirectory) match {
      case None =>
        Console.err.println("An input directory must be specified")
        return List()

      case Some(docsInputDirectory) =>
        CadixeDB.initSession(Some(props))

        val dir = new File(docsInputDirectory)
        if (!dir.isDirectory || ! dir.canRead) {
          Console.err.println("Invalid or unreadeable specified directory! Loading aborted.")
          return List()
        }
        val jsonDocFiles = dir.listFiles.filter( { f =>  f.getName.toLowerCase.endsWith(".json") &&  f.isFile } )


        val importedDocs = jsonDocFiles .map { filepath =>
          (filepath, JSONImporter.importDocument(filepath))
        }.toList

        importedDocs
    }

  }

  def importAnnotations(props : java.util.Properties, cmdSelector : CommandSelector) : List[AnnotationSet] = {
    import java.io.File
    import fr.inra.mig.cdxws.db._
    import org.squeryl.PrimitiveTypeMode._

    val inputFile = cmdSelector.strOptions.get(StrOption.annotationSetFile) match {
      case None =>
        Console.err.println("Missing input file")
        return List()
      case Some(fileName) =>
        new File(fileName)
    }

    val annSets = JSONImporter.getAnnotationSetsExtFromFile(inputFile)
    val annSetId = cmdSelector.intOptions.get(IntOption.annotationSetId)
    val importedASId = if (annSetId.isEmpty) {

      Console.err.println("Missing AnnotationSet Id - import will proceed with first UserAnnotation Annotation Set")

      annSets.filter{ annSetExt =>  AnnotationSetType.UserAnnotation==AnnotationSetType.withName(annSetExt.`type`) } .headOption match {
        case  Some(as) =>
          as.id
        case None =>
          Console.err.println("No user AnnotationSet found in input file")
          return List()
      }
    } else {

      annSets.filter{ annSetExt =>  annSetExt.id.equals(annSetId.head)} .headOption match {
        case  Some(as) =>
          as.id
        case None =>
          Console.err.println("Specified AnnotationSet id not found in input file")
          return List()
      }

    }

    CadixeDB.initSession(Some(props))

    val campaign = resolveCampaign(cmdSelector) match {
      case Some(c) => c
      case None => return List()
    }

    val document = resolveDocument(cmdSelector) match {
      case Some(d) => d
      case None => return List()
    }

    val taskId = cmdSelector.intOptions.get(IntOption.taskId)
    val taskName = cmdSelector.strOptions.get(StrOption.taskName)
    val task = if (taskId.isEmpty && taskName.isEmpty) {
      Console.err.println("A Task must be specified")
      return List()
    } else if (taskId.nonEmpty) {
      CadixeDB.getTaskDefinition(taskId.head) match {
        case Some(t) => t
        case None =>
          Console.err.println("Invalid specified task id")
          return List()
      }
    } else {
      CadixeDB.getTaskDefinition(campaign.id, taskName.head) match {
        case Some(t) => t
        case None =>
          Console.err.println("Invalid specified task name")
          return List()
      }
    }

    val user = resolveUser(cmdSelector) match {
      case Some(u) => Map(u.id -> u)
      case None => return List()
    }

    val taskById = CadixeDB.getTaskDefinitionsById(campaign.id)
    val taskIdMapping = Map(importedASId -> (task.id, AnnotationSetType.UserAnnotation) )

    JSONImporter.importAnnotationSets(campaign.id, document, user, taskById, taskIdMapping, annSets, inputFile)
  }

  def getMapFileContent(fileName : String) : Option[Map[Long,String]] = {
    val file = new File(fileName)

    if (!file.isFile || ! file.canRead) {
      None
    } else {
      val source = scala.io.Source.fromFile(fileName)
      val lines = source.getLines.toList
      source.close()
      val nameById = lines.map{ _.split("\\t") }
      .filter { fields => fields.length>=2 && fields(0).nonEmpty && fields(1).nonEmpty && (fields(0) forall Character.isDigit) }
      .map { fields => (fields(0).toLong -> fields(1))        
      }.toMap

      Some(nameById)
    }
  }

  def importAnnotationSets(props : java.util.Properties, cmdSelector : CommandSelector) : List[(DocumentExt, List[AnnotationSet])] = {

    import org.squeryl.PrimitiveTypeMode._
    import fr.inra.mig.cdxws.db._
    import AnnotationSetType._

    cmdSelector.strOptions.get(StrOption.docsDirectory) match {
      case None =>
        Console.err.println("An input directory must be specified")
        return List()

      case Some(docsInputDirectory) =>

        val taskMap = cmdSelector.strOptions.get(StrOption.taskList) match {
          case Some(taskListFile) =>
            val taskMap = getMapFileContent(taskListFile)
            if (taskMap.isEmpty) {
              Console.err.println("Invalid or unreadable task list file!")
              return List()
            }
            taskMap
          case None =>
            None
        }

        val userMap = cmdSelector.strOptions.get(StrOption.userList) match {
          case Some(userListFile) =>
            val userMap = getMapFileContent(userListFile)
            if (userMap.isEmpty) {
              Console.err.println("Invalid or unreadable user list file!")
              return List()
            }
            userMap
          case None =>
            None
        }


        CadixeDB.initSession(Some(props))

        val dir = new File(docsInputDirectory)
        if (!dir.isDirectory || ! dir.canRead) {
          Console.err.println("Invalid or unreadable specified directory! Import aborted.")
          return List()
        }
        val jsonDocFiles = dir.listFiles.filter( { f =>  f.getName.toLowerCase.endsWith(".json") &&  f.isFile } )

        resolveCampaign(cmdSelector) match {
          case None =>
            List()
          case Some(campaign) => campaign

            val taskById = CadixeDB.getTaskDefinitionsById(campaign.id)
            
            //map to translate Task ids
            val taskIdTranslator = taskMap match {
              case None =>
                taskById.keys.map( id => (id -> id ) ).toMap
                
              case Some(taskMap) =>
                val destTasksByName = taskById.values.map( td => (td.name -> td.id) ).toMap
                
                taskMap.filter{ case (id, name) => destTasksByName.get(name) match {
                      case None =>
                        Console.err.println("Warning task '" + name + "' (#" + id + ") could not be found in destination campaign - Any associated annotation sets will be skipped")
                        false
                      case Some(_) =>  
                        true
                    } }
                .map{ case (id, name) =>(id -> destTasksByName.get(name).head ) 
                }.toMap  
            }
             
            //map to translate User ids
            val userById = CadixeDB.getUsersById()
            
            val userIdTranslator = userMap match {
              case None =>
                userById.values.map( u => (u.id -> u ) ).toMap
                
              case Some(userMap) =>
                val destUsersByName = userById.values.map( u => (u.login -> u) ).toMap
                
                userMap.filter{ case (id, name) => destUsersByName.get(name) match {
                      case None =>
                        Console.err.println("Warning User '" + name + "' (#" + id + ") could not be found in destination database - Any associated annotation sets will be skipped")
                        false
                      case Some(_) =>  
                        true
                    } }
                .map{ case (id, name) =>(id -> destUsersByName.get(name).head ) 
                }.toMap  
            }
             
            //
            jsonDocFiles.map { inputFile =>

              val doc = JSONImporter.getDocumentExtFromFile(inputFile)

              JSONImporter.getDocExternalId(doc.props) match {

                case None =>
                  Console.err.println("No document external id specified - skipping " + inputFile.toPath)
                  (doc -> List())

                case Some(docExternalId) =>
                  CadixeDB.getDocumentByExternalId(docExternalId) match {

                    case None =>
                      Console.err.println("Unknown specified document external id (" + docExternalId + ") - skipping " + inputFile.toPath)
                      (doc -> List())

                    case Some(document) => document
                      //only import User's AnnotationSets
                      val userAnnSets = JSONImporter.getAnnotationSetsExtFromFile(inputFile)
                      .filter{ annSetExt =>  AnnotationSetType.UserAnnotation==AnnotationSetType.withName(annSetExt.`type`) }

                      //Keep AnnotationSets corresponding to actual user & task
                      val annSetToImport = userAnnSets.filter { annSetExt =>
                        if (userIdTranslator.get(annSetExt.owner).isEmpty) {
                          val unknownUser = userMap match {
                            case Some(userMap) if userMap.get(annSetExt.owner).nonEmpty => "name='" + userMap.get(annSetExt.owner).head + "'"
                            case _ => "id=" + annSetExt.owner
                          }
                          Console.err.println("\tUnknown Annotation Set's owner (" +unknownUser + ") can not be mapped to an existing user in destination database - skipping AnnotationSet #" + annSetExt.id)
                          false
                        } else if (annSetExt.task_id.isEmpty) {
                          Console.err.println("\tNo task specified for Annotation Set - skipping AnnotationSet #" + annSetExt.id)
                          false
                        } else if (taskIdTranslator.get(annSetExt.task_id.head).isEmpty) {
                          val unknownTask = taskMap match {
                            case Some(taskMap) if taskMap.get(annSetExt.task_id.head).nonEmpty => "name='" + taskMap.get(annSetExt.task_id.head).head + "'"
                            case _ => "id=" + annSetExt.task_id.head
                          }
                          Console.err.println("\tAnnotation Set's task (" + unknownTask + ") can not be mapped to an existing task in destination campaign - skipping AnnotationSet #" + annSetExt.id)
                          false
                        } else {
                          true
                        }
                      }

                      val taskIdMapping = annSetToImport.map{ as =>  
                        val translatedTaskId = taskIdTranslator.get(as.task_id.head).head
                        (as.id -> (translatedTaskId, AnnotationSetType.UserAnnotation)) }.toMap

                      val importedAS = JSONImporter.importAnnotationSets(campaign.id, document, userIdTranslator, taskById, taskIdMapping, annSetToImport, inputFile)
                      (doc -> importedAS)
                  }
              }
            }.toList

        }

    }


  }

  def resolveUser(cmdSelector : CommandSelector) : Option[User] = {
    import fr.inra.mig.cdxws.db._

    val userId = cmdSelector.intOptions.get(IntOption.userId)
    val userName = cmdSelector.strOptions.get(StrOption.userName)
    if (userId.isEmpty && userName.isEmpty) {
      Console.err.println("An User must be specified")
      None
    } else if (userId.nonEmpty) {
      CadixeDB.getUserById(userId.head) match {
        case Some(u) => Some(u)
        case None =>
          Console.err.println("Invalid specified user id")
          None
      }
    } else {
      CadixeDB.getUserByLogin(userName.head) match {
        case Some(u) => Some(u)
        case None =>
          Console.err.println("Invalid specified user name")
          None
      }
    }
  }


  def assignDocToCampaign(props : java.util.Properties, cmdSelector : CommandSelector) : Boolean = {
    import fr.inra.mig.cdxws.db._
    import org.squeryl.PrimitiveTypeMode._

    CadixeDB.initSession(Some(props))

    val campaign = resolveCampaign(cmdSelector) match {
      case Some(c) => c
      case None => return false
    }

    val document = resolveDocument(cmdSelector) match {
      case Some(d) => d
      case None => return false
    }

    JSONImporter.assignDocToCampaign(document.id, campaign.id)

    //if an optional user has been indicated in the commandline, then also assign document to user
    val user = resolveUser(cmdSelector) match {
      case Some(u) =>
        JSONImporter.assignDocToUserInCampaign(document.id, u.id, campaign.id)
      case None =>
    }
    true
  }

  def createUser(props : java.util.Properties, cmdSelector : CommandSelector) : User = {
    if (cmdSelector.strOptions.get(StrOption.userName).isEmpty) {
      Console.err.println("Missing user name")
      return null
    } else if (cmdSelector.strOptions.get(StrOption.password).isEmpty) {
      Console.err.println("Missing password")
      return null
    } else  {
      import fr.inra.mig.cdxws.db._

      val userName = cmdSelector.strOptions.get(StrOption.userName).head
      val password = cmdSelector.strOptions.get(StrOption.password).head
      CadixeDB.initSession(Some(props))
      CadixeDB.getUserByLogin(userName) match {
        case Some(u) =>
          Console.err.println("User already exists")
          return null
        case None =>
          val user = User(userName, password, false, true, "");
          CadixeDB.users.insert(user)
          return user
      }
    }
  }


  def resolveCampaign(cmdSelector : CommandSelector) : Option[Campaign] = {
    import fr.inra.mig.cdxws.db._

    val campaignName = cmdSelector.strOptions.get(StrOption.campaignName)
    val campaignId = cmdSelector.intOptions.get(IntOption.campaignId)

    if (campaignId.isEmpty && campaignName.isEmpty) {
      Console.err.println("A campaign must be specified")
      None
    } else if (campaignId.nonEmpty) {
      CadixeDB.getCampaignById(campaignId.head) match {
        case Some(c) => Some(c)
        case None =>
          Console.err.println("Invalid specified campaign id")
          None
      }
    } else  {
      CadixeDB.getCampaignByName(campaignName.head) match {
        case Some(c) => Some(c)
        case None =>
          Console.err.println("Invalid specified campaign name")
          None
      }
    }
  }

  def resolveDocument(cmdSelector : CommandSelector) : Option[Document] = {
    import fr.inra.mig.cdxws.db._

    val docIntId = cmdSelector.intOptions.get(IntOption.docInternalId)
    val docExtId = cmdSelector.strOptions.get(StrOption.docExternalId)

    if (docIntId.isEmpty && docExtId.isEmpty) {
      Console.err.println("A document must be specified")
      None
    } else if (docExtId.nonEmpty) {
      CadixeDB.getDocumentByExternalId(docExtId.head) match {
        case Some(d) => Some(d)
        case None =>
          Console.err.println("Invalid specified document external id")
          None
      }
    } else {
      CadixeDB.getDocumentById(docIntId.head) match {
        case Some(d) => Some(d)
        case None =>
          Console.err.println("Invalid specified document internal id")
          None
      }
    }
  }

  def exportCampaign(props : java.util.Properties, cmdSelector : CommandSelector) : String = {

    import java.io.File
    import fr.inra.mig.cdxws.db._

    val dirPath = cmdSelector.strOptions.get(StrOption.outDirectory) match {
      case None =>
        //user current working dir if none specifed
        System.getProperty("user.dir")
      case Some(dirName) =>
        dirName
    }

    val format = cmdSelector.strOptions.get(StrOption.outFormat) match {
      case None =>
        OutFormat.CSV
      case Some(format) =>
        try {
          OutFormat.withName(format.toUpperCase)
        } catch { case ex : NoSuchElementException =>
            Console.err.println("Unrecognized output format!")
            return null
        }
    }


    val dir = new File(dirPath)
    if (!dir.isDirectory || ! dir.canRead || ! dir.canWrite) {
      Console.err.println("Invalid or unwritable output directory!")
      return null
    }

    CadixeDB.initSession(Some(props))

    val campaign = resolveCampaign(cmdSelector) match {
      case Some(c) => c
      case None => return null
    }

    val archiveBaseName = "aae_" + campaign.id + ".zip"
    val archiveAbsoluteName = dir.getAbsolutePath + "/" + archiveBaseName
    val archiveFile = new File(archiveAbsoluteName)

    if (archiveFile.exists) {
      Console.err.println("Output file already exists! " + archiveAbsoluteName)
      return null
    } else {
      val tempDir = Utils.createTempDir()
      val workingDirBaseName = "ExportAlvisAE"
      val workingDir = new File(tempDir.getAbsolutePath + "/" + workingDirBaseName + "/")
      workingDir.mkdir()
      format match {
        case OutFormat.CSV =>
          CVSFormatHandler.exportCampaignAnnotationAsCSV(workingDir.getAbsolutePath, campaign.id, true)
        case OutFormat.JSON =>
          JSONExporter.exportCampaign(workingDir.getAbsolutePath, campaign)
      }

      Utils.createZipFromFolder(workingDir.getAbsolutePath, archiveAbsoluteName, true)
    }
    archiveAbsoluteName
  }

}
