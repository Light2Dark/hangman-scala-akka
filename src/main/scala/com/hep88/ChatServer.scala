package com.hep88
import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.receptionist.{Receptionist,ServiceKey}
import com.hep88.Upnp
import com.hep88.Upnp.AddPortMapping
import com.hep88.MyConfiguration
import scalafx.collections.ObservableHashSet
object ChatServer {
  sealed trait Command
  case class JoinChat(name: String, from: ActorRef[ChatClient.Command]) extends Command
  case class Leave(name: String, from: ActorRef[ChatClient.Command]) extends Command
  val members = new ObservableHashSet[User]()

  members.onChange{(ns, _) =>
    for(member <- ns){
      member.ref ! ChatClient.MemberList(members.toList)
    }
  }

  val ServerKey: ServiceKey[ChatServer.Command] = ServiceKey("ChatServer")

  def apply(): Behavior[ChatServer.Command] =
    Behaviors.setup { context =>
      val upnpRef = context.spawn(Upnp(), Upnp.name)
      upnpRef ! AddPortMapping(20000)

      context.system.receptionist ! Receptionist.Register(ServerKey, context.self)
      
      Behaviors.receiveMessage { message =>
        message match {
            case JoinChat(name, from) =>
                members += User(name, from)
                from ! ChatClient.Joined(members.toList)
                Behaviors.same
            case Leave(name, from) => 
                members -= User(name, from)
                Behaviors.same
            }
        
      }
    }
}

object ChatServerApp extends App {
  val greeterMain: ActorSystem[ChatServer.Command] = ActorSystem(ChatServer(), "HelloSystem")

}
