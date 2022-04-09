package models

import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.{JsNumber, JsObject, JsPath, JsResult, JsString, Reads, Writes}

case class PokedexEntry(
                         dex: Int,
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
    def lookupMoves[T](movesResult: JsResult[List[String]], list: Map[String, T]): JsResult[Set[T]] = {
      movesResult.map(moves => {
        moves.map(move => {
          val words = move.split("_").map(s => s.toLowerCase.capitalize).toList
          val possibles = List(
            words.mkString(" "),
            words.mkString("-"),
            words.head + '-' + words.tail.mkString(" "),
            (s"(${words.reverse.head})" :: words.reverse.tail).reverse.mkString(" "),
            words.map(w => w.toLowerCase).mkString("").capitalize
          )
          val result = possibles.foldLeft(None: Option[T])((soFar, next) => if (soFar.isEmpty) list.get(next) else soFar)
          if (result.isEmpty) {
            throw new IllegalArgumentException("Can't find $move")
          }
          result.get
        })
      }.toSet)
    }
    val id = (JsPath \ "id").read[Int].reads(js)
    val dex = (JsPath \ "dex").read[Int].reads(js)
    val name = (JsPath \ "name").read[String].reads(js)
    val baseStats = (JsPath \ "baseStats").read[Stats].reads(js)
    val types = (JsPath \ "types").read[List[PokeType]].reads(js).map(l => l.toSet)
    val allFastMoves = (JsPath \ "fastMoves").read[List[String]].reads(js)
    val allChargedMoves = (JsPath \ "chargedMoves").read[List[String]].reads(js)
    val cp500 = (JsPath \ "defaultIVs" \ "cp500").read[EstimateParameters].reads(js)
    val cp1500 = (JsPath \ "defaultIVs" \ "cp1500").read[EstimateParameters].reads(js)
    val cp2500 = (JsPath \ "defaultIVs" \ "cp2500").read[EstimateParameters].reads(js)
    val level25Cp = (JsPath \ "level25CP").readNullable[Int].reads(js)
    val cpBenchmarks = (level25Cp and cp500 and cp1500 and cp2500)(CpBenchmarks.apply _)
    val chargedMoves = lookupMoves(allChargedMoves, moveList.chargedMoves)
    val fastMoves = lookupMoves(allFastMoves, moveList.fastMoves)
    (id and dex and name and baseStats and types and fastMoves and chargedMoves and cpBenchmarks)(PokedexEntry.apply _)
  })
  implicit val pokedexEntryWrites: Writes[PokedexEntry] = Writes[PokedexEntry](entry => {
    JsObject(
      Seq(
        "id" -> JsNumber(entry.id),
        "name" -> JsString(entry.name),
        "allFastMoves" -> JsString(entry.fastMoves.map(m => m.name).mkString(",")),
        "allChargedMoves" -> JsString(entry.chargedMoves.map(m => m.name).mkString(","))
      )
    )
  })
}
