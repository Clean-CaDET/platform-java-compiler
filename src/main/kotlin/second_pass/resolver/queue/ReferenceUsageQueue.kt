package second_pass.resolver.queue

class ReferenceUsageQueue {
    private val queue = mutableListOf<ReferenceUsageEvent>()

    fun pushUsageEvent(event: ReferenceUsageEvent) {
        this.queue.add(event)
    }

    fun getUsageEvents(): List<ReferenceUsageEvent> = queue
}