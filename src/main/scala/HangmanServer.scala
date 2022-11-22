package com.hep99
import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.receptionist.{Receptionist,ServiceKey}
import com.hep99.Model._
import com.hep88.Upnp
import com.hep88.Upnp.AddPortMapping
import com.hep88.MyConfiguration
import scalafx.collections.ObservableHashSet

object HangmanServer {
  //TODO: fix properties of messages
  sealed trait Command
  //handles client loading the lobby
  case class LoadLobby(name: String, from: ActorRef[HangmanClient.Command]) extends Command
  //handles client creating a room
  case class CreateRoom(name: String, from: ActorRef[HangmanClient.Command]) extends Command
  //handles client joining a room
  case class JoinRoom(name: String, from: ActorRef[HangmanClient.Command], room: Room) extends Command
  //handles client leaving a room
  case class LeaveRoom((name: String, from: ActorRef[HangmanClient.Command], room: Room) extends Command)
  //handles client guessing an alphabet
  case class GuessAlphabet(name: String, from: ActorRef[HangmanClient.Command], alphabet: Char, game: Game) extends Command
  //handles client closing the application
  case class Leave(name: String, from: ActorRef[HangmanClient.Command]) extends Command

  val usersOnMainMenu: User[] = []
  val lobby = new ObservableHashSet[Room]
  val games: Game[] = []

  lobby.onChange{(ns, _) =>
    for(user <- usersOnMainMenu){
      user.ref ! HangmanClient.Lobby(ns.toList)
    }
  }

  val ServerKey: ServiceKey[HangmanServer.Command] = ServiceKey("HangmanServer")

  def apply(): Behavior[HangmanServer.Command] =
    Behaviors.setup { context =>
      val upnpRef = context.spawn(Upnp(), Upnp.name)
      upnpRef ! AddPortMapping(20000)

      context.system.receptionist ! Receptionist.Register(ServerKey, context.self)
      
      Behaviors.receiveMessage { message =>
        message match {
            case LoadLobby(name, from) =>
                //add user to userOnMainMenu list and send them the lobby
                Behaviors.same
            case CreateRoom(name, from) =>
                //create a new room, send the user to the room, remove the user from the userOnMainMenu list, update all users on the new room, send the user a RoomDetails msg
                Behaviors.same
            case LeaveRoom(name, from, room) =>
                //add the user to the userOnMainMenu list, update all users on the deleted room
                Behaviors.same
            case JoinRoom(name, from, room) =>
                //create a new game, remove the room from the lobby, send both users into the game by sending them GameState msgs.
                Behaviors.same 
            case GuessAlphabet(name, from, alphabet, game) =>
                //update game state, inform other player of the update
                //if game has ended, send a GameEnded msg to the players
                Behaviors.same
            case Leave(name, from) => 
                //if player is in game, end the game, send the other player back to lobby
                //if the player is in the main menu, remove him from the usersOnMainMenu list
                //if the player is in a room, delete the room and inform all users on the main menu about it
                Behaviors.same
            }
        
      }
    }
}

object HangmanServerApp extends App {
  val greeterMain: ActorSystem[HangmanServer.Command] = ActorSystem(HangmanServer(), "HelloSystem")

}
