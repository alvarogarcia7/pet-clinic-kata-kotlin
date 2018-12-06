package pet.clinic.test

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import pet.clinic.infrastructure.delivery.SpecialtyDTO
import pet.clinic.infrastructure.delivery.VeterinarianDTO
import kotlin.test.assertEquals

object VeterinariansSpec : Spek({
    val embeddedServer: EmbeddedServer = ApplicationContext.run(EmbeddedServer::class.java)
    val url = embeddedServer.url
    fun get(uri: String) = (url.toString() + uri)
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
    describe("Veterinarians") {
        println(url.toString())
        val listOfRadiology = listOf(SpecialtyDTO("1", "radiology"))
        val JOHN = VeterinarianDTO("1", "John", listOfRadiology)

        it("should have a detail") {

            val content = get("/veterinarians/1").response().second

            assertEquals(JOHN, readAs(content))
        }
        it("should have a list of all of them") {

            val content = get("/veterinarians/").response().second

            assertEquals(listOf(
                    JOHN,
                    VeterinarianDTO("2", "Paul", listOfRadiology)),
                    readAs(content))
        }
    }
})


val objectMapper = jacksonObjectMapper()
inline fun <reified T : Any> readAs(content: Response) = objectMapper.readValue<T>(content.data)

