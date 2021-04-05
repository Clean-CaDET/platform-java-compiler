package second_pass.resolver.solver_nodes.cadet

import cadet_model.CadetMember
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.ObjectCreationExpr
import second_pass.resolver.SymbolResolver
import second_pass.resolver.solver_nodes.abs.MemberCallSolverNode
import second_pass.signature.MemberSignature

class ConstructorSolverNode(node: ObjectCreationExpr, resolver: SymbolResolver) : MemberCallSolverNode(node, resolver) {
    override var caller: Node? = null

    override fun resolveViaSignature(): CadetMember? {
        return resolver.getConstructor(
            (node as ObjectCreationExpr).typeAsString,
            // TODO This kind of injection is fucking ugly, remove this
            MemberSignature(this, resolver.getHierarchyGraph())
        )
    }

    override fun initChildCondition(child: Node): Boolean = true

    override fun getName(): String = (node as ObjectCreationExpr).typeAsString
}