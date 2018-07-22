package fr.davit.tiptop.common

import com.typesafe.config.Config
import configs.{Configs, Result}
import configs.syntax._

abstract class Settings(config: Config, basePath: String) {
  def get[T](value: String)(implicit c: Configs[T]): T = config.get[T](s"$basePath.$value").value

  def read[T](value: String)(implicit c: Configs[T]): Result[T] = config.get[T](s"$basePath.$value")
}