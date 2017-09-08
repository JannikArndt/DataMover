package de.jannikarndt.datamover.io

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, OpenOption, Paths, StandardOpenOption}

object File {

    def append(path: String, txt: String): Unit =
        write(path, txt, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND)

    def overwrite(path: String, txt: String): Unit =
        write(path, txt, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)

    private def write(path: String, txt: String, options: OpenOption*): Unit = {
        val filepath = Paths.get(path)
        if (filepath.getParent != null)
            Files.createDirectories(filepath.getParent)

        Files.write(filepath, txt.getBytes(StandardCharsets.UTF_8), options: _*)
    }

    def read(path: String): String =
        scala.io.Source.fromFile(path, "UTF-8").getLines.mkString
}
