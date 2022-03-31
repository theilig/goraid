package models

import play.api.libs.json.{Json, OFormat}

case class Stats(attack: Int, defense: Int, hitPoints: Int) {
  def +(other: Stats): Stats = {
    Stats(attack + other.attack, defense = defense + other.defense, hitPoints = hitPoints + other.hitPoints)
  }
}

object Stats {
  implicit val statsFormat: OFormat[Stats] = Json.format[Stats]
}
