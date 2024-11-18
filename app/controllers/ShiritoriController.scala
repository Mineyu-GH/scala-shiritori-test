// app/controllers/ShiritoriController.scala
package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import models.Game
import play.filters.csrf.CSRF

@Singleton
class ShiritoriController @Inject()(val controllerComponents: ControllerComponents) 
  extends BaseController {
  
  private var game = Game.initialize
  
  implicit val gameWrites: Writes[Game] = Json.writes[Game]
  
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }
  
  def getGameState() = Action { implicit request =>
    // Get CSRF token and include it in response
    val token = CSRF.getToken.map(_.value).getOrElse("")
    Ok(Json.toJson(game)).withHeaders("Csrf-Token" -> token)
  }
  
  def playWord() = Action(parse.json) { implicit request =>
    val word = (request.body \ "word").as[String]
    
    if (game.isValidMove(word)) {
      game = Game(
        currentWord = word,
        usedWords = game.usedWords + word,
        isGameOver = word.endsWith("ん")
      )
      Ok(Json.toJson(game))
    } else {
      BadRequest(Json.obj("error" -> "Invalid move"))
    }
  }
  
  def resetGame() = Action { implicit request =>
    game = Game.initialize
    Ok(Json.toJson(game))
  }
}
