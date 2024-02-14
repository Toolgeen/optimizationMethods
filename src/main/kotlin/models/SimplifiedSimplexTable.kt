package models

class SimplifiedSimplexTable(
	matrix: MutableList<MutableList<Double>>,
	val basisArgs: List<Int>,
	val nonBasisArgs: List<Int>
) : Matrix(matrix) {

	override fun toString() = StringBuilder().apply {
		val maxLength = maxLengthInElements()
		val rowNames: List<String> = basisArgs.map { "x${it}" } + listOf("p")
		val colNames = this@SimplifiedSimplexTable.nonBasisArgs.map {
			"x${it}".padStart(maxLength, ' ')
		} + "b".padStart(maxLength, ' ')
		appendLine("[      ${colNames.joinToString(separator = "  ")}]")
		matrix.mapIndexed { index, elem ->
			appendLine("[${rowNames[index].padStart(maxLength, ' ')} ${elem.formatRowWithPaddings().joinToString(separator = ", ")}]")
		}
	}.toString()
}