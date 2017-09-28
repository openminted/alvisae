/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig.cdxws.db

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

import org.squeryl._
import org.squeryl.dsl._

import org.squeryl.PrimitiveTypeMode._
import net.liftweb.json._
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import net.liftweb.json.JsonDSL._
import scala.collection.mutable.ListBuffer
import scala.util.parsing.json.JSON._

import CadixeDB._
import AnnotationSetType._

object Utils {

  // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

  val TEMP_DIR_ATTEMPTS : Int = 10

  def createTempDir() : File = {
    val baseDir = new File(System.getProperty("java.io.tmpdir"));
    val baseName = System.currentTimeMillis() + "-";

    val counter = 0 to TEMP_DIR_ATTEMPTS
    counter.foreach ( c => 
      {
        val tempDir = new File(baseDir, baseName + c)
        if (tempDir.mkdir()) {
          return tempDir
        }
      }
    )
    null
  }

  //
  def getRecurseFileList(file : File) : List[File] ={
    if (file.isDirectory) {
      file.listFiles.foldLeft(Nil : List[File]){(accu,f) => 
        getRecurseFileList(f) ++ accu
      }
    } else {
      List(file)
    }
  }

  //  create a Zip archive containing all files in the specified directory
  def createZipFromFolder(pathToFolder : String, archiveFileName : String, deleteSource : Boolean) {
    val folder = new File(pathToFolder)
    
    if (!folder.isDirectory) {
      throw new IllegalArgumentException("Source to be compressed is not a directory");
    }
    
    val baseName = folder.getParent()
    val f = new FileOutputStream(archiveFileName)
    val fileList = getRecurseFileList(folder)

    val zip = new ZipOutputStream(new BufferedOutputStream(f))
    fileList.foreach( f => {
        //add entry name (starting from the base folder)
        val entryName = f.getAbsolutePath().replaceFirst(baseName, "")
        val e = new ZipEntry(entryName)
        zip.putNextEntry(e)
        
        //add data
        val data = new Array[Byte](4096)
        val origin = new BufferedInputStream(new FileInputStream( f ));
        Iterator.continually( origin.read(data) )
        .takeWhile(_ != -1)
        .foreach ( count =>  zip.write(data, 0, count) )
      
        zip.flush();
        origin.close();
      }
    )
    zip.close();
    
    if (deleteSource) {
      fileList.foreach( f => f.delete )
      folder.delete
    }
    
  }
  
  def getUserByLogin(userLogin : String ) : User = {
    val user =  CadixeDB.getUserByLogin(userLogin) match {
      case Some(u) => u
      case None => null
    }
    user
  }

  def getUserById(user_id : Long ) : User = {
    val user =  CadixeDB.getUserById(user_id) match {
      case Some(u) => u
      case None => null
    }
    user
  }
  
  def getRootUser() : User = {
    val user = CadixeDB.getRootUser() match {
      case Some(u) => u
      case None => null
    }
    user
  }

  def getCampaignByName(name : String ) : Campaign = {
    val campaign = CadixeDB.getCampaignByName(name) match {
      case Some(c) => c
      case None => null
    }
    campaign
  }

  def getCampaignById(campaign_id : Long ) : Campaign = {
    val campaign =  CadixeDB.getCampaignById(campaign_id) match {
      case Some(c) => c
      case None => null
    }
    campaign
  }


  def assignUsersToCampaign(campaign_id : Long, assignees : Set[Long] )  {
    val documents = from(campaign_documents)( (cd) =>
      where(cd.campaign_id === campaign_id)
      select(cd))

    assignees.foreach(
      user_id => {
        CadixeDB.campaign_annotators.insert(CampaignAnnotator(campaign_id,user_id))
        documents.foreach(
          cdoc => {
            CadixeDB.document_assignment.insert(DocumentAssignment(campaign_id, user_id, cdoc.doc_id))
          })
      }
    )
  }

  def unassignUsersToCampaign(campaign_id : Long, assignees : Set[Long] )  {
    val documents = from(campaign_documents)( (cd) =>
      where(cd.campaign_id === campaign_id)
      select(cd))

    assignees.foreach(
      user_id => {
        CadixeDB.campaign_annotators.deleteWhere( ca => ca.campaign_id === campaign_id and ca.user_id === user_id)
        
        documents.foreach(
          cdoc => {
            CadixeDB.document_assignment.deleteWhere(da => da.campaign_id === campaign_id and da.user_id === user_id and da.doc_id === cdoc.doc_id)
          })
      }
    )
  }  
  
  // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  case class Properties(var props : Map[String,List[String]])

  trait PropsGenerator {
    def getUpdatedProps(properties : Properties) : (Boolean, Properties)
  }

  class AddHLinkPropsGenerator(prefix :String, suffix : String) extends PropsGenerator {

    def getUpdatedProps(properties : Properties) = {
      var updated = false;
      var result = properties;

      result.props.get("shortName") match {
        case Some(shortname) => {
            if (shortname.length>0) {
              result.props += "hlink" -> List(prefix + shortname(0) + suffix)
              updated = true;
            }
          }
        case None => ;
      }
      (updated, result)
    }
  }

  def updateDocumentProps(campaignId : Long, generator : PropsGenerator) {
    import net.liftweb.json.Serialization.write
    implicit val formats = Serialization.formats(NoTypeHints)

    val campaign = campaigns.lookup(campaignId).get

    //retrieve documents in the campaign
    val docs = from(documents,campaign_documents)((d,cd) =>
      where(cd.campaign_id===campaign.id and d.id === cd.doc_id)
      select(d)
    )

    //loop over every documents of the campaign
    docs.foreach(d => {
        val parsed = net.liftweb.json.JsonParser.parse("{ \"props\": " + d.props + " }")
        val properties = parsed.extract[Properties]
        //generate new properties
        val (updated, upProps) = generator.getUpdatedProps(properties)
        Console.println(upProps);
        if (updated) {
          //update Document in DB if necessary
          d.props = write(upProps.props)
          documents.update(d)
        }
      })
  }

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

  def processTextFile(filepath : String , campaign : Campaign, task : TaskDefinition, ownerId : Long, userId : Long, assignees : scala.collection.mutable.Set[Long]) = {
    import net.liftweb.json.Serialization.write
    implicit val formats = Serialization.formats(NoTypeHints)

    val fileNameParser = """(?:.*/)?([^/]+)(?:\.)(.+)""".r
    val sectionTitleParser = """<(h\d)>(.+)</(h\d)>""".r

    val fileNameParser(shortFileName, extension) = filepath

    var aNum = 0;
    //Formatting annotation
    var formatAnnotations = ListBuffer[TextAnnotation]()
    val sb = new StringBuilder()
    val source = io.Source.fromFile(filepath)
    for(line <- source.getLines()) {

      try {
        val sectionTitleParser(openingtag, sectionTitle, closingtag) = line
        val startPos = sb.length
        sb.append(sectionTitle)
        val endPos = sb.length
        aNum += 1
        formatAnnotations += TextAnnotation("fmt-" + aNum, Map(), List(List(startPos, endPos)), openingtag, None)

      } catch {
        case ex:MatchError =>
          sb.append(line)
          aNum += 1
          val currLen = sb.length
          formatAnnotations += TextAnnotation("fmt-" + aNum, Map(), List(List(currLen, currLen)),"br", None)
      }
    }
    source.close()

    //add imported file path to the Document properties
    val properties = Map("filePath" -> List(filepath), "shortName" -> List(shortFileName))

    val doc =  Document(ownerId,
                        write(properties),
                        sb.toString,
                        "", //comment
                        shortFileName)
    documents.insert(doc)

    campaign_documents.insert(CampaignDocument(campaign.id,doc.id, None))

    assignees.foreach( aId => document_assignment.insert(DocumentAssignment(campaign.id, aId, doc.id)) )

    val formatAnnSet = AnnotationSet(
      task.id,
      doc.id,
      ownerId,
      campaign.id,
      write(formatAnnotations),
      write(Nil),
      write(Nil),
      true,
      0,
      HtmlAnnotation,
      "HTML formatting",
      CadixeDB.now(), 
      Some(CadixeDB.now()),
      None)

    annotation_sets.insert(formatAnnSet)

    doc
  }



}
