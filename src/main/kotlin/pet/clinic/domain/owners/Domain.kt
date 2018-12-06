package pet.clinic.domain.owners

import pet.clinic.domain.common.Name
import pet.clinic.domain.pets.Pet

data class Owner(val name: Name, val address: Address, val pets: List<Pet>) {
    fun changeWith(changeRequest: Owner): Owner {
        return changeRequest
    }
}

data class Address(val fullAddress: String, val city: String, val phone: String)