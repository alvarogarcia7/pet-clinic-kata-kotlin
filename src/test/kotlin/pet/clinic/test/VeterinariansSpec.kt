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
import pet.clinic.infrastructure.delivery.ResponseBody
import pet.clinic.infrastructure.delivery.SpecialtyDTO
import pet.clinic.infrastructure.delivery.VeterinarianDTO
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.fail

object VeterinariansSpec : Spek({
    val embeddedServer: EmbeddedServer = ApplicationContext.run(EmbeddedServer::class.java)
    val url = embeddedServer.url
    val client = HttpClient(url)

    describe("Veterinarians") {
        val listOfRadiology = listOf(SpecialtyDTO("1", "radiology"))
        val JOHN = VeterinarianDTO("1", "John", listOfRadiology)

        it("should have a detail") {
            val creation = client.post("/veterinarians/", JOHN)
            assertEquals(HttpStatus.ACCEPTED.code, creation.response().second.statusCode)
            val newResource = creation.response().second.headers["Location"]!![0]

            creation.responseString{_,res,it->
                when(it){
                    is Result.Success -> {
                        val x = it.get()
                        val responseBody = readAs<ResponseBody<Any>>(res)
                        assertTrue(responseBody.links.isNotEmpty())
                    }
                    else -> {
                        fail("")
                    }
                }
            }

            val content = client.get(newResource).response().second

            assertEquals(JOHN.copy(id=newResource.split("/").last()), readAs(content))
        }
        it("should have a list of all of them") {
            val PAUL = VeterinarianDTO("2", "Paul", listOfRadiology)
            val creation = client.post("/veterinarians/", PAUL)
            assertEquals(HttpStatus.ACCEPTED.code, creation.response().second.statusCode)
            creation.response().second

            val content = client.get("/veterinarians/").response().second

            assertEquals(listOf(
                    JOHN,
                    PAUL),
                    readAs(content))
        }
    }
})



