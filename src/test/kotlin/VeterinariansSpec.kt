package pet.clinic

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import khttp.get
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import pet.clinic.infrastructure.delivery.VeterinarianDTO
import kotlin.test.assertEquals

object VeterinariansSpec : Spek({
    describe("Veterinarians") {
        it("should be listed, with ID and name, but no specialties") {
            val embeddedServer: EmbeddedServer = ApplicationContext.run(EmbeddedServer::class.java)

            var veterinarian = jacksonObjectMapper().readValue<VeterinarianDTO>(get(embeddedServer))

            assertEquals(VeterinarianDTO("1", "John"), veterinarian)
        }
    }
})

private fun get(embeddedServer: EmbeddedServer) =
        get(embeddedServer.url.toString() + "/veterinarians/1").text

