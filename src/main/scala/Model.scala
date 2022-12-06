// package 

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
  var selectedAlphabets: ArrayBuffer[Char] = ArrayBuffer()

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
      selectedAlphabets += alphabet
    }
    else {
      livesLeft -= 1
    }
    turn = players.filter(player => player.name != turn.name).head
    if (livesLeft == 0) status = "lost"
    if (alphabetsToGuess.size == 0) status = "won"
  }

  def isEnded: Boolean = status != "ongoing"
}

class Room(val player: User) extends Serializable {
    //logic to randomly generate word to be passed into the game
    def generateWord: String = {
      val wordList = List("food", "drink", "test", "lame", "walk")
      val random_var = new Random
      val randomWord = wordList(random_var.nextInt(wordList.length))
      randomWord
    }
}