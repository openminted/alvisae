/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig.cdxws.db

import TaskVisibility._
import org.jgrapht.alg.ConnectivityInspector
import org.jgrapht.alg.CycleDetector
import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.DirectedSubgraph
import scala.collection.JavaConversions._
import scala.xml.Elem


object TaskDefinitionXMLImporter {

  // default workflow allowing anyone to edit any annotations
  val DefaultTaskDefinitions =
    <alvisae-workflow>
      <task-definition name="default-task" cardinality="*" visibility="public">
        <edit-annotations>
          <all-annotation-types/>
        </edit-annotations>
      </task-definition>
      <task-definition name="review" cardinality="1" visibility="public">
        <dependencies>
          <review-of task="default-task"/>
        </dependencies>        
        <edit-annotations>
          <all-annotation-types/>
        </edit-annotations>
      </task-definition>
    </alvisae-workflow>

  // default workflow only allowing to view annotations of one single annotation set
  val DefaultReadOnlyTaskDefinitions =
    <alvisae-workflow>
      <task-definition name="default-task" cardinality="1" visibility="public">
        <edit-annotations>
          <all-annotation-types/>
        </edit-annotations>
      </task-definition>
    </alvisae-workflow>
  
  val TaskDefinitionsSample =
    <alvisae-workflow schema="OntoBiotope">
      <task-definition name="preannotation" cardinality="1" visibility="public">
        <edit-annotations>
          <annotation-type>Bacteria</annotation-type>
          <annotation-type>Host</annotation-type>
        </edit-annotations>
      </task-definition>
      <task-definition name="annotation" cardinality="2" visibility="restricted">
        <dependencies>
          <review-of task="preannotation"/>
          <successor-of task="preannotation"/>
        </dependencies>
        <edit-annotations>
          <all-annotation-types/>
        </edit-annotations>
      </task-definition>
      <task-definition name="conciliation" cardinality="1" visibility="public">
        <dependencies>
          <review-of task="annotation"/>
        </dependencies>
        <edit-annotations>
          <all-annotation-types/>
        </edit-annotations>
      </task-definition>
    </alvisae-workflow>

  val RootTaskName = "AlvisAE-preprocessing"
  
  val UnboundedStrCardinality = "*"
  val UnboundedIntCardinality = -1

  class TaskCardinality(intValue: Int) {
    //unbounded cardinality
    
    val cardinalityValue = intValue match {
      case cardinality if (cardinality > 0 || cardinality == UnboundedIntCardinality) => 
        cardinality
      case _ => 
        throw new IllegalArgumentException("Cardinality must be "+UnboundedIntCardinality+" or an integer value above 0 : " + intValue)
    }
    
    def this(strValue: String) = this( 
      strValue.trim match {
        case UnboundedStrCardinality => UnboundedIntCardinality
        case _ => try {
            strValue.toInt
          }  catch { case ex : Exception =>
              throw new IllegalArgumentException("Cardinality must be '"+UnboundedStrCardinality+"' or an integer value above 0 : " + strValue)
          }
      })

    //default cardinality is 1
    def this() = this(1)
    
    def isUnbounded = {
      cardinalityValue == UnboundedIntCardinality
    }

    def isUnit = {
      cardinalityValue == 1
    }

    def toInt : Int = {
      intValue
    }
    
    override def toString : String = {
      isUnbounded match {
        case true => UnboundedStrCardinality
        case _ => cardinalityValue.toString
      }
    }
       
  }  

  case class TaskDefinitionExt(
    val name : String, 
    val cardinality : TaskCardinality, 
    val visibility : TaskVisibility.TaskVisibility, 
    val annotationTypes : Set[String],
    val review_of : Option[String],
    val successor_of : Set[String],
    var dependent_on : Set[String],
    var precedenceLevel : Int
  )
  
  case class InvalidTaskDefinitionException(message : String) extends IllegalArgumentException(message : String)

  
  def createDefaultTaskDefinition(campaign : Campaign) =   {
    val taskDefs = loadTaskDefinition(campaign.id, DefaultTaskDefinitions)
    val tasks = storeTaskDefinitions(campaign, taskDefs)
    tasks.head
  }
  
  def loadTaskDefinitionFile(campaign_id : Long, fileName : String ) : List[TaskDefinitionExt] =   {
    val taskDefsElem = scala.xml.XML.loadFile(fileName)
    loadTaskDefinition(campaign_id, taskDefsElem)
  }
  
  def loadTaskDefinition(campaign_id : Long, taskDefsElem : Elem ) : List[TaskDefinitionExt] =   {
    import fr.inra.mig.cdxws.db.AnnotationSchemaHandler.InvalidSchemaException
    
    CadixeDB.getCampaignById(campaign_id) match {
      case None =>
        throw new IllegalArgumentException("Campaign " + campaign_id + " not found")

      case Some(campaign) => 
        
        try {
          val parsedSchema = net.liftweb.json.JsonParser.parse(campaign.schema)
          val annotationSchema = AnnotationSchemaHandler.getSchemaFromJson(parsedSchema)
          
          checkTaskDefinition(annotationSchema, taskDefsElem)
          
        } catch {
          case ex @ (_ : InvalidTaskDefinitionException | _ : InvalidSchemaException) => 
            throw new IllegalArgumentException(ex.getMessage)
        }        
    }
  }

  def storeTaskDefinitions(campaign : Campaign, taskDefs : List[TaskDefinitionExt]) =   {

    val reviewOfByName = (taskDefs map {taskdef => 
        taskdef.name -> taskdef.review_of
      }).toMap
    
    //1rst pass : create the individual Task entries
    val taskDefsByName = (taskDefs map {taskdef => 
        val newTask = TaskDefinition(campaign.id, 
                                     taskdef.name, 
                                     taskdef.cardinality.toInt, 
                                     taskdef.visibility,
                                     taskdef.annotationTypes.mkString(","),
                                     taskdef.precedenceLevel)
        CadixeDB.task_definitions.insert(newTask)
      
        taskdef.name -> newTask
      }).toMap

    //2nd pass : add references to reviewed or succeeded Tasks
    import org.squeryl._
    import org.squeryl.dsl._
    import org.squeryl.PrimitiveTypeMode._

    for (taskdef <- taskDefs) {
      
      val currentTaskId = taskDefsByName.get(taskdef.name).head.id
      
      //add direct and indirect dependency references to reviewed Tasks
      val directDeps = taskdef.review_of match {
        case Some(taskName) => 
          
          def reviewDependencies(taskNames : Set[String]) : Set[String] = {
            val depTaskNames = taskNames.map { taskName => reviewOfByName.get(taskName).head.orNull }.filter{ _ != null }
            if (depTaskNames.isEmpty) {
              taskNames
            } else {
              taskNames ++ reviewDependencies(depTaskNames)
            }
          }
          
          val allReviewDep = reviewDependencies(Set(taskName))
          allReviewDep.map { depTaskName =>
            val depTaskId = taskDefsByName.get(depTaskName).head.id
            val directDep = taskName.equals(depTaskName)
            CadixeDB.task_precedencies.insert(TaskPrecedency(currentTaskId, depTaskId, campaign.id, directDep, true, false, false))
            (currentTaskId, depTaskId)
          }.toSet
          
        case _ =>
          Set()
      }
      //add direct dependencies between Tasks
      val allDependency = taskdef.successor_of ++ taskdef.dependent_on
      for (taskName <- allDependency) {
        //add direct dependency references to succeeded Tasks
        val succeeding_dep = taskdef.successor_of.contains(taskName)
        //add direct Tasks dependencies induced by Annotation Type references 
        val annotationTypeDep = taskdef.dependent_on.contains(taskName)
        
        val depTaskId = taskDefsByName.get(taskName).head.id
        //add indirect dependancy only if no direct one exists fro this couple of Tasks
        if (!directDeps.contains((currentTaskId, depTaskId))) {
          CadixeDB.task_precedencies.insert(TaskPrecedency(currentTaskId, depTaskId, campaign.id, true, false, succeeding_dep, annotationTypeDep))
        }
      }
    }
    
    
    
    from(CadixeDB.task_definitions)(td => where(td.campaign_id === campaign.id) select (td) ).toList
  }
    
  def checkTaskDefinition(annotationSchema : AnnotationSchemaHandler.SchemaDefinition, taskDefsElem : Elem ) =   {
    
    val rootTask = TaskDefinitionExt(RootTaskName, new TaskCardinality(), TaskVisibility.Public, Set(), None, Set(), Set(), 0)
    
    //1rst pass : read and convert external Task definitions
    val definedTasks = (taskDefsElem \\ "alvisae-workflow" \ "task-definition") map { 

      taskdef => 
      {
        val name = (taskdef \ "@name").text.trim match { 
          case taskName if (taskName.nonEmpty) => taskName 
          case _ => throw new InvalidTaskDefinitionException("Missing task name")
        }

        val cardinality = (taskdef \ "@cardinality").text.trim match { 
          case cardinalityAttr if (cardinalityAttr.nonEmpty) =>
            try {
              new TaskCardinality(cardinalityAttr)
            }catch { 
              case ex : Exception =>
                throw new InvalidTaskDefinitionException("Invalid value for cardinality attribute: " + cardinalityAttr + " for task " + name)

            }
          case _ => 
            new TaskCardinality()
        }
        
        val yieldsAnyType = (taskdef \ "edit-annotations" \ "all-annotation-types").nonEmpty
        
        val yields = yieldsAnyType match {
          case true => 
            val yieldedTypes = (taskdef \ "edit-annotations" \ "annotation-type")
            if (yieldedTypes.nonEmpty) {
              throw new InvalidTaskDefinitionException("Task "+name+ " should not have explicit edited annotation type(s) when all annotation types is specified")
            }
            annotationSchema.keySet.toSet
          case _ => 
            val yieldedTypes = (taskdef \ "edit-annotations" \ "annotation-type") map { yieldedType =>
              val annotationType = yieldedType.text.trim
                        
              if (annotationType.isEmpty) {
                throw new InvalidTaskDefinitionException("Empty edited annotation type for task " + name)
              }else if (!annotationSchema.keySet.contains(annotationType)) {
                throw new InvalidTaskDefinitionException("Unknown edited annotation type '"+annotationType + "' for task " + name)
              } else {
                annotationType
              }
            }
            if (yieldedTypes.isEmpty) {
              throw new InvalidTaskDefinitionException("Missing edited annotation types for task " + name)
            } else {
              yieldedTypes.toSet
            }
        }
          
        val withDependencies = (taskdef \ "dependencies").nonEmpty
        
        val reviewOf = (taskdef \ "dependencies" \ "review-of") match { 
          case reviewOfElem if (reviewOfElem.nonEmpty) => 
            (reviewOfElem \ "@task").text.trim match {
              case name if (name.nonEmpty) => Some(name)
              case _ => 
                throw new InvalidTaskDefinitionException("Missing reviewed task name for task " + name)
            }
          case _ => None 
        }

        val succeeded = (taskdef \ "dependencies" \ "successor-of") match {
          case followedElem if (followedElem.nonEmpty) =>
            (followedElem map { followedTask => 
                val followedName = (followedTask \ "@task").text.trim 
                if (followedName.isEmpty) {
                  throw new InvalidTaskDefinitionException("Missing succeeded task name for task " + name)
                } else {
                  followedName
                }
              }).toSet
          case _ =>
            Set[String]()
        }
        
        //all task must be successor-of the root task 
        val follows = succeeded + rootTask.name
          
        val visibility = (taskdef \ "@visibility").text.toLowerCase.trim match {
          case visibilityAttr if (visibilityAttr.nonEmpty) =>
            try {
              TaskVisibility.withName(visibilityAttr)
            }catch { case ex : Exception =>
                throw new InvalidTaskDefinitionException("Invalid value for Visibility attribute: " + visibilityAttr + " for task " + name)
            }
            //default visibility is 'restricted'
          case _ => TaskVisibility.Restricted
        }
  
        //default precedence level is 1
        TaskDefinitionExt(name, cardinality, visibility, yields, reviewOf, follows.toSet, Set(), 1)
      }
    }
    
    val tasks = rootTask :: definedTasks.toList 
      
    //2nd pass : check validity of Task definitions
    val duplicateTaskNames = tasks.groupBy(_.name).filter(_._2.size>1).keySet
    if (duplicateTaskNames.nonEmpty)  { 
      throw new InvalidTaskDefinitionException("Task name must be unique: " + duplicateTaskNames.mkString(","))
    }
    
    val definedTask = (tasks map { tdef => tdef.name -> tdef }).toMap
    
    for (taskdef <- tasks) {
      //referenced Task must be defined
      taskdef.review_of match { 
        case Some(taskName) if (!definedTask.keySet.contains(taskName)) => 
          throw new InvalidTaskDefinitionException("Unknown rewieved task '" + taskName + "' referenced by task '" + taskdef.name + "'")
        case _ => 
      }

      if (taskdef.successor_of.nonEmpty)  { 
        for (followedTask <- taskdef.successor_of) {
          if (!definedTask.keySet.contains(followedTask)) {
            throw new InvalidTaskDefinitionException("Unknown succeeded task '" + followedTask + "' referenced by task '" + taskdef.name + "'")
          }
        }
      }
    }

    val yieldedAnnotationTypes = (tasks.flatMap { tdef => tdef.annotationTypes.toList }).toSet
          
    val unyieldedTypes = annotationSchema.keySet &~ yieldedAnnotationTypes  
    if (unyieldedTypes.nonEmpty) {
      throw new InvalidTaskDefinitionException("No task edit the following annotation type(s): " + unyieldedTypes.mkString(","))
    }

    val reviewingGraph : DefaultDirectedGraph[String,DefaultEdge] = new DefaultDirectedGraph(classOf[DefaultEdge])
    for (taskdef <- tasks) {
      reviewingGraph.addVertex(taskdef.name)
    } 

    //TODO Add more checking
    //A task may either be reviewed or followed by another one, but not both.
    
    //Reviewed tasks must be reviewed by at most one other task
    for (taskdef <- tasks) {
      taskdef.review_of match {
        case Some(reviewedTaskName) =>
          if (reviewedTaskName.equals(taskdef.name)) {
            throw new InvalidTaskDefinitionException("The task '"+ reviewedTaskName +"' cannot review itself")
          }
          
          reviewingGraph.addEdge(taskdef.name, reviewedTaskName)
          val reviewers = reviewingGraph.incomingEdgesOf(reviewedTaskName) map { reviewerEdge => reviewingGraph.getEdgeSource(reviewerEdge) }
          if (reviewers.size>1) {
            throw new InvalidTaskDefinitionException("The task '"+ reviewedTaskName +"' must be reviewed by at most one task (reviewed by: " + reviewers.mkString(", ")+")")
          }
        case None =>
      }
    }    
    //Any task with a cardinality different from 1 must be reviewed
    for (taskdef <- tasks) {
      if (!taskdef.cardinality.isUnit) {
        val reviewerNames = reviewingGraph.incomingEdgesOf(taskdef.name) map { reviewerEdge => reviewingGraph.getEdgeSource(reviewerEdge) }
        if (reviewerNames.isEmpty) {
          throw new InvalidTaskDefinitionException("The task '"+ taskdef.name +"' with cardinality of "+ taskdef.cardinality +" must be reviewed")
        }
      }
    }
    
    //Reviewing task must at least yield all annotation types of reviewed task
    for (taskdef <- tasks) {
      val reviewedNames = reviewingGraph.outgoingEdgesOf(taskdef.name) map { reviewerEdge => reviewingGraph.getEdgeTarget(reviewerEdge) }
      for (reviewed <- reviewedNames) {
        definedTask.get(reviewed) match {
          case Some(reviewedTask) => 
            val unreviewedAnnotationTypes = reviewedTask.annotationTypes &~ taskdef.annotationTypes
            if (unreviewedAnnotationTypes.nonEmpty) {
              throw new InvalidTaskDefinitionException("The reviewed task '"+ reviewed +"' edits annotation type(s) not edited by reviewing task '"+ taskdef.name +"' (" + unreviewedAnnotationTypes.mkString(", ") + ")")
            }
          case _ =>
            //should not happen
        }
      }
    }
        
    //No cycle allowed within the graph of reviewing tasks
    val cd = new CycleDetector(reviewingGraph)
    if (cd.detectCycles) {
      val cmsg = cd.findCycles().toArray.foreach(cycle => 
        throw new InvalidTaskDefinitionException("Tasks reviewing graph contains cycle(s) (involving the following Task: " + cycle + ")")
      )
    }

    //if an annotation type occurs in two tasks, then one must be the review of the other
    val tasksByYieldedTypes = (tasks.foldLeft(List[(String,String)]()) { 
        (acc, value) => acc ++ (value.annotationTypes.map{ annotationType => (annotationType, value.name) }).toList
      }).groupBy(_._1)
    
    //map giving for each reviewed task its top reviewing task 
    val reviewingByReviewed = tasksByYieldedTypes.map{ case (annotationType, annTypeYieldingTasks) =>
      
        val yieldingTasks = (annTypeYieldingTasks.map{ tuple => tuple._2 }).toSet
      
        val subgraph = new DirectedSubgraph(reviewingGraph, yieldingTasks, null)
        val inspector = new ConnectivityInspector(subgraph)
        if (!inspector.isGraphConnected) {
          throw new InvalidTaskDefinitionException("Every tasks editing the annotation type '" + annotationType + "' must be a review of each other (tasks: " + yieldingTasks.mkString(", ") + ")")
        }
        val topReviewingTaskName =  subgraph.vertexSet.map {taskName =>
          if (reviewingGraph.incomingEdgesOf(taskName).isEmpty) {
            Some(taskName)
          } else {
            None
          }
        }.flatten.head
     
        val reviewedNames = yieldingTasks &~ Set(topReviewingTaskName)
        reviewedNames.map { reviewedName => (reviewedName -> topReviewingTaskName) }
      
    }.flatten.toMap
  
    //build the graph of dependancies created by Groups or Relations referencing other Annotation Types
    val annTypeDependancyGraph : DefaultDirectedGraph[String,DefaultEdge] = new DefaultDirectedGraph(classOf[DefaultEdge])
    for (taskdef <- tasks) {
      annTypeDependancyGraph.addVertex(taskdef.name)
    } 

    //generate the map giving for any Annotation Type name the list of Task names where Group or Relation referencing the key Annotation Type can be edited
    val refingTaskNRefedType = tasks .map { taskdef =>
      val referencedTypes = taskdef.annotationTypes .map { annTypeName =>
        val annTypeDef = annotationSchema.get(annTypeName).head
        annTypeDef.referencedTypes
      }.flatten
        
      referencedTypes.map { referencedType => (referencedType, taskdef.name) }.toList
      
    }.flatten
    
    val refingTasksByRefedType = refingTaskNRefedType.groupBy{ case(referencedType, taskName) => referencedType }.map { case (k,v) => k -> (v.map {_._2} ) }

    //add the Annotation Type dependancies to the graph
    for (taskdef <- tasks) {
      for (annotationType <- taskdef.annotationTypes) {
        refingTasksByRefedType.get(annotationType) match {
          case Some(referencingTaskNames) => 
            for (referencingTaskName <- referencingTaskNames) {

              //of course no dependancy of a Task on itself
              if (!referencingTaskName.equals(taskdef.name)) {
                
                //the task referencing the Annotation Type have a dependancy on the top reviewing task of this Annotation Type
                reviewingByReviewed.get(taskdef.name) match {
                  //create the dependancy only to the top reviewing Tasks of the considered Annotation Type
                  case Some(topReviewer) =>
                    //again, no dependancy of a Task on itself
                    if (!referencingTaskName.equals(topReviewer)) {
                      annTypeDependancyGraph.addEdge(referencingTaskName, topReviewer)
                    }
                  case None =>
                    //the task referencing the Annotation Type have no reviewing task
                    //But, as a strict rule: if 2 or more Task edit the same annotation type, one is the review of the others

                    if (taskdef.review_of.nonEmpty && taskdef.review_of.head.equals(referencingTaskName)) {
                    
                      //So the current Task is actually the one reviewing the current Annotation Type
                      //This case should only allowed only for Task editing "all-annotation-types" (otherwise it means the workflow is not well designed), 
                      //
                      //The dependency is of course in the same direction of the Reviewing dependency
                      annTypeDependancyGraph.addEdge(taskdef.name, referencingTaskName)
                    } else {
                      annTypeDependancyGraph.addEdge(referencingTaskName, taskdef.name)
                    }
                }
              }
            }
          case _ =>
        }
      }
    } 

    //No cycle allowed within the graph of Annotation Types dependencies
    val annTypeDepCycleDetector = new CycleDetector(annTypeDependancyGraph)
    if (annTypeDepCycleDetector.detectCycles) {
      val cmsg = annTypeDepCycleDetector.findCycles().toArray.foreach(cycle => 
        throw new InvalidTaskDefinitionException("Annotation Types dependencies graph contains cycle(s) (involving the following Task: " + cycle + ")")
      )
    }

    
    //3rd pass : Compute Task precedence of the valid workflow
    
    //the global dependancy graph of the Task is the reviewing graph:
    //+ completed with the "follows" relations...
    val dependancyGraph = reviewingGraph
    for (taskdef <- tasks) {
      for ( followed <- taskdef.successor_of) {
        dependancyGraph.addEdge(taskdef.name, followed)
      }
    } 
    //+ ... and by the referenced Annotation Types depedencies
    for (taskdef <- tasks) {
      val taskName = taskdef.name
      val dependentOnTask = annTypeDependancyGraph.outgoingEdgesOf(taskName).map {
        dependentOnTaskEdge =>
        val targetDependancy = annTypeDependancyGraph.getEdgeTarget(dependentOnTaskEdge)
        dependancyGraph.addEdge(taskName, targetDependancy)
        targetDependancy
      }.toSet
      //update Task definition with the Task it depends on because of Annotation Type referencing
      taskdef.dependent_on = dependentOnTask
    }
    
    //No cycle allowed within the global graph of dependencies
    val globalDepCycleDetector = new CycleDetector(dependancyGraph)
    if (globalDepCycleDetector.detectCycles) {
      val cmsg = globalDepCycleDetector.findCycles().toArray.foreach(cycle => 
        throw new InvalidTaskDefinitionException("Global Task dependencies graph contains cycle(s) (involving the following Task: " + cycle + ")")
      )
    }
    
    //recursively update Tasks with their precedence level 
    def processNextLevel(prevLevel : Int, prevTaskNames : List[String]) {
      val currentLevel = prevLevel + 1
      //get the tasks on the level above
      val currentLevelTaskNames = prevTaskNames flatMap { precedingTask => 
        dependancyGraph.incomingEdgesOf(precedingTask) map { toCurrentLevelTask => dependancyGraph.getEdgeSource(toCurrentLevelTask)  } }
      //update precedence level of the tasks
      for (currentLevelTaskName <- currentLevelTaskNames) {
        val currentLevelTask = definedTask.get(currentLevelTaskName).head
        currentLevelTask.precedenceLevel = currentLevel
      }
      //continue as long as there is some tasks
      if (currentLevelTaskNames.nonEmpty) {
        processNextLevel(currentLevel, currentLevelTaskNames)
      }
    }
    
    //The root Task come first (level 0) because it is neither reviewing or following any other tasks 
    processNextLevel(0, List(rootTask.name))

    val orderedTask = tasks.toList.sortWith( (a, b) => (a.precedenceLevel < b.precedenceLevel) )
    
    Console.err.println
    Console.err.println("Tasks will be performed in that order:")
    for (t <- orderedTask) {
      Console.err.println(t.precedenceLevel + " : " + ("  " * t.precedenceLevel) + t.name)
    }
    Console.err.println
    
    //
    orderedTask
  }
  
}
