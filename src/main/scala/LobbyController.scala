import scalafxml.core.macros.sfxml

@sfxml
class LobbyController() {
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
}
