package pet.clinic.domain

import arrow.core.Option
import javax.inject.Singleton

interface VeterinarianService {
    fun list(id: Id): Option<Veterinarian>
}

@Singleton
class InMemoryVeterinarianService : VeterinarianService {
    override fun list(id: Id): Option<Veterinarian> {
        return Option.just(Veterinarian("John"))
    }
}
