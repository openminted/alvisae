/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig.cdxws.db

import java.io.FileInputStream
import net.liftweb.json._
import org.apache.commons.vfs._
import scala.io.Source

import AnnotationSetType._

object BioNLP {
  
  private def get(archive : FileObject, fn : String) : FileObject = {
    val file = archive.getChild(fn)
    if(file  == null) { 
      assert(false, fn + "does not belong the archive")
      file
    } else { file }     
  }

  private def getopt(archive : FileObject, fn : String) = {
    val file = archive.getChild(fn)
    if(file == null) { None } else { Some(file) }
  }

  private def parse_entity_descr(descr : String) = {
    descr.split(" ") match {
      case Array(kind,st,ed) => (kind,st.toInt,ed.toInt)
      case _ => throw (new Exception("BioNLP parse error"))
    }
  }
  
  private def parse_argument(n : Int, arg_str : String) = {
    arg_str.split(':') match {
      case Array(x,y) => (x,AnnotationReference(y,None))
      case Array(x) => ("role"+n,AnnotationReference(x,None))
      case _ => throw (new Exception("BioNLP parse_argument: " + arg_str))
    }
  }
  
  private def parse_relation_descr(descr : String) = {
    descr.split(" ").toList match {
      case rtype :: rest => {
          val args = rest.foldLeft((Map() : Map[String,AnnotationReference],0))({ (accu,arg) => 
              (accu._1 + parse_argument(accu._2,arg),accu._2 + 1) })
          (rtype,args._1)
      }
      case _ => throw (new Exception("BioNLP parse error"))
    }
  }
  
  private def parse_entity(line : Array[String]) = line match {
    case Array(id,edescr,form) => { // the line encodes an entity
        val descr = parse_entity_descr(edescr)
        TextAnnotation(id,Map(),List(List(descr._2,descr._3)),descr._1, None)
      }
    case _ => throw (new Exception("BioNLP parse error"))
  }
  
  private def parse_relation(line : Array[String]) = line match {
    case Array(id,rdescr) => { // the line encodes a relation
        val descr = parse_relation_descr(rdescr)
        Relation(id,Map(),descr._1,descr._2, None)
      }
      
  }
    
  private def parse_annotation_file(src : Source) = {
    val split_lines = src.getLines.map(_.split('\t')).toList
    val text_annotations = split_lines.filter{l => l.length == 3}.map(parse_entity(_))
    val relations = split_lines.filter{l => l.length == 2}.map(parse_relation(_))
    (text_annotations, relations)
  }
  
  private def regenerate_ids(x : Tuple3[String,List[TextAnnotation],List[Relation]]) = x match {
    case Tuple3(text, text_annotations,relations) => {
        
        import Substitution._
        val dict1 = text_annotations.foldLeft(
          Map() : Map[String,String]){ 
          (accu,ta) => accu + Pair(ta.id, java.util.UUID.randomUUID().toString()) }
        val dict2 = relations.foldLeft(
          dict1){ 
          (accu,re) => accu + Pair(re.id, java.util.UUID.randomUUID().toString()) }
        (text,
         text_annotations.map(_.id_substitution(dict2)),
         relations.map(_.id_substitution(dict2)))
    }
  }
  
  def import_archive(user : User, schema : JValue, path : String, campaign_name : String) = {
    val fsman = VFS.getManager()
    val archive = fsman.resolveFile("tgz:" + path).getChildren()(0)
    val doc_ids = archive.getChildren.collect { 
      case f if f.getName.toString.endsWith(".txt") => 
        f.getName.getBaseName.dropRight(4)
    }
    
    def text_of_id(id : String) = {
      val file = get(archive, id + ".txt")
      val src = Source.fromInputStream(file.getContent.getInputStream)
      src.getLines.mkString("\n") 
    }
      
    def annot_of_id(id : String, ext : String) = {
      val file = getopt(archive, id + ext)
      val src = file map { f => Source.fromInputStream(f.getContent.getInputStream) }
      src.map(parse_annotation_file(_))
    }

    // A1 and A2 files are currently merged. They could also be put in two different
    // in two different annotationset. In that case, one should be cautious for two reasons
    // - first A2 may contain references to A1 annotations (so references in alvisAE annotations 
    //   should be fully qualified)
    // - second the generation of uuid cannot be made independently for the A1 and A2
    val documents = doc_ids map { 
      id => (text_of_id(id),
             annot_of_id(id, ".a1"),
             annot_of_id(id, ".a2")) } map {
      case Tuple3(t,Some (Pair(ta_a1,re_a1)),Some (Pair(ta_a2,re_a2))) =>
        Tuple3(t,ta_a1 ::: ta_a2,re_a1 ::: re_a2)
      case Tuple3(t,Some (Pair(ta_a1,re_a1)),None) =>
        Tuple3(t,ta_a1,re_a1)
      case Tuple3(t,None,Some (Pair(ta_a2,re_a2))) =>
        Tuple3(t,ta_a2,re_a2)
      case Tuple3(t,None,None) => Tuple3(t,Nil,Nil)
     } map(regenerate_ids(_))
     val campaign = CadixeDB.createCampaign(campaign_name, schema)
     val task = TaskDefinitionXMLImporter.createDefaultTaskDefinition(campaign)
     
     documents.foreach { case Tuple3(t,ta,re) => {
           val d = CadixeDB.createDocument(user, t, Map(), "", "")
           val as = CadixeDB.addUserAnnotationSet(d, user, campaign, task, ThirdPartyAnnotation, ta, re, Nil, "")
           CadixeDB.addDocument2Campaign(d, campaign)
         } 
     }
     campaign
  }
}
