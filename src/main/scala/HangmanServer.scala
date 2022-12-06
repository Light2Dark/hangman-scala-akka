import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.receptionist.{Receptionist,ServiceKey}
import scalafx.collections.ObservableHashSet
import scala.collection.mutable.Set

object HangmanServer {
  //TODO: fix properties of messages
  sealed trait Command
  //handles client loading the lobby
  case class LoadLobby(user: User) extends Command
  //handles client returning to menu
  case class ReturnToMenu(user: User) extends Command
  //handles client creating a room
  case class CreateRoom(user: User) extends Command
  //handles client joining a room
  case class JoinRoom(user: User, room: Room) extends Command
  //handles client leaving a room
  case class LeaveRoom(user: User) extends Command
  //handles client guessing an alphabet
  case class GuessAlphabet(user: User, alphabet: Char) extends Command
  //handles client closing the application
  case class Leave(user: User) extends Command

  val users: Set[User] = Set()
  val usersOnMainMenu: Set[User] = Set()
  val lobby = new ObservableHashSet[Room]
  val games: Set[Game] = Set()

  lobby.onChange{(ns, _) =>
    for(user <- usersOnMainMenu){
      user.ref ! HangmanClient.Lobby(ns.toList)
    }
  }

  val ServerKey: ServiceKey[HangmanServer.Command] = ServiceKey("HangmanServer")

  def apply(): Behavior[HangmanServer.Command] =
    Behaviors.setup { context =>
      // val upnpRef = context.spawn(Upnp(), Upnp.name)
      // upnpRef ! AddPortMapping(20000)

      context.system.receptionist ! Receptionist.Register(ServerKey, context.self)
      
      Behaviors.receiveMessage { message =>
        message match {
            case LoadLobby(user) =>
                //first check if the username has been taken
                if (users.map(existingUser => existingUser.name) contains user.name) {
                  user.ref ! HangmanClient.UsernameTaken
                }
                else {
                  //add user to userOnMainMenu list and send them the lobby
                  users += user
                  usersOnMainMenu += user
                  println(s"users on main menu: $usersOnMainMenu")
                  user.ref ! HangmanClient.Lobby(lobby.toList)
                }
                Behaviors.same
            case ReturnToMenu(user) =>
                usersOnMainMenu.retain(x => x.name != user.name)
                users.retain(x => x.name != user.name)
                Behaviors.same
            case CreateRoom(user) =>
                //create a new room, send the user to the room, remove the user from the userOnMainMenu list, update all users on the new room, send the user a RoomDetails msg
                var newRoom = new Room(user)
                user.ref ! HangmanClient.RoomDetails(newRoom)
                usersOnMainMenu.retain(x => x.name != user.name)
                println(s"users on main menu: $usersOnMainMenu")
                lobby += newRoom
                Behaviors.same
            case LeaveRoom(user) =>
                //add the user to the userOnMainMenu list, update all users on the deleted room
                usersOnMainMenu += user
                println(s"users on main menu: $usersOnMainMenu")
                lobby -= lobby.find(room => room.player.name == user.name).get
                Behaviors.same
            case JoinRoom(user, room) =>
                //create a new game, remove the room from the lobby, send both users into the game by sending them GameState msgs.
                var newGame = new Game(List(room.player, user), room.generateWord, 6, room.player, "ongoing")
                games += newGame
                lobby -= lobby.find(lobbyRoom => lobbyRoom == room).get
                usersOnMainMenu.retain(x => x.name != user.name)
                room.player.ref ! HangmanClient.GameState(newGame)
                user.ref ! HangmanClient.GameState(newGame)
                Behaviors.same 
            case GuessAlphabet(user, alphabet) =>
                //update game state, inform other player of the update
                //if game has ended, send a GameEnded msg to the players
                var game = games.find(ongoingGame => ongoingGame.players.contains(user)).get
                game.guess(alphabet)
                game.players.foreach(player => player.ref ! HangmanClient.GameState(game))
                if (game.isEnded) {
                  game.players.foreach(player => player.ref ! HangmanClient.GameEnded(game.status))
                  games -= game
                  game.players.foreach(player => {
                    usersOnMainMenu += player
                    player.ref ! HangmanClient.Lobby(lobby.toList)
                  })
                }
                Behaviors.same
            case Leave(user) => 
                //if player is in game, end the game, send the other player back to lobby
                //if the player is in the main menu, remove him from the usersOnMainMenu list
                //if the player is in a room, delete the room and inform all users on the main menu about it
                if (user.status == "inGame") {
                  var game = games.find(ongoingGame => ongoingGame.players.contains(user)).get
                  game.players.foreach(player => player.ref ! HangmanClient.GameEnded("The other player disconnected"))
                  games -= game
                  game.players.foreach(player => usersOnMainMenu += player)
                }
                else if (user.status == "lobby") {
                  usersOnMainMenu.retain(x => x.name != user.name)
                }
                else if (user.status == "waiting") {
                  var playerRoom = lobby.find(lobbyRoom => lobbyRoom.player.name == user.name).get
                  lobby -= playerRoom
                }
                users.retain(x => x.name != user.name)
                Behaviors.same
            }
      }
    }
}

object HangmanServerApp extends App {
  val greeterMain: ActorSystem[HangmanServer.Command] = ActorSystem(HangmanServer(), "HelloSystem")

}
