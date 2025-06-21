
fun dijkstra(graph: Array<IntArray>, src: Int): IntArray {
    val V = graph.size
    val dist = IntArray(V) { Int.MAX_VALUE }
    val visited = BooleanArray(V)
    dist[src] = 0

    for (i in 0 until V - 1) {
        var u = -1
        for (v in 0 until V) {
            if (!visited[v] && (u == -1 || dist[v] < dist[u])) {
                u = v
            }
        }

        visited[u] = true

        for (v in 0 until V) {
            if (!visited[v] && graph[u][v] != 0 && dist[u] != Int.MAX_VALUE &&
                dist[u] + graph[u][v] < dist[v]
            ) {
                dist[v] = dist[u] + graph[u][v]
            }
        }
    }

    return dist
}
