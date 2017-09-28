/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig.cdxws {
  package snippet {

    import _root_.scala.xml.NodeSeq
    import _root_.net.liftweb.util.Helpers
    import Helpers._

    class HelloWorld {
      def howdy(in: NodeSeq): NodeSeq =
        Helpers.bind("b", in, "time" -> (new _root_.java.util.Date).toString)
    }

  }
}
