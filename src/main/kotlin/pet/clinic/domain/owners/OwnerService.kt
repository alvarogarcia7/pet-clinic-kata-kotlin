package pet.clinic.domain.owners

import arrow.core.Option
import io.micronaut.context.annotation.Prototype
import pet.clinic.domain.common.Id
import pet.clinic.domain.common.Persisted

interface OwnerService {
    fun list(id: Id): Option<Persisted<Owner>>
    fun all(): List<Persisted<Owner>>
    fun upsert(id: Id, changeRequest: Owner)
}


@Prototype // https://docs.micronaut.io/latest/guide/index.html#scopes
class InMemoryOwnerService : OwnerService {
    private val values: MutableMap<Id, Persisted<Owner>> = mutableMapOf()

    override fun upsert(id: Id, changeRequest: Owner) {
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
