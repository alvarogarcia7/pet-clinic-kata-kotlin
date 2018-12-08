package pet.clinic.domain.specialties

import io.micronaut.context.annotation.Prototype
import pet.clinic.domain.common.Persisted
import pet.clinic.domain.veterinarians.Specialty
import pet.clinic.domain.veterinarians.VeterinarianService

@Prototype
class SpecialtiesService(val veterinarianService: VeterinarianService) {
    fun all(): List<Persisted<Specialty>> {
        val result = veterinarianService.allVeterinarians().flatMap { it.value.specialties }
        return result
    }
}
