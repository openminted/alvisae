/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */

package fr.inra.mig.cdxws.db

object ConsolidationStatus extends Enumeration {
  type ConsolidationStatus = Value

  val POSTPONED= Value(0, "POSTPONED")
  val RESOLVED= Value(1, "RESOLVED")
  val REJECTED= Value(2, "REJECTED")
  val SPLIT= Value(3, "SPLIT")
}
