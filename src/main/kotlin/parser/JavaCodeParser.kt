package parser

import SourceCodeDto
import com.github.javaparser.ParseProblemException
import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import context.VisitorContext
import hierarchy.HierarchyGraph
import model.CadetClass
import resolver.SymbolSolvingBundle
import resolver.SymbolResolver
import signature.MemberSignature
import visitor.HierarchyVisitor
import visitor.InnerVisitor
import visitor.OuterVisitor


class JavaCodeParser {
    private val hierarchyGraph = HierarchyGraph()
    private val visitorContext = VisitorContext()
    private val symbolSolvingBundle = SymbolSolvingBundle(hierarchyGraph, visitorContext)

    private val outerVisitor = OuterVisitor(hierarchyGraph)
    private val hierarchyVisitor = HierarchyVisitor(visitorContext, hierarchyGraph)
    private val innerVisitor = InnerVisitor(visitorContext, SymbolResolver(symbolSolvingBundle))

    private val classParserBundleList = mutableListOf<CadetClassParserBundle>()

    fun parseFiles(sourceCodeDtoList: List<SourceCodeDto>): List<CadetClass>? {
        lateinit var cUnit: CompilationUnit

        for (sourceCodeDto in sourceCodeDtoList) {
            try { cUnit = parse(sourceCodeDto.sourceCode) }
            catch (e: ParseProblemException) {
                println("Syntax errors at file ${sourceCodeDto.fileName}")
                return null
            }
            classParserBundleList.add(CadetClassParserBundle(cUnit, outerVisit(cUnit)))
        }

        MemberSignature.injectHierarchyGraph(hierarchyGraph)
        resolveHierarchy()
        innerVisit()

        return hierarchyGraph.getClasses()
    }

    private fun outerVisit(cUnit: CompilationUnit): CadetClass {
        return outerVisitor.parseTree(cUnit)
            .also { cadetClass ->
                hierarchyGraph.addClass(cadetClass)
            }
    }

    private fun resolveHierarchy() {
        for (classBundle in classParserBundleList)
            hierarchyVisitor.parseTree(classBundle.compilationUnit, classBundle.cadetClass)
    }

    private fun innerVisit() {
        for (classBundle in classParserBundleList)
            innerVisitor.parseTree(classBundle.compilationUnit, classBundle.cadetClass)
    }

    private class CadetClassParserBundle(
        val compilationUnit: CompilationUnit,
        val cadetClass: CadetClass
    ) {}


// JavaParser method
    private fun parse(sourceCode: String): CompilationUnit {
        return StaticJavaParser.parse(sourceCode)
    }
}