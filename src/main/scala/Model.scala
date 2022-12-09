import akka.actor.typed.ActorRef
import scala.util.Random
import scala.collection.mutable.{Set, ArrayBuffer}

//the status of the user can either be lobby, waiting, or inGame
case class User(name: String, ref: ActorRef[HangmanClient.Command], var status: String) {
  override def toString: String = {
    name
  }
}

class Game(val players: List[User], val wordToGuess: String, var livesLeft: Int, var turn: User, var status: String) extends Serializable {
  override def toString: String = {
    s"word: $wordToGuess, lives: $livesLeft, turn: $turn, alphabets to guess: $alphabetsToGuess"
  }

  var availableAlphabets: ArrayBuffer[Char] = ArrayBuffer(
    'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
  )

  val alphabetsToGuess: Set[Char] = {
    val alphabetSet: Set[Char] = Set()
    for (alphabet <- wordToGuess) {
      if (!alphabetSet.apply(alphabet)) {
        alphabetSet += alphabet
      }
    }
    alphabetSet
  }
  
  def guess(alphabet: Char): Unit = {
    //if alphabet is in the wordToGuess, remove guessed alphabet from string, else deduct one life
    //if number of lives == 0 change status to "lost"
    //else if all alphabets have been guessed, change status to "won"
    if (alphabetsToGuess.apply(alphabet)) {
      alphabetsToGuess -= alphabet
    }
    else {
      livesLeft -= 1
    }
    availableAlphabets -= alphabet
    turn = players.filter(player => player.name != turn.name).head
    if (livesLeft == 0) status = "lost"
    if (alphabetsToGuess.size == 0) status = "won"
  }

  def isEnded: Boolean = status != "ongoing"

  //total number of guesses - number of incorrect guesses
  def calculateNumberOfCorrectGuesses: Int = (26 - availableAlphabets.length) - (6 - livesLeft)
}

class Room(val player: User) extends Serializable {
    //logic to randomly generate word to be passed into the game
    def generateWord: String = {
      val wordList = List("FOOD", "DRINK", "TEST", "LAME", "WALK")
      val random_var = new Random
      val randomWord = wordList(random_var.nextInt(wordList.length))
      randomWord
    }
}