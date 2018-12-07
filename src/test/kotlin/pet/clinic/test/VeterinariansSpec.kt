package pet.clinic.test

import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpStatus
import io.micronaut.runtime.server.EmbeddedServer
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import pet.clinic.infrastructure.delivery.MyResource
import pet.clinic.infrastructure.delivery.ResponseBody
import pet.clinic.infrastructure.delivery.SpecialtyDTO
import pet.clinic.infrastructure.delivery.VeterinarianDTO
import kotlin.test.assertEquals
import kotlin.test.assertTrue

object VeterinariansSpec : Spek({
    val embeddedServer: EmbeddedServer = ApplicationContext.run(EmbeddedServer::class.java)
    val url = embeddedServer.url
    val client = HttpClient(url)

    describe("Veterinarians") {
        val listOfRadiology = listOf(SpecialtyDTO("1", "radiology"))
        val JOHN = VeterinarianDTO("John", listOfRadiology)

        it("should have a detail") {
            val creation = client.post("/veterinarians/", JOHN)
            assertEquals(HttpStatus.ACCEPTED.code, creation.response().second.statusCode)
            val newResource = creation.response().second.headers["Location"]!![0]

            val content = client.get(newResource).response().second

            assertEquals(JOHN, readAs<ResponseBody<VeterinarianDTO>>(content).body)
        }
        it("should have a list of all of them") {
            val PAUL = VeterinarianDTO("Paul", listOfRadiology)
            val creation = client.post("/veterinarians/", PAUL)
            assertEquals(HttpStatus.ACCEPTED.code, creation.response().second.statusCode)
            creation.response().second

            val content = client.get("/veterinarians/").response().second

            assertTrue(readAs<ResponseBody<List<ResponseBody<VeterinarianDTO>>>>(content).body.map { it.body }.containsAll(listOf(JOHN, PAUL)))
        }
    }
})



