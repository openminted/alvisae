/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig.cdxws.db

import fr.inra.mig.cdxws.db._
import java.net.MalformedURLException
import java.net.URL
import scala.util.parsing.json.JSON._
import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.JsonAST.JNothing
import net.liftweb.json.MappingException
import net.liftweb.json.NoTypeHints
import net.liftweb.json.JsonDSL._
import net.liftweb.json.Serialization
import net.liftweb.json._
import org.jgrapht.alg.CycleDetector
import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge

object AnnotationSchemaHandler {

  //Annotation schema related data structures
  case class PropertyValType(valTypeName : String, TyDIRefBaseURL : Option[String], domain : Option[List[String]], defaultVal : Option[String])
  case class PropertyDefinition(key : String, mandatory : Boolean, minVal : Int, maxVal : Int, valType : PropertyValType)
  type PropertiesDefinition = Map[String,PropertyDefinition]
  case class TextBinding (minFrag: Int, maxFrag: Int, boundRef: Option[String], crossingAllowed : Boolean)
  case class GroupDefinition (minComp: Int, maxComp: Int, compType: List[String], homogeneous : Boolean)
  //Note : RelationDefinition is a List of single keyed Maps (and not simply Map) to preserved order of arguments in JSON format
  type RelationDefinition = List[Map[String, List[String]]]
  case class AnnotationType(kind : Int, 
                            `type`: String, 
                            color : String,
                            url : Option[String],
                            propDef : Option[PropertiesDefinition],
                            txtBindingDef : Option[TextBinding],
                            groupDef : Option[GroupDefinition],
                            relationDef : Option[RelationDefinition])   {

    def referencedTypes : Set[String] = {
      if (AnnotationKind.GroupAnnotation.id.equals(kind)) {
        groupDef.head.compType .toSet
      } else if (AnnotationKind.RelationAnnotation.id.equals(kind)) {
        relationDef.head.map { arg => arg.map { case (role, types) => types }.flatten }.flatten .toSet
      } else {
        Set()
      }
    }
  }
        
  type SchemaDefinition = Map[String, AnnotationType]

  case class InvalidSchemaException(message : String) extends IllegalArgumentException(message : String)
  
  val EmptyPropertiesDefinition : PropertiesDefinition = Map[String,PropertyDefinition]()
  
  def retrieveSchema(filepath : String) : Option[JValue] = {

    val source = io.Source.fromFile(filepath);
    val jsonText = source.mkString
    source.close()

    val parsed = net.liftweb.json.JsonParser.parse(jsonText)
    val jsSchema = parsed \ "schema"

    jsSchema match {
      case JNothing => None
      case _ => 
        checkSchemaConformance(jsSchema)
        Some(jsSchema)
    }
  }
  
  def getSchemaFromJsonStr(jsonText : String) = {
    getSchemaFromJson(net.liftweb.json.JsonParser.parse(jsonText))
  }
  
  def getSchemaFromJson(schema : JValue) = {
    implicit val formats = Serialization.formats(NoTypeHints)
    
    val schemaDefinition = try {
      schema.extract[SchemaDefinition]
    } catch {
      case ex:MappingException => throw new InvalidSchemaException("Something other than a list of types was found:\n\n" + ex.getMessage())
    }
    schemaDefinition
  }
  
  /**
   * Perform syntax and conformance checking of the specified JSON annotation schema.
   *
   * @throw InvalidSchemaException 
   */
  def checkSchemaConformance(schema : JValue) : Unit = {

    val schemaDefinition = getSchemaFromJson(schema)

    val referentialGraph : DefaultDirectedGraph[String,DefaultEdge] = new DefaultDirectedGraph(classOf[DefaultEdge])
    //1rst pass just retrieve the AnnotationType names
    schemaDefinition.map(annTypeEntry => {
        val annTypeName = annTypeEntry._1

        if (!referentialGraph.addVertex(annTypeName)) {
          throw new InvalidSchemaException("Duplicate AnnotationType name:\t" + annTypeName)
        }
      })

    //2nd pass perform some referential checking
    schemaDefinition.map(annTypeEntry => {
        val annTypeName = annTypeEntry._1
        val annotationType = annTypeEntry._2


        if (!annotationType.`type`.equals(annTypeName)) {
          throw new InvalidSchemaException("Annotation Type mismatch:\t'" + annTypeName + "' <> '" + annotationType.`type` +"'")
        }

        //Console.out.print("Name:\t" + annTypeName + "\t")
        
        val colour = annotationType.color
        if (!colour.matches("#[0-9A-Fa-f]{6}")) {
          Console.err.println("!! Check validity of color declaration '" + colour + "' for AnnotationType '" + annTypeName  + "'")
        }
        
        annotationType.url match {
          case Some(urlStr) =>
            try {
              val url = new URL(urlStr);
            } catch {
              case ex:MalformedURLException =>
                throw new InvalidSchemaException("Malformed URL associated to AnnotationType '" + annTypeName  + "'")
            }          
          case _ =>
            //URL definition is optional
        }
        
        annotationType.propDef match {
          case Some(propertiesDef) =>
              
            propertiesDef.map(entry => {
                val propName = entry._1
                val propDef = entry._2

                if (!propDef.key.equals(propName)) {
                  throw new InvalidSchemaException("Property name mismatch:\t'" + propName + "' <> '" + propDef.key + "' for AnnotationType '" + annTypeName  + "'")
                }

                if (propDef.minVal<=0) {
                  throw new InvalidSchemaException("Invalid minimum number of property values in for AnnotationType '" + annTypeName  + "' [" + propName + "]")
                } else if (propDef.minVal >propDef.maxVal) {
                  throw new InvalidSchemaException("Invalid maximum number of property values in for AnnotationType '" + annTypeName  + "' [" + propName + "]")
                }
                
                //properties value type definition is optional
                val valTypeDef = propDef.valType
                if (valTypeDef!=null) {
                  valTypeDef.valTypeName match {
                    case "ClosedDomain" =>
                      val domainValues = valTypeDef.domain match {
                        case Some(domainValues) => domainValues
                        case _ =>
                          throw new InvalidSchemaException("Missing domain values in property '" + propName + "' definition for Text AnnotationType '" + annTypeName  + "'")
                      }
                      
                      if (domainValues.isEmpty) {
                        throw new InvalidSchemaException("Empty domain values in property '" + propName + "' definition for Text AnnotationType '" + annTypeName  + "'")
                      }

                      valTypeDef.defaultVal match {
                        case Some(value) => if (!domainValues.contains(value)) {
                            throw new InvalidSchemaException("Unknown default value ("+value +") in property '" + propName + "' definition for Text AnnotationType '" + annTypeName  + "'")
                          }
                        case _ =>
                          throw new InvalidSchemaException("Missing default values in property '" + propName + "' definition for Text AnnotationType '" + annTypeName  + "'")
                        
                      }
                      
                    case "TyDI_termRef" | "TyDI_semClassRef" | "TyDI_conceptRef" =>
                      valTypeDef.TyDIRefBaseURL match {
                        case Some(tydiURL) => try {
                            val url = new URL(tydiURL)
                          } catch {
                            case ex:MalformedURLException =>
                              throw new InvalidSchemaException("Malformed TyDIRefBaseURL in property '" + propName + "' definition for Text AnnotationType '" + annTypeName  + "'")
                          }
                        case _ => 
                          throw new InvalidSchemaException("Missing TyDIRefBaseURL in property '" + propName + "' definition for Text AnnotationType '" + annTypeName  + "'")
                      }
					  
					  case "FreeText" =>
						
                      
                    case other => 
                      throw new InvalidSchemaException("Unrecognized property value type '"+other + "' declared in AnnotationType '" + annTypeName  + "'")
                  }
                    
                }
                
              })
            
          case _ =>
            //properties definition is optional
        }

        if (AnnotationKind.TextAnnotation.id.equals(annotationType.kind)) {

          annotationType.txtBindingDef match {
            case Some(textBinding : TextBinding) =>

              if (textBinding.minFrag<=0) {
                throw new InvalidSchemaException("Invalid minimum number of fragment in TextBinding definition for AnnotationType '" + annTypeName  + "'")
              } else if (textBinding.minFrag>textBinding.maxFrag) {
                throw new InvalidSchemaException("Invalid maximum number of fragment in TextBinding definition for AnnotationType '" + annTypeName  + "'")
              }

            case None =>
              throw new InvalidSchemaException("Missing TextBinding definition for Text AnnotationType '" + annTypeName  + "'")
            case _ =>
              throw new InvalidSchemaException("Something other than a valid TextBinding definition for AnnotationType '" + annTypeName  + "'")

          }

        } else if (AnnotationKind.GroupAnnotation.id.equals(annotationType.kind)) {

          annotationType.groupDef match {
            case Some(groupDef : GroupDefinition) =>
              if (groupDef.minComp<=0) {
                throw new InvalidSchemaException("Invalid minimum number of component in Group definition for AnnotationType '" + annTypeName  + "'")
              } else if (groupDef.minComp>groupDef.maxComp) {
                throw new InvalidSchemaException("Invalid maximum number of component in Group definition for AnnotationType '" + annTypeName  + "'")
              } else if (groupDef.compType.isEmpty) {
                throw new InvalidSchemaException("Missing allowed component type(s) in Group definition for AnnotationType '" + annTypeName  + "'")
              }

              groupDef.compType.foreach(referencedType =>
                try {
                  if (referentialGraph.addEdge(annTypeName, referencedType)==false) {
                    throw new InvalidSchemaException("AnnotationType '" + referencedType  + "' is already referenced from Group definition of AnnotationType '" + annTypeName  + "'")
                  }
                } catch {
                  case ex:IllegalArgumentException => throw new InvalidSchemaException("Missing referenced AnnotationType '" + referencedType  + "' declared in Group definition of AnnotationType '" + annTypeName  + "'")
                })

            case None =>
              throw new InvalidSchemaException("Missing Group definition for Group AnnotationType '" + annTypeName  + "'")
            case _ =>
              throw new InvalidSchemaException("Something other than a valid Group definition for AnnotationType '" + annTypeName  + "'")
          }

        } else if (AnnotationKind.RelationAnnotation.id.equals(annotationType.kind)) {

          annotationType.relationDef match {
            case Some(relationDef) =>

              if (relationDef.isEmpty) {
                throw new InvalidSchemaException("Invalid number of argument (0) in Relation definition for AnnotationType '" + annTypeName  + "'")
              }
              val argumentames = relationDef.map( arg => {
                  if (arg.keySet.size!=1) {
                    throw new InvalidSchemaException("Invalid number of argument name in Relation definition for AnnotationType '" + annTypeName  + "'\n" + arg.keySet.toString)
                  }

                  //Map with only one entry
                  val argName = arg.keySet.toList(0)
                  arg.get(argName) match {
                    case Some(argTypes) => 
                      if (argTypes.isEmpty) {
                        throw new InvalidSchemaException("Empty referenced types set from Relation definition of AnnotationType '" + annTypeName  + "' (argument= '" + argName+"')\n\n")
                      }
                        
                      argTypes.foreach(referencedType =>
                        try {
                          //Relation AnnotationType can reference the same AnnoationType in distinct arguments
                          referentialGraph.addEdge(annTypeName, referencedType)
                        } catch {
                          case ex:IllegalArgumentException => throw new InvalidSchemaException("Missing referenced AnnotationType '" + referencedType  + "' declared in Relation definition of AnnotationType '" + annTypeName  + "' (argument= '" + argName+"')")
                        })

                    case other =>
                      throw new InvalidSchemaException("Missing referenced type(s) from Relation definition of AnnotationType '" + annTypeName  + "' (argument= '" + argName+"')\n\n" + other)
                  }
                })
            case None =>
              throw new InvalidSchemaException("Missing Relation definition for Relation AnnotationType '" + annTypeName  + "'")
            case _ =>
              throw new InvalidSchemaException("Something other than a valid Relation definition for AnnotationType '" + annTypeName  + "'")
          }

        } else {
          throw new InvalidSchemaException("Something other than a valid annotation kind was found for AnnotationType '" + annTypeName  + "'")
        }
      })
    
    //3rd check that there is no cyclic referencing in AnnotationTypes
    val cd = new CycleDetector(referentialGraph)
    if (cd.detectCycles) {
      val cmsg = cd.findCycles().toArray.foreach(cycle => 
        throw new InvalidSchemaException("Some cyclic referencing has been found in this schema (at least the following type is involved: " + cycle + ")")
      )
    }
    
  }
 
}
