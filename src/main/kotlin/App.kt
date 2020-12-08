import util.SourceCodeDto
import java.io.File

const val root = "test\\"
const val javaFileExtension = "java"

fun main(args: Array<String>) {

    val parser = JavaCodeParser()

    parser.parseFiles(extractSourceCode(getAllFilePaths(root + "shapes")))
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

private fun extractSourceCode(paths: List<String>): List<SourceCodeDto> {
    return mutableListOf<SourceCodeDto>()
        .also { sourceCodeList ->
            paths.forEach { filePath ->
                sourceCodeList.add(SourceCodeDto(filePath, File(filePath).readText()))
            }
        }
}
