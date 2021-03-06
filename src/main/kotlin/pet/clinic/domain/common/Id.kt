package pet.clinic.domain.common

import java.util.*

data class Id private constructor(val value: String) {
    companion object {
        fun random(): Id {
            return Id(UUID.randomUUID().toString())
        }

        fun from(value: String): Id {
            return Id(value)
        }
    }
}

data class Persisted<out T>(val id: Id, val value: T)