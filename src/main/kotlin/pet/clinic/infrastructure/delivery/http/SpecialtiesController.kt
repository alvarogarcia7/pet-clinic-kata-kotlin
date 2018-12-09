package pet.clinic.infrastructure.delivery.http

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import pet.clinic.domain.specialties.SpecialtiesService
import pet.clinic.infrastructure.delivery.hateoas.MyResource
import pet.clinic.infrastructure.delivery.hateoas.ResponseBody
import pet.clinic.infrastructure.delivery.io.SpecialtyDTO

@Controller("/specialties")
class SpecialtiesController(private val service: SpecialtiesService) {

    @Get("/")
    @Produces
    @Consumes
    fun list(): HttpResponse<ResponseBody<List<ResponseBody<SpecialtyDTO>>>> {
        return HttpResponse.ok(ResponseBody(service.all().map {
            ResponseBody(SpecialtyDTO.from(it.value), listOf(MyResource("self", "/specialties/${it.id.value}")))
        }, listOf(MyResource("self", "/specialties/"))))
    }
}