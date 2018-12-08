package pet.clinic.domain.specialties

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import pet.clinic.domain.common.Id
import pet.clinic.domain.common.Persisted
import pet.clinic.domain.veterinarians.InMemoryVeterinarianService
import pet.clinic.domain.veterinarians.Specialty
import pet.clinic.domain.veterinarians.Veterinarian
import kotlin.test.assertTrue

object SpecialtiesServiceSpec : Spek({
    describe("Specialties") {
        val morphology = Specialty("morphology")
        val radiology = Specialty("radiology")

        it("Groups the specialties of all veterinarians") {
            val veterinarianService = InMemoryVeterinarianService()
            registerSpecialty(veterinarianService, morphology)
            registerSpecialty(veterinarianService, radiology)
            assertTrue(SpecialtiesService(veterinarianService).all().map { it.value }.containsAll(listOf(morphology, radiology)))
        }
    }
})

private fun registerSpecialty(veterinarianService: InMemoryVeterinarianService, specialty: Specialty) {
    veterinarianService.upsert(Id.random(), Persisted(Id.random(), Veterinarian("john", listOf(Persisted(Id.random(), specialty)))))
}
