import parser.JavaCodeParser

const val directoryPath = "src\\main\\kotlin\\test\\"

fun main(args: Array<String>) {

    val parser = JavaCodeParser()

    val test = path("Test.java")
    val baseTest = path("BaseTest.java")
    val extTest = path("ExtendTest.java")
    val staticTest = path("StaticTest.java")

    parser.parseFiles(listOf(test, baseTest, extTest, staticTest))
}

fun path(fileName: String): String {
    return directoryPath + fileName
}