package pet.clinic.domain.specialties

import arrow.core.Either
import io.micronaut.context.annotation.Context
import pet.clinic.domain.common.Persisted
import pet.clinic.domain.veterinarians.Specialty

interface SpecialtyService {
    fun all(): List<Persisted<Specialty>>
    fun register(specialty: Persisted<Specialty>): Either<Persisted<Specialty>, Persisted<Specialty>>
}

@Context
open class InMemorySpecialtyService : SpecialtyService {
    private val values: MutableMap<String, Persisted<Specialty>> = mutableMapOf()

    override fun all(): List<Persisted<Specialty>> {
        return values.values.toList()
    }

    override fun register(specialty: Persisted<Specialty>): Either<Persisted<Specialty>, Persisted<Specialty>> {
        val specialtyId = specialty.value.asId()

        return if (this.values.contains(specialtyId)) {
            Either.left(this.values[specialtyId]!!)
        } else {
            this.values[specialtyId] = specialty
            Either.right(specialty)
        }
    }

    private fun Specialty.asId(): String {
        return this.description.toLowerCase()
    }
}
