package services

import com.sendgrid.helpers.mail.Mail
import com.sendgrid.{Method, Request, SendGrid}
import com.sendgrid.helpers.mail.objects.{Content, Email}
import play.api.{Configuration, Logging}

import javax.inject.Inject

class Mailer @Inject() (config: Configuration) extends Logging {
  def sendConfirmation(email: String, token: String): Boolean = {
    val key = config.get[String]("sendGridApiKey")
    val sendgrid = new SendGrid(key)
    val request = new Request()

    val from = new Email("heilig+noreply@heilig.com")
    val subject = "Sign in attempt for Pokemon Go Raid Selector"
    val to = new Email(email)
    val content = new Content(
      "text/plain",
      s"Either enter $token into login form, or follow the link below to sign in\nhttps://games.heilig.com/goraid/confirm/$email/$token"
    )
    val mail = new Mail(from, subject, to, content)

    try {
      request.setMethod(Method.POST)
      request.setEndpoint("mail/send")
      request.setBody(mail.build())
      val response = sendgrid.api(request)
      if (response.getStatusCode >= 300) {
        logger.error(s"Failure in sending code: ${response.getStatusCode}: ${response.getBody}")
      }
      response.getStatusCode < 300
    } catch {
      // Should be more specific about the errors being caught, but ultimately we just want to report a failure
      // back to the user.
      case e: Throwable =>
        logger.error("Unexpected failure in email send", e)
        false
    }
  }
}
