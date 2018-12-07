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
            is Some -> HttpResponse.ok(ResponseBody(aNew(dto.t), listOf(MyResource("self", "/veterinarians/${dto.t.id.value}"))))
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
            ResponseBody(aNew(it), listOf(MyResource("self", "/veterinarians/${it.id.value}")))
        }, listOf(MyResource("self", "/veterinarians/"))))
    }

    private fun allVeterinarians(): List<Persisted<Veterinarian>> = service.allVeterinarians()

    private fun aNew(value: Persisted<Veterinarian>): VeterinarianDTO {
        return VeterinarianDTO(value.value.name, map(value.value.specialties))

    }

    private fun map(specialties: List<Specialty>): List<SpecialtyDTO> {
        return specialties.map { SpecialtyDTO(it.id.value, it.description) }
    }
}