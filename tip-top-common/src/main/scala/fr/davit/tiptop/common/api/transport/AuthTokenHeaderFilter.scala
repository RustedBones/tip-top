package fr.davit.tiptop.common.api.transport

import com.lightbend.lagom.scaladsl.api.transport.{HeaderFilter, RequestHeader, ResponseHeader}
import com.typesafe.config.Config
import fr.davit.tiptop.common.Settings
import fr.davit.tiptop.common.api.transport.AuthTokenHeaderFilter.AuthTokenHeader

object AuthTokenHeaderFilter {

  trait AuthTokenHeader {
    def name: String
    def value: String
  }

  class AuthTokenHeaderSettings(config: Config, basePath: String) extends Settings(config, basePath) with AuthTokenHeader {
    override val name: String = get[String]("name")
    override val value: String = get[String]("value")
  }
}

class AuthTokenHeaderFilter(authTokenHeader: AuthTokenHeader) extends HeaderFilter {

  override def transformClientRequest(request: RequestHeader): RequestHeader = {
    request.withHeader(authTokenHeader.name, authTokenHeader.value)
  }

  override def transformServerRequest(request: RequestHeader): RequestHeader = request

  override def transformServerResponse(
      response: ResponseHeader,
      request: RequestHeader
  ): ResponseHeader = response

  override def transformClientResponse(
      response: ResponseHeader,
      request: RequestHeader
  ): ResponseHeader = response
}
