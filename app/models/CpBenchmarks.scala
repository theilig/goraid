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
case class CpBenchmarks(level25Cp: Option[Int], cp500: EstimateParameters, cp1500: EstimateParameters, cp2500: EstimateParameters)
