package models

import play.api.libs.json.{JsArray, JsError, JsSuccess, Reads}

case class EstimateParameters(level: Double, attack: Int, defense: Int, hitPoints: Int)

object EstimateParameters {
  implicit val estimateReads: Reads[EstimateParameters] = Reads[EstimateParameters] {
    case JsArray(inner) =>
      val numbers: collection.IndexedSeq[Double] = inner.map(f => f.as[Double])
      JsSuccess(EstimateParameters(numbers(0), numbers(1).toInt, numbers(2).toInt, numbers(3).toInt))
    case _ => JsError("unexpected type")
  }
}

/**
 * This class attempts to guess the level of a Pokemon based on it's CP and stats
 */
case class CpBenchmarks(level25Cp: Option[Int], cp500: EstimateParameters, cp1500: EstimateParameters, cp2500: EstimateParameters) {
  def guessLevelRange(combatPower: Int): (Int, Int) = {
    val levelGuess = if (combatPower < 500) {
      cp500.level * combatPower.toDouble / 500
    } else if (combatPower < 1500) {
      cp500.level + (cp1500.level - cp500.level) * (combatPower.toDouble - 500) / 1000
    } else {
      cp1500.level + (cp2500.level - cp1500.level) * (combatPower.toDouble - 1500) / 1000
    }
    (((levelGuess - 2) * 2 + .5).toInt, ((levelGuess + 2) * 2 + .5).toInt)
  }
}
