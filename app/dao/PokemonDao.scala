package dao

import models.{FastMove, Move, MoveList, Normal, Pokedex, Pokemon, Stats}
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

  def getPokemon(userId: Int): Future[List[Pokemon]] = {
    db.run(UserPokemon.filter(_.userId === userId).result.map(pokemonRows =>
     pokemonRows.flatMap( pokemonRow => {
       val pokedexEntry = pokedex.getPokemon(pokemonRow.pokemonId)
       pokedexEntry.map(dex =>
         Pokemon(
           dex,
           Some(Stats(pokemonRow.ivAttack, pokemonRow.ivDefense, pokemonRow.ivHitPoints)),
           pokemonRow.combatPower,
           (pokemonRow.minLevel, pokemonRow.maxLevel),
           FastMove("none", 0, 0, 0, 0, 0, Normal),
           Set()
        )
      )
    }).toList))
  }

}
