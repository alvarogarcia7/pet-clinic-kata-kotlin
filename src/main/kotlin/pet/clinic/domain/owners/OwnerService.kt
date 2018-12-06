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
    fun update(id: Id, changeRequest: Owner)
}


@Prototype // https://docs.micronaut.io/latest/guide/index.html#scopes
class InMemoryOwnerService : OwnerService {
    private val lucky: Pet = Pet(Id.from("1"), Name("Lucky"))
    private val JOHN = Owner(Name("John"), Address("1450 Oak Blvd", "Morona", "608555387"), listOf(lucky))
    private val Harry = Owner(Name("Harry"), Address("1451 Oak Blvd", "Morona", "608555388"), listOf(lucky, Pet(Id.from("2"), Name("Agatha"))))
    private val values: MutableMap<Id, Persisted<Owner>> = mutableMapOf(Id.from("1") to Persisted(Id.from("1"), JOHN), Id.from("2") to Persisted(Id.from("2"), Harry))

    override fun update(id: Id, changeRequest: Owner) {
        val newOwner = changeRequest.changeWith(changeRequest)
        this.values[id] = Persisted(id, newOwner)
    }

    override fun list(id: Id): Option<Persisted<Owner>> {
        return Option.fromNullable(values[id])
    }

    override fun all(): List<Persisted<Owner>> {
        return values.values.toList()
    }
}
