import scalafx.scene.image.ImageView
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml

@sfxml
class LobbyController(private val spinner: ImageView, val playerOneText: Text) {

  // if we already have the user name, we can call playerOneText's value to set the name

  def launchGameSession() = {
    Hangman.showView(getClass.getResource("com.hangman.view/GameView.fxml"))
  }

  def backToLobby = {
    Hangman.showView(getClass.getResource("com.hangman.view/LobbyView.fxml"))
  }

  def backToMenu = {
    Hangman.showView(getClass.getResource("com.hangman.view/MainHangmanView.fxml"))
  }

  def createNewGame = {
    Hangman.showView(getClass.getResource("com.hangman.view/CreateNewGameView.fxml"))
  }

  // when user clicks on cancel in the waiting room
  def cancelGame = {
    Hangman.showView(getClass.getResource("com.hangman.view/LobbyView.fxml"))
  }
}
