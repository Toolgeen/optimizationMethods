package SimplexMethod

import Constants.VALUE_ONE
import Constants.VALUE_ZERO
import Input.Task
import models.Matrix
import models.SimplifiedSimplexTable

fun Matrix.mapToLeastElementMethodSolve(basisArgs: List<Int>) {

	basisArgs.forEach { colIndex ->

		var minimumRightElement = Double.MAX_VALUE
		var minIndex = 0

		this.matrix.forEachIndexed { rowIndex, _ ->
			if (this.getValue(rowIndex, colIndex) > 0.0 && this.getValue(rowIndex, this.colSize - 1) * this.getValue(rowIndex, colIndex) < minimumRightElement) {
				minimumRightElement = this.getValue(rowIndex, this.colSize - 1)  * this.getValue(rowIndex, colIndex)
				minIndex = rowIndex
			}
		}

		var currentCoefficient = this.getValue(minIndex, colIndex)

		for (k in 0 until this.colSize) {
			this.setValue(this.getValue(minIndex, k) * currentCoefficient, minIndex, k)
		}

		for (k in 0 until this.rowSize) {
			if (this.getValue(k, colIndex) != 0.0 && k != minIndex) {

				currentCoefficient = this.getValue(k, colIndex)

				for (j in 0 until this.colSize) {
					this.setValue(
						value = this.getValue(k, j) - (currentCoefficient * this.getValue(minIndex, j)),
						row = k,
						col = j
					)
				}
			}
		}
	}
}

fun Matrix.formatSimplexTable(basisArgs: List<Int>): SimplifiedSimplexTable {
	val cleanedZeroRowMatrix = this.matrix.filter { !it.none { it != 0.0 } }
	val newMatrix = mutableListOf<MutableList<Double>>()
	basisArgs.forEach { basisArgIndex ->
		val cleanedRow = cleanedZeroRowMatrix
			.first { it[basisArgIndex] == 1.0 }
			.filterIndexed { index, d -> index !in basisArgs }
		val basisRow = cleanedRow.mapIndexed { index, element ->
			if (index != cleanedRow.lastIndex) element.unaryMinus() else element
		}.toMutableList()
		newMatrix.add(basisRow)
	}
	return SimplifiedSimplexTable(
		newMatrix,
		basisArgs.toMutableList(),
		this.colIndices.filter { !basisArgs.contains(it) }.dropLast(1).toMutableList()
	)
}

fun createRestrictionsSystem(task: Task): Matrix {
	var counter = 1
	return Matrix(mutableListOf<MutableList<Double>>().apply {
		for (i in 0 until (task.a.size + task.b.size)) {
			if (i < task.b.size) {
				add(
					List(task.a.size * task.b.size) {
						if ((it - i) % task.b.size == 0) {
							VALUE_ONE
						} else {
							VALUE_ZERO
						}
					}.plus(task.b[i]).toMutableList()
				)

			} else {
				add(
					List(task.a.size * task.b.size) {
						if (it + 1 <= counter * task.b.size && it + 1 > (i - task.b.size) * task.b.size) {
							VALUE_ONE
						} else {
							VALUE_ZERO
						}

					}.plus(task.a[i - task.b.size]).toMutableList()
				)
				counter++
			}
		}
	}
	)
}

fun SimplifiedSimplexTable.findReferenceElementIndices(): Pair<Int, Int> {
	return this.findRefCol().let {
		this.findRefRow(it) to it
	}
}


fun SimplifiedSimplexTable.findRefCol(): Int = matrix.last().indexOf(matrix.last().dropLast(1).maxOrNull())

fun SimplifiedSimplexTable.findRefRow(colIndex: Int): Int {
	var min = Double.MAX_VALUE
	var refRow = 0
	matrix.forEachIndexed { index, row ->
		if (row[colIndex] > 0 && row.last() / row[colIndex] < min) {
			min = row.last() / row[colIndex]
			refRow = index
		}
	}
	return refRow
}

fun SimplifiedSimplexTable.isSolved(): Boolean {
	var isSolved = true
	this.matrix.last().dropLast(1).forEach {
		isSolved = !(it > 0)
	}
	return isSolved
}

fun SimplifiedSimplexTable.switchVariables(row: Int, col: Int) {
	(this.basisArgs[row] to this.nonBasisArgs[col]).apply {
		this@switchVariables.basisArgs[row] = this.second
		this@switchVariables.nonBasisArgs[col] = this.first
	}
}