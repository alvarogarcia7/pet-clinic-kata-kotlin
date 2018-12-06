package pet.clinic.infrastructure.delivery

import pet.clinic.domain.common.Id
import pet.clinic.domain.common.Name
import pet.clinic.domain.owners.Address
import pet.clinic.domain.pets.Pet

data class ChangeOwnerDTO(val name: String,
                          val address: String,
                          val city: String,
                          val phone: String,
                          val pets: List<PetDTO>) {
    fun toName(): Name {
        return Name(name)
    }

    fun toAddress(): Address {
        return Address(address, city, phone)
    }

    fun toPets(): List<Pet> {
        return pets.map(this::toPet)
    }

    private fun toPet(dto: PetDTO) : Pet{
        return Pet(Id.from(dto.id), Name(dto.name))
    }
}
