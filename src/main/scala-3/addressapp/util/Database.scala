package addressapp.util

import scalikejdbc.*
import addressapp.model.Person

trait Database:
  val derbyDriverClassName = 
    "org.apache.derby.jdbc.EmbeddedDriver"
  val dbURL = "jdbc:derby:myDB;create=true;"  
  // initialize JDBC driver & connection pool
  Class.forName(derbyDriverClassName)
  ConnectionPool.singleton(dbURL, "me", "mine")
  // ad-hoc session provider on the REPL
  given AutoSession = AutoSession
  
object Database extends Database:
  def setupDB() =
    if (!hasDBInitialize) then
      Person.initializeTable()
  def hasDBInitialize: Boolean =
    DB getTable "Person" match
      case Some(x) => true
      case None    => false
