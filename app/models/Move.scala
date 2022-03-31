package models

import play.api.libs.functional.FunctionalBuilder
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.{JsError, JsPath, JsResult, JsSuccess, JsValue, Json, Reads, Writes}

sealed trait Move {
  val damage: Int
  val start: Int
  val stop: Int
  val done: Int
  val netEnergy: Int
  val pokeType: PokeType
}

object Move {
  def createMove(js: JsValue, netEnergyConstraint: Int => Boolean):
  (FunctionalBuilder[JsResult]#CanBuild7[String, Int, Int, Int, Int, Int, PokeType], JsResult[Boolean]) = {
    val name = (JsPath \ "name").read[String].reads(js)
    val damage = (JsPath \ "damage").read[Int].reads(js)
    val netEnergy = (JsPath \ "netEnergy").read[Int].reads(js)
    val start = (JsPath \ "start").read[Int].reads(js)
    val stop = (JsPath \ "stop").read[Int].reads(js)
    val done = (JsPath \ "done").read[Int].reads(js)
    val pokeType = (JsPath \ "pokeType").read[PokeType].reads(js)
    (name and damage and netEnergy and start and stop and done and pokeType, netEnergy.map(netEnergyConstraint))
  }
}

case class FastMove(
                     name: String,
                     damage: Int,
                     netEnergy: Int,
                     start: Int,
                     stop: Int,
                     done: Int,
                     pokeType: PokeType
                   ) extends Move

case class ChargedMove(
                        name: String,
                        damage: Int,
                        netEnergy: Int,
                        start: Int,
                        stop: Int,
                        done: Int,
                        pokeType: PokeType
                      ) extends Move

object FastMove {
  implicit val fastMoveReads: Reads[FastMove] = Reads[FastMove]( js => {
    val (builder, valid) = Move.createMove(js, x => x >= 0)

    valid match {
      case JsSuccess(true, _) =>
        builder(FastMove.apply _)
      case _ =>
        JsError(s"Fast move does not have positive energy value")
    }
  })
  implicit val fastMoveWrites: Writes[FastMove] = Json.writes[FastMove]
}

object ChargedMove {
  implicit val chargedMoveReads: Reads[ChargedMove] = Reads[ChargedMove]( js => {
    val (builder, valid) = Move.createMove(js, x => x <= 0)
    valid match {
      case JsSuccess(true, _) =>
        builder(ChargedMove.apply _)
      case _ =>
        JsError(s"Charged move has positive energy value")
    }
  })
  implicit val chargedMoveWrites: Writes[ChargedMove] = Json.writes[ChargedMove]
}
