package pet.clinic

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPatch
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.httpPut
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

    fun patch(uri: String, payload: String) = (url.toString() + "/" + uri)
            .httpPost()
            .body(payload)
            .header("Content-Type" to "application/json")
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
    describe("Objects") {
        val JOHN = OwnerDTO("1", "John", "1450 Oak Blvd", "Morona", "608555387", listOf(PetDTO("1", "Lucky")))
        val HARRY = OwnerDTO("2", "Harry", "1451 Oak Blvd", "Morona", "608555388", listOf(
                PetDTO("1", "Lucky"),
                PetDTO("2", "Agatha")))

        it("should have a detail") {

            val content = get("/owners/1").response().second

            assertEquals(JOHN, readAs(content))
        }
        it("should have a list of all of them") {

            val content = get("/owners/").response().second

            assertEquals(listOf(
                    JOHN,
                    HARRY),
                    readAs(content))
        }
        it("should be updateable") {

            val update = patch("/owners/1", payload = objectMapper.writeValueAsString(ChangeOwnerDTO("Jaume", "1450 Oak Blvd", "Morona", "608555387", listOf(PetDTO("1", "Lucky")))))

            assertEquals(HttpStatus.ACCEPTED.code, update.response().second.statusCode)

            val content = get("/owners/1").response().second

            assertEquals(JOHN.copy(name = "Jaume"), readAs(content))

        }
    }
})


