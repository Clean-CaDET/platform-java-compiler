package visitor

import com.google.common.collect.ImmutableList
import context.ClassContext
import context.MemberContext
import hierarchy.InheritanceGraph
import javassist.NotFoundException
import model.CadetClass
import model.CadetMember
import resolver.SymbolMap
import signature.MemberSignature

class CadetClassMap : SymbolMap {
    private val classes = mutableListOf<CadetClass>()
    private val hierarchyGraph = InheritanceGraph()
    private lateinit var classContext: ClassContext
    private lateinit var memberContext: MemberContext

    fun getClassAt(index: Int) = classes[index]
    fun getClasses(): List<CadetClass> = classes
    fun addClass(cadetClass: CadetClass) {
        classes.add(cadetClass)
        hierarchyGraph.addClass(cadetClass)
    }

    fun createClassContext(cadetClass: CadetClass) {
        classContext = ClassContext(cadetClass)
    }
    fun createMemberContext(signature: MemberSignature) {
        memberContext = MemberContext(classContext, signature)
    }
    fun getMemberContext() = memberContext

    fun modifyClassHierarchy(superClassName: String) {
        hierarchyGraph.modifyClassParent(currentClass().name, superClassName)
    }

    // SymbolMap overriden methods
    override fun getContextClassName(): String = currentClass().name
    override fun findCadetMemberInContext(name: String?, signature: MemberSignature): CadetMember? {
        val cadetClass = getContextScopedCadetClass(name)
        hierarchyGraph.getClassHierarchy(cadetClass.name)
            .forEach { hierarchyClass ->
                hierarchyClass.findMemberViaSignature(signature)
                ?.let { return it }
            }
        return null
    }

    override fun getContextSuperType(): String? = hierarchyGraph.getClassParent(currentClass().name)?.name

    override fun getContextScopedType(name: String): String = getContextScopedCadetClass(name).name

    private fun getContextScopedCadetClass(caller: String?): CadetClass {
        caller ?: return currentClass()

        memberContext.getContextScopedLocalVariable(caller)
            ?.let {variable ->
                findCadetClass(variable.type) ?.let { return it }
            }

        memberContext.getContextScopedParameter(caller)
            ?.let {param ->
                findCadetClass(param.type) ?.let { return it }
            }

        classContext.getContextScopedCadetField(caller)
            ?.let {field ->
                findCadetClass(field.type) ?.let { return it }
            }

        classes.find { c -> c.name == caller }
            ?.let { return it; }

        throw NotFoundException("Caller: '${caller}' is unidentified.")
    }

    private fun findCadetClass(className: String) = classes.find { c -> c.name == className }
    private fun currentClass() = classContext.cadetClass
}