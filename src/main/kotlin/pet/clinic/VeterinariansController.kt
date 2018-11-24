package pet.clinic

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import pet.clinic.infrastructure.delivery.VeterinarianDTO

@Controller("/veterinarians")
class VeterinariansController {

    @Get("/1")
    @Produces(MediaType.APPLICATION_JSON)
    fun list(): String {
        return jacksonObjectMapper().writeValueAsString(VeterinarianDTO("1", "John"))
    }
}