package models

import com.google.inject.ImplementedBy
import play.api.Environment
import play.api.libs.json.{JsPath, JsSuccess, Json}

import javax.inject.{Inject, Singleton}

@ImplementedBy(classOf[JsonMoveList])
trait MoveList {
  def chargedMoves: Map[String, ChargedMove]
  def fastMoves: Map[String, FastMove]
}

@Singleton
class JsonMoveList @Inject() (environment: Environment) extends MoveList {
  val (chargedListByName, fastListByName) = parseJson()
  def chargedMoves: Map[String, ChargedMove] = chargedListByName
  def fastMoves: Map[String, FastMove] = fastListByName

  def parseJson(): (Map[String, ChargedMove], Map[String, FastMove]) = {
    val data = Json.parse(environment.classLoader.getResourceAsStream("moves.json"))
    val chargedMoveList = (JsPath \ "charged").read[List[ChargedMove]].reads(data)
    val fastMoveList = (JsPath \ "fast").read[List[FastMove]].reads(data)
    (chargedMoveList, fastMoveList) match {
      case (JsSuccess(charged: List[ChargedMove], _), JsSuccess(fast: List[FastMove], _)) =>
        (charged.map(s => s.name -> s).toMap, fast.map(s => s.name -> s).toMap)
      case _ => throw new IllegalArgumentException("Bad move.json")
    }
  }
}
