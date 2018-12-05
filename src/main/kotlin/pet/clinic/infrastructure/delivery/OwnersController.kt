package pet.clinic.infrastructure.delivery

import arrow.core.None
import arrow.core.Some
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import pet.clinic.domain.common.Id
import pet.clinic.domain.common.Persisted
import pet.clinic.domain.owners.Owner
import pet.clinic.domain.owners.OwnerService
import pet.clinic.domain.pets.Pet

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

    @Post("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun update(id: String): HttpResponse<Void> {
        return HttpResponse.accepted()
    }

    private fun allOwners() =
            service.all()
                    .map(this::aNewOwner)

    private fun fakeOwner(id: String) = this.service
            .list(Id.from(id))
            .map { aNewOwner(it) }

    private fun aNewOwner(it: Persisted<Owner>): OwnerDTO {
        return OwnerDTO(it.id.value, it.value.name.first, it.value.address.fullAddress, it.value.address.city, it.value.address.phone, map(it.value.pets))
    }

    private fun map(specialties: List<Pet>): List<PetDTO> {
        return specialties.map { PetDTO(it.id.value, it.name.first) }
    }
}