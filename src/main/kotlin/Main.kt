
import Input.InputHandler
import Input.isSimplexMethodNeeded
import LeastElementMethod.LeastElementMethod
import SimplexMethod.SimplexMethod

private fun main() {

    InputHandler.getFile()
    val task = InputHandler.task

    task?.let {
        val basis = LeastElementMethod(task)

        if (isSimplexMethodNeeded()) {
            SimplexMethod(basis, task)
        }
    }
}

