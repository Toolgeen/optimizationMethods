package models

import Constants.VALUE_ZERO
import printMatrix

class Matrix(
	private val matrix: MutableList<MutableList<Double>>
) {
	fun setValue(value: Double, row: Int, col: Int) {
		matrix[row][col] = value
	}

	fun getValue(row: Int, col: Int) = matrix[row][col]

	constructor(
		rows: Int,
		cols: Int
	) : this(MutableList(rows) { MutableList(cols) { VALUE_ZERO } })

	fun print() = matrix.printMatrix()

	val rowSize = matrix.size
	val colSize = matrix[0].size
	val indices = 0 until rowSize
}