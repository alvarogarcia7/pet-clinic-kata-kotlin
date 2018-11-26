package pet.clinic

import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import pet.clinic.infrastructure.delivery.OwnerDTO
import pet.clinic.infrastructure.delivery.PetDTO
import kotlin.test.assertEquals

object OwnersSpec : Spek({
    val embeddedServer: EmbeddedServer = ApplicationContext.run(EmbeddedServer::class.java)
    val url = embeddedServer.url
    fun get(uri: String) = khttp.get(url.toString() + uri).text
    describe("Objects") {
        val JOHN = OwnerDTO("1", "John", "1450 Oak Blvd", "Morona", "608555387", listOf(PetDTO("1", "Lucky")))
        val HARRY = OwnerDTO("2", "Harry", "1451 Oak Blvd", "Morona", "608555388", listOf(
                PetDTO("1", "Lucky"),
                PetDTO("2", "Agatha")))

        it("should have a detail") {

            val content = get("/owners/1")

            assertEquals(JOHN, readAs(content))
        }
        it("should have a list of all of them") {

            val content = get("/owners/")

            assertEquals(listOf(
                    JOHN,
                    HARRY),
                    readAs(content))
        }
    }
})


