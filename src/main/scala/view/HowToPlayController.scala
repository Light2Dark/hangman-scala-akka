import scalafx.scene.control.TextField
import scalafxml.core.macros.sfxml

@sfxml
class HowToPlayController(private val playerName: TextField) {
  def goToLobby = {
    // Can get the user's name from here
    val name: String = playerName.text.value
    Hangman.showView(getClass.getResource("com.hangman.view/LobbyView.fxml"))
  }

  def backToMenu = {
    Hangman.showView(getClass.getResource("com.hangman.view/MainHangmanView.fxml"))
  }
}
