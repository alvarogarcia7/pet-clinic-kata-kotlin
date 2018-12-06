package pet.clinic.domain.veterinarians

import arrow.core.Option
import arrow.core.getOrElse
import io.micronaut.context.annotation.Prototype
import pet.clinic.domain.common.Id
import pet.clinic.domain.common.Persisted

interface VeterinarianService {
    fun list(id: Id): Option<Persisted<Veterinarian>>
    fun allVeterinarians(): List<Persisted<Veterinarian>>
    fun upsert(id: Id, value: Persisted<Veterinarian>)
}


@Prototype // https://docs.micronaut.io/latest/guide/index.html#scopes
class InMemoryVeterinarianService : VeterinarianService {
    private val values: MutableMap<Id, Persisted<Veterinarian>> = mutableMapOf()

    override fun upsert(id: Id, value: Persisted<Veterinarian>) {
        val veterinarian = Option.fromNullable(values[id]).map { it.value.changeFrom(value) }.getOrElse { value }
        values[id] = veterinarian
    }

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
