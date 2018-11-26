package pet.clinic

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
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
    fun get(uri: String) = khttp.get(url.toString() + uri).text
    describe("Veterinarians") {
        it("should be listed, with ID and name") {

            val content = get("/veterinarians/1")

            assertEquals(VeterinarianDTO("1", "John", listOf(SpecialtyDTO("1", "radiology"))), readAs(content))
        }
    }
})


val objectMapper = jacksonObjectMapper()
private inline fun <reified T : Any> readAs(content: String) = objectMapper.readValue<T>(content)


