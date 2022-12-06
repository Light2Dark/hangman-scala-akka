import scalafx.scene.control.TextField
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml

@sfxml
class HowToPlayController(private val playerName: TextField, val usernameTakenError: Text) {
  def showUsernameTakenError = {
    println(usernameTakenError.visible)
    usernameTakenError.visible = true
  }
  
  def goToLobby = {
    // Can get the user's name from here
    val name: String = playerName.text.value
    Hangman.hangmanClient ! HangmanClient.StartLoadLobby(name)
  }

  //the lobby is not shown until the server replies the client actor with a Lobby msg
  def changeViewToLobby = Hangman.showView(getClass.getResource("com.hangman.view/LobbyView.fxml"))

  def backToMenu = {
    Hangman.showView(getClass.getResource("com.hangman.view/MainHangmanView.fxml"))
  }
}
