import java.io.File

const val root = "test\\"
const val javaFileExtension = "java"

fun main(args: Array<String>) {

    JavaCodeParser().parseSourceCode(extractSourceCode(getAllFilePaths(root + "shapes")))
}

private fun getAllFilePaths(path: String): List<String> {
    return mutableListOf<String>()
        .also { paths ->
            File(path).walkBottomUp().forEach {
                if (it.isFile && it.extension == javaFileExtension)
                    paths.add(it.absolutePath)
            }
        }
}

private fun extractSourceCode(paths: List<String>): List<String> {
    return mutableListOf<String>()
        .also { sourceCodeList ->
            paths.forEach { filePath ->
                sourceCodeList.add(File(filePath).readText())
            }
        }
}
