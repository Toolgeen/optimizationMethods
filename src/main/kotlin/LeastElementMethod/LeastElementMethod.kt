package LeastElementMethod

import Constants.VALUE_ZERO
import Input.Task
import findMaxElement
import findMinElementPosition
import models.LeastElemMethodBasis

object LeastElementMethod {

	operator fun invoke(
		task: Task
	): LeastElemMethodBasis {

		var rows = task.rows
		var cols = task.cols
		val a = task.a.toMutableList()
		val b = task.b.toMutableList()
		var x = task.matrix
		val disabledRows = MutableList(rows) { true }
		val disabledCols = MutableList(cols) { true }
		val basis = LeastElemMethodBasis(rows, cols)


		var iterations = VALUE_ZERO.toInt()

		val maxElement = x.findMaxElement()

		while ((rows != 1) || (cols != 1)) {
			val leastElement = x.findMinElementPosition()
			println("least element = ${leastElement.element}, indices ${leastElement.row}:${leastElement.col}")

			basis.setValue(
				value = if (a[leastElement.row] > b[leastElement.col]) {
					b[leastElement.col]
				} else {
					a[leastElement.row]
				},
				row = leastElement.row,
				col = leastElement.col
			)
			basis.print()

			a[leastElement.row] -= basis.getValue(leastElement.row, leastElement.col)
			b[leastElement.col] -= basis.getValue(leastElement.row, leastElement.col)
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
				for (i in basis.indices) {
					if (disabledRows[i]) {
						for (k in basis.indices) {
							if (disabledCols[k]) {
								basis.setValue(b[k], i, k)
							}
						}
					}
				}
			}
			cols == 1 -> {
				for (i in basis.indices) {
					if (disabledCols[i]) {
						for (k in basis.indices) {
							if (disabledRows[k]) {
								basis.setValue(a[k], k, i)
							}
						}
					}
				}
			}
		}

		basis.print()
		println("Решено за $iterations итераций.")
		println("ПОИСК БАЗИСА МЕТОДОМ НАИМЕНЬШЕЙ СТОИМОСТИ ЗАКОНЧЕН")
		return basis
	}
}