package pet.clinic.domain

data class Persisted<out T>(val id: Id, val value: T)
