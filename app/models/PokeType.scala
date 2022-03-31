package models

import play.api.libs.json.{JsError, JsString, JsSuccess, Reads, Writes}

sealed trait PokeType {
  val weak: Set[PokeType]
  val strong: Set[PokeType]
}

case object Fire extends PokeType {
  override val weak = Set()
  override val strong = Set()
}
case object Water extends PokeType {
  override val weak = Set()
  override val strong = Set()
}
case object Grass extends PokeType {
  override val weak = Set()
  override val strong = Set()
}
case object Ghost extends PokeType {
  override val weak = Set()
  override val strong = Set()
}
case object Poison extends PokeType {
  override val weak = Set()
  override val strong = Set()
}
case object Dark extends PokeType {
  override val weak = Set()
  override val strong = Set()
}
case object Steel extends PokeType {
  override val weak = Set()
  override val strong = Set()
}
case object Psychic extends PokeType {
  override val weak = Set()
  override val strong = Set()
}
case object Normal extends PokeType {
  override val weak = Set()
  override val strong = Set()
}
case object Fighting extends PokeType {
  override val weak = Set()
  override val strong = Set()
}
case object Fairy extends PokeType {
  override val weak = Set()
  override val strong = Set()
}
case object Dragon extends PokeType {
  override val weak = Set()
  override val strong = Set()
}
case object Flying extends PokeType {
  override val weak = Set()
  override val strong = Set()
}
case object Ground extends PokeType {
  override val weak = Set()
  override val strong = Set()
}
case object Rock extends PokeType {
  override val weak = Set()
  override val strong = Set()
}
case object Bug extends PokeType {
  override val weak = Set()
  override val strong = Set()
}
case object Electric extends PokeType {
  override val weak = Set()
  override val strong = Set()
}
case object Ice extends PokeType {
  override val weak = Set()
  override val strong = Set()
}

object PokeType {
  implicit val pokeTypeWrites: Writes[PokeType] = Writes[PokeType]( t => {
    JsString(t.getClass.getSimpleName.replace("$", ""))
  })

  implicit val pokeTypeReads: Reads[PokeType] = Reads[PokeType]( js => js match {
    case JsString(s) if s.toLowerCase.equals("fire") => JsSuccess(Fire)
    case JsString(s) if s.toLowerCase.equals("water") => JsSuccess(Water)
    case JsString(s) if s.toLowerCase.equals("grass") => JsSuccess(Grass)
    case JsString(s) if s.toLowerCase.equals("ghost") => JsSuccess(Ghost)
    case JsString(s) if s.toLowerCase.equals("poison") => JsSuccess(Poison)
    case JsString(s) if s.toLowerCase.equals("dark") => JsSuccess(Dark)
    case JsString(s) if s.toLowerCase.equals("steel") => JsSuccess(Steel)
    case JsString(s) if s.toLowerCase.equals("psychic") => JsSuccess(Psychic)
    case JsString(s) if s.toLowerCase.equals("normal") => JsSuccess(Normal)
    case JsString(s) if s.toLowerCase.equals("fighting") => JsSuccess(Fighting)
    case JsString(s) if s.toLowerCase.equals("fairy") => JsSuccess(Fairy)
    case JsString(s) if s.toLowerCase.equals("dragon") => JsSuccess(Dragon)
    case JsString(s) if s.toLowerCase.equals("flying") => JsSuccess(Flying)
    case JsString(s) if s.toLowerCase.equals("ground") => JsSuccess(Ground)
    case JsString(s) if s.toLowerCase.equals("rock") => JsSuccess(Rock)
    case JsString(s) if s.toLowerCase.equals("bug") => JsSuccess(Bug)
    case JsString(s) if s.toLowerCase.equals("electric") => JsSuccess(Electric)
    case JsString(s) if s.toLowerCase.equals("ice") => JsSuccess(Ice)
    case _ => JsError("Unknown PokeType")
  })
}
