
import java.util.PriorityQueue

data class Edge(val node: Int, val weight: Int)
data class Node(val id: Int, val dist: Int): Comparable<Node> {
    override fun compareTo(other: Node): Int = this.dist - other.dist
}

fun dijkstraWithMinHeap(adj: List<List<Edge>>, src: Int): IntArray {
    val dist = IntArray(adj.size) { Int.MAX_VALUE }
    val pq = PriorityQueue<Node>()
    dist[src] = 0
    pq.add(Node(src, 0))

    while (pq.isNotEmpty()) {
        val current = pq.poll()
        for (edge in adj[current.id]) {
            val newDist = dist[current.id] + edge.weight
            if (newDist < dist[edge.node]) {
                dist[edge.node] = newDist
                pq.add(Node(edge.node, newDist))
            }
        }
    }
    return dist
}

fun main() {
    val adj = listOf(
        listOf(Edge(1, 2), Edge(2, 4)),
        listOf(Edge(2, 1), Edge(3, 7)),
        listOf(Edge(4, 3)),
        listOf(Edge(5, 1)),
        listOf(Edge(5, 5)),
        emptyList()
    )
    val result = dijkstraWithMinHeap(adj, 0)
    println("Shortest distances from node 0: ${result.joinToString()}")
}
