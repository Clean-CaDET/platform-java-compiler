import parser.JavaCodeParser

const val directoryPath = "src\\main\\kotlin\\test\\"

fun main(args: Array<String>) {

    val parser = JavaCodeParser()

    val path1 = path("Test1.java")
    val path2 = path("Test2.java")

    parser.parseFiles(listOf(path1, path2))
}

fun path(path: String): String {
    return directoryPath + path
}