import akka.actor.typed.ActorRef
import com.hep88.ChatClient
import scalafx.collections.ObservableBuffer
import scalafxml.core.macros.sfxml

@sfxml
class MainHangmanController() {

  var chatClientRef: Option[ActorRef[ChatClient.Command]] = None
  val receivedText: ObservableBuffer[String] =  new ObservableBuffer[String]()

  def startGame() = {
    HangmanClient.showView(getClass.getResource("com.hangman.view/LobbyView.fxml"))
  }

  def howToPlay() = {
    println("how to play")
  }

}