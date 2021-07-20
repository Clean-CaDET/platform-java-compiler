package second_pass

import cadet_model.CadetClass
import cadet_model.CadetLocalVariable
import cadet_model.CadetMember
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.body.*
import com.github.javaparser.ast.expr.FieldAccessExpr
import com.github.javaparser.ast.expr.MethodCallExpr
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import prototype_dto.ClassPrototype
import prototype_dto.JavaPrototype
import second_pass.resolver.ResolverWizard
import second_pass.hierarchy.HierarchyGraph
import second_pass.resolver.SymbolResolver
import second_pass.resolver.node_parser.LocalVariableParser
import second_pass.signature.SignableMemberNode
import second_pass.signature.MemberSignature
import util.AstNodeUtil

// TODO Add support for multi-threaded resolving, aka remove state-persistence
// TODO Add support for global reference access, like
//      private Type field = SomeReference.callMethod()
class SymbolResolverVisitor : VoidVisitorAdapter<CadetMember?>() {

    private lateinit var resolver: SymbolResolver

    private var currentCadetClass: CadetClass? = null
    private var currentCadetMember: CadetMember? = null
    private val localVariables = mutableListOf<CadetLocalVariable>()

    private lateinit var hierarchyGraph: HierarchyGraph

    fun resolveSourceCode(resolverPairs: List<Pair<ClassOrInterfaceDeclaration, JavaPrototype>>)
            : List<JavaPrototype> {
        val prototypes = isolatePrototypes(resolverPairs)
        this.hierarchyGraph = HierarchyGraph.Factory.initializeHierarchyGraph(prototypes)

        this.resolver = SymbolResolver(this.hierarchyGraph)
        resolvePrototypes(resolverPairs)

        return prototypes
    }

    private fun isolatePrototypes(resolverPairs: List<Pair<ClassOrInterfaceDeclaration, JavaPrototype>>)
            : List<JavaPrototype> {
        return resolverPairs.map { pair -> pair.second }
    }

    private fun resolvePrototypes(resolverPairs: List<Pair<ClassOrInterfaceDeclaration, JavaPrototype>>) {
        val classPairs = resolverPairs.filter { pair -> pair.second is ClassPrototype }
        classPairs.forEach { classPair ->
            this.currentCadetClass = (classPair.second as ClassPrototype).cadetClass
            visitTopLevelChildren(classPair.first)
        }
    }

    private fun visitTopLevelChildren(node: ClassOrInterfaceDeclaration) {
        AstNodeUtil
            .getDirectChildNodes(node)
            .forEach { child ->
                when (child) {
                    is MethodDeclaration -> visit(child, null)
                    is ConstructorDeclaration -> visit(child, null)
                    is FieldDeclaration -> visit(child, null)
                }
            }
    }

    // Top level nodes are visited manually
    override fun visit(node: ClassOrInterfaceDeclaration, arg: CadetMember?) { return }

    override fun visit(node: MethodDeclaration, arg: CadetMember?) {
        if (inMember())
            return
        enterMember(node)
        super.visit(node, this.currentCadetMember)
        exitMember()
    }

    override fun visit(node: ConstructorDeclaration, arg: CadetMember?) {
        if (inMember())
            return
        enterMember(node)
        super.visit(node, this.currentCadetMember)
        exitMember()
    }

    override fun visit(node: MethodCallExpr, arg: CadetMember?) {
        if (inMember())
            this.resolver.resolve(node, instantiateResolverWizard())
    }

    override fun visit(node: FieldAccessExpr, arg: CadetMember?) {
        if (inMember())
            this.resolver.resolve(node, instantiateResolverWizard())
    }

    override fun visit(node: VariableDeclarator, arg: CadetMember?) {
        if (inMember()) {
            this.localVariables.add(LocalVariableParser.instantiateLocalVariable(node))
            super.visit(node, arg)
        }
    }

    // Context handling methods

    private fun enterMember(node: Node) {
        when (node) {
            is MethodDeclaration -> {
                this.currentCadetMember = this.currentCadetClass!!.getMemberViaSignature(
                    MemberSignature(SignableMemberNode(node)).withHierarchyGraph(hierarchyGraph)
                )
            }
            is ConstructorDeclaration -> {
                this.currentCadetMember = this.currentCadetClass!!.getMemberViaSignature(
                    MemberSignature(SignableMemberNode(node)).withHierarchyGraph(hierarchyGraph)
                )
            }
        }
    }

    private fun exitMember() {
        this.currentCadetMember = null
        this.localVariables.clear()
    }

    private fun inMember() = this.currentCadetMember != null

    private fun instantiateResolverWizard(): ResolverWizard {
        return ResolverWizard(hierarchyGraph, currentCadetMember!!, localVariables)
    }
}