package models

import Constants.VALUE_ZERO

open class Matrix(
	val matrix: MutableList<MutableList<Double>>
) {
	fun setValue(value: Double, row: Int, col: Int) {
		matrix[row][col] = value
	}

	fun getValue(row: Int, col: Int) = matrix[row][col]

	constructor(
		rows: Int,
		cols: Int
	) : this(MutableList(rows) { MutableList(cols) { VALUE_ZERO } })

	fun print() = println(this.toString())

	override fun toString() = StringBuilder().apply {
		matrix.map {
			appendLine("[${it.formatRowWithPaddings().joinToString(separator = ", ")}]")
		}
	}.toString()

	private fun maxLengthInElements() = matrix.flatten().map { it.toString() }.maxByOrNull { it.length }?.length ?: 0
	private fun List<Double>.formatRowWithPaddings() = this.map { it.toString().padStart(maxLengthInElements(), ' ') }

	val rowSize = matrix.size
	val colSize = matrix[0].size
	val indices = 0 until rowSize
}