import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import scalafx.Includes._
import javafx.{scene => jfxs}
import akka.actor.typed.ActorSystem

object Hangman extends JFXApp {
  // implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
  val hangmanClient: ActorSystem[HangmanClient.Command] = ActorSystem(HangmanClient(), "HelloSystem")

  // hangmanClient ! HangmanClient.start

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

  def getLobbyController = {
    val resource = getClass.getResource(s"com.hangman.view/LobbyView.fxml")
    val fxmlLoader = new FXMLLoader(resource, NoDependencyResolver)
    fxmlLoader.load()
    val control = fxmlLoader.getController[LobbyController#Controller]()
    control
  }

  def getHowToPlayController = {
    val resource = getClass.getResource(s"com.hangman.view/SetNameView.fxml")
    val fxmlLoader = new FXMLLoader(resource, NoDependencyResolver)
    fxmlLoader.load()
    val control = fxmlLoader.getController[HowToPlayController#Controller]()
    control
  }

  def getGameController = {
    val resource = getClass.getResource(s"com.hangman.view/GameView.fxml")
    val fxmlLoader = new FXMLLoader(resource, NoDependencyResolver)
    fxmlLoader.load()
    val control = fxmlLoader.getController[GameController#Controller]()
    control
  }

  val mainView = getClass.getResource("com.hangman.view/MainHangmanView.fxml")
  showView(mainView)

  // stage.onCloseRequest = handle( {
  // hangmanClient.terminate
}