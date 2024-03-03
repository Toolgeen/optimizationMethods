package Input

import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

object InputHandler {
	var task: Task? = null
	var path: String? = null
	private var outputText = StringBuilder()

	fun getFile() {
		while (task == null) {
			try {
				println("Укажите путь к файлу с json с условиями для транспортной задачи")
				val fileName : String? = readlnOrNull()
				val json = Json { ignoreUnknownKeys = true }
				task = fileName?.let {
					val file = File(it)
					path = file.path.removeSuffix(file.name)
					json.decodeFromString<Task>(string = file.readText())
				}
			} catch (e: SerializationException) {
				println("Ошибка при десериализации: ${e.message}")
				println("Попробуйте снова")
			} catch (e: Exception) {
				println("\nФайл не найден, попробуйте снова, укажите полный путь")
			}
		}
	}

	fun writeToOutputFile(text: String) {
		outputText.append(text)
		outputText.append("\n")
	}

	fun createOutputFile() {
		File("${path}output.txt").apply {
			if (!this.exists()) {
				this.createNewFile()
			}
			bufferedWriter().use { writer ->
				writer.write(outputText.toString())
			}
		}
	}

}