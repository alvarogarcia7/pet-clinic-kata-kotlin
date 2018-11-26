package pet.clinic.domain

data class Owner(val name: Name, val address: Address, val pets: List<Pet>)
