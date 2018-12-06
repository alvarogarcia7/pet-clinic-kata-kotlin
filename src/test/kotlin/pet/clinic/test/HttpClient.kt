package pet.clinic.test

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import java.net.URL

class HttpClient(private val url: URL) {
    fun get(uri: String): Request {
        return (url.toString() + uri)
                .httpGet()
                .responseString { _, _, result ->
                    when (result) {
                        is Result.Failure -> {
                            result.getException()
                        }
                        is Result.Success -> {
                            result.get()
                        }
                    }
                }
    }

    fun post(uri: String, payload: Any): Request {
        val destination = url.toString() + uri
        val jsonBody = destination
                .httpPost()
                .body(objectMapper.writeValueAsString(payload))
        jsonBody.headers["Content-Type"] = "application/json"
        return jsonBody
                .responseString { _, _, result ->
                    when (result) {
                        is Result.Failure -> {
                            result.getException()
                        }
                        is Result.Success -> {
                            result.get()
                        }
                    }
                }
    }
}

val objectMapper = jacksonObjectMapper()
inline fun <reified T : Any> readAs(content: Response) = objectMapper.readValue<T>(content.data)
