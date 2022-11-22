import scalafxml.core.macros.sfxml

@sfxml
class GameController() {
  def quitGame = {
    Hangman.showView(getClass.getResource("com.hangman.view/MainHangmanView.fxml"))
  }
}
