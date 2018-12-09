package pet.clinic.domain.veterinarians

import arrow.core.Either
import arrow.core.Option
import arrow.core.getOrElse
import io.micronaut.context.annotation.Prototype
import pet.clinic.domain.common.Id
import pet.clinic.domain.common.Persisted
import pet.clinic.domain.specialties.SpecialtyService
import java.lang.RuntimeException

interface VeterinarianService {
    fun list(id: Id): Option<Persisted<Veterinarian>>
    fun allVeterinarians(): List<Persisted<Veterinarian>>
    fun upsert(id: Id, value: Persisted<Veterinarian>)
}


@Prototype // https://docs.micronaut.io/latest/guide/index.html#scopes
class InMemoryVeterinarianService(private val specialtyService: SpecialtyService) : VeterinarianService {
    private val values: MutableMap<Id, Persisted<Veterinarian>> = mutableMapOf()

    override fun upsert(id: Id, value: Persisted<Veterinarian>) {
        val veterinarian = Option.fromNullable(values[id]).map { it.value.changeFrom(value) }.getOrElse { value }
        val newSpecialties = value.value.specialties.map {
            val spec = specialtyService.register(it)
            when (spec) {
                is Either.Left -> spec.a
                is Either.Right -> spec.b
                else->
                {
                    println(spec)
                    println("${spec.isLeft()}, ${spec.isRight()}")
                    throw RuntimeException(spec.toString())
                }
            }
        }
        val newVeterinarian = value.value.copy(specialties = newSpecialties)
        values[id] = Persisted(veterinarian.id, newVeterinarian)
    }

    override fun allVeterinarians(): List<Persisted<Veterinarian>> {
        return this.values.values.toList()
    }

    override fun list(id: Id): Option<Persisted<Veterinarian>> {
        return Option.fromNullable(this.values[id])
    }
}
