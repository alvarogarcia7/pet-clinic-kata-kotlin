package pet.clinic.infrastructure.delivery

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces

@Controller("/veterinarians")
class VeterinariansController {

    private val objectMapper = jacksonObjectMapper()

    @Get("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun detail(id: String): String {
        return objectMapper.writeValueAsString(VeterinarianDTO(id, "John"))
    }
}