package com.whisk.blackpepper

import com.datastax.driver.core.querybuilder.{ Clause, Delete }

class DeleteQuery[T <: CTable[T, R], R](table: T, val qb: Delete) extends ExecutableStatement {

  def where(c: T => Clause): DeleteWhere[T, R] = {
    new DeleteWhere[T, R](table, qb.where(c(table)))
  }

}

class DeleteWhere[T <: CTable[T, R], R](table: T, val qb: Delete.Where) extends ExecutableStatement {

  def where(c: T => Clause): DeleteWhere[T, R] = {
    new DeleteWhere[T, R](table, qb.and(c(table)))
  }

  def and(c: T => Clause) = where(c)

}
