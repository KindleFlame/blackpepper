package com.whisk.blackpepper

import com.datastax.driver.core.querybuilder.Insert

class InsertQuery[T <: CTable[T, R], R](table: T, val qb: Insert) extends ExecutableStatement {

  def value[RR](c: T => AbstractColumn[RR], value: RR): InsertQuery[T, R] = {
    val col = c(table)
    new InsertQuery[T, R](table, qb.value(col.name, col.toCType(value)))
  }

  def valueOrNull[RR](c: T => AbstractColumn[RR], value: Option[RR]): InsertQuery[T, R] = {
    val col = c(table)
    new InsertQuery[T, R](table, qb.value(col.name, value.map(col.toCType).orNull))
  }

}
