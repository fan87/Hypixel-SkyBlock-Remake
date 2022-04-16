package me.fan87.hypixelskyblock.utils

class TopologicalGraph<NodeType> {
    private val input = HashMap<NodeType, Node<NodeType>>()
    private val cachedNodes = HashMap<NodeType, Node<NodeType>>()

    fun add(item: NodeType, before: List<NodeType>, after: List<NodeType>) {
        if (cachedNodes.containsKey(item)) {
            cachedNodes[item]!!.mustAfter
                .addAll(after.map { cachedNodes.getOrDefault(it, Node(this, ArrayList(), ArrayList(), it)) })
            cachedNodes[item]!!.mustBefore
                .addAll(before.map { cachedNodes.getOrDefault(it, Node(this, ArrayList(), ArrayList(), it)) })

        } else {
            cachedNodes[item] = Node(
                this,
                ArrayList(after.map { cachedNodes.getOrDefault(it, Node(this, ArrayList(), ArrayList(), it)) }),
                ArrayList(before.map { cachedNodes.getOrDefault(it, Node(this, ArrayList(), ArrayList(), it)) }),
                item)

        }
        input[item] = cachedNodes[item]!!
    }

    private fun visit(node: Node<NodeType>, stack: ArrayList<Node<NodeType>>) {
        node.visited = true
        for (s in node.getAfters()) {
            if (input.values.contains(s) && !s.visited) {
                println("Visiting: ${node.value}")
                visit(node, stack)
            }
        }
        stack.add(node)
    }

    fun calculate(): List<NodeType> {
        val stack = ArrayList<Node<NodeType>>()

        for (node in input.values) {
            if (!node.visited) {
                visit(node, stack)
            }
        }

        return stack.map { it.value }
    }

    private class Node<NodeType>(val graph: TopologicalGraph<NodeType>,
                         var mustAfter: ArrayList<Node<NodeType>>,
                         var mustBefore: ArrayList<Node<NodeType>>,
                         val value: NodeType
    ) {

        var visited: Boolean = false

        fun getAfters(): List<Node<NodeType>> {
            val out = ArrayList(mustAfter)
            for (item in graph.input) {
                if (item.value.mustBefore.contains(this)) {
                    out.add(item.value)
                }
            }
            return out
        }

    }

}

