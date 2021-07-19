import java.io.File

const val root = "test\\"
const val javaFileExtension = "java"

// TODO Remove ClassContext it is really not necessary
// TODO Remove methods from MemberContext into CadetModel classes
// TODO Crashed on primitive array syntax (array[0].call())
// TODO Crashed on enum parsing when it has functions (FieldNamingPolicy.java in gson project)
// TODO Crashed on interface method calls when doing DI (parent)
fun main(args: Array<String>) {

//    parse("gson")
//    parse("json_iter")
    parse("tests")
//    parse("shapes")
}

private fun parse(directory: String) {
    JavaCodeParser().parseSourceCode(extractSourceCode(getAllFilePaths(root + directory)))
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
