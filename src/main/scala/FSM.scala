
import Entities.Status._
import Entities._


object ScalaFSM {
  case class Transformation[M <: Message, From <: State, To <: State](f: M => Command) {
    def apply(m: M) = f(m)
  }

  object Transformation {
    implicit def `Registered in Allocated` =
      Transformation[AllocateMessage, Registered, Allocated] { m =>
        AllocateCommand(m.id)
      }

    implicit def `Allocated in Registered` =
      Transformation[DeallocateMessage, Allocated, Registered] { m =>
        DeRegisterCommand(m.id)
      }
  }

  class ScalaFSM[CurrentState <: State] (fmsID: String) {
    val id: String = fmsID
    def apply[M <: Message, NewState <: State](message: M)(implicit transformWith: Transformation[M, CurrentState, NewState]) = {
      this.asInstanceOf[ScalaFSM[NewState]] -> transformWith(message)
    }
  }
}

object SimpleFSM {
  case class FSM(fmsId: String, fsmStatus: Status = Registered) extends ((Command) => Message2) {

    val id: String = fmsId
    val status: Status = fsmStatus

    def apply(message: Command): Message2 = (status, message) match {

      case _ if message.id != this.id =>
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
