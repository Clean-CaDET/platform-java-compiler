package parser

import com.github.javaparser.ParseProblemException
import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import model.CadetClass
import resolver.SymbolContextMap
import resolver.SymbolResolver
import visitor.HierarchyVisitor
import visitor.InnerVisitor
import visitor.OuterVisitor
import java.lang.IllegalArgumentException
import java.nio.file.Path


class JavaCodeParser {
    private val classMap = SymbolContextMap()
    private val outerVisitor = OuterVisitor(classMap)
    private val hierarchyVisitor = HierarchyVisitor(classMap)
    private val innerVisitor = InnerVisitor(classMap, SymbolResolver(classMap))
    private val rootNodes = mutableListOf<CompilationUnit>()

    fun parseFiles(paths: List<String>): List<CadetClass>? {
        if (paths.isEmpty()) throw IllegalArgumentException("File list empty.")

        lateinit var cUnit: CompilationUnit

        for (path in paths) {
            try { cUnit = parse(Path.of(path)) }
            catch (e: ParseProblemException) {
                println("Syntax errors at file: \n $path")
                return null
            }
            rootNodes.add(cUnit)
            outerVisit(cUnit)
        }

        resolveHierarchy()
        innerVisit()
        testPrint()

        classMap.printClassHierarchy()
        return classMap.getClasses()
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

    private fun resolveHierarchy() {
        for (index in rootNodes.indices)
            hierarchyVisitor.parseTree(rootNodes[index], classMap.getClassAt(index))
    }

    private fun innerVisit() {
        for (index in rootNodes.indices)
            innerVisitor.parseTree(rootNodes[index], classMap.getClassAt(index))
    }


// JavaParser methods
    private fun parse(sourceCode: String): CompilationUnit {
        return StaticJavaParser.parse(sourceCode)
    }

    private fun parse(pathToFile: Path): CompilationUnit {
        return StaticJavaParser.parse(pathToFile)
    }
}