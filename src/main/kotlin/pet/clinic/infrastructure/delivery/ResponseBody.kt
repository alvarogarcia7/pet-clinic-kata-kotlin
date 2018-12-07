package pet.clinic.infrastructure.delivery

data class ResponseBody<out T> (val body: T, val links: List<MyResource>)