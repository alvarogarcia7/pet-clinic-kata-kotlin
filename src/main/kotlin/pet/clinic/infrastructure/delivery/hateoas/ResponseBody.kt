package pet.clinic.infrastructure.delivery.hateoas

data class ResponseBody<out T> (val body: T, val links: List<MyResource>)