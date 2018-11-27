package pet.clinic.infrastructure.delivery

import arrow.core.None
import arrow.core.Some
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import pet.clinic.domain.*

@Controller("/veterinarians")
class VeterinariansController(private val service: VeterinarianService) {

    private val objectMapper = jacksonObjectMapper()

    @Get("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun detail(id: String): String {
        val dto = fakeVeterinarian(id)
        return when (dto) {
            is None -> ""
            is Some -> objectMapper.writeValueAsString(dto.t)
        }
    }

    @Get("/")
    @Produces(MediaType.APPLICATION_JSON)
    fun list(): String {
        return objectMapper.writeValueAsString(allVeterinarians())
    }

    private fun allVeterinarians(): List<VeterinarianDTO> =
            service.allVeterinarians()
                    .map(this::aNewVeterinarian)

    private fun fakeVeterinarian(id: String) = this.service
            .list(Id.from(id))
            .map { aNewVeterinarian(it) }

    private fun aNewVeterinarian(value: Persisted<Veterinarian>): VeterinarianDTO {
        return VeterinarianDTO(value.id.value, value.value.name, map(value.value.specialties))

    }

    private fun map(specialties: List<Specialty>): List<SpecialtyDTO> {
        return specialties.map { SpecialtyDTO(it.id.value, it.description) }
    }
}