package de.jannikarndt.datamover.monitor

import java.time.{LocalDateTime, ZoneOffset}

import scala.collection.mutable

object Monitor {
    val monitors: mutable.Map[String, Monitor] = mutable.Map[String, Monitor]()

    def getMonitor(className: String): Monitor = {
        monitors.getOrElseUpdate(className, new Monitor(className))
    }
}

class Monitor(name: String) {
    private var inputLong = mutable.Map[LocalDateTime, Long]()
    private var inputString = mutable.Map[LocalDateTime, String]()

    private var outputLong = mutable.Map[LocalDateTime, Long]()
    private var outputString = mutable.Map[LocalDateTime, String]()

    def input(number: Long): Unit = inputLong += (LocalDateTime.now() -> number)

    def input(text: String): Unit = inputString += (LocalDateTime.now() -> text)

    def output(number: Long): Unit = outputLong += (LocalDateTime.now() -> number)

    def output(text: String): Unit = outputString += (LocalDateTime.now() -> text)

    implicit val localDateOrdering: Ordering[LocalDateTime] = Ordering.by(_.toEpochSecond(ZoneOffset.UTC))

    private def makeString(map: collection.Map[_ <: LocalDateTime, _ <: Any]) = map.toList.sortBy(_._1).map(_.productIterator.mkString("\t")).mkString("\n")

    def dumpIn: String = makeString(inputLong.mapValues(x => x.toString) ++ inputString)

    def dumOut: String = makeString(outputLong.mapValues(x => x.toString) ++ outputString)

    def lastIn: String = inputLong.lastOption.map(_._2.toString).getOrElse("")

    def lastOut: String = outputLong.lastOption.map(_._2.toString).getOrElse("")
}