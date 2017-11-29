/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig.cdxws.db

import scala.collection.mutable.ListBuffer
import org.squeryl._
import org.squeryl.dsl._

import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.UnsupportedEncodingException
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.util.regex.Pattern
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import org.apache.commons.vfs.FileNotFoundException
import org.squeryl.PrimitiveTypeMode._

import CadixeDB._
import AnnotationSetType._


object CVSFormatHandler {

  val FIELD_SEPARATOR = ", ";
  val FIELD_INNERDELIMITER = "'";
  val FIELD_DELIMITER = "\"";
  val LIST_SEPARATOR = "; ";
  val LINE_TERMINATOR = "\n";
  val FRAGMENT_SEPARATOR = "";

  //Format for Comma Separated Value Export/Import
  object CVSLine {
    case class Field(name :String, classRef : Class[_], mandatory : Boolean)
    
    //fields name & type
    val CAMPAIGN_ID = Field("CAMPAIGN_ID", classOf[Long], false)
    val DOC_ID = Field("DOC_ID", classOf[Long], false)
    val USER_ID = Field("USER_ID", classOf[Long], false)
    val ANNSET_ID = Field("ANNSET_ID", classOf[Long], false)
    val ANNSET_TYPE = Field("ANNSET_TYPE", classOf[String], false)
    val TASK_ID = Field("TASK_ID", classOf[Long], false)
    val ANN_ID = Field("ANN_ID", classOf[String], true)
    val ANN_KIND = Field("ANN_KIND", classOf[String], true)
    val ANN_TYPE = Field("ANN_TYPE", classOf[String], true)
    val ANN_DETAILS = Field("ANN_DETAILS", classOf[String], true)
    val ANN_PROPS = Field("ANN_PROPS", classOf[String], true)
    val ANN_TEXT = Field("ANN_TEXT", classOf[String], false)
    //fields in the order they are exported
    val CSVFields = List(CAMPAIGN_ID,
                         DOC_ID, 
                         USER_ID, 
                         ANNSET_ID,
                         ANNSET_TYPE, 
                         TASK_ID, 
                         ANN_ID, 
                         ANN_KIND, 
                         ANN_TYPE, 
                         ANN_DETAILS, 
                         ANN_PROPS, 
                         ANN_TEXT)

    val fieldNames = CSVFields.map( f => f.name -> f ).toMap
    val mandatoryFields = CSVFields.foldLeft(List[Field]()) {
      (keptOnes, field) =>  if (field.mandatory) {
        field :: keptOnes
      } else {
        keptOnes
      }
    }.toSet
    
    def getField(name : String) : Option[Field] ={
      fieldNames.get(name)
    }

    def missingMandatoryFields(values : Set[Field]) : Set[Field] = {
      mandatoryFields.foldLeft(Set[Field]()) 
      {
        (missingOnes, field) =>  if (! values.contains(field)) {
          missingOnes + field 
        } else {
          missingOnes
        }
      }
    }
    
    def getHeader() : String = {
      val line = new StringBuilder()
      var once = false
      CSVFields.foreach(f => {
          if (once) {
            line.append(FIELD_SEPARATOR)
          } else {
            once = true;
          } 
          line.append(f.name)
        } 
      )
      line.toString
    }
    
    def dumpLine(values : Map[Field, Any]) : String = {
      val line = new StringBuilder()
      var once = false
      CSVFields.foreach(f => {
          if (once) {
            line.append(FIELD_SEPARATOR)
          } else {
            once = true;
          } 
          
          values.get(f) match {
            case Some(value:String) =>
              val escapedValue = value.replaceAll("\"", "\\\"")
              line.append(FIELD_DELIMITER).append(escapedValue).append(FIELD_DELIMITER)
            case Some(value:Long) =>
              line.append(value)
            case None =>  
            case Some(value) =>  
              line.append(FIELD_DELIMITER).append(value).append(FIELD_DELIMITER)
          }
        }) 
      line.toString
    }
  }
  



  /**
   * Retrieve the text of the specified TextAnnotation, and a string representation of its Fragment(s) coordinates
   */
  def getTextAnnotationTextNCoord(netText : String, annotation : TextAnnotation, listSeparator : String) : (String, String) = {
    var once = false
    val coord = new StringBuilder()
    val texte = new StringBuilder()
    annotation.text.foreach( frag => {
        if (once) {
          texte.append(FRAGMENT_SEPARATOR);
        } else {
          once = true;
        }
        val start = frag(0)
        val end = frag(1)
        coord.append("[").append(start).append(listSeparator).append(end).append("]")
        texte.append(netText.substring(start, end));
      }
    )
    return (texte.toString, coord.toString)
  }


  def importFromCSV(inputFilePath : String) {

    val lines = readCSVFile(inputFilePath, checkCSVLine _) 
    
    if (lines!=null){
      
      //Id of the declared annotations
      //FIXME retrieve owning AnnotationSet 
      val annotationIds = lines.map( l => l.get(CVSLine.ANN_ID).head.toString).toSet
      
      //Data consistancy check 
      lines.foreach(values => {
          val kind = values .get(CVSLine.ANN_KIND).head
          
          val annRef = kind match {
            case AnnotationKind.TextAnnotation =>
              List() 
            case AnnotationKind.GroupAnnotation =>
              val components = values.get(CVSLine.ANN_DETAILS).head.asInstanceOf[List[AnnotationReference]]
              components
            case AnnotationKind.RelationAnnotation =>
              val arguments = values.get(CVSLine.ANN_DETAILS).head.asInstanceOf[Map[String, AnnotationReference]]
              arguments.values.toList
          }
          //FIXME check also AnnotationSet Ids
          annRef.foreach(annRef => if(!annotationIds.contains(annRef.ann_id)) {
              Console.err.println("Unknown referenced Annotation \""+ annRef.ann_id +"\" in input file")
              Console.err.println("Import Aborted!")
              return false
            })
          
        } 
      )
    
      lines.foreach(l =>Console.out.println(l))
    }
    
     
  }
  
  /**
   * Export all annotation contained within a Campaign as CSV text files
   * @param outputDir absolute path of the directory where the files will be created.
   * @param campaignId identifier of the Campaign to be exported.
   */
  def exportCampaignAnnotationAsCSV(outputDir : String, campaignId : Long, exportDocuments : Boolean) {
    Console.println("")

    //parameter checking
    val outDir = (
      Option(outputDir) match {
        case None =>
          Console.err.println("Missing parameter output directory! Export aborted.")
          return Unit
        case Some(dir) =>
          val outDir = new File(dir)
          if (! outDir.exists || !outDir.isDirectory) {
            Console.err.println("Invalid parameter output directory " + dir + " ! Export aborted.")
            return Unit
          } else if (! outDir.canWrite) {
            Console.err.println("Unable to write in output directory " + dir + " ! Export aborted.")
            return Unit
          }
          outDir
      }
    )
    val campaign = (
      Option(campaignId) match {
        case None =>
          Console.err.println("Missing parameter Campaign Id! Export aborted.")
          return Unit
        case Some(cId) =>
          try {
            campaigns.lookup(cId).get
          } catch {
            case ex:NoSuchElementException =>
              Console.err.println("Unknown campaignId (" +  campaignId + ")! Export aborted")
              return Unit
          }
      }
    )

    //retrieve all head Annotation Sets (except Formatting ones) associated to documents within the Campaign
    val annotationSetToExport = from(documents,document_assignment,annotation_sets)((d,da,as) =>
      where(    da.campaign_id === campaign.id
            and da.doc_id === d.id
            and as.doc_id === da.doc_id
            and as.campaign_id === da.campaign_id
            and as.user_id === da.user_id
            and as.head === true
            and as.`type` <> HtmlAnnotation).select(d, da, as).orderBy(da.doc_id, da.user_id, as.`type`, as.task_id)).distinct.toList

    import net.liftweb.json._
    import net.liftweb.json.JsonDSL._
    import net.liftweb.json.Serialization.write

    var previousDoc : Document = null;

    try {
      var out : OutputStreamWriter = null
      var nbAS = 0
      var nbText = 0
      var nbGrp = 0
      var nbRel = 0
      var outputfilename : String = null

      var nbFile = 0
      //loop over Annotation Sets
      for(d_as <- annotationSetToExport) {
        val d = d_as._1
        val da = d_as._2
        val as = d_as._3

        if (out==null || previousDoc.id!=d.id) {

          //display some detail about the last generated file
          if (out!=null) {
            out.close()
            Console.err.print("\t" + nbText + " TextAnnotation(s)\t"+ nbGrp + " Group(s)\t"+ nbRel + " Relation(s)\twithin "+ nbAS + " AnnotationSet(s)\n")
          }
          nbText = 0
          nbGrp = 0
          nbRel = 0
          nbAS = 0

          if (exportDocuments) {
            val docfilename = "aaeDocument_c" + da.campaign_id + "_d" + da.doc_id + ".txt"
            val fos = new FileOutputStream(outDir.getAbsolutePath() + File.separatorChar + docfilename)
            val out = new OutputStreamWriter(fos, "utf-8")
            out.append("DocumentId=").append(d.id.toString).append(LINE_TERMINATOR)
            out.append("CampaignId=").append(da.campaign_id.toString).append(LINE_TERMINATOR)
            out.append("Description=").append(d.description).append(LINE_TERMINATOR)
            out.append("Content=").append(d.contents).append(LINE_TERMINATOR)
            out.close()
          }
          
          //An output file contains all Annotation Sets related to one single document
          outputfilename = "aaeAnnotations_c" + da.campaign_id + "_d" + da.doc_id + ".cvs"
          Console.err.print("New File: " + outputfilename)

          val fos = new FileOutputStream(outDir.getAbsolutePath() + File.separatorChar + outputfilename)
          out = new OutputStreamWriter(fos, "utf-8")
          out.append("#DocumentId=").append(d.id.toString).append(FIELD_SEPARATOR)
          out.append("DocumentDescription=").append(FIELD_DELIMITER).append(d.description).append(FIELD_DELIMITER).append(LINE_TERMINATOR).append(LINE_TERMINATOR)
          
          out.append(CVSLine.getHeader()).append(LINE_TERMINATOR)
          nbFile += 1
        }

        previousDoc = d
        nbAS += 1

        val annSetPrefix = Map(
          CVSLine.CAMPAIGN_ID -> campaignId,
          CVSLine.DOC_ID -> d.id,
          CVSLine.USER_ID -> da.user_id,
          CVSLine.ANNSET_ID -> as.id,
          CVSLine.ANNSET_TYPE -> as.`type`.toString,
          CVSLine.TASK_ID -> as.task_id
        )
        
        //Text Annotations
        val txt_ann = parse(as.text_annotations).extract[List[TextAnnotation]]
        txt_ann.foreach(annotation =>
          {
            val (texte, coord) = getTextAnnotationTextNCoord(previousDoc.contents, annotation, LIST_SEPARATOR)
            
            implicit val formats = Serialization.formats(NoTypeHints)
            import net.liftweb.json.Serialization.write
            val props = write(annotation.properties)
            
            val mline =  annSetPrefix ++ Map(CVSLine.ANN_ID -> annotation.id,  
                                             CVSLine.ANN_KIND -> AnnotationKind.TextAnnotation.toString,
                                             CVSLine.ANN_TYPE -> annotation.`type`,
                                             CVSLine.ANN_DETAILS -> coord,  
                                             CVSLine.ANN_PROPS -> props,
                                             CVSLine.ANN_TEXT -> texte)
            out.append(CVSLine.dumpLine(mline)).append(LINE_TERMINATOR)
            nbText += 1
          }
        )

        //Group Annotations
        val grp_ann = parse(as.groups).extract[List[Group]]
        grp_ann.foreach(annotation =>  {
            val dline = new StringBuilder();
            val details = {
              annotation.group.foreach( annRef =>
                {
                  annRef.set_id match {
                    case Some(annSetId) => dline.append(annSetId).append(".")
                    case _ =>
                  }
                  dline.append(FIELD_INNERDELIMITER).append(annRef.ann_id).append(FIELD_INNERDELIMITER).append(LIST_SEPARATOR)
                })
              dline.toString
            }

            val mline =  annSetPrefix ++ Map(CVSLine.ANN_ID -> annotation.id,  
                                             CVSLine.ANN_KIND -> AnnotationKind.GroupAnnotation.toString,
                                             CVSLine.ANN_TYPE -> annotation.`type`,
                                             CVSLine.ANN_DETAILS -> dline.toString)
            out.append(CVSLine.dumpLine(mline)).append(LINE_TERMINATOR)
            
            nbGrp += 1
          }
        )

        //Relation Annotations
        val rel_ann = parse(as.relations).extract[List[Relation]]
        rel_ann.foreach(annotation => {
            val dline = new StringBuilder();
            val details = {
            
              annotation.relation.map( { case(role, annRef) =>
                    dline.append(FIELD_INNERDELIMITER).append(role).append(FIELD_INNERDELIMITER).append(":")
                    annRef.set_id match {
                      case Some(annSetId) => dline.append(annSetId).append(".")
                      case _ =>
                    }
                    dline.append(FIELD_INNERDELIMITER).append(annRef.ann_id).append(FIELD_INNERDELIMITER).append(LIST_SEPARATOR)
                }
              )
            }

            val mline =  annSetPrefix ++ Map(CVSLine.ANN_ID -> annotation.id,  
                                             CVSLine.ANN_KIND -> AnnotationKind.RelationAnnotation.toString,
                                             CVSLine.ANN_TYPE -> annotation.`type`,
                                             CVSLine.ANN_DETAILS -> dline.toString)
            out.append(CVSLine.dumpLine(mline)).append(LINE_TERMINATOR)
            nbRel += 1
          }
        )

      }

      out.close()
      //display some details about the last generated file
      Console.err.print("\t" + nbText + " TextAnnotation(s)\t"+ nbGrp + " Group(s)\t"+ nbRel + " Relation(s)\twithin "+ nbAS + " AnnotationSet(s)\n")

      //display some detail about the overall exportation process
      Console.err.println("\n " + nbFile + " file(s) exported.")

    } catch  {
      case ex:UnsupportedEncodingException =>
        ex.printStackTrace(Console.err)
      case ex:FileNotFoundException =>
        ex.printStackTrace(Console.err)
      case ex:IOException =>
        ex.printStackTrace(Console.err)
    }

  }

  import scala.collection.mutable

  def readCSVFile(inputFilePath : String, lineChecker : (Int, mutable.Map[CVSLine.Field, Any]) => Boolean) : List[mutable.Map[CVSLine.Field, Any]] = {
    //field delimited by double quote, separated by comma
    val delimitedField = Pattern.compile("\\s*\"((?:[^\"]|\\\")*?)\"\\s*,(.*?)")
    //field not delimited by double quote, separated by comma
    val undelimitedField = Pattern.compile("\\s*([^,]*?)\\s*,(.*?)")
    
    val source = io.Source.fromFile(inputFilePath)
    val cvsLines = source.getLines()
    
    var headerFound = false
    var fileFields : Map[Int, Option[CVSLine.Field]] = null

    import scala.collection.mutable
    val lines = ListBuffer.empty[mutable.Map[CVSLine.Field, Any]]

    //read all data from file
    var lineNum = 0
    cvsLines.foreach( line => 
      {
        lineNum+=1
      
        if (line.trim.isEmpty) {
        } else if (line.startsWith("#")) {
        } else if (!headerFound) {
          //get the name of the fields declared in the file and their order
          val fieldNames = line.split(FIELD_SEPARATOR.trim).map(f => f.trim).toList
        
          //get the corresponding known fields 
          fileFields = fieldNames.zipWithIndex.map{ 
            case(name, index) => {
                val f = CVSLine.getField(name)
                f match {
                  case None =>
                    Console.err.println("Warning: Unknown field # "+index+" '" + name +"' in input file, it will be ignored during import.")
                  case _ =>
                }
                index -> f
              }}.toMap
        
          //filter to the known fields only 
          val declaredField = fileFields.values.foldLeft(List[CVSLine.Field]()) {
            (declaredOnes, f) =>  f match { 
              case Some(field) =>
                field :: declaredOnes
              case _ =>
                declaredOnes
            }
          }.toSet

          //check if any mandatory fields is not declared in the header
          val missing = CVSLine.missingMandatoryFields(declaredField)
          if (! missing.isEmpty) {
            missing.foreach( missingField => Console.err.println("Error: Missing field '" + missingField.name +"' in input file header at line #" + lineNum))
            Console.err.println("Import Aborted!")
            return null
          }

          headerFound = true
        } else {
          val values = mutable.Map.empty[CVSLine.Field, Any]
        
          //add a final field separator given the regex expect it
          var remaining = line + FIELD_SEPARATOR
          var fieldNum = 0

          while(!remaining.isEmpty) {
          
            val dm = delimitedField.matcher(remaining)
            if (dm.matches()) {
              val value = dm.group(1).trim
              fileFields(fieldNum) match {
                case Some(f) if (!value.isEmpty) => values += f -> value
                case _ =>
              }
              remaining = dm.group(2)
            } else {
              val um = undelimitedField.matcher(remaining)
              if (um.matches()) {
                val value = um.group(1).trim
                fileFields(fieldNum) match {
                  case Some(f) if (!value.isEmpty) => values += f -> value
                  case _ =>
                }
                remaining = um.group(2)
              }   else {
                //unable to process the field
                remaining = ""
              }
            }
            fieldNum +=1
            remaining.trim
          }
        
          if (!lineChecker(lineNum, values)) {
            return null
          }
          
          lines += values
        }
      }
    )
    source.close()

    lines.toList
  }
  
  def checkCSVLine(lineNum : Int, values : mutable.Map[CVSLine.Field, Any]) : Boolean = {
    val fragment = Pattern.compile("\\s*\\[\\s*(\\d+)\\s*;\\s*(\\d+)\\s*\\]\\s*") 
    val component = Pattern.compile("\\s*(?:(\\d+)\\s*\\.)?\\s*'((?:[^'])+)'\\s*;\\s*")
    val argument = Pattern.compile("\\s*'((?:[^'])+)'\\s*:\\s*(?:(\\d+)\\s*\\.)?\\s*'((?:[^'])+)'\\s*;\\s*")

    //check for mandatory field
    val missing = CVSLine.missingMandatoryFields(values.keySet.toSet)
    if (! missing.isEmpty) {
      missing.foreach( missingField => Console.err.println("Error: Missing value for field '" + missingField.name +"' in input file at line #" + lineNum))
      Console.err.println("Import Aborted!")
      return false
    }
          
    //Check for Kind
    val kindName = values.get(CVSLine.ANN_KIND).head.toString
    val kind = 
      try {
        val kind = AnnotationKind.withName(kindName)
        values.put(CVSLine.ANN_KIND, kind)
        kind
            
      } catch {
        case ex : NoSuchElementException =>
          Console.err.println("Unknown Annotation Kind '"+ kindName +"' in input file at line #" + lineNum)
          Console.err.println("Import Aborted!")
          return false
      }

    //Check for AnnotationSetType
    values.get(CVSLine.ANNSET_TYPE) match {
      case Some(annSetTypeName : String) =>
        try {
          val annSetType = AnnotationSetType.withName(annSetTypeName)
          values.put(CVSLine.ANNSET_TYPE, annSetType)
        } catch {
          case ex : NoSuchElementException =>
            Console.err.println("Unknown AnnotationSetType '"+ annSetTypeName +"' in input file at line #" + lineNum)
            Console.err.println("Import Aborted!")
            return false
        }
      case _ =>
        //should not happen
        Console.err.println("Missing AnnotationSetType in input file at line #" + lineNum)
        Console.err.println("Import Aborted!")
        return false
    }
        
    kind match {
      
      case AnnotationKind.TextAnnotation =>
        //retrieve fragments coordinates
        {
          val frags = values.get(CVSLine.ANN_DETAILS).head.toString

          val fm = fragment.matcher(frags)
          var prevMatchEnd = 0
          val fragments = Iterator.continually(fm.find )
          .takeWhile(_ == true)
          .map( found =>  {
              if (fm.start()>prevMatchEnd) {
                Console.err.println("Invalid Fragment value '"+ frags +"' in input file at line #" + lineNum)
                Console.err.println("Import Aborted!")
                return false
              } else {
                prevMatchEnd =fm.end()
              }
              List(fm.group(1).toInt, fm.group(2).toInt) 
            }).toList
      
          if (fragments.size>0) {
            values.put(CVSLine.ANN_DETAILS, fragments)
          } else {
            Console.err.println("Invalid Fragment value '"+ frags +"' in input file at line #" + lineNum)
            Console.err.println("Import Aborted!")
            return false
          }
        }
      case AnnotationKind.GroupAnnotation =>
        //retrieve Group components (Annotation reference)
        {
          val comps = values.get(CVSLine.ANN_DETAILS).head.toString
          val cm = component.matcher(comps)
          var prevMatchEnd = 0
          val components = Iterator.continually(cm.find )
          .takeWhile(_ == true)
          .map( found =>  {
              if (cm.start()>prevMatchEnd) {
                Console.err.println("Invalid Component value \""+ comps +"\" in input file at line #" + lineNum)
                Console.err.println("Import Aborted!")
                return false
              } else {
                prevMatchEnd =cm.end()
              }
              val annRef = cm.group(1) match {
                case annSetId : String =>
                  new AnnotationReference(cm.group(2), Some(annSetId.toInt))
                case _ =>
                  new AnnotationReference(cm.group(2), None)
              }
              annRef
            }).toList
      
          if (components.size>0) {
            values.put(CVSLine.ANN_DETAILS, components)
          } else {
            Console.err.println("Empty Group of invalid Component value \""+ comps +"\" in input file at line #" + lineNum)
            Console.err.println("Import Aborted!")
            return false
          }
          
        }
        
      case AnnotationKind.RelationAnnotation =>
        //retrieve Relation arguments (Annotation reference)
        {
          val args = values.get(CVSLine.ANN_DETAILS).head.toString
          val am = argument.matcher(args)
          var prevMatchEnd = 0
          val arguments = Iterator.continually(am.find )
          .takeWhile(_ == true)
          .map( found =>  {
              if (am.start()>prevMatchEnd) {
                Console.err.println("Invalid Argument value \""+ args +"\" in input file at line #" + lineNum)
                Console.err.println("Import Aborted!")
                return false
              } else {
                prevMatchEnd =am.end()
              }
              
              val role = am.group(1)
              am.group(2) match {
                case annSetId : String =>
                  role -> new AnnotationReference(am.group(3), Some(annSetId.toInt))
                case _ =>
                  role -> new AnnotationReference(am.group(3), None)
              }
            }).toMap
      
          if (arguments.size>0) {
            values.put(CVSLine.ANN_DETAILS, arguments)
          } else {
            Console.err.println("Empty Relation of invalid Argument value \""+ args +"\" in input file at line #" + lineNum)
            Console.err.println("Import Aborted!")
            return false
          }
        }
      
    }

    return true
  }

}
