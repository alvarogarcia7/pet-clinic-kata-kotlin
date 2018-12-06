package pet.clinic.test

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpStatus
import io.micronaut.runtime.server.EmbeddedServer
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import pet.clinic.infrastructure.delivery.SpecialtyDTO
import pet.clinic.infrastructure.delivery.VeterinarianDTO
import kotlin.test.assertEquals

object VeterinariansSpec : Spek({
    val embeddedServer: EmbeddedServer = ApplicationContext.run(EmbeddedServer::class.java)
    val url = embeddedServer.url
    val client = HttpClient(url)

    describe("Veterinarians") {
        println(url.toString())
        val listOfRadiology = listOf(SpecialtyDTO("1", "radiology"))
        val JOHN = VeterinarianDTO("1", "John", listOfRadiology)

        it("should have a detail") {
            val creation = client.post("/veterinarians/1", JOHN)
            assertEquals(HttpStatus.ACCEPTED.code, creation.response().second.statusCode)

            val content = client.get("/veterinarians/1").response().second

            assertEquals(JOHN, readAs(content))
        }
        it("should have a list of all of them") {
            val PAUL = VeterinarianDTO("2", "Paul", listOfRadiology)
            val creation = client.post("/veterinarians/2", PAUL)
            assertEquals(HttpStatus.ACCEPTED.code, creation.response().second.statusCode)

            val content = client.get("/veterinarians/").response().second

            assertEquals(listOf(
                    JOHN,
                    PAUL),
                    readAs(content))
        }
    }
})



