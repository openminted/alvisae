/*
 *
 *      This software is a result of Quaero project and its use must respect the rules of the Quaero Project Consortium Agreement.
 *
 *      Copyright Institut National de la Recherche Agronomique, 2012.
 *
 */
package bootstrap.liftweb

import java.io.File

import _root_.net.liftweb.common._
import _root_.net.liftweb.util._
import _root_.net.liftweb.http._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import Helpers._

import net.liftweb.http.auth.{AuthRole,HttpBasicAuthentication,userRoles}
import org.squeryl.{Session,SessionFactory}
import net.liftweb.http.provider.servlet.HTTPServletContext
import org.squeryl.PrimitiveTypeMode._

import fr.inra.mig.cdxws.api._
import fr.inra.mig.cdxws.db._

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  /* The purpose of this class is to disable the prompt launched by the navigator
   * when authentication fails. This is done by sending a response with an empty
   * header instead of the usual challenge tag
   */
  class CustomUnauthorizedResponse(realm: String) extends UnauthorizedResponse(realm) {  
    override def toResponse = InMemoryResponse(Array(), List(), Nil, 401)  
  }

  class CustomHttpBasicAuthentication(realmName : String)(func : PartialFunction[(String, String, Req), Boolean]) extends HttpBasicAuthentication(realmName)(func) {
    override def unauthorizedResponse = new CustomUnauthorizedResponse(realmName)
  }

  def boot {
    val logger = Logger(classOf[Boot])

    // where to search snippet
    LiftRules.addToPackages("fr.inra.mig.cdxws")

    //Check if WebService is configured by external parameters (param of the web container or JNDI) 
    val (configFile, h2DatabasePath) = LiftRules.context match {
      case context: HTTPServletContext => 
       
        val h2dbPath = context.ctx.getAttribute("h2DbPath") match {
          case specifiedPath : String => Some(specifiedPath)
          case _ => None
        }
        
        //regular servlet web container, check if configured by a param of the web container, or via JNDI custom ressource
        (ConfigResourceLocator.getConfigFile(context.ctx), h2dbPath)
      case _ => 
        //may happen when Lift is running on non-servlet web container, 
        //check if ws is parametrized by a custom resource in JNDI
        (ConfigResourceLocator.getConfigFile(LiftRules.context.path), None)
    }  
    
    SessionFactory.concreteFactory = Option(configFile) match {
      case Some(configFile) => Some(
          //use property file specified in JNDI to retreive DB connection parameters
          () => CadixeDB.createSession(configFile))
      case None => Some(
          //use embedded property file to retreive DB connection parameters
          () => CadixeDB.createSession(h2DatabasePath))
    }

    LiftRules.dispatch.prepend(RestAPI.dispatch)
    LiftRules.httpAuthProtectedResource.append(RestAPI.protection)

    LiftRules.authentication = new CustomHttpBasicAuthentication("cdxws")({
        case (login, password, _) => transaction {
            CadixeDB.users.where(u => u.login === login and u.password === password).headOption match {
              case Some(user) => {
                  userRoles(AuthRole("logged"))
                  RestAPI.user(Some(user))
                  logger.info(RestAPI.user.is.get.login + " logged") 
                  true
                } 
              case None => { 
                  logger.info("no user with such pass") 
                  false
                }
            }
          }
      })

    // Build SiteMap
    val entries = Menu(Loc("Home", List("index"), "Home")) :: Nil
    LiftRules.setSiteMap(SiteMap(entries:_*))
  }
}

