package pet.clinic.domain.owners

import arrow.core.Option
import io.micronaut.context.annotation.Prototype
import pet.clinic.domain.common.Id
import pet.clinic.domain.common.Name
import pet.clinic.domain.common.Persisted
import pet.clinic.domain.pets.Pet

interface OwnerService {
    fun list(id: Id): Option<Persisted<Owner>>
    fun all(): List<Persisted<Owner>>
}


@Prototype // https://docs.micronaut.io/latest/guide/index.html#scopes
class InMemoryOwnerService : OwnerService {
    override fun list(id: Id): Option<Persisted<Owner>> {
        return Option.just(Persisted(id, JOHN))
    }

    override fun all(): List<Persisted<Owner>> {
        return listOf(Persisted(Id.from("1"), JOHN), Persisted(Id.from("2"), Harry))
    }

    private val lucky: Pet = Pet(Id.from("1"), Name("Lucky"))
    private val JOHN = Owner(Name("John"), Address("1450 Oak Blvd", "Morona", "608555387"), listOf(lucky))
    private val Harry = Owner(Name("Harry"), Address("1451 Oak Blvd", "Morona", "608555388"), listOf(lucky, Pet(Id.from("2"), Name("Agatha"))))

}
