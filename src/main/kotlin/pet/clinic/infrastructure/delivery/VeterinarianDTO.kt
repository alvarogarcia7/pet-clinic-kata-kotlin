package pet.clinic.infrastructure.delivery

import pet.clinic.domain.common.Id
import pet.clinic.domain.common.Persisted
import pet.clinic.domain.veterinarians.Specialty
import pet.clinic.domain.veterinarians.Veterinarian

data class VeterinarianDTO(val name: String, val listOf: List<SpecialtyDTO>) {
    fun toDomain(id: Id): pet.clinic.domain.common.Persisted<pet.clinic.domain.veterinarians.Veterinarian> {
        return Persisted(id, Veterinarian(this.name,this.listOf.map(this::toDomain)))
    }
    fun toDomain(specialtyDTO: SpecialtyDTO): Specialty{
        return Specialty(Id.from(specialtyDTO.id), specialtyDTO.name)
    }
}