import akka.actor.typed.{ActorRef, PostStop, ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.receptionist.{Receptionist,ServiceKey}
import akka.cluster.typed._
import akka.{ actor => classic }
import akka.actor.typed.scaladsl.adapter._
import scalafx.collections.ObservableHashSet
import scalafx.application.Platform
import akka.cluster.ClusterEvent.ReachabilityEvent
import akka.cluster.ClusterEvent.ReachableMember
import akka.cluster.ClusterEvent.UnreachableMember
import akka.cluster.ClusterEvent.MemberEvent
import akka.actor.Address

object HangmanClient {
    sealed trait Command
    //internal protocol
    // TODO: modify properties of the msgs
    //start the application and initialize the client actor
    case object Start extends Command
    //sent from the controller to client, the client will then send a LoadLobby msg to the server to get lobby details
    case class StartLoadLobby(name: String) extends Command
    //lobby details sent from server to client actor
    case class Lobby(lobby: List[Room]) extends Command
    //sent from the controller to the client actor to start the process of creating a room
    case object StartCreateRoom extends Command
    //room details upon successful creation, sent from server to client actor
    case class RoomDetails(room: Room) extends Command
    //sent from the controller to the client actor to leave the room
    case object StartLeaveRoom extends Command
    //sent from the controller to the client actor to join a room
    case class StartJoinRoom(room: Room) extends Command
    //game details sent from server to client actor when the room is full and game is starting, as well as every time after a player makes a guess
    //the client can tell from the game state if it is his turn to make a guess and disable and enable buttons accordingly
    case class GameState(game: Game) extends Command
    //sent from controller to client actor to make a guess
    case class Guess(alphabet: Char) extends Command
    //sent from server to client after the game ends. 
    case class GameEnded(won: Boolean) extends Command

    final case object FindTheServer extends Command
    private case class ListingResponse(listing: Receptionist.Listing) extends Command
    //private final case class MemberChange(event: MemberEvent) extends Command
    // private final case class ReachabilityChange(reachabilityEvent: ReachabilityEvent) extends Command
    val lobby = new ObservableHashSet[Room]()

//   TODO: decide if we want to implement unreachable
//    val unreachables = new ObservableHashSet[Address]()
//     unreachables.onChange{(ns, _) =>
//         Platform.runLater {
//             Client.control.updateList(members.toList.filter(y => ! unreachables.exists (x => x == y.ref.path.address)))
//         }
//     }

//  TODO: change this function to update the lobby list
//  members.onChange{(ns, _) =>
//     Platform.runLater {
//         Client.control.updateList(ns.toList.filter(y => ! unreachables.exists (x => x == y.ref.path.address)))
//     }  
//   }

    var defaultBehavior: Option[Behavior[HangmanClient.Command]] = None
    var remoteOpt: Option[ActorRef[HangmanServer.Command]] = None 
    var nameOpt: Option[String] = None

    def lobbyBehavior(): Behavior[HangmanClient.Command] = Behaviors.receive[HangmanClient.Command] { (context, message) => 
        message match {
            case Lobby(roomList) =>
                //update the list of rooms in the lobby
                Behaviors.same

            case StartCreateRoom =>
                //send a create room msg to the server
                Behaviors.same

            case RoomDetails(room) =>
                //this msg serves as an acknowledgement that the room has been successfully created. Update the UI to show the room 
                waitingBehavior()

            case StartJoinRoom(list: Iterable[User]) =>
                //send a join room msg to the server
                Behaviors.same

            case GameState(game) =>
                //this msg serves as an acknowledgement that the user has successfully joined the room
                //start the game by showing the in game UI
                inGameBehavior()
        }
    }.receiveSignal {
        case (context, PostStop) =>
            for (name <- nameOpt;
                remote <- remoteOpt){
            remote ! HangmanServer.Leave(name, context.self)
            }
            defaultBehavior.getOrElse(Behaviors.same)
    }

    def waitingBehavior(): Behavior[HangmanClient.Command] = Behaviors.receive[HangmanClient.Command] { (context, message) => 
        message match {
            case StartLeaveRoom =>
                //send a LeaveRoom msg to the server
                Behaviors.same

            case Lobby(roomList) =>
                //this serves as an acknowledgement that the user has successfully left the room
                //show the lobby UI
                lobbyBehavior()

            case GameState(game) =>
                //this msg is received when someone has joined the room and the game can be started
                //start the game by showing the in game UI
                inGameBehavior()
        }
    }.receiveSignal {
        case (context, PostStop) =>
            for (name <- nameOpt;
                remote <- remoteOpt){
            remote ! HangmanServer.Leave(name, context.self)
            }
            defaultBehavior.getOrElse(Behaviors.same)
    }

    def inGameBehavior(): Behavior[HangmanClient.Command] = Behaviors.receive[HangmanClient.Command] { (context, message) => 
        message match {
            case GameState(game) =>
                //update the UI to reflect the latest game state
                Behaviors.same

            case Guess(alphabet) =>
                //send a GuessAlphabet msg to the server
                Behaviors.same

            case GameEnded(won) =>
                //update the UI to show if the players won/lost
                //the behavior is automatically switched back to lobby behavior. The user will not return to the lobby unless a button is clicked on, 
                //but the lobby operations (e.g. updating the lobby with the latest rooms) will be performed nonetheless
                lobbyBehavior()
        }
    }.receiveSignal {
        case (context, PostStop) =>
            for (name <- nameOpt;
                remote <- remoteOpt){
            remote ! HangmanServer.Leave(name, context.self)
            }
            defaultBehavior.getOrElse(Behaviors.same)
    }

    def apply(): Behavior[HangmanClient.Command] =
        Behaviors.setup { context =>
        var counter = 0
        // (1) a ServiceKey is a unique identifier for this actor

    //    val upnpRef = context.spawn(Upnp(), Upnp.name)
    //     upnpRef ! AddPortMapping(20000)
        
          
    // val reachabilityAdapter = context.messageAdapter(ReachabilityChange)
    // Cluster(context.system).subscriptions ! Subscribe(reachabilityAdapter, classOf[ReachabilityEvent])

        val listingAdapter: ActorRef[Receptionist.Listing] =
            context.messageAdapter { listing =>
                println(s"listingAdapter:listing: ${listing.toString}")
                HangmanClient.ListingResponse(listing)
            }

        context.system.receptionist ! Receptionist.Subscribe(HangmanServer.ServerKey, listingAdapter)
        //context.actorOf(RemoteRouterConfig(RoundRobinPool(5), addresses).props(Props[HangmanClient.TestActorClassic]()), "testA")
        defaultBehavior = Some(Behaviors.receiveMessage[HangmanClient.Command] { message =>
            message match {
                case Start =>
                    context.self ! FindTheServer 
                    Behaviors.same

                case FindTheServer =>
                    println(s"Hangman Client: got a FindTheServer message")
                    context.system.receptionist !
                        Receptionist.Find(HangmanServer.ServerKey, listingAdapter)
                    Behaviors.same

                case ListingResponse(HangmanServer.ServerKey.Listing(listings)) =>
                    val xs: Set[ActorRef[HangmanServer.Command]] = listings
                    for (x <- xs) {
                        remoteOpt = Some(x)
                    }
                    Behaviors.same

                case StartLoadLobby(x) =>
                    //send a LoadLobby msg to the server to get the lobby details
                    Behaviors.same
                
                case Lobby(lobby) => 
                    //show lobby UI, update lobby details and chg behavior to lobby behavior
                    lobbyBehavior()

                // case ReachabilityChange(reachabilityEvent) =>
                // reachabilityEvent match {
                //     case UnreachableMember(member) =>
                //         unreachables += member.address
                //         Behaviors.same
                //     case ReachableMember(member) =>
                //         unreachables -= member.address
                //         Behaviors.same
                // }                    
                case _=>
                    Behaviors.unhandled
                
            }
        }.receiveSignal {
        case (context, PostStop) =>
            for (name <- nameOpt;
                remote <- remoteOpt){
                remote ! HangmanServer.Leave(name, context.self)
            }
            Behaviors.same
        })
        defaultBehavior.get
    }
}
