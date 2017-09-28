/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2014.
 *
 */
package fr.inra.mig.cdxws.db

import org.squeryl._
import org.squeryl.dsl._
import fr.inra.mig.cdxws.api.RestAPI
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.UnsupportedEncodingException
import org.squeryl.PrimitiveTypeMode._

import CadixeDB._
import AnnotationSetType._


object JSONExporter {

  def getReferencedAnnotationSets(referentAnnotationSets : List[AnnotationSet]) : List[AnnotationSet] ={
     
    def getReferencedAnnotationSetIds(referentAnnotationSetIds : List[Long]) : List[Long] = {
      val referencedAnnotationSetIds = from(annotationset_dependencies)(
        (asd) =>
        where( (asd.referent_id in { referentAnnotationSetIds })
              and (asd.referred_id notIn { referentAnnotationSetIds }))
        .select(asd.referred_id))
      .distinct.toList 
    
      if (referencedAnnotationSetIds.isEmpty) {
        referentAnnotationSetIds 
      }  else {
        referentAnnotationSetIds ++ getReferencedAnnotationSetIds(referencedAnnotationSetIds)
      }
    }  
   
    val referentAnnotationSetIds = referentAnnotationSets.map(_.id)
    val allReferencedIds =  getReferencedAnnotationSetIds(referentAnnotationSetIds) -- referentAnnotationSetIds
    val referencedAnnotationSets = from(annotation_sets)(
      (as) =>
      where(as.id in {allReferencedIds} )
      .select(as))
    .distinct.toList
    
    referencedAnnotationSets
  }
  
  def printToFile(f: java.io.File)(op: java.io.PrintWriter => Unit) {
    val p = new java.io.PrintWriter(f, "utf-8")
    try { op(p) } finally { p.close() }
  }

  def exportCampaign(outputDir : String, campaign : Campaign ) {
    import net.liftweb.json.JsonAST.JObject
    import net.liftweb.json._
    import net.liftweb.json.JsonDSL._
    import net.liftweb.json.Serialization.write
    import net.liftweb.json.Printer.pretty
    
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
    
    val campaign_id = campaign.id
    
    //retrieve all documents associated to the Campaign
    val docToExport = from(documents,campaign_documents)(
      (d,cd) =>
      where(    cd.campaign_id === campaign_id
            and cd.doc_id === d.id)
      .select(d)
      .orderBy(cd.doc_id)).toList
    
    var nbFile=0;
    docToExport.foreach{ doc =>
      val annotationSetToExport = from(annotation_sets)(
        (as) =>
        where( as.campaign_id === campaign_id
              and as.doc_id === doc.id 
              and as.head === true)
        .select(as)
        .orderBy(as.user_id, as.task_id)).distinct.toList

      val referencedAnnotationSets = getReferencedAnnotationSets(annotationSetToExport);
      val allAnnotationSets = annotationSetToExport ++ referencedAnnotationSets
           
      //ensure props contain doc.external_id
      if (doc.external_id.nonEmpty) {
        val parsed = net.liftweb.json.JsonParser.parse("{ \"props\": " + doc.props + " }")
        val sourceProps = parsed.extract[Utils.Properties]
        val externalId = doc.external_id.head
        sourceProps.props.get(JSONImporter.DOCPROP_EXTERNALDOCID) match {
          case Some(ids) => {
              //prepend new value to the existing ones
              if (! ids.head.equals(externalId)) {
                sourceProps.props -- List(JSONImporter.DOCPROP_EXTERNALDOCID)
                sourceProps.props += JSONImporter.DOCPROP_EXTERNALDOCID ->  (externalId :: ids)
              }
            }
          case None => sourceProps.props += JSONImporter.DOCPROP_EXTERNALDOCID -> List(externalId);
        }        
        doc.props = write(sourceProps.props);
      }
  
      val json = RestAPI.json_document(doc, campaign.schema, None, allAnnotationSets, List(), Set())
      try {
        val docfilename = "aaeDocument_c" + campaign_id + "_d" + doc.id + ".json"
        val fos = new FileOutputStream(outDir.getAbsolutePath() + File.separatorChar + docfilename)
        val out = new OutputStreamWriter(fos, "utf-8")
          
        out.append(pretty(render(json)))
        out.close()
        nbFile+=1
        
        Console.err.print("\t" + docfilename + " containing "+ allAnnotationSets.length + " AnnotationSet(s)\n")
      } catch  {
        case ex:UnsupportedEncodingException =>
          ex.printStackTrace(Console.err)
        case ex:FileNotFoundException =>
          ex.printStackTrace(Console.err)
        case ex:IOException =>
          ex.printStackTrace(Console.err)
      }
    }
    Console.err.println("\n " + nbFile + " file(s) exported.")

    
    printToFile(new File(outDir.getAbsolutePath() + File.separatorChar + "tasks.csv"))(p => {
        from(task_definitions)(
          (td) =>
          where( td.campaign_id === campaign_id)
          .select(td)
          .orderBy(td.id)).toList.foreach(
          taskDef => p.println(taskDef.id +"\t" + taskDef.name)
        )
      })
    
    printToFile(new File(outDir.getAbsolutePath() + File.separatorChar + "users.csv"))(p => {
        from(users,campaign_annotators)(
          (u,ca) =>
          where( ca.campaign_id === campaign_id
                and u.id === ca.user_id)
          .select(u)
          .orderBy(u.id)).toList.foreach(
          user => p.println(user.id +"\t" + user.login)
        )
      })
  }
}
