import scalafxml.core.macros.sfxml

@sfxml
class LobbyController() {
  def launchGameSession() = {
    HangmanClient.showView(getClass.getResource("com.hangman.view/GameView.fxml"))
  }

  def backToLobby = {
    HangmanClient.showView(getClass.getResource("com.hangman.view/LobbyView.fxml"))
  }

  def backToMenu = {
    HangmanClient.showView(getClass.getResource("com.hangman.view/MainHangmanView.fxml"))
  }

  def createNewGame = {
    HangmanClient.showView(getClass.getResource("com.hangman.view/CreateNewGameView.fxml"))
  }
}
