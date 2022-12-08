import scalafxml.core.macros.sfxml
import scalafx.event.ActionEvent
import scalafx.scene.control.{Button, Label}
import akka.actor.typed.ActorRef
import scalafx.scene.image.{Image, ImageView}
import scala.collections.mutable.{ListBuffer}

@sfxml
class GameController(
    // Alphabet buttons
    private val buttonA: Button,
    private val buttonB: Button,
    private val buttonC: Button,
    private val buttonD: Button,
    private val buttonE: Button,
    private val buttonF: Button,
    private val buttonG: Button,
    private val buttonH: Button,
    private val buttonI: Button,
    private val buttonJ: Button,
    private val buttonK: Button,
    private val buttonL: Button,
    private val buttonM: Button,
    private val buttonN: Button,
    private val buttonO: Button,
    private val buttonP: Button,
    private val buttonQ: Button,
    private val buttonR: Button,
    private val buttonS: Button,
    private val buttonT: Button,
    private val buttonU: Button,
    private val buttonV: Button,
    private val buttonW: Button,
    private val buttonX: Button,
    private val buttonY: Button,
    private val buttonZ: Button,

    // Player names
    private val player1Name: Label,
    private val player2Name: Label,

    // Word results
    private val wordGuess: Label,

    // Game over page
    private val gameOverMessage: Label,

    // Hangman image
    private val hangmanImage: ImageView
  ) {

  val alphabetButtons = Map(
    'A' -> buttonA,
    'B' -> buttonB,
    'C' -> buttonC,
    'D' -> buttonD,
    'E' -> buttonE,
    'F' -> buttonF,
    'G' -> buttonG,
    'H' -> buttonH,
    'I' -> buttonI,
    'J' -> buttonJ,
    'K' -> buttonK,
    'L' -> buttonL,
    'M' -> buttonM,
    'N' -> buttonN,
    'O' -> buttonO,
    'P' -> buttonP,
    'Q' -> buttonQ,
    'R' -> buttonR,
    'S' -> buttonS,
    'T' -> buttonT,
    'U' -> buttonU,
    'V' -> buttonV,
    'W' -> buttonW,
    'X' -> buttonX,
    'Y' -> buttonY,
    'Z' -> buttonZ
  )

  // Reference to current Client
  var hangmanClientRef: Option[ActorRef[HangmanClient.Command]] = None

  def getButtonChar(button: Button): Char = {
    button.getId.charAt(button.getId.length()-1)
  }

  def handleAlphabetClicked(action: ActionEvent) = {
    // Get alphabet character of clicked button 
    var buttonClicked = action.getSource.asInstanceOf[javafx.scene.control.Button]
    var alphaClicked: Char = buttonClicked.getText.charAt(0)

    // Send Guess message to client - Update game state
    hangmanClientRef.get ! HangmanClient.Guess(alphaClicked)    
  }

  // Customize UI elements according to Game state
  def setGameState(game: Game): Unit = {
    // Set text
    var displayText = game.wordToGuess.toUpperCase().replace("", " ").trim()
    for (a <- alphabetsToGuess) {
      displayText = displayText.replace(a.toString(), "_")
    }

    // Disable all alphabet buttons
    for(alpha, button <-alphabetButtons) {
      button.disable = true
    }

    // Set button availability and current player based on turn
    if (HangmanClient.userOpt.get.name == game.turn.name) {
      for (a <- game.availableAlphabets) {
        alphabetButtons(a).disable = false
      }
    } 

    // to determine if it is the player's turn, say HangmanClient.userOpt.get.name == game.turn.name. We cannot directly compare the two
    //objects bc their references will be different

    // Set hangman life state
    val fileNames = Array("rope7.png", "rope6.png", "rope5.png", "rope4.png", "rope3.png", "rope2.png", "rope1.png")
    changeImage(fileNames(game.livesLeft))
  }

  def quitGame(reason: String) = {
    Hangman.showView(getClass.getResource("com.hangman.view/GameOverView.fxml"))
    if(reason == "Won") {
      gameOverMessage.text = "You Won!"
    } else {
      gameOverMessage.text = "You Lost..."
    }
  }

  def backToMenu = {
    Hangman.showView(getClass.getResource("com.hangman.view/MainHangmanView.fxml"))
  }

  // Change image of the hangman after a guess
  def changeImage(newImageLink: String) = {
    hangmanImage.setImage(new Image(newImageLink))
    // eg: hangmanImage.setImage(new Image("hangman-states-images/base2.png"))
  }
}
