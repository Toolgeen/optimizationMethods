
import Input.Task
import Input.isSimplexMethodNeeded
import LeastElementMethod.LeastElementMethod
import SimplexMethod.SimplexMethod

private fun main() {

    val task = Task.TestData

    val basis = LeastElementMethod(task)

    if (isSimplexMethodNeeded()) {
        SimplexMethod(basis, task)
    }
}

