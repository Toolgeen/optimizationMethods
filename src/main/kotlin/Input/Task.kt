package Input

class Task(
	val matrix: MutableList<MutableList<Double>>,
	val a: List<Double>,
	val b: List<Double>,
) {
	val rows = matrix.size
	val cols = matrix[0].size

	companion object {
		val TestData = Task(
			matrix = mutableListOf(
				mutableListOf(800.0, 100.0, 900.0, 300.0),
				mutableListOf(400.0, 600.0, 200.0, 1200.0),
				mutableListOf(700.0, 500.0, 800.0, 900.0),
				mutableListOf(400.0, 900.0, 0.0, 500.0)
			),
			a = listOf(110.0, 190.0, 90.0, 70.0), b = listOf(100.0, 60.0, 170.0, 130.0)
		)
		val TestData2 = Task(
			matrix = mutableListOf(
				mutableListOf(18.0, 9.0, 7.0, 10.0),
				mutableListOf(6.0, 4.0, 11.0, 14.0),
				mutableListOf(12.0, 2.0, 8.0, 13.0),
				mutableListOf(5.0, 12.0, 14.0, 16.0)
			),
			a = listOf(6.0, 8.0, 12.0, 14.0), b = listOf(12.0, 7.0, 8.0, 13.0)
		)
	}

	val targetFunCoefficients = matrix.flatMap {
		it.asIterable()
	}

	val rightPartOfRestrictionsSystem = b + a
}

