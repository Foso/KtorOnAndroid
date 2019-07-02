package de.jensklingenberg.ktoronandroid


import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.Compression
import io.ktor.features.ContentNegotiation
import io.ktor.features.PartialContent
import io.ktor.features.gzip
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.websocket.WebSockets


/**
 * This is the configuration for the Ktor Server
 *
 */
fun Application.ktorApplicationModule(

) {
    with(this) {

        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()

            }
        }
        install(WebSockets) {

        }

        install(Compression) {
            gzip()
        }
        install(PartialContent) {
            maxRangeCount = 10
        }

        routing {
            get("/") {
                call.respondText("Hello, world!", ContentType.Text.Html)
            }
        }

    }

}