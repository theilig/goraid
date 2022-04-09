package models.schema
// AUTO-GENERATED Slick data model for table UserPokemon
trait UserPokemonTable {

  self:Tables  =>

  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}
  /** Entity class storing rows of table UserPokemon
   *  @param userPokemonId Database column user_pokemon_id SqlType(INT), AutoInc, PrimaryKey
   *  @param userId Database column user_id SqlType(INT)
   *  @param pokemonId Database column pokemon_id SqlType(INT)
   *  @param combatPower Database column combat_power SqlType(INT)
   *  @param ivAttack Database column iv_attack SqlType(INT)
   *  @param ivDefense Database column iv_defense SqlType(INT)
   *  @param ivHitPoints Database column iv_hit_points SqlType(INT)
   *  @param minLevel Database column min_level SqlType(INT)
   *  @param maxLevel Database column max_level SqlType(INT)
   *  @param fastMove Database column fast_move SqlType(VARCHAR), Length(300,true)
   *  @param chargedMoves Database column charged_moves SqlType(VARCHAR), Length(1024,true) */
  case class UserPokemonRow(userPokemonId: Int, userId: Int, pokemonId: Int, combatPower: Int, ivAttack: Int, ivDefense: Int, ivHitPoints: Int, minLevel: Int, maxLevel: Int, fastMove: String, chargedMoves: String)
  /** GetResult implicit for fetching UserPokemonRow objects using plain SQL queries */
  implicit def GetResultUserPokemonRow(implicit e0: GR[Int], e1: GR[String]): GR[UserPokemonRow] = GR{
    prs => import prs._
    UserPokemonRow.tupled((<<[Int], <<[Int], <<[Int], <<[Int], <<[Int], <<[Int], <<[Int], <<[Int], <<[Int], <<[String], <<[String]))
  }
  /** Table description of table UserPokemon. Objects of this class serve as prototypes for rows in queries. */
  class UserPokemon(_tableTag: Tag) extends profile.api.Table[UserPokemonRow](_tableTag, Some("goraid"), "UserPokemon") {
    def * = (userPokemonId, userId, pokemonId, combatPower, ivAttack, ivDefense, ivHitPoints, minLevel, maxLevel, fastMove, chargedMoves) <> (UserPokemonRow.tupled, UserPokemonRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(userPokemonId), Rep.Some(userId), Rep.Some(pokemonId), Rep.Some(combatPower), Rep.Some(ivAttack), Rep.Some(ivDefense), Rep.Some(ivHitPoints), Rep.Some(minLevel), Rep.Some(maxLevel), Rep.Some(fastMove), Rep.Some(chargedMoves))).shaped.<>({r=>import r._; _1.map(_=> UserPokemonRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column user_pokemon_id SqlType(INT), AutoInc, PrimaryKey */
    val userPokemonId: Rep[Int] = column[Int]("user_pokemon_id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id SqlType(INT) */
    val userId: Rep[Int] = column[Int]("user_id")
    /** Database column pokemon_id SqlType(INT) */
    val pokemonId: Rep[Int] = column[Int]("pokemon_id")
    /** Database column combat_power SqlType(INT) */
    val combatPower: Rep[Int] = column[Int]("combat_power")
    /** Database column iv_attack SqlType(INT) */
    val ivAttack: Rep[Int] = column[Int]("iv_attack")
    /** Database column iv_defense SqlType(INT) */
    val ivDefense: Rep[Int] = column[Int]("iv_defense")
    /** Database column iv_hit_points SqlType(INT) */
    val ivHitPoints: Rep[Int] = column[Int]("iv_hit_points")
    /** Database column min_level SqlType(INT) */
    val minLevel: Rep[Int] = column[Int]("min_level")
    /** Database column max_level SqlType(INT) */
    val maxLevel: Rep[Int] = column[Int]("max_level")
    /** Database column fast_move SqlType(VARCHAR), Length(300,true) */
    val fastMove: Rep[String] = column[String]("fast_move", O.Length(300,varying=true))
    /** Database column charged_moves SqlType(VARCHAR), Length(1024,true) */
    val chargedMoves: Rep[String] = column[String]("charged_moves", O.Length(1024,varying=true))

    /** Index over (userId) (database name user_id) */
    val index1 = index("user_id", userId)
  }
  /** Collection-like TableQuery object for table UserPokemon */
  lazy val UserPokemon = new TableQuery(tag => new UserPokemon(tag))
}
