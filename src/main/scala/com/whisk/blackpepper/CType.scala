package com.whisk.blackpepper

import com.datastax.driver.core.Row
import java.util.{ UUID, Date }

trait CSWrites[T] {

  def toCType(v: T): AnyRef
}

trait CSPrimitive[T] extends CSWrites[T] {

  def cls: Class[_]
  def toCType(v: T): AnyRef = v.asInstanceOf[AnyRef]
  def fromCType(c: AnyRef): T = c.asInstanceOf[T]
  def fromRow(row: Row, name: String): Option[T]
}

object CSPrimitive {

  def apply[T: CSPrimitive]: CSPrimitive[T] = implicitly[CSPrimitive[T]]

  implicit object IntIsCSPrimitive extends CSPrimitive[Int] {

    def cls: Class[_] = classOf[java.lang.Integer]
    def fromRow(row: Row, name: String): Option[Int] = {
      if (row.isNull(name)) None else Some(row.getInt(name))
    }
  }

  implicit object LongIsCSPrimitive extends CSPrimitive[Long] {

    def cls: Class[_] = classOf[java.lang.Long]
    def fromRow(row: Row, name: String): Option[Long] = {
      if (row.isNull(name)) None else Some(row.getLong(name))
    }
  }

  implicit object StringIsCSPrimitive extends CSPrimitive[String] {

    def cls: Class[_] = classOf[java.lang.String]
    def fromRow(row: Row, name: String): Option[String] = {
      if (row.isNull(name)) None else Some(row.getString(name))
    }
  }

  implicit object DoubleIsCSPrimitive extends CSPrimitive[Double] {

    def cls: Class[_] = classOf[java.lang.Double]
    def fromRow(row: Row, name: String): Option[Double] = {
      if (row.isNull(name)) None else Some(row.getDouble(name))
    }
  }

  implicit object DateIsCSPrimitive extends CSPrimitive[Date] {

    def cls: Class[_] = classOf[Date]
    def fromRow(row: Row, name: String): Option[Date] = {
      if (row.isNull(name)) None else Some(row.getDate(name))
    }
  }

  implicit object BooleanIsCSPrimitive extends CSPrimitive[Boolean] {

    def cls: Class[_] = classOf[Boolean]
    def fromRow(row: Row, name: String): Option[Boolean] = {
      if (row.isNull(name)) None else Some(row.getBool(name))
    }
  }

  implicit object UUIDIsCSPrimitive extends CSPrimitive[UUID] {

    def cls: Class[_] = classOf[UUID]
    def fromRow(row: Row, name: String): Option[UUID] = {
      if (row.isNull(name)) None else Some(row.getUUID(name))
    }
  }
}
