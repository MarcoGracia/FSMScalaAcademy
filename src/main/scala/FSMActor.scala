import Entities.Status.Registered
import Entities._
import SimpleFSM.FSM
import akka.actor.Actor

import scala.collection.concurrent.TrieMap

class FSMActor extends Actor {

  private var pool: TrieMap[String, FSM] = new TrieMap[String, FSM]

  def receive = {

    case "hello" => println("hello back at you")

    case command: Command =>
      val fsm = this.pool.get(command.id) match {
        case Some(fsm) => fsm(command)
        case None => FSM(command.id, Registered)(command)
      }
      self ! fsm

    case message: Message2 =>
      if (!message.isInstanceOf[TransitionNotSupported2]) {
        val newEntry = (message.id, message.simpleFSM)
        pool = pool += newEntry
        message match {
          case _: ToBeDeactivatedMessage => println(s"Performing activation for rocket: ${message.id}")
          case _: ToBeActivatedMessage => println(s"Performing deactivation for rocket: ${message.id}")
          case _ => //Do nothing
        }
      } else {
        println(s"Transition not supported for rocket: ${message.id}")
      }

    case "gimmyState" => sender ! this.pool

    case m => println("huh?", m)

  }
}
