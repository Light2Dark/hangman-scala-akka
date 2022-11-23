import scalafxml.core.macros.sfxml

@sfxml
class GameController() {
  def quitGame = {
    HangmanClient.showView(getClass.getResource("com.hangman.view/GameOverView.fxml"))
  }

  def backToMenu = {
    HangmanClient.showView(getClass.getResource("com.hangman.view/MainHangmanView.fxml"))
  }
}
