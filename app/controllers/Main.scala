package controllers

import dao.PokemonDao
import models.{MoveList, Pokedex, Pokemon, PokemonCreation}
import play.api.mvc._
import play.api.libs.json.{JsObject, JsPath, JsResult, JsSuccess, Json}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class Main @Inject()(
                      controllerComponents: ControllerComponents,
                      authenticatedAction: AuthenticatedAction,
                      pokedex: Pokedex,
                      pokemonDao: PokemonDao,
                      moveList: MoveList
                    )(implicit executionContext: ExecutionContext)
  extends AbstractController(controllerComponents)
{
  def getCollection: Action[AnyContent] = authenticatedAction.async { implicit request: UserRequest[AnyContent] =>
    pokemonDao.getPokemon(request.user.userId).map(list => Ok(Json.toJson(list)))
  }

  def getPokedex: Action[AnyContent] = authenticatedAction.async { implicit request: UserRequest[AnyContent] =>
    Future(Ok(Json.toJson(pokedex.getAllPokemon)(list => {
      JsObject(
        list.map(l => (l._1, Json.toJson(l._2)))
      )
    })))
  }

  def addPokemon(): Action[AnyContent] = authenticatedAction.async { implicit request: UserRequest[AnyContent] =>
    val creationInfo = request.body.asJson.get.as[PokemonCreation]
    try {
      pokemonDao.addPokemon(
        request.user.userId,
        Pokemon.makeNewPokemon(creationInfo, pokedex, moveList)
      ).map(pokemon => Ok(Json.toJson(pokemon))).recover({
        case _: Throwable => ServiceUnavailable("Unexpected error")
      })
    } catch {
      case _: IllegalArgumentException => Future(BadRequest("Pokemon could not be created"))
    }
  }

  def updatePokemon(): Action[AnyContent] = authenticatedAction.async { implicit request: UserRequest[AnyContent] =>
    try {
      val updatedPokemon: Option[JsResult[Future[Option[Pokemon]]]] = request.body.asJson.map(js => {
        (JsPath \ "id").read[Int].reads(js).map(id => {
          pokemonDao.getSinglePokemon(request.user.userId, id)
        }).map(eventualPossibleExistingPokemon => eventualPossibleExistingPokemon.map( possibleExistingPokemon => {
          possibleExistingPokemon.map(pokemon => pokemon.update(js))
        }))
      })
      updatedPokemon match {
        case Some(JsSuccess(eventualPossibleUpdatedPokemon, _)) =>
          eventualPossibleUpdatedPokemon.flatMap({
            case Some(updatedPokemon) =>
              pokemonDao.updatePokemon(request.user.userId, updatedPokemon).map(p => Ok(Json.toJson(updatedPokemon)))
            case None => Future(BadRequest("Couldn't find pokemon"))
          })
        case _ => Future(BadRequest("Invalid Json"))
      }
    } catch {
      case _ : NoSuchElementException => Future(BadRequest("Invalid Json"))
    }
  }
}
