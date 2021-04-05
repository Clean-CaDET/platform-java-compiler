package second_pass.context

import cadet_model.CadetField
import cadet_model.CadetLocalVariable
import cadet_model.CadetMember
import second_pass.signature.MemberSignature

class MemberContext(
    classContext: ClassContext,
    signature: MemberSignature
) : ClassContext(classContext.cadetClass) {

    private val cadetMember: CadetMember

    init {
        cadetClass.getMemberViaSignature(signature)
            .let {
                if (it == null) {
                    println("MEMBERS FOR CLASS ${cadetClass.fullName}:")
                    cadetClass.members.forEach { member ->
                        print("\t${member.name}(")
                        member.params.forEach {param ->
                            print("${param.type} ${param.name}")
                        }
                        println(")")
                    }
                    error("Failed to find $signature inside of ClassContext ${classContext.cadetClass.fullName}")
                }
                else
                //it ?: throw IllegalArgumentException(
                 //   "Failed to create member context in class ${classContext.cadetClass.fullName}. No matching signatures found."
                //)
                this.cadetMember = it
            }
    }

    fun addInvokedMember(member: CadetMember) = cadetMember.invokedMethods.add(member)
    fun addLocalVariable(localVariable: CadetLocalVariable) = cadetMember.localVariables.add(localVariable)
    fun addAccessedField(field: CadetField) = cadetMember.accessedFields.add(field)

    fun getParameter(name: String) = cadetMember.params.find { it.name == name }
    fun getLocalVariable(name: String) = cadetMember.localVariables.find { it.name == name }
}