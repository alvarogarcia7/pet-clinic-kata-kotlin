package pet.clinic.test

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.left
import com.nhaarman.mockito_kotlin.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import pet.clinic.domain.common.Id
import pet.clinic.domain.common.Persisted
import pet.clinic.domain.specialties.InMemorySpecialtyService
import pet.clinic.domain.specialties.SpecialtyService
import pet.clinic.domain.veterinarians.InMemoryVeterinarianService
import pet.clinic.domain.veterinarians.Specialty
import pet.clinic.domain.veterinarians.Veterinarian
import pet.clinic.domain.veterinarians.VeterinarianService
import kotlin.test.assertEquals
import kotlin.test.fail

object VeterinarianServiceSpec : Spek({
    describe("Veterinarian Service") {
        lateinit var veterinarianService: VeterinarianService
        lateinit var specialtyService: SpecialtyService

        beforeEach {
            specialtyService = spy(InMemorySpecialtyService())
            veterinarianService = InMemoryVeterinarianService(specialtyService)
        }

        it("should upsert the underlying specialties") {
            val id = Id.random()
            val specialty = Persisted(Id.random(), Specialty("radiology"))
            val veterinarian = Persisted(id, Veterinarian("john", listOf(specialty)))

            veterinarianService.upsert(id, veterinarian)

            verify(specialtyService).register(specialty)
        }

        it("should update the reference to the existing specialties") {
            val radiologyId = Id.random()
            val specialtyService = mock<SpecialtyService> {
                on { register(any()) } doReturn Either.left(Persisted(radiologyId, Specialty("RADIOLOGY")))
            }
            val veterinarianService: VeterinarianService = InMemoryVeterinarianService(specialtyService)
            val id = Id.random()
            val specialty = Persisted(Id.random(), Specialty("radiology"))
            val veterinarian = Persisted(id, Veterinarian("john", listOf(specialty)))

            veterinarianService.upsert(id, veterinarian)

            veterinarianService.list(id).map { assertEquals(it.value.specialties.first().id, radiologyId) }.getOrElse { fail("no veterinarian") }
        }
    }
})
