package com.jonnymatts.classy

import ro.pippo.core.Application
import ro.pippo.core.ParameterValue
import ro.pippo.core.Response
import ro.pippo.core.route.RouteContext

class PippoApplication(val loadedClasses: List<ClassInfo>, val randomizer: Randomizer) : Application() {

    override fun onInit() {
        registerKotlinContentEngine()
        GET("/randomClass", { handleRequest(it) })
    }

    private fun handleRequest(routeContext: RouteContext) {
        val queryParams: Map<String, ParameterValue> = routeContext.request.parameters
        val packageParam: ParameterValue? = queryParams["package"]
        val packages: List<String> = packageParam?.values?.toList() ?: emptyList()
        val classes: List<ClassInfo> = packages.flatMap { packageName -> loadedClasses.filter { it.fromPackage.startsWith(packageName) } }
        if(classes.isEmpty()) {
            sendResponse(routeContext, 400, ErrorResponse("No classes found for packages $packages"))
        } else {
            val randomClass: ClassInfo = randomizer.randomEntry(classes)
            sendResponse(routeContext, 200, randomClass)
        }
    }

    private fun sendResponse(routeContext: RouteContext, statusCode: Int, body: Any) {
        val response: Response = routeContext.response
        response.status(statusCode)
        response.contentType("application/json")
        routeContext.send(body)
    }

    private fun registerKotlinContentEngine() {
        val jsonEngine = KotlinJacksonJsonEngine()
        jsonEngine.init(this)
        contentTypeEngines.setContentTypeEngine(jsonEngine)
    }

    data class ErrorResponse(val message: String)
}