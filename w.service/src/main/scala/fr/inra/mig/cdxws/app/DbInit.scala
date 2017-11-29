/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package fr.inra.mig.cdxws.app

import fr.inra.mig.cdxws.db.{BioNLP,CadixeDB,User}
import net.liftweb.http.LiftRules
import org.squeryl._
import scala.io.Source

object DbInit {
  def bddevSession(schema : String) = {
    CadixeDB.createPGSession("bddev", 5432, "annotation", schema, "annotation_admin", "annotdba84")
  }
  
  def withSession(session : Session) = {
    import net.liftweb.json.{ parse => json_parse }
    
    session.bindToCurrentThread
    CadixeDB.create
    CadixeDB.addUser("aae_root","##?##",true)
    val foo = CadixeDB.addUser("foo","foo",false)
    val bar = CadixeDB.addUser("bar","bar",false)
    val cadixe = CadixeDB.addUser("cadixe","cadixe",true)
    val philippe = CadixeDB.addUser("philippe","philippe",false)

    val bi_schema = json_parse(Source.fromInputStream(
        LiftRules.getClass.getResourceAsStream("/schema/BioNLP-ST/2011/BI.json")).mkString)
    val bi_campaign = BioNLP.import_archive(foo,bi_schema, 
                                            "http://www-tsujii.is.s.u-tokyo.ac.jp/GENIA/BioNLP-ST/downloads/files/BioNLP-ST_2011_bacteria_interactions_train_data_rev1.tar.gz", 
                                            "BioNLP-ST BI")
    
    CadixeDB.addUser2Campaign(foo, bi_campaign)
    CadixeDB.addUser2Campaign(philippe, bi_campaign)
    CadixeDB.assignUser2AllDocuments(philippe,bi_campaign)
  }
}
