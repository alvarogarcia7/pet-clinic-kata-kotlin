package pet.clinic.infrastructure.delivery.http

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
import pet.clinic.infrastructure.delivery.hateoas.MyResource
import pet.clinic.infrastructure.delivery.hateoas.ResponseBody
import pet.clinic.infrastructure.delivery.io.SpecialtyDTO
import pet.clinic.infrastructure.delivery.io.VeterinarianDTO
import java.net.URI

@Controller("/veterinarians")
class VeterinariansController(private val service: VeterinarianService) {

    private val objectMapper = jacksonObjectMapper()

    @Get("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun detail(id: String): HttpResponse<ResponseBody<Any>> {
        val dto = service.list(Id.from(id))
        return when (dto) {
            is None -> HttpResponse.notFound()
            is Some -> HttpResponse.ok(ResponseBody(aNew(dto.t.value), listOf(MyResource("self", "/veterinarians/${dto.t.id.value}"))))
        }
    }

    @Post("/")
    @Produces()
    fun upsert(body: VeterinarianDTO): HttpResponse<Void> {
        val id = Id.random()
        service.upsert(id, body.toDomain(id))
        return HttpResponse.accepted(URI.create("/veterinarians/${id.value}"))
    }

    @Get("/")
    @Produces(MediaType.APPLICATION_JSON)
    fun list(): HttpResponse<ResponseBody<List<ResponseBody<VeterinarianDTO>>>> {
        return HttpResponse.ok(ResponseBody(allVeterinarians().map {
            ResponseBody(aNew(it.value), listOf(MyResource("self", "/veterinarians/${it.id.value}")))
        }, listOf(MyResource("self", "/veterinarians/"))))
    }

    private fun allVeterinarians(): List<Persisted<Veterinarian>> = service.allVeterinarians()

    private fun aNew(value: Veterinarian): VeterinarianDTO {
        return VeterinarianDTO(value.name, map(value.specialties))
    }

    private fun map(specialties: List<Persisted<Specialty>>): List<Persisted<SpecialtyDTO>> {
        return specialties.map { Persisted(it.id, SpecialtyDTO(it.value.description)) }
    }
}