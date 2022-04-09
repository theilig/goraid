package models

import play.api.libs.json.{JsArray, JsNumber, JsObject, JsPath, JsString, JsValue, Json, Writes}

case class Pokemon(
                    id: Int,
                    dex: PokedexEntry,
                    intrinsicValue: Option[Stats],
                    combatPower: Int,
                    levelRange: (Int, Int),
                    fastMove: FastMove,
                    chargedMoves: Set[ChargedMove]
                  ) {
  def update(newData: JsValue): Pokemon = {
    val t = (JsPath \ "stats" \ "attack").readNullable[Int].reads(newData)
    val iv = Some(Stats(
      (JsPath \ "stats" \ "attack").readNullable[Int].reads(newData).get.getOrElse(intrinsicValue.map(_.attack).getOrElse(0)),
      (JsPath \ "stats" \ "defense").readNullable[Int].reads(newData).get.getOrElse(intrinsicValue.map(_.defense).getOrElse(0)),
      (JsPath \ "stats" \ "hitPoints").readNullable[Int].reads(newData).get.getOrElse(intrinsicValue.map(_.hitPoints).getOrElse(0))
    ))
    val oldIvSum = intrinsicValue.map(s => s.attack + s.defense + s.hitPoints).getOrElse(0)
    val newIvSum = iv.map(s => s.attack + s.defense + s.hitPoints).get
    val adjustedOldIv = if (oldIvSum == 0) { 22 } else { oldIvSum }
    val levelAdjustment = if (oldIvSum != newIvSum) {
      (adjustedOldIv - newIvSum) / 10
    } else {
      0
    }
    new Pokemon(
      id = id,
      dex = dex,
      intrinsicValue = iv,
      combatPower = combatPower,
      levelRange = (levelRange._1 + levelAdjustment, levelRange._2 + levelAdjustment),
      fastMove = fastMove,
      chargedMoves = chargedMoves
    )
  }
}

object Pokemon {
  val InitialId = 0 // used before a database id is assigned
  implicit val pokemonWrites: Writes[Pokemon] = Writes[Pokemon](p => {
    val levelGuess = (p.levelRange._1 + p.levelRange._2) / 2
    val combinedStats = p.dex.baseStats + p.intrinsicValue.getOrElse(Stats(0, 0, 0))
    val iv = p.intrinsicValue.getOrElse(Stats(0, 0, 0))
    JsObject(Seq(
      "id" -> JsNumber(p.id),
      "name" -> JsString(p.dex.name),
      "combatPower" -> JsNumber(p.combatPower),
      "level" -> JsNumber(levelGuess),
      "dexId" -> JsNumber(p.dex.id),
      "stats" -> Stats.statsFormat.writes(combinedStats),
      "iv" -> Stats.statsFormat.writes(iv),
      "fastMove" -> Json.toJson(p.fastMove),
      "chargedMoves" -> JsArray(p.chargedMoves.toList.map(m => Json.toJson(m))),
      "types" -> JsArray(p.dex.types.toList.map(t => Json.toJson(t)))
    ))
  })

  def makeNewPokemon(creation: PokemonCreation, pokedex: Pokedex, moveList: MoveList): Pokemon = {
    val dexEntry = pokedex.getPokemon(creation.id)
    val chargedMoves = List(Some(creation.chargedMove), creation.alternateChargedMove).flatten
    val pokemon = dexEntry.map(entry => {
      val levelRange = entry.cpEstimates.guessLevelRange(creation.combatPower)
      if (!entry.fastMoves.exists(m => m.name == creation.fastMove)) {
        throw new IllegalArgumentException(s"This pokemon doesn't have ${creation.fastMove} as a move")
      }
      if (chargedMoves.exists(m => !entry.chargedMoves.exists(allowedMove => allowedMove.name == m))) {
        throw new IllegalArgumentException(s"This pokemon doesn't have ${chargedMoves.mkString(",")} move(s)")
      }
      new Pokemon(
        Pokemon.InitialId,
        entry,
        None,
        creation.combatPower,
        levelRange,
        moveList.fastMoves(creation.fastMove),
        chargedMoves.map(m => moveList.chargedMoves(m)).toSet
      )
    })
    if (pokemon.isEmpty) {
      throw new IllegalArgumentException(s"Invalid id for pokemon ${creation.id}")
    }
    pokemon.get
  }
}
