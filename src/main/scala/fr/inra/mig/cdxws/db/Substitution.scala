/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig.cdxws.db

trait Substitution[A] {
  def underlying : collection.immutable.Map[A,A]
  
  def subst(k : A) : A = this.underlying.get(k) match {
    case Some(other_k) => other_k
    case None => k
  }
}

object Substitution {
  implicit def substitutionMap[A](map0 : collection.immutable.Map[A,A]) : Substitution[A] = {
    new Substitution[A] { def underlying = map0 }
  }
}