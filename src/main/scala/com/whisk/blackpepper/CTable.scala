package com.whisk.blackpepper

import com.datastax.driver.core.Row
import com.datastax.driver.core.querybuilder._
import play.api.libs.json.Format

abstract class CTable[T <: CTable[T, R], R](val tableName: String) {

  def fromRow(r: Row): R

  def column[RR: CSPrimitive](name: String): PrimitiveColumn[RR] =
    new PrimitiveColumn[RR](name)

  def optColumn[RR: CSPrimitive](name: String): OptionalPrimitiveColumn[RR] =
    new OptionalPrimitiveColumn[RR](name)

  def counterColumn(name: String): CounterColumn = new CounterColumn(name)

  def jsonColumn[RR: Format](name: String): JsonColumn[RR] =
    new JsonColumn[RR](name)

  def optionalJsonColumn[RR: Format](name: String): OptionalJsonColumn[RR] =
    new OptionalJsonColumn[RR](name)

  def enumColumn[EnumType <: Enumeration](enum: EnumType, name: String): EnumColumn[EnumType] =
    new EnumColumn[EnumType](enum, name)

  def seqColumn[RR: CSPrimitive](name: String): SeqColumn[RR] =
    new SeqColumn[RR](name)

  def setColumn[RR: CSPrimitive](name: String): SetColumn[RR] =
    new SetColumn[RR](name)

  def mapColumn[K: CSPrimitive, V: CSPrimitive](name: String) =
    new MapColumn[K, V](name)

  def jsonSeqColumn[RR: Format](name: String): JsonSeqColumn[RR] =
    new JsonSeqColumn[RR](name)

  def jsonSetColumn[RR: Format](name: String): JsonSetColumn[RR] =
    new JsonSetColumn[RR](name)

  def jsonMapColumn[K: CSPrimitive, V: Format](name: String): JsonMapColumn[K, V] =
    new JsonMapColumn[K, V](name)

  def select: SelectQuery[T, R] =
    new SelectQuery[T, R](this.asInstanceOf[T], QueryBuilder.select().from(tableName), this.asInstanceOf[T].fromRow)

  def select[A](f1: T => SelectColumn[A]): SelectQuery[T, A] = {
    val t = this.asInstanceOf[T]
    val c = f1(t)
    new SelectQuery[T, A](t, QueryBuilder.select(c.col.name).from(tableName), c.apply)
  }

  def select[A, B](f1: T => SelectColumn[A], f2: T => SelectColumn[B]): SelectQuery[T, (A, B)] = {
    val t = this.asInstanceOf[T]
    val c1 = f1(t)
    val c2 = f2(t)
    new SelectQuery[T, (A, B)](t, QueryBuilder.select(c1.col.name, c2.col.name).from(tableName), r => (c1(r), c2(r)))
  }

  def update = new UpdateQuery[T, R](this.asInstanceOf[T], QueryBuilder.update(tableName))

  def insert = new InsertQuery[T, R](this.asInstanceOf[T], QueryBuilder.insertInto(tableName))

  def delete = new DeleteQuery[T, R](this.asInstanceOf[T], QueryBuilder.delete.from(tableName))

}

