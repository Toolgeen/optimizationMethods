package LeastElementMethod

import Constants.VALUE_ZERO
import Input.Task
import findMaxElement
import findMinElementPosition
import printMatrix

object LeastElementMethod {

	operator fun invoke(
		task: Task
	): List<List<Double>> {

		var rows = task.rows
		var cols = task.cols
		val a = task.a.toMutableList()
		val b = task.b.toMutableList()
		var x = task.matrix
		val disabledRows = MutableList(rows) { true }
		val disabledCols = MutableList(cols) { true }
		val base = MutableList(rows) { MutableList(cols) { VALUE_ZERO } }


		var iterations = VALUE_ZERO.toInt()

		val maxElement = x.findMaxElement()

		while ((rows != 1) || (cols != 1)) {
			val leastElement = x.findMinElementPosition()
			println("least element = ${leastElement.element}, indices ${leastElement.row}:${leastElement.col}")

			base[leastElement.row][leastElement.col] = if (a[leastElement.row] > b[leastElement.col]) {
				b[leastElement.col]
			} else {
				a[leastElement.row]
			}
			base.printMatrix()

			a[leastElement.row] -= base[leastElement.row][leastElement.col]
			b[leastElement.col] -= base[leastElement.row][leastElement.col]
			if (a[leastElement.row] == VALUE_ZERO) {
				disabledRows[leastElement.row] = false
				x = x.disableRow(leastElement.row, maxElement)
				rows--
			} else if (b[leastElement.col] == VALUE_ZERO) {
				disabledCols[leastElement.col] = false
				x = x.disableColumn(leastElement.col, maxElement)
				cols--
			}
			iterations++
		}

		when {
			rows == 1 -> {
				for (i in base.indices) {
					if (disabledRows[i]) {
						for (k in base.indices) {
							if (disabledCols[k]) {
								base[i][k] = b[k]
							}
						}
					}
				}
			}
			cols == 1 -> {
				for (i in base.indices) {
					if (disabledCols[i]) {
						for (k in base.indices) {
							if (disabledRows[k]) {
								base[k][i] = a[k]
							}
						}
					}
				}
			}
		}

		base.printMatrix()
		println("Решено за $iterations итераций.")
		println("ПОИСК БАЗИСА МЕТОДОМ НАИМЕНЬШЕЙ СТОИМОСТИ ЗАКОНЧЕН")
		return base
	}
}