package pet.clinic.infrastructure.delivery

import pet.clinic.domain.veterinarians.Specialty

data class SpecialtyDTO(val name: String){
    companion object {
        fun from(domain: Specialty): SpecialtyDTO {
            return SpecialtyDTO(domain.description)
        }
    }
}
