package models

import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.{JsPath, Reads}

case class PokedexEntry(
                         id: Int,
                         name: String,
                         baseStats: Stats,
                         types: Set[PokeType],
                         fastMoves: Set[FastMove],
                         chargedMoves: Set[ChargedMove],
                         cpEstimates: CpBenchmarks
                       )

object PokedexEntry {
  def pokedexReads(moveList: MoveList): Reads[PokedexEntry] = Reads[PokedexEntry]( js => {
    val id = (JsPath \ "dex").read[Int].reads(js)
    val name = (JsPath \ "name").read[String].reads(js)
    val baseStats = (JsPath \ "baseStats").read[Stats].reads(js)
    val types = (JsPath \ "types").read[List[PokeType]].reads(js).map(l => l.toSet)
    val fastMoveList = (JsPath \ "fastMoves").read[List[String]].reads(js)
    val chargedMoveList = (JsPath \ "chargedMoves").read[List[String]].reads(js)
    val cp500 = (JsPath \ "defaultIVs" \ "cp500").read[EstimateParameters].reads(js)
    val cp1500 = (JsPath \ "defaultIVs" \ "cp1500").read[EstimateParameters].reads(js)
    val cp2500 = (JsPath \ "defaultIVs" \ "cp1500").read[EstimateParameters].reads(js)
    val level25Cp = (JsPath \ "level25CP").readNullable[Int].reads(js)
    val cpBenchmarks = (level25Cp and cp500 and cp1500 and cp2500)(CpBenchmarks.apply _)
    val chargedMoves = chargedMoveList.map(moves => {
      moves.flatMap(move => moveList.chargedMoves.get(move)).toSet
    })
    val fastMoves = fastMoveList.map(moves => {
      moves.flatMap(move => moveList.fastMoves.get(move)).toSet
    })
    (id and name and baseStats and types and fastMoves and chargedMoves and cpBenchmarks)(PokedexEntry.apply _)
  })
}
