import parser.JavaCodeParser
import java.io.File

const val root = "src\\main\\kotlin\\test\\"
const val javaFileExtension = "java"

fun main(args: Array<String>) {

    val parser = JavaCodeParser()

    parser.parseFiles(getAllFilePaths(root + "shapes"))
}

private fun getAllFilePaths(path: String): List<String> {
    return mutableListOf<String>()
        .also {paths ->
            File(path).walkBottomUp().forEach {
                if (it.isFile && it.extension == javaFileExtension)
                    paths.add(it.absolutePath)
            }
        }
}
