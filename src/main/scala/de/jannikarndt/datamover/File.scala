package de.jannikarndt.datamover

object File {
    def CSV(filepath: String) = new CsvFile(filepath)
}
