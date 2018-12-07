package pet.clinic.infrastructure.delivery

import io.micronaut.http.hateos.AbstractResource

data class MyResource(val name: String, val url: String) : AbstractResource<MyResource>()
