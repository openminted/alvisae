/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig.cdxws.db

import net.liftweb.json.JsonAST.JObject
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import net.liftweb.json.Serialization.write




object AnnotationSetComparator {

  implicit val formats = Serialization.formats(NoTypeHints)
  
  
  def compareTextBound(asets : Set[AnnotationSet]) : JObject = {
    
    val toto = asets.map { as =>
      
      val textAnnotations = parse(as.text_annotations).extract[List[TextAnnotation]]

      
      //determine the global span that fragment(s) of the annotation are covering
      val spans = textAnnotations.map {  ta => 
        
        var spanStart = Int.MaxValue
        var spanEnd = Int.MinValue
        val annotationFragments = ta.text .map { fragment =>
          require (fragment.size == 2, "invalid fragment for annotation "+ ta.id+ " :" + fragment)
          
          val start = fragment.head
          val end = fragment(1)
          
          if (start<spanStart) spanStart=start
          if (end>spanEnd) spanEnd=end
          
        }

        (ta.id, spanStart, spanEnd)
      }
      
      val sortedSpans = spans.sortWith{ case (a, b) =>  
          //
          val samestart = a._2 < b._2
          if (!samestart) {
            samestart
          } else {
            //place first the longer segment if 2 segement start 
            a._3 > b._3
          }
      }
      
      Console.err.println
      sortedSpans.map { case(id, start, end) =>
          Console.err.println( id + " [" + start+ ", " + end + "]")
      }
      Console.err.println
      
    }
    
    ("empty" -> true)
  }
  
  //Opening or closing point of Annotation
  case class FrontierEvent(annotationSet_id :Long, annotation :TextAnnotation, position :Int, startEvent :Boolean)
  
  def compare(asets : Set[AnnotationSet]) : JObject = {
    
    val allEvents = asets.map { as =>
      
      val textAnnotations = parse(as.text_annotations).extract[List[TextAnnotation]]

      
      //determine the global span that fragment(s) of the annotation are covering
      val events = textAnnotations.map {  ta => 
        
        var spanStart = Int.MaxValue
        var spanEnd = Int.MinValue
        val annotationFragments = ta.text .map { fragment =>
          require (fragment.size == 2, "invalid fragment for annotation "+ ta.id+ " :" + fragment)
          
          val start = fragment.head
          val end = fragment.last
          
          if (start<spanStart) spanStart=start
          if (end>spanEnd) spanEnd=end
          
        }

        List(
          FrontierEvent(as.id, ta, spanStart, true),
          FrontierEvent(as.id, ta,  spanEnd, false)
        )
      }


      events.flatten
    }.toList.flatten
    
   
    //Order all events to be able to detect overlap (if 2 events are at same position, place first the starting event)
    val sortedEvents = allEvents.sortBy { fe => (fe.position, fe.startEvent) }
      

    //val openedSpan = new scala.collection.mutable.Queue[String]
    val blockEvents = new scala.collection.mutable.ListBuffer[FrontierEvent]
    
    var nbMembersInBlock : Int = 0
    
    val blocks = sortedEvents.map { fe =>
      if (fe.startEvent) {
        nbMembersInBlock += 1
        blockEvents += fe
        (0, 0, Nil)
      } else {
        nbMembersInBlock -= 1
        if (nbMembersInBlock == 0) {
          val blockMembers = blockEvents.toList
          //start position of first annotation of the block
          val start = blockMembers.head.position
          //end position of last annotation of the block
          val end = fe.position
          blockEvents.clear
          (start, end, blockMembers)
        } else {
          (0, 0, Nil)
        }
      }
    }.filter{ _._3.nonEmpty }.map{ case (start, end, blockMembers) =>       
        val members = blockMembers.map{ e => (e.annotationSet_id, e.annotation) }
        
        //check if there is conflicting annotation in the block
        
        val membersBySubBlock = members.groupBy{ case(setId, annotation) => setId } 
        
        //check if sub-blocks are identical
        val signatureBySubBlock = membersBySubBlock.map{ case(setId, groupedAnnotations) => 
            val annBySignature = groupedAnnotations.map { case(setId, ann) =>  
                // signature is a string used to detect "identical" annotation
                val signature = ann.text.flatten.foldLeft(""){case(acc, pos) => acc + pos + "_"} + "-" + ann.`type`
                (signature -> ann) }.toMap
            
            (setId, annBySignature)
        }
        
        val referenceSignatures = signatureBySubBlock.values.head.keySet        
        val identicalSubBlocks =  
          // more than one sub-bloc (only one AnnotationSet represented in the block is assumed as a conflict)
        (membersBySubBlock.size>1) &&    
        // each sub-bloc contain identical annotations
        (signatureBySubBlock.values.drop(1).foldLeft(true){ 
            case (acc, annBySignature) =>  acc && referenceSignatures == annBySignature.keySet })

        val producedBlocks = if (identicalSubBlocks) {
          
          //split into one block for each signature          
          val fromAllSetsGroupedBySignature = signatureBySubBlock.map{ case(setId, annBySignature) => annBySignature.map{ case(signature, ann) => (signature, setId, ann) } }.flatten
          .groupBy { case(signature, setId, ann) =>  signature }.values
          .map{ signSetAnn => signSetAnn.map{ case(signature, setId, ann) =>  (setId, ann) } }
          
          
          fromAllSetsGroupedBySignature.map{ setIdAndAnnotations => 
            //since all Annotations of this group share same signature, they have same fragments
            val refAnnotation = setIdAndAnnotations.head._2
            val start = refAnnotation.text.head.head
            val end = refAnnotation.text.last.last
   
            //allow automatic resolution only if properties are also identical (in values and order)
            val sameProps = setIdAndAnnotations.drop(1).foldLeft(true){ 
              case (acc, setIdAndAnn) => acc && refAnnotation.properties == setIdAndAnn._2.properties}
            
            (start, end, sameProps, setIdAndAnnotations.toList)
             
          }
        } else {
          //every annotations grouped in one single consolidation block 
          List((start, end, false, members))
        }

        producedBlocks
    }.flatten
    
    /*    
     blocks.zipWithIndex.map { case ((start, end, members), index)  => 
     Console.err.println(index + " : [" + start + ", " + end + "]")
     Console.err.println(members)
     Console.err.println()
     }
     */
    
     
     //Compute the Adjudication Level of higher order annotations.
    
    
     //Notes:
     // The Adjudication Level of TextAnnotation is 1, meaning that they can be immediately adjudicated
     // Relation and Group can only be adjudicated once all their components/arguments are themselves adjudicated. 
     // Hence their level is 1 above the top most Adjudication level of its components/arguments.
     //
     // Since Annotations belonging to other Annotation Sets than those compared do not have to be adjudicated, their level is 0.
     // Indeed, a Relation or Group referencing only Annotation from other AnnotationSets can be immediately adjudicated.
     //
    
    
     //Map of Annotation's order
     val  levelByAnnId = asets.map { as => 
        //init with text annotations whose Adjudication level is 1
        parse(as.text_annotations)
        .extract[List[TextAnnotation]]
        .map{ textAnnotation =>   AnnotationReference(textAnnotation.id) }
      }.flatten.map{ (_, 1L) }.toMap
    
     //get a mutable version of the map
     val levelByAnnotationId = collection.mutable.Map(levelByAnnId.toSeq: _*)
    
     val higherOrderAnnotationKind = asets.map { as =>
        val relations = parse(as.relations).extract[List[Relation]]
        val groups = parse(as.groups).extract[List[Group]]
      
        (relations.map{ relAnnotation =>  (relAnnotation.id, relAnnotation.kind) }) ++
        (groups.map{ relGroup => (relGroup.id, relGroup.kind) })
  
      }.flatten.toMap
    
     //Map of higher order annotations' members
     val higherOrderAnnotation = asets.map { as =>
        val relations = parse(as.relations).extract[List[Relation]]
        val groups = parse(as.groups).extract[List[Group]]
      
        val rmap = relations.map{ relAnnotation =>   
          (AnnotationReference(relAnnotation.id, Some(as.id))
           , relAnnotation.relation.values.toSet
          )}.toMap
      
        val gmap = groups.map{ relGroup =>   
          (AnnotationReference(relGroup.id, Some(as.id))
           , relGroup.group.toSet
          )}.toMap
      
        (rmap ++ gmap).toList
      }.flatten.toMap
    

     // function to recursively compute the order of an annotation 
     def computeAnnotationOrder : (AnnotationReference => Long) = { (annotationRef : AnnotationReference) =>
        levelByAnnotationId.get(annotationRef)  match {
          case Some(order) => order
          case None => 
            //Note: when reviewing Annotation, it is assumed that Group and Relation are always complete (i.e. Groups are non-empty, and every roles have an argument in Relation)
            //Hence, if a referenced Annotation has no members, it means it is from another Annotation Set than those reviewed
            higherOrderAnnotation.get(annotationRef) match {
              case Some(members) =>
                val order = members.map{ memberRef => computeAnnotationOrder(memberRef) }.max + 1L 
                levelByAnnotationId.put(annotationRef, order) 
                order
              case None =>
                //referenced annotation belong to some other Annotation Set
                //Note: of course, reviewed AnnotationSets can not reference each other
                0L
            }
          
        } 
      }
        
     //compute adjudication level and sort annotations by level
     val higherOrderAnnotationByLevel = higherOrderAnnotation.keys.map{ annotationRef => 
        (computeAnnotationOrder(annotationRef), annotationRef) }
     .toList.sortBy{ case(order, annotationRefs) => order }
    
    
     //## At this point, we have the group of colliding Annotations
     //check if the annotations are identical
    
     (
        ("blocks" -> (blocks.zipWithIndex.map { 
              case ((start, end, noConflict, members), index)  =>
                ("level" -> 1L) ~
                ("kind" -> AnnotationKind.TextAnnotation.id) ~
                ("index" -> index) ~
                ("start" -> start) ~
                ("end" -> end) ~
                ("noconflict" -> noConflict) ~
                ("members" -> members.groupBy{ 
                    case(setId, annotation) => 
                      setId }.map{  
                    case(setId, annotationRefs) => 
                      annotationRefs.map{ case(set_id, annotation) => 
                          ("set_id" -> set_id) ~
                          ("ann_id" -> annotation.id)
                      }
                  } )
            }) 
        ) ~
        ("higherorderanns" -> higherOrderAnnotationByLevel.map { case(order, annotationRef) => 
              ("ann_ref" ->  (("set_id" -> annotationRef.set_id) ~
                              ("ann_id" -> annotationRef.ann_id) ) ) ~
              ("level" -> order)  ~
              ("kind" -> higherOrderAnnotationKind(annotationRef.ann_id)) ~
              ("referenced" ->  higherOrderAnnotation(annotationRef).map{ memberRef => memberRef.ann_id } )
            
          }  
        )
      )
     }
  
     }
