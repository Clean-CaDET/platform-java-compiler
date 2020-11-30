package parser

import com.github.javaparser.ParseProblemException
import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import resolver.SymbolResolver
import visitor.CadetClassMap
import visitor.InnerVisitor
import visitor.OuterVisitor
import java.lang.IllegalArgumentException
import java.nio.file.Path


class JavaCodeParser {
    private val outerVisitor = OuterVisitor()
    private val classMap = CadetClassMap()
    private val innerVisitor = InnerVisitor(classMap, SymbolResolver(classMap))
    private val rootNodes = mutableListOf<CompilationUnit>()

    fun parseFiles(paths: List<String>): CadetClassMap? {
        if (paths.isEmpty()) throw IllegalArgumentException("File list empty.")

        for (path in paths) {
            try {
                parse(Path.of(path))
                .also { cUnit ->
                    rootNodes.add(cUnit)
                    outerVisit(cUnit)
                }
            }
            catch (e: ParseProblemException) {
                println("Syntax errors at file: \n $path")
                return null
            }
        }

        innerVisitAll()
        return classMap
    }

    private fun testPrint() {
        for (cadetClass in classMap.getClasses()) {
            Console.printCadetClass(cadetClass)
            println("_________________________________________")
        }
    }

    private fun outerVisit(cUnit: CompilationUnit) {
        outerVisitor.parseTree(cUnit)
            .also { cadetClass ->
                classMap.addClass(cadetClass)
            }
    }

    private fun innerVisitAll() {
        for (index in 0 until rootNodes.size) {
            println(classMap.getClassAt(index).name);
            innerVisitor.parseTree(rootNodes[index], classMap.getClassAt(index))
            println()
        }
    }


// JavaParser methods
    private fun parse(sourceCode: String): CompilationUnit {
        return StaticJavaParser.parse(sourceCode)
    }

    private fun parse(pathToFile: Path): CompilationUnit {
        return StaticJavaParser.parse(pathToFile)
    }
}