package de.jannikarndt.datamover.monitor

import java.time.{LocalDateTime, ZoneOffset}

import scala.collection.mutable

object Monitor {
    val monitors: mutable.Map[String, Monitor] = mutable.Map[String, Monitor]()

    val servlet = new EmbeddedServlet()
    servlet.start()

    def getMonitor(className: String): Monitor = {
        monitors.getOrElseUpdate(className, new Monitor(className))
    }
}

class Monitor(name: String) {
    private var inputLong = mutable.Map(LocalDateTime.now() -> 0L)
    private var inputString = mutable.Map(LocalDateTime.now() -> "")

    private var outputLong = mutable.Map(LocalDateTime.now() -> 0L)
    private var outputString = mutable.Map(LocalDateTime.now() -> "")

    def input(number: Long): Unit = inputLong += (LocalDateTime.now() -> number)

    def input(text: String): Unit = inputString += (LocalDateTime.now() -> text)

    def output(number: Long): Unit = outputLong += (LocalDateTime.now() -> number)

    def output(text: String): Unit = outputString += (LocalDateTime.now() -> text)

    def dump(): String = {
        implicit val localDateOrdering: Ordering[LocalDateTime] = Ordering.by(_.toEpochSecond(ZoneOffset.UTC))

        def makeString(map: collection.Map[_ <: LocalDateTime, _ <: Any]) = map.toList.sortBy(_._1).map(_.productIterator.mkString("\t")).mkString("\n")

        val in = makeString(inputLong.mapValues(x => x.toString) ++ inputString)
        val out = makeString(outputLong.mapValues(x => x.toString) ++ outputString)
        s"Input: \n$in\nOutput: \n$out"
    }
}