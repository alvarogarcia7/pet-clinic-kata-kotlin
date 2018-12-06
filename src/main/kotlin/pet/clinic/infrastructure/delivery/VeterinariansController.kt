package pet.clinic.infrastructure.delivery

import arrow.core.None
import arrow.core.Some
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Produces
import pet.clinic.domain.common.Id
import pet.clinic.domain.common.Persisted
import pet.clinic.domain.veterinarians.Specialty
import pet.clinic.domain.veterinarians.Veterinarian
import pet.clinic.domain.veterinarians.VeterinarianService

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

    @Post("/{id}")
    @Produces()
    fun upsert(id: String, body: VeterinarianDTO): HttpResponse<Void> {
        val id = Id.from(id)
        service.upsert(id, body.toDomain(id))
        return HttpResponse.accepted()
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