package models

import play.api.libs.json.{Json, OFormat}

case class PokemonCreation(
                            id: Int,
                            combatPower: Int,
                            fastMove: String,
                            chargedMove: String,
                            alternateChargedMove: Option[String]
                          )

object PokemonCreation {
  implicit val pokemonCreationFormat: OFormat[PokemonCreation] = Json.format[PokemonCreation]
}
