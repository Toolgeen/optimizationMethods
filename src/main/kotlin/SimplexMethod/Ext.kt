package SimplexMethod

import Constants.VALUE_ONE
import Constants.VALUE_ZERO
import Input.Task
import models.Matrix
import models.SimplifiedSimplexTable

fun Matrix.mapToLeastElementMethodSolve(
	basisArgs: List<Int>,
	vectorB: List<Double>,
) {

	this.matrix.forEachIndexed { rowIndex, row ->
		row.forEachIndexed { colIndex, element ->

			if (colIndex in basisArgs) {

				var minimumRightElement = Double.MAX_VALUE
				var minIndex = 0

				vectorB.forEachIndexed { indexB, b ->
					if (this.getValue(indexB, colIndex) > 0.0 && b < minimumRightElement) {
						minimumRightElement = b
						minIndex = indexB
					}
				}

				var currentCoefficient = this.getValue(minIndex, colIndex)

				for (k in 0 until this.colSize) {
					if (currentCoefficient != 0.0) {
						this.setValue(this.getValue(minIndex, k) * currentCoefficient, minIndex, k)
					}
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
	}
}

fun Matrix.formatSimplexTable(basisArgs: List<Int>): SimplifiedSimplexTable {
	return SimplifiedSimplexTable(this.matrix.filter { !it.none { it != 0.0 } }.map {
		it.filterIndexed { index, d -> index !in basisArgs || index == this.colSize - 1 }.toMutableList()
	}.toMutableList(), basisArgs, this.colIndices.filter { !basisArgs.contains(it) }.dropLast(1))
}

fun createInitialSimplexTable(task: Task): Matrix {
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
		add(task.simplexTablePRow.map { it.unaryMinus() }.toMutableList())
	}
	)
}