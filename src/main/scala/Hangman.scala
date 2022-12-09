import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import scalafx.Includes._
import javafx.{scene => jfxs}
import akka.actor.typed.ActorSystem

object Hangman extends JFXApp {
  val hangmanClient: ActorSystem[HangmanClient.Command] = ActorSystem(HangmanClient(), "HelloSystem")
  hangmanClient ! HangmanClient.Start

  val rootResource = getClass.getResource("com.hangman.view/RootLayout.fxml")
  val loader = new FXMLLoader(rootResource, NoDependencyResolver)
  loader.load()
  var control = loader.getController[MainHangmanController#Controller]()
  val roots = loader.getRoot[jfxs.layout.BorderPane]

  stage = new PrimaryStage {
    title = "Hangman"
    resizable = false
    scene = new Scene {
      root = roots
    }
  }

  //controllers for all pages, these variables will be reassigned with the actual controllers once the views are loaded
  var getLobbyController: LobbyController#Controller = null
  var getHowToPlayController: HowToPlayController#Controller = null
  var getGameController: GameController#Controller = null
  var getGameOverController: GameOverController#Controller = null

  def showMainHangman = {
    val resourceLoader = new FXMLLoader(getClass.getResource("com.hangman.view/MainHangmanView.fxml"), NoDependencyResolver)
    resourceLoader.load()
    val viewRoots = resourceLoader.getRoot[jfxs.layout.AnchorPane]
    roots.setCenter(viewRoots)
  }

  def showSetNameView = {
    val resourceLoader = new FXMLLoader(getClass.getResource("com.hangman.view/SetNameView.fxml"), NoDependencyResolver)
    resourceLoader.load()
    getHowToPlayController = resourceLoader.getController[HowToPlayController#Controller]()
    val viewRoots = resourceLoader.getRoot[jfxs.layout.AnchorPane]
    roots.setCenter(viewRoots)
  }

  def showLobby = {
    val resourceLoader = new FXMLLoader(getClass.getResource("com.hangman.view/LobbyView.fxml"), NoDependencyResolver)
    resourceLoader.load()
    getLobbyController = resourceLoader.getController[LobbyController#Controller]()
    val viewRoots = resourceLoader.getRoot[jfxs.layout.AnchorPane]
    roots.setCenter(viewRoots)
  }

  def showCreateNewGame = {
    val resourceLoader = new FXMLLoader(getClass.getResource("com.hangman.view/CreateNewGameView.fxml"), NoDependencyResolver)
    resourceLoader.load()
    getLobbyController = resourceLoader.getController[LobbyController#Controller]()
    val viewRoots = resourceLoader.getRoot[jfxs.layout.AnchorPane]
    roots.setCenter(viewRoots)
  }

  def showGame = {
    val resourceLoader = new FXMLLoader(getClass.getResource("com.hangman.view/GameView.fxml"), NoDependencyResolver)
    resourceLoader.load()
    getGameController = resourceLoader.getController[GameController#Controller]()
    val viewRoots = resourceLoader.getRoot[jfxs.layout.AnchorPane]
    roots.setCenter(viewRoots)
  }

  def showGameOver = {
    val resourceLoader = new FXMLLoader(getClass.getResource("com.hangman.view/GameOverView.fxml"), NoDependencyResolver)
    resourceLoader.load()
    getGameOverController = resourceLoader.getController[GameOverController#Controller]()
    val viewRoots = resourceLoader.getRoot[jfxs.layout.AnchorPane]
    roots.setCenter(viewRoots)
  }

  showMainHangman

  stage.onCloseRequest = handle({
  hangmanClient.terminate
  })
}