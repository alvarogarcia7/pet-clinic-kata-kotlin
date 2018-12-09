package pet.clinic.infrastructure.delivery.hateoas

import io.micronaut.http.hateos.AbstractResource

data class MyResource(val name: String, val url: String) : AbstractResource<MyResource>()
