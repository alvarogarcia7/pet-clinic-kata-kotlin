package pet.clinic.test

import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpStatus
import io.micronaut.runtime.server.EmbeddedServer
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import pet.clinic.infrastructure.delivery.io.ChangeOwnerDTO
import pet.clinic.infrastructure.delivery.io.OwnerDTO
import pet.clinic.infrastructure.delivery.io.PetDTO
import pet.clinic.infrastructure.delivery.hateoas.ResponseBody
import kotlin.test.assertEquals
import kotlin.test.assertTrue

object OwnersSpec : Spek({
    val embeddedServer: EmbeddedServer = ApplicationContext.run(EmbeddedServer::class.java)
    val url = embeddedServer.url
    val client = HttpClient(url)

    describe("Objects") {
        val JOHN = OwnerDTO("John", "1450 Oak Blvd", "Morona", "608555387", listOf(PetDTO("1", "Lucky")))
        val HARRY = OwnerDTO("Harry", "1451 Oak Blvd", "Morona", "608555388", listOf(
                PetDTO("1", "Lucky"),
                PetDTO("2", "Agatha")))

        it("should have a detail") {
            val creation = registerOwner(client, JOHN)
            assertEquals(HttpStatus.ACCEPTED.code, creation.response().second.statusCode)
            val newResource = creation.response().second.headers["Location"]!![0]

            val content = client.get(newResource).response().second

            assertEquals(JOHN, readAs<ResponseBody<OwnerDTO>>(content).body)
        }
        it("should have a list of all of them") {
            assertEquals(HttpStatus.ACCEPTED.code, registerOwner(client, JOHN).response().second.statusCode)
            assertEquals(HttpStatus.ACCEPTED.code, registerOwner(client, HARRY).response().second.statusCode)

            val content = client.get("/owners/").response().second

            assertTrue(readAs<ResponseBody<List<ResponseBody<OwnerDTO>>>>(content).body.map { it.body }.containsAll(listOf(JOHN, HARRY)))
        }
        it("should be updateable") {
            val owners = readAs<ResponseBody<List<ResponseBody<OwnerDTO>>>>(client.get("/owners/").response().second)
            val newResource = owners.body.first().links.find { it.name == "self" }!!.url
            val someId = newResource.split("/").last()
            val response = client.put("/owners/$someId", ChangeOwnerDTO("Jaume", "1450 Oak Blvd", "Morona", "608555387", listOf(PetDTO("1", "Lucky")))).response().second
            assertEquals(HttpStatus.ACCEPTED.code, response.statusCode)
            val newResource2 = response.headers["Location"]!![0]

            val content = client.get(newResource).response().second

            assertEquals(JOHN.copy(name = "Jaume"), readAs<ResponseBody<OwnerDTO>>(content).body)

        }
    }
})

private fun registerOwner(client: HttpClient, owner: OwnerDTO) =
        client.post("/owners/", owner)


