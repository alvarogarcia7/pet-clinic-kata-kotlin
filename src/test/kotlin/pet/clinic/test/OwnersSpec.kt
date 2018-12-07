package pet.clinic.test

import com.github.kittinunf.result.Result
import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpStatus
import io.micronaut.runtime.server.EmbeddedServer
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import pet.clinic.infrastructure.delivery.ChangeOwnerDTO
import pet.clinic.infrastructure.delivery.OwnerDTO
import pet.clinic.infrastructure.delivery.PetDTO
import pet.clinic.infrastructure.delivery.ResponseBody
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

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
            val creation = client.post("/owners/", JOHN)
            assertEquals(HttpStatus.ACCEPTED.code, creation.response().second.statusCode)
            val newResource = creation.response().second.headers["Location"]!![0]

            val content = client.get(newResource).response().second

            assertEquals(JOHN, readAs<ResponseBody<OwnerDTO>>(content).body)
        }
        it("should have a list of all of them") {
            val creation1 = client.post("/owners/", JOHN)
            assertEquals(HttpStatus.ACCEPTED.code, creation1.response().second.statusCode)
            val creation2 = client.post("/owners/", HARRY)
            assertEquals(HttpStatus.ACCEPTED.code, creation2.response().second.statusCode)

            val content = client.get("/owners/").response().second

            assertTrue(readAs<ResponseBody<List<ResponseBody<OwnerDTO>>>>(content).body.map { it.body }.containsAll(listOf(JOHN, HARRY)))
        }
        it("should be updateable") {
            val list= client.get("/owners/")
            list.responseString { _, res, it ->
                when (it) {
                    is Result.Success -> {
                        val responseBody = readAs<ResponseBody<Any>>(res)
                        assertTrue(responseBody.links.isNotEmpty())
                    }
                    else -> {
                        fail("")
                    }
                }
            }
            val update = client.post("/owners/", ChangeOwnerDTO("Jaume", "1450 Oak Blvd", "Morona", "608555387", listOf(PetDTO("1", "Lucky"))))
            assertEquals(HttpStatus.ACCEPTED.code, update.response().second.statusCode)
            val newResource = update.response().second.headers["Location"]!![0]

            val content = client.get(newResource).response().second

            assertEquals(JOHN.copy(name = "Jaume"), readAs<ResponseBody<OwnerDTO>>(content).body)

        }
    }
})


