package addressapp
import javafx.fxml.FXMLLoader
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes.*
import scalafx.scene as sfxs
import javafx.scene as jfxs
import scalafx.collections.ObservableBuffer
import addressapp.model.Person
import addressapp.view.PersonEditDialogController
import scalafx.stage.{Modality, Stage}


object MainApp extends JFXApp3:

  // window root pane
  var roots: Option[sfxs.layout.BorderPane] = None
  // ... AFTER THE OTHER VARIABLES ...

  // The data as an observable list of Persons.
  val personData = new ObservableBuffer[Person]()
  
  // Constructor
  personData += new Person("Hans", "Muster")
  personData += new Person("Ruth", "Mueller")
  personData += new Person("Heinz", "Kurz")
  personData += new Person("Cornelia", "Meier")
  personData += new Person("Werner", "Meyer")
  personData += new Person("Lydia", "Kunz")
  personData += new Person("Anna", "Best")
  personData += new Person("Stefan", "Meier")
  personData += new Person("Martin", "Mueller")

  // ... THE REST OF THE CLASS ...
  
  override def start(): Unit =
    // transform path of RootLayout.fxml to URI for resource location.
    val rootResource = getClass.getResource("view/RootLayout.fxml")
    // initialize the loader object.
    val loader = new FXMLLoader(rootResource)
    // load root layout from fxml file.
    loader.load()
    // retrieve the root component BorderPane from the FXML
    // refer to slides on scala option monad
    roots = Option(loader.getRoot[jfxs.layout.BorderPane])
    stage = new PrimaryStage():
      title = "AddressApp"
      scene = new Scene():
        root = roots.get
    // call to display PersonOverview when app starts
    showPersonOverview()

  // actions for displaying PersonOverview window
  def showPersonOverview(): Unit =
    val resource = getClass.getResource("view/PersonOverview.fxml")
    val loader = new FXMLLoader(resource)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.get.center = roots

  def showPersonEditDialog(person: Person): Boolean =
    val resource = getClass.getResource("view/PersonEditDialog.fxml")
    val loader = new FXMLLoader(resource)
    loader.load()
    val roots2 = loader.getRoot[jfxs.Parent]
    val control = loader.getController[PersonEditDialogController]
    val dialog = new Stage():
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      scene = new Scene:
        root = roots2
    control.dialogStage = dialog
    control.person = person
    dialog.showAndWait()
    control.okClicked // return true if OK
  end showPersonEditDialog
