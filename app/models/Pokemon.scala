package models

import play.api.libs.json.{JsArray, JsNumber, JsObject, JsString, Json, Writes}

case class Pokemon(
                    dex: PokedexEntry,
                    intrinsicValue: Option[Stats],
                    combatPower: Int,
                    levelRange: (Int, Int),
                    fastMove: FastMove,
                    chargedMoves: Set[ChargedMove]
                  )

object Pokemon {
  implicit val pokemonWrites: Writes[Pokemon] = Writes[Pokemon](p => {
    val levelGuess = (p.levelRange._1 + p.levelRange._2) / 2
    val stats = p.dex.baseStats + p.intrinsicValue.getOrElse(Stats(0, 0, 0))
    JsObject(Seq(
      "name" -> JsString(p.dex.name),
      "combatPower" -> JsNumber(p.combatPower),
      "level" -> JsNumber(levelGuess),
      "id" -> JsNumber(p.dex.id),
      "stats" -> Stats.statsFormat.writes(stats),
      "fastMove" -> Json.toJson(p.fastMove),
      "chargedMoves" -> JsArray(p.chargedMoves.toList.map(m => Json.toJson(m))),
      "types" -> JsArray(p.dex.types.toList.map(t => Json.toJson(t)))
    ))
  })
}
