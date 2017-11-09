
import Entities.Status._
import Entities._


object ScalaFSM {
  case class Transformation[M <: Command, From <: State, To <: State](f: M => Message) {
    def apply(m: M) = f(m)
  }

  object Transformation {
    implicit def `Registered in Allocated` =
      Transformation[AllocateCommand, Registered, Allocated] { m =>
        AllocateMessage(m.id)
      }

    implicit def `Allocated in Registered` =
      Transformation[DeallocateCommand, Allocated, Registered] { m =>
        DeallocateMessage(m.id)
      }
  }

  class ScalaFSM[CurrentState <: State] (fmsID: String) {
    val id: String = fmsID
    def apply[M <: Command, NewState <: State](message: M)(implicit transformWith: Transformation[M, CurrentState, NewState]) = {
      this.asInstanceOf[ScalaFSM[NewState]] -> transformWith(message)
    }
  }
}

object SimpleFSM {
  case class FSM(fmsId: String, fsmStatus: Status = Registered) extends ((Command) => Message2) {

    val id: String = fmsId
    val status: Status = fsmStatus

    def apply(command: Command): Message2 = (status, command) match {

      case _ if command.id != this.id =>
        throw new IllegalArgumentException("Wrong rocket buddy!")

      case (Registered, AllocateCommand(vId)) =>
        AllocateMessage2(vId, FSM(id, Allocated))

      case (Allocated, DeallocateCommand(vId)) =>
        DeallocateMessage2(vId, FSM(id, Registered))

      case _ =>
        TransitionNotSupported2(id, this)
    }
  }
}
