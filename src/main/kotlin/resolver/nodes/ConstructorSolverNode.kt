package resolver.nodes

import com.github.javaparser.ast.expr.ObjectCreationExpr
import resolver.SymbolMap
import resolver.nodes.abs.CallSolverNode

class ConstructorSolverNode(node: ObjectCreationExpr, symbolMap: SymbolMap)
    : CallSolverNode(node, symbolMap)
{
    override fun getName(): String = (node as ObjectCreationExpr).typeAsString
}