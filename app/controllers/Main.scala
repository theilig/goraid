package controllers

import dao.{PokemonDao, UserDao}
import play.api.mvc._
import play.api.libs.json.Json

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class Main @Inject()(
                      controllerComponents: ControllerComponents,
                      authenticatedAction: AuthenticatedAction,
                      pokemonDao: PokemonDao
                    )(implicit executionContext: ExecutionContext)
  extends AbstractController(controllerComponents)
{
  def getCollection: Action[AnyContent] = authenticatedAction.async { implicit request: UserRequest[AnyContent] =>
    pokemonDao.getPokemon(request.user.userId).map(list => Ok(Json.toJson(list)))
  }
}
