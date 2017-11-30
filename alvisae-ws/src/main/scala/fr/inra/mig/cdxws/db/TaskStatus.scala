/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig.cdxws.db

object TaskStatus extends Enumeration {
  type TaskStatus = Value
  val Pending = Value(0, "pending")
  val ToDo = Value(1, "todo")
  val Upcoming = Value(2, "upcoming")
  val Done = Value(3, "done")
  val Unavailable_Done = Value(4, "na_done")
}
