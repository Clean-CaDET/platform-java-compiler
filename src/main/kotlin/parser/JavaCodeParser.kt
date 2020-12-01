package parser

import com.github.javaparser.ParseProblemException
import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import resolver.SymbolResolver
import resolver.VisitorClassMap
import visitor.InnerVisitor
import visitor.OuterVisitor
import java.lang.IllegalArgumentException
import java.nio.file.Path


class JavaCodeParser {
    private val outerVisitor = OuterVisitor()
    private val classMap = VisitorClassMap()
    private val innerVisitor = InnerVisitor(classMap, SymbolResolver(classMap))
    private val rootNodes = mutableListOf<CompilationUnit>()

    fun parseFiles(paths: List<String>): VisitorClassMap? {
        if (paths.isEmpty()) throw IllegalArgumentException("File list empty.")

        for (path in paths) {
            try {
                parse(Path.of(path))
                .also { cUnit ->
                    //Console.printTree(cUnit)
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
        testPrint()

        return classMap
    }

    private fun testPrint() {
        classMap.getClasses().forEach { Class ->
            Console.printCadetClass(Class)
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
            innerVisitor.parseTree(rootNodes[index], classMap.getClassAt(index))
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