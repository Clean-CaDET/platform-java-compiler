package second_pass.resolver.resolver_tree

import cadet_model.CadetField
import cadet_model.CadetLocalVariable
import cadet_model.CadetMember
import cadet_model.CadetParameter

class CadetReferenceUsageProxy(private val cadetMember: CadetMember) {

    fun recordReferenceUsage(reference: Any) {
        when(reference) {
            is CadetMember -> {
                cadetMember.invokedMethods.add(reference)
                println("[Record] Member ${reference.name} invoked in member ${cadetMember.parent.name}.${cadetMember.name}")
            }
            is CadetField -> {
                cadetMember.accessedFields.add(reference)
                println("[Record] Field ${reference.name} accessed in member ${cadetMember.parent.name}.${cadetMember.name}")
            }
            is CadetParameter -> {
                println("[Record] Parameter ${reference.name} accessed in member ${cadetMember.parent.name}.${cadetMember.name}")
            }
            is CadetLocalVariable -> {
                println("[Record] Local variable ${reference.name} accessed in member ${cadetMember.parent.name}.${cadetMember.name}")
            }
            else -> error("Unsupported reference type being recorded: [${reference.javaClass}]")
        }
    }
}