package second_pass.resolver.solver_nodes.cadet

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.ObjectCreationExpr
import cadet_model.CadetMember
import second_pass.resolver.SymbolSolvingBundle
import second_pass.resolver.solver_nodes.abs.MemberCallSolverNode
import second_pass.signature.MemberSignature

class ConstructorSolverNode(node: ObjectCreationExpr, symbolSolvingBundle: SymbolSolvingBundle)
    : MemberCallSolverNode(node, symbolSolvingBundle)
{
    override var caller: Node? = null

    override fun callResolveReference(): CadetMember? {
        return symbolSolvingBundle.getConstructor(
                (node as ObjectCreationExpr).typeAsString,
                MemberSignature(this, symbolSolvingBundle.getHierarchyGraph())
        )
    }

    override fun initChildCondition(child: Node): Boolean = true

    override fun getName(): String = (node as ObjectCreationExpr).typeAsString
}// new Object(param1, param2)