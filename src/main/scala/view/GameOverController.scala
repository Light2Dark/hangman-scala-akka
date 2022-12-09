import Hangman.stage
import scalafx.scene.control.{TextField, Alert}
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.text.Text
import scalafx.application.Platform
import scalafxml.core.macros.sfxml

@sfxml
class GameOverController(val gameOverMessage: Text, val alphabetsCorrect: Text) {
    def showQuitGameReason(reason: String) = {
        if(reason == "won") {
            gameOverMessage.text = "You Won!"
        } 
        else if(reason == "lost") {
            gameOverMessage.text = "You Lost..."
        }
        else {
            gameOverMessage.text = "The other player disconnected"
        }
        println(gameOverMessage)   
    }

    def showNumberOfCorrectGuesses(numberOfCorrectGuesses: Int) = {
        alphabetsCorrect.text = numberOfCorrectGuesses.toString
    }

    def backToLobby = {
        Hangman.showLobby
        Hangman.getLobbyController.populateLobbyList()
    }
}
