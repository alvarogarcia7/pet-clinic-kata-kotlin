package pet.clinic.infrastructure.delivery

data class ChangeOwnerDTO(val name: String,
                          val address: String,
                          val city: String,
                          val phone: String,
                          val pets: List<PetDTO>)
