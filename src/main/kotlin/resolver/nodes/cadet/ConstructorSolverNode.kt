package resolver.nodes.cadet

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.ObjectCreationExpr
import model.CadetMember
import resolver.SymbolContextMap
import resolver.nodes.abs.MemberCallSolverNode
import signature.MemberSignature

class ConstructorSolverNode(node: ObjectCreationExpr, symbolMap: SymbolContextMap)
    : MemberCallSolverNode(node, symbolMap)
{
    override var caller: Node? = null

    override fun callResolveReference(): CadetMember? {
        return symbolMap.getConstructor((node as ObjectCreationExpr).typeAsString, MemberSignature(this))
    }

    override fun initChildCondition(child: Node): Boolean = true

    override fun getName(): String = (node as ObjectCreationExpr).typeAsString
}// new Object(param1, param2)