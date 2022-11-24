import akka.actor.typed.ActorRef

case class User(name: String, ref: ActorRef[HangmanClient.Command]) {
  override def toString: String = {
    name
  }
}
class Game(val players: List[User], val wordToGuess: String, val livesLeft: Int, var turn: User)

class Room(val id: Int, val player: User) {
    //logic to randomly generate word to be passed into the game
}