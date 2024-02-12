import Constants.VALUE_ZERO
import models.MinElement

fun List<List<Double>>.findMaxElement(): Double {
	var max = VALUE_ZERO
	this.map {
		it.map { matrixElement ->
			if (max < matrixElement) {
				max = matrixElement
			}
		}
	}
	return max
}

fun List<List<Double>>.findMinElementPosition(): MinElement {
	var leastCostRow = 0
	var leastCostCol = 0
	var leastCost = this[leastCostRow][leastCostCol]
	for (row in this.indices) {
		for (col in this[row].indices) {
			if (leastCost > this[row][col]) {
				leastCostRow = row
				leastCostCol = col
				leastCost = this[row][col]
			}
		}
	}
	return MinElement(leastCost, leastCostRow, leastCostCol)
}

fun List<List<Double>>.printMatrix() {
	this.map {
		println("[${it.joinToString(", ")}]")
	}
}