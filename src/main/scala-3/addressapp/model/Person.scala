package addressapp.model

import scalafx.beans.property.{IntegerProperty, ObjectProperty, StringProperty}

import java.time.LocalDate

class Person(firstNameS: String, lastNameS: String):
  var firstName = new StringProperty(firstNameS)
  var lastName = new StringProperty(lastNameS)
  var street = new StringProperty("some Street")

  // var postalCode  = IntegerProperty(1234)
  var postalCode = ObjectProperty[Int](1234)
  var city = new StringProperty("some city")
  var date = ObjectProperty[LocalDate](LocalDate.of(1999, 2, 21))

