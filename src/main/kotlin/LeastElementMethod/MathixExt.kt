package LeastElementMethod

fun MutableList<MutableList<Double>>.disableColumn(column: Int, maxElement: Double): List<List<Double>> {
	println("disabled column = $column")
	this.map {
		it[column] = maxElement
	}
	return this
}

fun MutableList<MutableList<Double>>.disableRow(disabledRow: Int, maxElement: Double): List<List<Double>> {
	println("disabled row = $disabledRow")
	val newMatrix = mutableListOf<List<Double>>()
	for (row in this.indices) {
		if (row != disabledRow) {
			newMatrix.add(row, this[row])
		} else {
			newMatrix.add(row, List(this[row].size) { maxElement })
		}
	}
	return newMatrix
}