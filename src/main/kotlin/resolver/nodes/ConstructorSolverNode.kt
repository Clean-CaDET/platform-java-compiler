package resolver.nodes

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.ObjectCreationExpr
import resolver.SymbolMap
import resolver.nodes.abs.CallSolverNode

class ConstructorSolverNode(node: ObjectCreationExpr, symbolMap: SymbolMap)
    : CallSolverNode(node, symbolMap)
{
    override var caller: Node?
        get() = null
        set(value) {}

    override fun resolve() {
        this.returnType = (node as ObjectCreationExpr).typeAsString
        super.resolve()
    }

    override fun getName(): String = (node as ObjectCreationExpr).typeAsString
}