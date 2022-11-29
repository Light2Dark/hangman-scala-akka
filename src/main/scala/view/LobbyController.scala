import scalafx.scene.image.ImageView
import scalafx.scene.text.Text
import scalafx.scene.layout.GridPane
import scalafxml.core.macros.sfxml

@sfxml
class LobbyController(val lobbyList: GridPane, private val spinner: ImageView, val playerOneText: Text) {

  // if we already have the user name, we can call playerOneText's value to set the name

  def addRoom(room: Room) {
    //add a room to the lobby view
    //need to check if the lobby list is full, if it is then don't create the room and create an alert box 
  }

  def deleteRoom(room: Room) {
    //delete a room from the lobby view
  }

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
