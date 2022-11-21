package com.hep88
import akka.actor.typed.ActorRef

case class User(name: String, ref: ActorRef[ChatClient.Command]) {
  override def toString: String = {
    name
  }
}
