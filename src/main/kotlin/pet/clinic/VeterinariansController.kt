package pet.clinic

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import pet.clinic.infrastructure.delivery.VeterinarianDTO

@Controller("/veterinarians")
class VeterinariansController {

    @Get("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun detail(id: String): String {
        return jacksonObjectMapper().writeValueAsString(VeterinarianDTO(id, "John"))
    }
}