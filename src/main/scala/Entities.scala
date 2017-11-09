import SimpleFSM.FSM
import enumeratum.{Enum, EnumEntry}

object Entities {
  sealed trait Command {
    val id: String
  }

  case class RegisterCommand(id: String) extends Command
  case class DeRegisterCommand(id: String) extends Command
  case class AllocateCommand(id: String) extends Command
  case class DeallocateCommand(id: String) extends Command
  case class ActivateCommand(id: String) extends Command
  case class DeActivateCommand(id: String) extends Command
  case class ToBeActivatedCommand(id: String) extends Command
  case class ToBeDeactivatedCommand(id: String) extends Command

  sealed trait Message {
    val id: String
  }

  case class RegisterMessage(id: String) extends Message
  case class DeRegisterMessage(id: String) extends Message
  case class AllocateMessage(id: String) extends Message
  case class DeallocateMessage(id: String) extends Message
  case class ActivateMessage(id: String) extends Message
  case class ToBeActivatedMessage(id: String) extends Message
  case class ToBeDeactivatedMessage(id: String) extends Message
  case class TransitionNotSupported(id: String) extends Message

  sealed trait Message2 {
    val id: String
    val simpleFSM: FSM
  }

  case class RegisterMessage2(id: String, simpleFSM: FSM) extends Message2
  case class DeRegisterMessage2(id: String, simpleFSM: FSM) extends Message2
  case class AllocateMessage2(id: String, simpleFSM: FSM) extends Message2
  case class DeallocateMessage2(id: String, simpleFSM: FSM) extends Message2
  case class ActivateMessage2(id: String, simpleFSM: FSM) extends Message2
  case class ToBeActivatedMessage2(id: String, simpleFSM: FSM) extends Message2
  case class ToBeDeactivatedMessage2(id: String, simpleFSM: FSM) extends Message2
  case class TransitionNotSupported2(id: String, simpleFSM: FSM) extends Message2

  sealed trait Status extends EnumEntry {
    val code: Int
  }

  object Status extends Enum[Status] {
    val values = findValues

    case object Registered extends Status {
      override val code = 3
    }

    case object Allocated extends Status {
      override val code = 2
    }

    case object ToBeActivated extends Status {
      override val code = 2
    }

    case object Activated extends Status {
      override val code = 1
    }

    case object ToBeDeactivated extends Status {
      override val code = 1
    }

    case object Unavailable extends Status {
      override val code = 4
    }

  }

  sealed trait State
  final class Registered extends State
  final class Allocated extends State
  final class ToBeActivated extends State
  final class Activated extends State
  final class ToBeDeactivated extends State
  final class Unavailable extends State
}
