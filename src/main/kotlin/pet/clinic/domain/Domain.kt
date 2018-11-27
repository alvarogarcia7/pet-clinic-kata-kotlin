package pet.clinic.domain

data class Name(val first: String)
data class Owner(val name: Name, val address: Address, val pets: List<Pet>)
data class Pet(val id: Id, val name: Name)
data class Specialty(val id: Id, val description: String)
data class Veterinarian(val name: String, val specialties: List<Specialty>)
class Address(val fullAddress: String, val city: String, val phone: String)