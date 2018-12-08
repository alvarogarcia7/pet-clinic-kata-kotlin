package pet.clinic.domain.veterinarians

import arrow.core.Option
import arrow.core.getOrElse
import io.micronaut.context.annotation.Context
import io.micronaut.context.annotation.Prototype
import pet.clinic.domain.common.Id
import pet.clinic.domain.common.Persisted
import javax.inject.Singleton

interface VeterinarianService {
    fun list(id: Id): Option<Persisted<Veterinarian>>
    fun allVeterinarians(): List<Persisted<Veterinarian>>
    fun upsert(id: Id, value: Persisted<Veterinarian>)
}


@Context // https://docs.micronaut.io/latest/guide/index.html#scopes
class InMemoryVeterinarianService : VeterinarianService {
    private val values: MutableMap<Id, Persisted<Veterinarian>> = mutableMapOf()

    override fun upsert(id: Id, value: Persisted<Veterinarian>) {
        val veterinarian = Option.fromNullable(values[id]).map { it.value.changeFrom(value) }.getOrElse { value }
        values[id] = veterinarian
    }

    override fun allVeterinarians(): List<Persisted<Veterinarian>> {
        return this.values.values.toList()
    }

    override fun list(id: Id): Option<Persisted<Veterinarian>> {
        return Option.fromNullable(this.values[id])
    }
}
