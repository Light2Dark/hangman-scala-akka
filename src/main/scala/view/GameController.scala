import scalafxml.core.macros.sfxml
import scalafx.event.ActionEvent
import scalafx.scene.control.{Button}

@sfxml
class GameController(
    // Alphabet buttons
    private val buttonA: Button
    private val buttonB: Button
    private val buttonC: Button
    private val buttonD: Button
    private val buttonE: Button
    private val buttonF: Button
    private val buttonG: Button
    private val buttonH: Button
    private val buttonI: Button
    private val buttonJ: Button
    private val buttonK: Button
    private val buttonL: Button
    private val buttonM: Button
    private val buttonN: Button
    private val buttonO: Button
    private val buttonP: Button
    private val buttonQ: Button
    private val buttonR: Button
    private val buttonS: Button
    private val buttonT: Button
    private val buttonU: Button
    private val buttonV: Button
    private val buttonW: Button
    private val buttonX: Button
    private val buttonY: Button
    private val buttonZ: Button
  ) {

  def handleAlphabetClicked(): Char = {
    // Get text from clicked button 
    // Update game state
    
  }

  def quitGame = {
    Hangman.showView(getClass.getResource("com.hangman.view/GameOverView.fxml"))
  }

  def backToMenu = {
    Hangman.showView(getClass.getResource("com.hangman.view/MainHangmanView.fxml"))
  }
}
