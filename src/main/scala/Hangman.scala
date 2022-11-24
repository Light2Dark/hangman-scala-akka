import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import scalafx.Includes._
import javafx.{scene => jfxs}

object HangmanClient extends JFXApp {
  val rootResource = getClass.getResource("com.hangman.view/RootLayout.fxml")
  val loader = new FXMLLoader(rootResource, NoDependencyResolver)
  loader.load()
  val control = loader.getController[MainHangmanController#Controller]()
  val roots = loader.getRoot[jfxs.layout.BorderPane]

  stage = new PrimaryStage {
    title = "Hangman"
    // icons += new Image(getClass.getResourceAsStream("resources/images/logo.png"))
    resizable = false
    scene = new Scene {
      root = roots
      // stylesheets = List(getClass.getResource("view/styles.css").toString())
    }
  }

  def showView(resource: java.net.URL) = {
    val resourceLoader = new FXMLLoader(resource, NoDependencyResolver)
    resourceLoader.load()

    val viewRoots = resourceLoader.getRoot[jfxs.layout.AnchorPane]
    roots.setCenter(viewRoots)
  }

  val mainView = getClass.getResource("com.hangman.view/MainHangmanView.fxml")
  showView(mainView)
}