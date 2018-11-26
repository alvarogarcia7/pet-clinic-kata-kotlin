package pet.clinic.infrastructure.delivery

import arrow.core.None
import arrow.core.Some
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import pet.clinic.domain.Id
import pet.clinic.domain.Owner
import pet.clinic.domain.OwnerService
import pet.clinic.domain.Pet

@Controller("/owners")
class OwnersController(private val service: OwnerService) {

    private val objectMapper = jacksonObjectMapper()

    @Get("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun detail(id: String): String {
        val dto = fakeOwner(id)
        return when (dto) {
            is None -> ""
            is Some -> objectMapper.writeValueAsString(dto.t)
        }
    }

    @Get("/")
    @Produces(MediaType.APPLICATION_JSON)
    fun list(): String {
        return objectMapper.writeValueAsString(allOwners())
    }

    private fun allOwners() =
            service.all()
                    .map { aNewOwner(it.id.value, it.value) }

    private fun fakeOwner(id: String) = this.service
            .list(Id.from(id))
            .map { aNewOwner(id, it) }

    private fun aNewOwner(id: String, value: Owner): OwnerDTO {
        return OwnerDTO(id, value.name.first, value.address.fullAddress, value.address.city, value.address.phone, map(value.pets))

    }

    private fun map(specialties: List<Pet>): List<PetDTO> {
        return specialties.map { PetDTO(it.id.value, it.name.first) }
    }
}