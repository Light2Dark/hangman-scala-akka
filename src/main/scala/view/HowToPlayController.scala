import Hangman.stage
import scalafx.scene.control.{TextField, Alert}
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml

@sfxml
class HowToPlayController(private val playerName: TextField, val usernameTakenError: Text) {
  def showUsernameTakenError = {
    new Alert(AlertType.Error) {
        initOwner(stage)
        title = "Error"
        headerText = "Error: Username Taken."
        contentText = "This username has already been taken, please enter another username!"
      }.showAndWait()
  }
  
  def goToLobby = {
    // Can get the user's name from here
    val name: String = playerName.text.value

    // check if name is empty
    if (name.isEmpty) {
      new Alert(AlertType.Error) {
        initOwner(stage)
        title = "Error"
        headerText = "Error: Name not chosen"
        contentText = "Hey, you've not chosen a name! Enter a name and click Next again."
      }.showAndWait()
    } else {
      // start lobby
      Hangman.hangmanClient ! HangmanClient.StartLoadLobby(name)
    }
  }

  //the lobby is not shown until the server replies the client actor with a Lobby msg
  def changeViewToLobby = Hangman.showLobby

  def backToMenu = Hangman.showMainHangman
}
