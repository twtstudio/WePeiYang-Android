package com.twt.service.push

object DebugProxyRouter {
    val resistedRoute = hashMapOf<String, (queries: MutableMap<String, String>?, out: EmbedHttpServer.ResponseOutputStream) -> Unit>()
    fun intercept(path: String, queries: MutableMap<String, String>?, out: EmbedHttpServer.ResponseOutputStream): Boolean {
        for ((key, value) in resistedRoute) {
            if (path == key) {
                value.invoke(queries, out)
                return true
            }
        }
        return false
    }

    fun get(path: String, block: (queries: MutableMap<String, String>?, out: EmbedHttpServer.ResponseOutputStream) -> Unit) {
        resistedRoute[path] = block
    }

    fun route(init: DebugProxyRouter.() -> Unit) {
        this.init()
    }
}