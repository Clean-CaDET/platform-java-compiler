package resolver.nodes.cadet

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.ObjectCreationExpr
import resolver.SymbolContextMap
import resolver.nodes.abs.MemberCallSolverNode

class ConstructorSolverNode(node: ObjectCreationExpr, symbolMap: SymbolContextMap)
    : MemberCallSolverNode(node, symbolMap)
{
    override var caller: Node? = null

    override fun doResolve() {
        this.returnType = (node as ObjectCreationExpr).typeAsString
        super.doResolve()
    }

    override fun getName(): String = (node as ObjectCreationExpr).typeAsString
}