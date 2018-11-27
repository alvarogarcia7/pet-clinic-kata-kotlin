package pet.clinic.domain.veterinarians

import pet.clinic.domain.common.Id

data class Veterinarian(val name: String, val specialties: List<Specialty>)
data class Specialty(val id: Id, val description: String)