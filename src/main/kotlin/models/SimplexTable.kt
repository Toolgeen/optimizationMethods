package models

class SimplexTable(
	matrix: MutableList<MutableList<Double>>,
	val basisArgs: MutableList<Int>,
	val nonBasisArgs: MutableList<Int>,
	val targetFunCoefficients: List<Double>
) : Matrix(matrix) {

	override fun toString() = StringBuilder().apply {
		val maxLength = maxLengthInElements()
		val rowNames: List<String> = basisArgs.map { "x${it}" } + listOf("p")
		val colNames = listOf("".padStart(maxLength, ' ')) + this@SimplexTable.nonBasisArgs.map {
			"x${it}".padStart(maxLength, ' ')
		} + "b".padStart(maxLength, ' ')
		appendLine("[${colNames.joinToString(separator = "  ")}]")
		rowNames.forEachIndexed { index, rowName ->
			val rowElements = listOf(rowName.padStart(maxLength, ' ')) + matrix[index].formatRowWithPaddings()
			val string = "[${rowElements.joinToString(", ")}]"
			appendLine(string)
		}
	}.toString()
}