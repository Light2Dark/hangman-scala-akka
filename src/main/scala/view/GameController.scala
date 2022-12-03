import scalafxml.core.macros.sfxml
import scalafx.event.ActionEvent
import scalafx.scene.control.{Button}
// import javafx.scene.control.{Button}


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
    private val buttonZ: Button
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

  def handleAlphabetClicked(action: ActionEvent) = {
    // Get text from clicked button 
    var buttonClicked = action.getSource.asInstanceOf[javafx.scene.control.Button]
    var alphaClicked: Char = buttonClicked.getText.charAt(0)

    println(alphaClicked)

    // Update game state
    
  }

  // Disable buttons according to Game state
  def setAlphabetButtons(game: Game): Unit = {

  }

  def quitGame = {
    Hangman.showView(getClass.getResource("com.hangman.view/GameOverView.fxml"))
  }

  def backToMenu = {
    Hangman.showView(getClass.getResource("com.hangman.view/MainHangmanView.fxml"))
  }
}
