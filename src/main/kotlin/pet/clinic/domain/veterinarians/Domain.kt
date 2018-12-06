package pet.clinic.domain.veterinarians

import pet.clinic.domain.common.Id
import pet.clinic.domain.common.Persisted

data class Veterinarian(val name: String, val specialties: List<Specialty>) {
    fun changeFrom(value: Persisted<Veterinarian>): Persisted<Veterinarian> {
return value
    }
}

data class Specialty(val id: Id, val description: String)