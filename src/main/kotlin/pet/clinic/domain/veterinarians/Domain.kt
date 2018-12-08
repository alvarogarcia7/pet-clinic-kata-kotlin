package pet.clinic.domain.veterinarians

import pet.clinic.domain.common.Persisted

data class Veterinarian(val name: String, val specialties: List<Persisted<Specialty>>) {
    fun changeFrom(value: Persisted<Veterinarian>): Persisted<Veterinarian> {
        return value
    }
}

data class Specialty(val description: String)