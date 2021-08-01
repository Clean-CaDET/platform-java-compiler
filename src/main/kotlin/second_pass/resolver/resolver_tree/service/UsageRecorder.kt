package second_pass.resolver.resolver_tree.service

import second_pass.resolver.resolver_tree.model.ReferenceNode
import second_pass.resolver.resolver_tree.model.SimpleNode
import util.Threading
import kotlin.time.ExperimentalTime

class UsageRecorder {

    fun recordReferenceUsages(root: ReferenceNode, cadetReferenceUsageProxy: CadetReferenceUsageProxy) {
        recursiveRecordReferenceUsages(root, cadetReferenceUsageProxy)
    }

    private fun recursiveRecordReferenceUsages(
        node: SimpleNode,
        cadetReferenceUsageProxy: CadetReferenceUsageProxy
    ) {
        if (node is ReferenceNode) {
            node.children.forEach { recursiveRecordReferenceUsages(it, cadetReferenceUsageProxy) }

            if (node.isResolved())
                cadetReferenceUsageProxy.recordReferenceUsage(node.resolvedReference)
        }
    }
}