package pet.clinic.test

import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpStatus
import io.micronaut.runtime.server.EmbeddedServer
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import pet.clinic.domain.common.Id
import pet.clinic.domain.common.Persisted
import pet.clinic.infrastructure.delivery.hateoas.ResponseBody
import pet.clinic.infrastructure.delivery.io.SpecialtyDTO
import pet.clinic.infrastructure.delivery.io.VeterinarianDTO
import kotlin.test.assertEquals
import kotlin.test.assertTrue

object SpecialtiesSpec : Spek({
    val embeddedServer: EmbeddedServer = ApplicationContext.run(EmbeddedServer::class.java)
    val url = embeddedServer.url
    val client = HttpClient(url)

    describe("Specialties") {
        val listOfRadiology = listOf(Persisted(Id.random(), SpecialtyDTO("radiology")))
        val JOHN = VeterinarianDTO("John", listOf(Persisted(Id.random(), SpecialtyDTO("radiology")), Persisted(Id.random(), SpecialtyDTO("morphology"))))
        val PAUL = VeterinarianDTO("Paul", listOfRadiology)

        it("should have a list of all of them") {
            assertEquals(HttpStatus.ACCEPTED.code, registerVeterinarian(client, JOHN).response().second.statusCode)
            assertEquals(HttpStatus.ACCEPTED.code, registerVeterinarian(client, PAUL).response().second.statusCode)

            println(String(client.get("/veterinarians/").response().second.data))

            val content = client.get("/specialties/").response().second
            println(String(content.data))

            assertTrue(readAs<ResponseBody<List<ResponseBody<SpecialtyDTO>>>>(content).body.map { it.body }.containsAll(listOf(SpecialtyDTO("morphology"), SpecialtyDTO("radiology"))))
        }
    }
})

private fun registerVeterinarian(client: HttpClient, veterinarian: VeterinarianDTO) =
        client.post("/veterinarians/", veterinarian)

