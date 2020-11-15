import parser.Parser

fun main(args: Array<String>) {

    val parserService = Parser()

    parserService.parseFile(
        "src\\main\\" +
                "kotlin\\test\\Test2.java"
    )
}