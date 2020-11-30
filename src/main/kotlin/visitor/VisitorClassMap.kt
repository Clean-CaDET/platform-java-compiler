package visitor

import com.google.common.collect.ImmutableList
import context.ClassContext
import context.ContextHolder
import context.MemberContext
import hierarchy.InheritanceGraph
import javassist.NotFoundException
import model.CadetClass
import model.CadetMember
import resolver.SymbolMap
import signature.MemberSignature

class VisitorClassMap : SymbolMap {
    private val classes = mutableListOf<CadetClass>()
    private val hierarchyGraph = InheritanceGraph()
    private val contextHolder = ContextHolder()

    override fun getClassAt(index: Int) = classes[index]
    override fun getClasses(): List<CadetClass> = classes
    override fun addClass(cadetClass: CadetClass) {
        classes.add(cadetClass)
        hierarchyGraph.addClass(cadetClass)
    }

    fun createClassContext(cadetClass: CadetClass) = contextHolder.createClassContext(cadetClass)
    fun createMemberContext(signature: MemberSignature) = contextHolder.createMemberContext(signature)
    fun getMemberContext() = contextHolder.memberContext

    override fun modifyCurrentClassParent(superClassName: String) {
        hierarchyGraph.modifyClassParent(currentClass().name, superClassName)
    }

    // SymbolMap overriden methods
    override fun findCadetMemberInContext(name: String?, signature: MemberSignature): CadetMember? {
        val cadetClass = contextHolder.getContextScopedCadetClass(name, classes)
        hierarchyGraph.getClassHierarchy(cadetClass.name)
            .forEach { hierarchyClass ->
                hierarchyClass.findMemberViaSignature(signature)
                ?.let { return it }
            }
        return null
    }

    override fun getContextSuperType(): String?
            = hierarchyGraph.getClassParent(currentClass().name)?.name

    override fun getContextScopedType(name: String): String
            = contextHolder.getContextScopedCadetClass(name, classes).name

    override fun getContextClassName(): String
            = currentClass().name

    private fun currentClass() = contextHolder.classContext.cadetClass
}