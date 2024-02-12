
import Input.Task
import Input.isSimplexMethodNeeded
import LeastElementMethod.LeastElementMethod
import SimplexMethod.SimplexMethod

enum class CoefficientPCheck {
    SOLVED, NO_SOLVES, CONTINUE
}

private fun main() {

    val task = Task.TestData2

    val basis = LeastElementMethod(task)

    if (isSimplexMethodNeeded()) {
        SimplexMethod(basis, task)
    }
}

