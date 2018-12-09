package pet.clinic.domain.specialties

import arrow.core.Either
import io.micronaut.context.annotation.Prototype
import pet.clinic.domain.common.Persisted
import pet.clinic.domain.veterinarians.Specialty
import pet.clinic.domain.veterinarians.VeterinarianService

@Prototype
class SpecialtiesService(val veterinarianService: VeterinarianService) {
    private val values: MutableMap<String, Persisted<Specialty>> = mutableMapOf()

    fun all(): List<Persisted<Specialty>> {
        val result = veterinarianService.allVeterinarians().flatMap { it.value.specialties }
        return result
    }

    fun register(specialty: Persisted<Specialty>): Either<Persisted<Specialty>, Persisted<Specialty>> {
        val specialtyId = specialty.value.asId()

        return if(this.values.containsKey(specialtyId)){
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
