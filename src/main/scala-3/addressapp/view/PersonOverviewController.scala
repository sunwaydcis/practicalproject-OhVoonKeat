package addressapp.view

import addressapp.MainApp
import addressapp.model.Person
import javafx.fxml.FXML
import scalafx.Includes.*
import javafx.scene.control.{Label, TableColumn, TableView}

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

  // Initialize Table View display contents model
  def initialize(): Unit =
    personTable.items = MainApp.personData
    // Initialize columns's cell values
    firstNameColumn.cellValueFactory = {_.value.firstName}
    lastNameColumn.cellValueFactory = {_.value.lastName}