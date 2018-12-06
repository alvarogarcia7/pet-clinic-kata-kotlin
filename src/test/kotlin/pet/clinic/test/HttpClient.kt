package pet.clinic.test

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

    fun post(uri: String, payload: String): Request {
        println(payload)
        val destination = url.toString() + uri
        println(destination)
        val jsonBody = destination
                .httpPost()
                .body(payload)
        jsonBody.headers["Content-Type"] = "application/json"
        return jsonBody
                .responseString { x, y, result ->
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