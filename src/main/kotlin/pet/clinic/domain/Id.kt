package pet.clinic.domain

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
