package dao

import models.{MoveList, Pokedex, Pokemon, Stats}
import models.schema.Tables
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class PokemonDao @Inject()(
                            protected val dbConfigProvider: DatabaseConfigProvider,
                            pokedex: Pokedex,
                            moveList: MoveList
                          )(implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  private val UserPokemon = TableQuery[Tables.UserPokemon]

  private val insertPokemonQuery = UserPokemon returning UserPokemon.map(_.userPokemonId) into
    ((userPokemon, userPokemonId) => userPokemon.copy(userPokemonId = userPokemonId))

  def getPokemon(userId: Int): Future[List[Pokemon]] = {
    db.run(UserPokemon.filter(_.userId === userId).result.map(pokemonRows =>
     pokemonRows.flatMap( pokemonRow => {
       pokemonFromRow(pokemonRow)
     }).toList))
  }

  private def pokemonFromRow(pokemonRow: Tables.UserPokemonRow): Option[Pokemon] = {
    val pokedexEntry = pokedex.getPokemon(pokemonRow.pokemonId)
    pokedexEntry.map(dex =>
      Pokemon(
        pokemonRow.userPokemonId,
        dex,
        Some(Stats(pokemonRow.ivAttack, pokemonRow.ivDefense, pokemonRow.ivHitPoints)),
        pokemonRow.combatPower,
        (pokemonRow.minLevel, pokemonRow.maxLevel),
        moveList.fastMoves(pokemonRow.fastMove),
        pokemonRow.chargedMoves.split(",").toSet.map(m => moveList.chargedMoves(m))
      )
    )
  }

  def getSinglePokemon(userId: Int, id: Int): Future[Option[Pokemon]] = {
    db.run(UserPokemon.filter(_.userPokemonId === id).filter(_.userId === userId).result.headOption.map(row => {
      row.flatMap(pokemonRow => pokemonFromRow(pokemonRow))
    }))
  }

  def updatePokemon(userId: Int, pokemon: Pokemon): Future[Pokemon] = {
    val newRow = createUserPokemonRow(userId, pokemon)
    db.run(UserPokemon.filter(_.userPokemonId === pokemon.id).filter(_.userId === userId).update(newRow)).map(_ => pokemon)
  }


  def addPokemon(userId: Int, pokemon: Pokemon): Future[Pokemon] = {
    val newRow = createUserPokemonRow(userId, pokemon)
      db.run(insertPokemonQuery += newRow).map(row => {
        pokemon.copy(id = row.userPokemonId)
      })
  }

  private def createUserPokemonRow(userId: Int, pokemon: Pokemon) = {
    Tables.UserPokemonRow(
      userPokemonId = 0,
      userId = userId,
      pokemonId = pokemon.dex.id,
      combatPower = pokemon.combatPower,
      ivAttack = pokemon.intrinsicValue.map(iv => iv.attack).getOrElse(0),
      ivDefense = pokemon.intrinsicValue.map(iv => iv.defense).getOrElse(0),
      ivHitPoints = pokemon.intrinsicValue.map(iv => iv.hitPoints).getOrElse(0),
      minLevel = pokemon.levelRange._1,
      maxLevel = pokemon.levelRange._2,
      fastMove = pokemon.fastMove.name,
      chargedMoves = pokemon.chargedMoves.map(m => m.name).mkString(",")
    )
  }
}
