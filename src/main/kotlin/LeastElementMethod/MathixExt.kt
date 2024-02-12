package LeastElementMethod

fun MutableList<MutableList<Double>>.disableColumn(column: Int, maxElement: Double): MutableList<MutableList<Double>> {
	println("disabled column = $column")
	this.map {
		it[column] = maxElement
	}
	return this
}

fun MutableList<MutableList<Double>>.disableRow(disabledRow: Int, maxElement: Double): MutableList<MutableList<Double>> {
	println("disabled row = $disabledRow")
	val newMatrix = mutableListOf<MutableList<Double>>()
	for (row in this.indices) {
		if (row != disabledRow) {
			newMatrix.add(row, this[row])
		} else {
			newMatrix.add(row, MutableList(this[row].size) { maxElement })
		}
	}
	return newMatrix
}