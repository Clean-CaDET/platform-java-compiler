package parser

import com.github.javaparser.ParseProblemException
import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import visitor.CadetClassMap
import visitor.InnerVisitor
import visitor.OuterVisitor
import java.io.IOException
import java.nio.file.Path


class Parser {
    private val outerVisitor = OuterVisitor()
    private val classMap = CadetClassMap()
    private val innerVisitor = InnerVisitor(classMap)

    fun parseFile(path: String) {
        try {
            parse(Path.of(path))
                .also { compilationUnit ->
                    val cadetClass = outerVisitor.parseTree(compilationUnit!!)
                    classMap.addCadetClass(cadetClass)
                    innerVisitor.parseTree(compilationUnit, cadetClass)

                    Console.printCadetClass(cadetClass)
                }
        } catch (e: ParseProblemException) {
            println("Syntax errors at file: \n $path")
        } catch (e: IOException) {
            println("File not found: \n $path")
        }
    }

    private fun parse(sourceCode: String): CompilationUnit? {
        return StaticJavaParser.parse(sourceCode)
    }

    private fun parse(pathToFile: Path): CompilationUnit? {
        return StaticJavaParser.parse(pathToFile)
    }
}