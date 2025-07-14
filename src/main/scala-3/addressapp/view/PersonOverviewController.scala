package addressapp.view

import addressapp.MainApp
import addressapp.util.DateUtil.asString
import addressapp.model.Person
import javafx.fxml.FXML
import scalafx.Includes.*
import scalafx.beans.binding.Bindings
import javafx.scene.control.{Label, TableColumn, TableView}
import javafx.event.ActionEvent
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType

@FXML
class PersonOverviewController():
  @FXML
  private var personTable: TableView[Person] = null
  @FXML
  private var firstNameColumn: TableColumn[Person, String] = null
  @FXML
  private var lastNameColumn: TableColumn[Person, String] = null
  @FXML
  private var firstNameLabel: Label = null
  @FXML
  private var lastNameLabel: Label = null
  @FXML
  private var streetLabel: Label = null
  @FXML
  private var postalCodeLabel: Label = null
  @FXML
  private var cityLabel: Label = null
  @FXML
  private var birthdayLabel: Label = null

  private def showPersonDetails(person: Option[Person]): Unit =
    person match
      case Some(person) =>
        // Fill the labels with info from the selected Person
        firstNameLabel.text <== person.firstName
        lastNameLabel.text <== person.lastName
        streetLabel.text <== person.street
        cityLabel.text <== person.city
        // ScalaFX delegates convert the ScalaFX property
        // into its JavaFX equivalent
        postalCodeLabel.text <== person.postalCode.delegate.asString()
        // We use a binding instead of delegate here,
        // to retain the util.DateUtil.asString extension method.
        birthdayLabel.text <== Bindings.createStringBinding(
          () => {person.date.value.asString}, person.date
        )

      case None =>
        // no Person is selected, clear all labels
        firstNameLabel.text.unbind()
        lastNameLabel.text.unbind()
        streetLabel.text.unbind()
        cityLabel.text.unbind()
        postalCodeLabel.text.unbind()
        birthdayLabel.text.unbind()
        
        firstNameLabel.text = ""
        lastNameLabel.text = ""
        streetLabel.text = ""
        cityLabel.text = ""
        postalCodeLabel.text = ""
        birthdayLabel.text = ""
  end showPersonDetails
  
  // Initialize Table View display contents model
  def initialize(): Unit =
    personTable.items = MainApp.personData
    // Initialize columns's cell values
    firstNameColumn.cellValueFactory = {_.value.firstName}
    lastNameColumn.cellValueFactory = {_.value.lastName}
    
    showPersonDetails(None)
    
    personTable.selectionModel().selectedItem.onChange(
      (_, _, newValue) => showPersonDetails(Option(newValue))
    )

  /**
   * Called when the user clicks on the delete button
   */
  def handleDeletePerson(action: ActionEvent): Unit =
    val selectedIndex =
      personTable.selectionModel().selectedIndex.value
    if (selectedIndex >= 0) then
      personTable.items().remove(selectedIndex)  
    else
      // Nothing selected.
      val alert = new Alert(AlertType.Warning):
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Person Selected"
        contentText = "Please select a person in the table."
      alert.showAndWait()  
  end handleDeletePerson
          
          