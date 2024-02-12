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
	}
}

