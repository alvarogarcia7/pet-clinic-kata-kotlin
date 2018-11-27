package pet.clinic.domain

import arrow.core.Option
import io.micronaut.context.annotation.Prototype

interface VeterinarianService {
    fun list(id: Id): Option<Persisted<Veterinarian>>
    fun allVeterinarians(): List<Persisted<Veterinarian>>
}


@Prototype // https://docs.micronaut.io/latest/guide/index.html#scopes
class InMemoryVeterinarianService : VeterinarianService {

    override fun allVeterinarians(): List<Persisted<Veterinarian>> {
        return listOf(Persisted(Id.from("1"), JOHN), Persisted(Id.from("2"), PAUL))
    }

    val COMMON_SPECIALTIES = listOf(Specialty(Id.from("1"), "radiology"))


    private val JOHN = Veterinarian("John", COMMON_SPECIALTIES)
    private val PAUL = Veterinarian("Paul", COMMON_SPECIALTIES)

    override fun list(id: Id): Option<Persisted<Veterinarian>> {
        return Option.just(Persisted(id, JOHN))
    }
}
