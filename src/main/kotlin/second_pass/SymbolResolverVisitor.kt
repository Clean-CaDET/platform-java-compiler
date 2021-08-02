package second_pass

import cadet_model.CadetClass
import cadet_model.CadetMember
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.body.*
import com.github.javaparser.ast.expr.FieldAccessExpr
import com.github.javaparser.ast.expr.MethodCallExpr
import com.github.javaparser.ast.expr.ObjectCreationExpr
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import kotlinx.coroutines.runBlocking
import prototype_dto.ClassPrototype
import prototype_dto.JavaPrototype
import second_pass.resolver.InjectedContext
import second_pass.hierarchy.HierarchyGraph
import second_pass.resolver.ResolverProxy
import second_pass.resolver.node_parser.LocalVariableParser
import second_pass.signature.MemberNodeSigWrapper
import second_pass.signature.MemberSignature
import util.Threading

// TODO Add support for global reference access, like
//      private Type field = SomeReference.callMethod()
class SymbolResolverVisitor : VoidVisitorAdapter<SymbolResolverVisitor.VisitorContext?>() {

    class VisitorContext(
        val cadetClass: CadetClass,
        val cadetMember: CadetMember,
        val hierarchyGraph: HierarchyGraph
    )

    private val threadSafeResolver: ResolverProxy = ResolverProxy()

    fun resolveSourceCode(resolverPairs: List<Pair<ClassOrInterfaceDeclaration, JavaPrototype>>): List<JavaPrototype> {
        val prototypes = resolverPairs.map { pair -> pair.second }
        resolvePrototypes(resolverPairs, prototypes)
        return prototypes
    }

    private fun resolvePrototypes(
        resolverPairs: List<Pair<ClassOrInterfaceDeclaration, JavaPrototype>>,
        prototypes: List<JavaPrototype>
    ) = runBlocking {
        resolverPairs
            .filter { pair -> pair.second is ClassPrototype }
            .let { classPairs ->
                Threading.iterateListSlicesViaThreads(
                    classPairs,
                    function = {
                        visitTopLevelChildren(
                            node = it.first,
                            cadetClass = (it.second as ClassPrototype).cadetClass,
                            hierarchyGraph = HierarchyGraph.Factory.initializeHierarchyGraph(prototypes)
                        )
                    }
                )
            }

//        [Single-threaded]
//        resolverPairs
//            .filter { pair -> pair.second is ClassPrototype }
//            .forEach {
//                 visitTopLevelChildren(
//                     node = it.first,
//                     cadetClass = (it.second as ClassPrototype).cadetClass
//                 )
//            }
//        }
    }

    private fun visitTopLevelChildren(
        node: ClassOrInterfaceDeclaration,
        cadetClass: CadetClass,
        hierarchyGraph: HierarchyGraph
    ) {
        node.childNodes
            .forEach { child ->
                when (child) {
                    is MethodDeclaration ->
                        visit(
                            child,
                            VisitorContext(
                                cadetClass,
                                initCadetMemberContext(child, cadetClass, hierarchyGraph)!!,
                                hierarchyGraph
                            )
                        )
                    is ConstructorDeclaration ->
                        visit(
                            child,
                            VisitorContext(
                                cadetClass,
                                initCadetMemberContext(child, cadetClass, hierarchyGraph)!!,
                                hierarchyGraph
                            )
                        )
                    // is FieldDeclaration -> visit(child, null)
                }
            }
    }

    override fun visit(node: ClassOrInterfaceDeclaration, arg: VisitorContext?) {}

    override fun visit(node: MethodDeclaration, arg: VisitorContext?) {
        arg ?: error("Visitor context not injected.")
        super.visit(node, arg)
    }

    override fun visit(node: ConstructorDeclaration, arg: VisitorContext?) {
        arg ?: error("Visitor context not injected.")
        super.visit(node, arg)
    }

    override fun visit(node: MethodCallExpr, arg: VisitorContext?) {
        this.threadSafeResolver.resolve(node, initInjectedContext(arg!!.cadetMember, arg.hierarchyGraph))
    }

    override fun visit(node: ObjectCreationExpr, arg: VisitorContext?) {
        this.threadSafeResolver.resolve(node, initInjectedContext(arg!!.cadetMember, arg.hierarchyGraph))
    }

    override fun visit(node: FieldAccessExpr, arg: VisitorContext?) {
        this.threadSafeResolver.resolve(node, initInjectedContext(arg!!.cadetMember, arg.hierarchyGraph))
    }

    override fun visit(node: VariableDeclarator, arg: VisitorContext?) {
        arg!!.cadetMember.localVariables.add(LocalVariableParser.instantiateLocalVariable(node))
        super.visit(node, arg)
    }

    // Context handling methods
    private fun initCadetMemberContext(
        node: Node,
        parent: CadetClass,
        hierarchyGraph: HierarchyGraph
    ): CadetMember? {
        return when (node) {
            is MethodDeclaration ->
                parent.getMemberViaSignature(
                    MemberSignature(MemberNodeSigWrapper(node)).withHierarchyGraph(hierarchyGraph)
                )
            is ConstructorDeclaration ->
                parent.getMemberViaSignature(
                    MemberSignature(MemberNodeSigWrapper(node)).withHierarchyGraph(hierarchyGraph)
                )
            else -> error("Cannot instantiate CadetMember from [${node.metaModel.typeName}]")
        }
    }

    private fun initInjectedContext(cadetMember: CadetMember, hierarchyGraph: HierarchyGraph): InjectedContext
        = InjectedContext(hierarchyGraph, cadetMember, cadetMember.localVariables)
}