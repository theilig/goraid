package models

import play.api.libs.json.{Json, OFormat}

case class LoginAttempt(email: String)

object LoginAttempt {
  implicit val loginFormat: OFormat[LoginAttempt] = Json.format[LoginAttempt]
}