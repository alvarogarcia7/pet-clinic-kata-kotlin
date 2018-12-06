package pet.clinic.infrastructure.delivery

import io.micronaut.http.hateos.Resource

data class  ResponseBody<out T>(val body: T, val links: List<Resource>)