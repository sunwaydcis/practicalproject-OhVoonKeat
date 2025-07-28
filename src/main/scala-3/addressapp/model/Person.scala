package addressapp.model

import scalafx.beans.property.{IntegerProperty, ObjectProperty, StringProperty}
import java.time.LocalDate
import addressapp.util.Database
import addressapp.util.DateUtil._
import scalikejdbc._
import scala.util.{Try, Success, Failure}

class Person(val firstNameS: String, val lastNameS: String) extends Database:
  def this() = this (null, null)
  var firstName = new StringProperty(firstNameS)
  var lastName = new StringProperty(lastNameS)
  var street = new StringProperty("some Street")

  // var postalCode  = IntegerProperty(1234)
  var postalCode = ObjectProperty[Int](1234)
  var city = new StringProperty("some city")
  var date = ObjectProperty[LocalDate](LocalDate.of(1999, 2, 21))
  
  def save(): Try[Int] =
    if (!(isExist)) then
      Try(DB autoCommit { implicit session =>
        sql"""
          INSERT INTO person (firstName, lastName,
          street, postalCode, city, date) VALUES
          (${firstName.value},${lastName.value},${street.value},
           ${postalCode.value},${city.value},${date.value.asString})
        """.update.apply()
      })
    else
      Try(DB autoCommit { implicit session =>
        sql"""
          UPDATE person
          SET
          firstName = ${firstName.value},
          lastName  = ${lastName.value},
          street    = ${street.value},
          postalCode= ${postalCode.value},
          city      = ${city.value},
          date      = ${date.value.asString}
          WHERE firstName = ${firstName.value}
          AND lastName = ${lastName.value}
        """.update.apply()
      })
    end if
  end save    
  
  def delete(): Try[Int] =
    if (isExist) then 
      Try(DB autoCommit { implicit session =>
        sql"""
          DELETE FROM person
          WHERE firstName = ${firstName.value}
          AND lastName = ${lastName.value}
        """.update.apply()
      })
    else
      throw new Exception("Person does not exist in Database")
    end if
  end delete
  
  def isExist: Boolean = 
    DB readOnly { implicit session =>
      sql"""
        SELECT * FROM person WHERE
        firstName = ${firstName.value}
        AND lastName = ${lastName.value}
      """.map(rs => rs.string("firstName")).single.apply()
    } match
      case Some(x) => true
      case None    => false
  end isExist
end Person

object Person extends Database:
  def apply(
     firstNameS : String,
     lastNameS  : String,
     streetS    : String,
     postalCodeI: Int,
     cityS      : String,
     dateS      : String
     ): Person =
    new Person(firstNameS, lastNameS):
     street.value     = streetS
     postalCode.value = postalCodeI
     city.value       = cityS
     date.value       = dateS.parseLocalDate  
  end apply
  
  def initializeTable() =
    DB autoCommit { implicit session =>
      sql"""
        CREATE TABLE person (
          id int NOT NULL GENERATED ALWAYS AS IDENTITY
          (START WITH 1, INCREMENT BY 1),
          firstName varchar(64),
          lastName varchar(64),
          street varchar(200),
          postalCode int,
          city varchar(100),
          date varchar(64)
          )
        """.execute.apply()
    }
  end initializeTable
  
  def getAllPersons: List[Person] =
    DB readOnly { implicit session =>
      sql"SELECT * FROM person".map(rs => Person(
        rs.string("firstName"), rs.string("lastName"),
        rs.string("street"), rs.int("postalCode"),
        rs.string("city"), rs.string("date")
    )).list.apply()
    }
  end getAllPersons
        

