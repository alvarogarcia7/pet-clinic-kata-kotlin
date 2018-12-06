package pet.clinic

import io.micronaut.runtime.Micronaut

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("pet.clinic.infrastructure")
                .mainClass(Application.javaClass)
                .start()
    }
}