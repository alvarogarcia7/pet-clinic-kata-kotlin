package pet.clinic.test

import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpStatus
import io.micronaut.runtime.server.EmbeddedServer
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import pet.clinic.infrastructure.delivery.ChangeOwnerDTO
import pet.clinic.infrastructure.delivery.OwnerDTO
import pet.clinic.infrastructure.delivery.PetDTO
import kotlin.test.assertEquals

object OwnersSpec : Spek({
    val embeddedServer: EmbeddedServer = ApplicationContext.run(EmbeddedServer::class.java)
    val url = embeddedServer.url
    val client = HttpClient(url)

    describe("Objects") {
        val JOHN = OwnerDTO("1", "John", "1450 Oak Blvd", "Morona", "608555387", listOf(PetDTO("1", "Lucky")))
        val HARRY = OwnerDTO("2", "Harry", "1451 Oak Blvd", "Morona", "608555388", listOf(
                PetDTO("1", "Lucky"),
                PetDTO("2", "Agatha")))

        it("should have a detail") {
            val update = client.post("/owners/1", JOHN)
            assertEquals(HttpStatus.ACCEPTED.code, update.response().second.statusCode)

            val content = client.get("/owners/1").response().second

            assertEquals(JOHN, readAs(content))
        }
        it("should have a list of all of them") {
            val update = client.post("/owners/2", HARRY)
            assertEquals(HttpStatus.ACCEPTED.code, update.response().second.statusCode)

            val content = client.get("/owners/").response().second

            assertEquals(listOf(
                    JOHN,
                    HARRY),
                    readAs(content))
        }
        it("should be updateable") {

            val update = client.post("/owners/1", ChangeOwnerDTO("Jaume", "1450 Oak Blvd", "Morona", "608555387", listOf(PetDTO("1", "Lucky"))))

            assertEquals(HttpStatus.ACCEPTED.code, update.response().second.statusCode)

            val content = client.get("/owners/1").response().second

            assertEquals(JOHN.copy(name = "Jaume"), readAs(content))

        }
    }
})


