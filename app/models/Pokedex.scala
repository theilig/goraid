package models
import com.google.inject.ImplementedBy
import play.api.Environment
import play.api.libs.json.{JsArray, JsError, JsSuccess, JsValue, Json}

import javax.inject.{Inject, Singleton}

@ImplementedBy(classOf[JsonPokedex])
trait Pokedex {
  def getPokemon(id: Int): Option[PokedexEntry]
  def getPokemon(name: String): Option[PokedexEntry]
  def getAllPokemon: Map[String, PokedexEntry]
}

@Singleton
class JsonPokedex @Inject()(moveList: MoveList, environment: Environment) extends Pokedex {
  private val jValue = Json.parse(environment.classLoader.getResourceAsStream("pokemon.json"))
  val dexList: List[PokedexEntry] = readEntries(jValue)
  lazy val dexById: Map[Int, PokedexEntry] = dexList.map(d => d.id -> d).toMap
  lazy val dexByName: Map[String, PokedexEntry] = dexList.map(d => d.name -> d).toMap

  def getPokemon(id: Int): Option[PokedexEntry] = dexById.get(id)

  def getPokemon(name: String): Option[PokedexEntry] = dexByName.get(name)

  def readEntries(js: JsValue): List[PokedexEntry] = {
    (js \ "pokemon").as[List[PokedexEntry]]({
      case JsArray(l) => JsSuccess(l.map(f => f.as[PokedexEntry](PokedexEntry.pokedexReads(moveList))).toList)
      case _ => JsError("Expected list of pokemon")
    }).filterNot(p => p.name.contains("(Shadow)") || p.name.contains("(Mega"))
  }

  def getAllPokemon: Map[String, PokedexEntry] = dexByName
}
