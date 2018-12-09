package pet.clinic.test

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import pet.clinic.domain.common.Id
import pet.clinic.domain.common.Persisted
import pet.clinic.domain.specialties.InMemorySpecialtyService
import pet.clinic.domain.veterinarians.Specialty
import kotlin.test.assertTrue

object SpecialtiesServiceSpec : Spek({
    describe("Specialties") {
        val morphology = Specialty("morphology")
        val radiology = Specialty("radiology")
        lateinit var specialtiesService: InMemorySpecialtyService

        beforeEach {
            specialtiesService = InMemorySpecialtyService()
        }

        it("Groups the specialties of all veterinarians") {
            registerSpecialty(specialtiesService, morphology)
            registerSpecialty(specialtiesService, radiology)

            assertTrue(specialtiesService.all().map { it.value }.containsAll(listOf(morphology, radiology)))
        }

        describe("cannot be repeated") {
            it("ignoring case") {
                val radiologyLowercase = Persisted(Id.random(), Specialty("radiology"))
                val radiologyUppercase = Persisted(Id.random(), Specialty("RADIOLOGY"))

                assertTrue(specialtiesService.register(radiologyLowercase).isRight())

                assertTrue(specialtiesService.register(radiologyUppercase).isLeft())
            }
            it("same value") {
                val specialty = Persisted(Id.random(), Specialty("radiology"))

                assertTrue(specialtiesService.register(specialty).isRight())

                assertTrue(specialtiesService.register(specialty).isLeft())
            }
            it("different values") {
                val specialty1 = Persisted(Id.random(), Specialty("radiology"))
                val specialty2 = Persisted(Id.random(), Specialty("morphology"))

                assertTrue(specialtiesService.register(specialty1).isRight())

                assertTrue(specialtiesService.register(specialty2).isRight())
            }
        }
    }
})

fun registerSpecialty(service: InMemorySpecialtyService, specialty: Specialty) {
    service.register(Persisted(Id.random(), specialty))
}

