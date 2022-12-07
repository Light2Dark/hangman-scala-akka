import akka.actor.typed.ActorRef
import scalafx.collections.ObservableBuffer
import scalafxml.core.macros.sfxml

@sfxml
class MainHangmanController() {

  var chatClientRef: Option[ActorRef[HangmanClient.Command]] = None
  val receivedText: ObservableBuffer[String] =  new ObservableBuffer[String]()

  def startGame() = {
    Hangman.showView(getClass.getResource("com.hangman.view/SetNameView.fxml"))
  }

  def handleExit() = {
    System.exit(0)
  }
}