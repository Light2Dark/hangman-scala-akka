import Hangman.stage
import scalafx.collections.ObservableSet
import scalafx.scene.control.{Alert, Button}
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.image.ImageView
import scalafx.scene.text.Text
import scalafx.scene.layout.GridPane
import scalafxml.core.macros.sfxml
import scalafx.scene.text.Font
import scalafx.Includes._
import scalafx.event.ActionEvent

@sfxml
class LobbyController(val lobbyList: GridPane, private val spinner: ImageView, val playerOneText: Text) {

  // if we already have the user name, we can call playerOneText's value to set the name

  def addRoom() {
    //add a room to the lobby view
    val maxRooms: Int = lobbyList.getRowConstraints.size() - 1 // not considering the header

    // checking if lobby list is full and alerting then returning if it is
    if (HangmanClient.lobby.size >= maxRooms) {
      // create alert box
      new Alert(AlertType.Error) {
        initOwner(stage)
        title = "Error"
        headerText = "Error: Max Rooms."
        contentText = "We have already reached the maximum number of rooms, please try again next time!"
      }.showAndWait()
    } else {
      populateLobbyList()
    }
  }

  // delete a room from the lobby view
  def deleteRoom() {
    if (HangmanClient.lobby.size <= 0) {
      println("Error: No rooms to delete")
      return
    }
    populateLobbyList()
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

  def populateLobbyList() = {
    // re-render whole grid pane so that the lobbyView is always consistent across clients
    // remove old lobbyList. removes rowIndex 1 onwards.
    lobbyList.getChildren().remove(3, lobbyList.getChildren().size() - 1)

    // add all existing rooms to the view from lobby
    var rowCount = 1 // skip 0th row for header
    for (room <- HangmanClient.lobby) {
      // TODO: get name of room
      val roomName: Text = new Text(room.toString)
      roomName.font = Font("Hololens MDL2 Assets", size = 18.0)

      // TODO: get player count in room
      val players: Text = new Text("1/2")
      players.font = Font("Hololens MDL2 Assets", size = 18.0)

      val button: Button = new Button("Join")
      button.font = Font("Gill Sans MT", size = 16.0)
      button.prefWidth = 70
      button.onAction = (event: ActionEvent) => {
        println("Joining!")
      }

      // add to a row in gridpane
      lobbyList.add(roomName, 0, rowCount)
      lobbyList.add(players, 1, rowCount)
      lobbyList.add(button, 2, rowCount)

      rowCount += 1
    }
  }
}
