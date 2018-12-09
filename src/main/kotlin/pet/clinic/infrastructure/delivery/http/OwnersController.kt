package pet.clinic.infrastructure.delivery.http

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
import pet.clinic.infrastructure.delivery.hateoas.MyResource
import pet.clinic.infrastructure.delivery.hateoas.ResponseBody
import pet.clinic.infrastructure.delivery.io.ChangeOwnerDTO
import pet.clinic.infrastructure.delivery.io.OwnerDTO
import pet.clinic.infrastructure.delivery.io.PetDTO
import java.net.URI

@Controller("/owners")
class OwnersController(private val service: OwnerService) {

    private val objectMapper = jacksonObjectMapper()

    @Get("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun detail(id: String): HttpResponse<ResponseBody<Any>> {
        val dto = service.list(Id.from(id))
        return when (dto) {
            is None -> HttpResponse.notFound()
            is Some -> HttpResponse.ok(ResponseBody(aNewOwner(dto.t).value, listOf(MyResource("self", "/owners/${dto.t.id.value}"))))
        }
    }

    @Get("/")
    @Produces(MediaType.APPLICATION_JSON)
    fun list(): HttpResponse<ResponseBody<List<ResponseBody<OwnerDTO>>>> {
        return HttpResponse.ok(ResponseBody(allOwners().map {
            ResponseBody(aNewOwner(it).value, listOf(MyResource("self", "/owners/${it.id.value}")))
        }, listOf(MyResource("self", "/owners/"))))
    }

    @Post("/")
    @Consumes
    @Produces
    fun upsert(body: OwnerDTO): HttpResponse<Void> {
        val id = Id.random()
        service.upsert(id, body.toDomain())
        return HttpResponse.accepted(URI.create("/owners/${id.value}"))
    }

    @Put("/{id}")
    @Consumes
    @Produces
    fun update(id: String, body: ChangeOwnerDTO): HttpResponse<Void> {
        val id = Id.from(id)
        service.upsert(id, body.toDomain())
        return HttpResponse.accepted(URI.create("/owners/${id.value}"))
    }

    private fun allOwners() = service.all()

    private fun aNewOwner(it: Persisted<Owner>): Persisted<OwnerDTO> {
        return Persisted(it.id, OwnerDTO(it.value.name.first, it.value.address.fullAddress, it.value.address.city, it.value.address.phone, map(it.value.pets)))
    }

    private fun map(specialties: List<Pet>): List<PetDTO> {
        return specialties.map { PetDTO(it.id.value, it.name.first) }
    }
}