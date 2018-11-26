package pet.clinic.infrastructure.delivery

import arrow.core.None
import arrow.core.Some
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import pet.clinic.domain.Id
import pet.clinic.domain.Specialty
import pet.clinic.domain.Veterinarian
import pet.clinic.domain.VeterinarianService

@Controller("/veterinarians")
class VeterinariansController(private val service: VeterinarianService) {

    private val objectMapper = jacksonObjectMapper()

    @Get("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun detail(id: String): String {
        val dto = x(id)
        return when (dto) {
            is None -> ""
            is Some -> objectMapper.writeValueAsString(dto.t)
        }
    }

    private fun x(id: String) = this.service
            .list(Id.from(id))
            .map { mapx(id, it) }

    private fun mapx(id: String, value: Veterinarian): VeterinarianDTO {
        return VeterinarianDTO(id, value.name, map(value.specialties))

    }

    private fun map(specialties: List<Specialty>): List<SpecialtyDTO> {
        return specialties.map { SpecialtyDTO(it.id.value, it.description) }
    }
}