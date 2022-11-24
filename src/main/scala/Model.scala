package com.hep99
import akka.actor.typed.ActorRef

case class User(name: String, ref: ActorRef[ChatClient.Command]) {
  override def toString: String = {
    name
  }

class Game(val players: User[], val wordToGuess: String, val livesLeft = 6, var turn: User)

class Room(val id: Int, val player: User) {
    //logic to randomly generate word to be passed into the game
}
}
