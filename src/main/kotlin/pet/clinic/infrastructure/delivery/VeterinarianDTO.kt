package pet.clinic.infrastructure.delivery

import pet.clinic.domain.common.Id
import pet.clinic.domain.common.Persisted
import pet.clinic.domain.veterinarians.Specialty
import pet.clinic.domain.veterinarians.Veterinarian

data class VeterinarianDTO(val name: String, val specialties: List<Persisted<SpecialtyDTO>>) {
    fun toDomain(id: Id): pet.clinic.domain.common.Persisted<pet.clinic.domain.veterinarians.Veterinarian> {
        return Persisted(id, Veterinarian(this.name,this.specialties.map(this::toDomain)))
    }
    fun toDomain(specialtyDTO: Persisted<SpecialtyDTO>): Persisted<Specialty> {
        return Persisted(specialtyDTO.id, Specialty(specialtyDTO.value.name))
    }
}