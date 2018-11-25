package pet.clinic.domain

import arrow.core.Option
import io.micronaut.context.annotation.Prototype

interface VeterinarianService {
    fun list(id: Id): Option<Veterinarian>
}


@Prototype // https://docs.micronaut.io/latest/guide/index.html#scopes
class InMemoryVeterinarianService : VeterinarianService {
    override fun list(id: Id): Option<Veterinarian> {
        return Option.just(Veterinarian("John"))
    }
}
