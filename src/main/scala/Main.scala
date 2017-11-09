import Entities.Status.Registered
import Entities._
import ScalaFSM.ScalaFSM
import SimpleFSM.FSM
import akka.pattern.ask
import akka.actor.{ActorSystem, Props}
import scala.concurrent.ExecutionContext.Implicits.global
import akka.util.Timeout

import scala.collection.concurrent.TrieMap
import scala.concurrent.duration._

object Main extends App {
  // Part 1, simple FSM
  def printFSMState(fsm: Message2): Unit = println(s"The state of the FSM is: ${fsm.simpleFSM.fsmStatus} with message: ${fsm.getClass}")
  val fsm1 = FSM("anID", Registered)

  //  Failure case, Registered -> Registered(?)
  val deallocateCommand = DeallocateCommand("anID")
  val c2 = fsm1(deallocateCommand)
  printFSMState(c2)

  // Valid command, Registered -> Allocated
  val allocateCommand = AllocateCommand("anID")
  val c3 = fsm1(allocateCommand)
  printFSMState(c3)

  // Valid Command, Allocated -> Registered
  val c4 = c3.simpleFSM(deallocateCommand)
  printFSMState(c4)

  //  Failure case
  //  val command3 = AllocateCommand("anotherID")
  //  val c5 = fsm1(command3)
  //  printFSMState(c5)

  // --------------------------------------------------------------
  // Part 2, complex FSM
  val fsm2 = new ScalaFSM[Registered]("anID")

  val (s2, r1) = fsm2(AllocateMessage("id1"))
  println(r1)

  val (s3, r2) = s2(DeallocateMessage("id2"))
  println(r2)

  //val (s4, r3) = s3(DeallocateMessage("id2"))


  // --------------------------------------------------------------
  // Part 3, Actors baby!
  val system = ActorSystem("HelloSystem")
  val fsmActor = system.actorOf(Props[FSMActor], name = "actor")
  val defaultTimeout = Timeout(10.seconds)

  val command1Rocket1: Command = DeallocateCommand("rocket1")
  val command1Rocket2: Command = AllocateCommand("rocket2")

  fsmActor ! command1Rocket1
  fsmActor ! command1Rocket2

  ask(fsmActor, "gimmyState")(defaultTimeout).mapTo[TrieMap[String, FSM]].map { fsms =>
    fsms.foreach(x => println(x._1, x._2.fsmStatus))
    System.exit(0)
  }

  // --------------------------------------------------------------
  // Part 4: Your turn! Implement the complex FSM with the actor system

}