package org.springframework.samples.petclinic.owner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.samples.petclinic.visit.Visit;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.utility.PetTimedCache;
import java.time.LocalDate;
import java.util.*;
import org.slf4j.Logger;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PetManagerTest {
	@Mock
	OwnerRepository ownerRepository;
	@Mock
	PetTimedCache petTimedCache;
	@Mock
	Logger logger;
	private PetManager petManager;

	/*
	 * Dummy & Mock Object; Mockist; State;
	 */

	@Test
	public void returns_owner_by_its_id() {
		petManager = new PetManager(petTimedCache, ownerRepository, logger);
		List<Owner> owners = new ArrayList<>();
		for (Integer i = 0; i < 5; i++) {
			Owner owner = new Owner();
			owner.setId(i);
			owners.add(owner);
			when(ownerRepository.findById(i)).thenReturn(owner);
		}

		for (Integer i = 0; i < 5; i++)
			assertEquals(owners.get(i), this.petManager.findOwner(i));
	}

	/*
	 * Dummy Object; Mockist; State;
	 */

	@Test
	public void Adds_pet_for_specified_owner() {
		this.petManager = new PetManager(petTimedCache, ownerRepository, logger);
		for (Integer i = 0; i < 5; i++) {
			Owner owner = new Owner();
			owner.setId(i);
			assertEquals(owner, petManager.newPet(owner).getOwner());
		}
	}

	/*
	 * Dummy & Mock Object; Mockist; State;
	 */

	@Test
	public void returns_pet_by_its_id () {
		petManager = new PetManager(petTimedCache, ownerRepository, logger);
		List<Pet> pets = new ArrayList<>();

		for (Integer i = 0; i < 5; i++) {
			Pet pet = new Pet();
			pet.setId(i);
			pets.add(pet);
			when(petTimedCache.get(i)).thenReturn(pet);
		}

		for (Integer i = 0; i < 5; i++)
			assertEquals(pets.get(i), petManager.findPet(i));
	}

	/*
	 * Dummy Object & Test Spy; Mockist; Behavior;
	 */

	@Test
	public void get_owner_id_by_finding_owner_and_log_it() {
		Logger spyLogger = Mockito.spy(logger);
		this.petManager = new PetManager(petTimedCache, ownerRepository, spyLogger);
		for (Integer i = 0; i < 5; i++) {
			Owner owner = new Owner();
			owner.setId(i);
			petManager.findOwner(i);
			Mockito.verify(spyLogger).info("find owner {}", owner.getId());
		}
	}

	/*
	 * Dummy Object & Test Spy; Mockist; Behavior;
	 */

	@Test
	public void get_owner_by_adding_pet_and_log_it() {
		Logger spyLogger = Mockito.spy(logger);
		this.petManager = new PetManager(petTimedCache, ownerRepository, spyLogger);
		for (Integer i = 0; i < 5; i++) {
			Owner owner = new Owner();
			owner.setId(i);
			petManager.newPet(owner);
			Mockito.verify(spyLogger).info("add pet for owner {}", owner.getId());
		}
	}

	/*
	 * Dummy Object & Test Spy ; Mockist; Behavior;
	 */

	@Test
	public void get_pet_id_by_finding_pet_and_log_it() {
		PetTimedCache spyPetTimedCache = Mockito.spy(petTimedCache);
		Logger spyLogger = Mockito.spy(logger);
		this.petManager = new PetManager(spyPetTimedCache, ownerRepository, spyLogger);
		for (Integer i = 0; i < 5; i++) {
			Pet pet = new Pet();
			pet.setId(i);
			petManager.findPet(i);
			Mockito.verify(spyLogger).info("find pet by id {}", pet.getId());
			Mockito.verify(spyPetTimedCache, times(1)).get(i);
		}
	}

	/*
	 * Dummy Object & Test Spy ; Mockist; State;
	 */

	@Test
	public void returns_owner_pets_by_it_id() {
		this.petManager = new PetManager(petTimedCache, ownerRepository, logger);
		Owner owner = new Owner();
		owner.setId(0);
		Set<Pet> pets = new HashSet<>();

		for (Integer i = 0; i < 5; i++) {
			Pet pet = new Pet();
			pet.setId(i);
			pets.add(pet);
		}
		when(ownerRepository.findById(anyInt())).thenReturn(owner);
		owner.setPetsInternal(pets);
		List<Pet> petsArray = new ArrayList<>(pets);
		assertEquals(petsArray, petManager.getOwnerPets(owner.getId()));
	}

	/*
	 * Dummy Object & Test Spy & Test Stub; Mockist; Behavior;
	 */

	@Test
	public void get_owner_id_by_getting_its_pets_and_log_it() {
		Logger spyLogger = Mockito.spy(logger);
		this.petManager = new PetManager(petTimedCache, ownerRepository, spyLogger);

		Owner owner = new Owner();
		owner.setId(0);
		Set<Pet> pets = new HashSet<>();

		for (Integer i = 0; i < 5; i++) {
			Pet pet = new Pet();
			pet.setId(i);
			pets.add(pet);
		}
		when(ownerRepository.findById(anyInt())).thenReturn(owner);
		owner.setPetsInternal(pets);
		Owner spyOwner = Mockito.spy(owner);
		when(ownerRepository.findById(anyInt())).thenReturn(spyOwner);
		petManager.getOwnerPets(spyOwner.getId());
		Mockito.verify(spyLogger).info("finding the owner's pets by id {}", owner.getId());
		Mockito.verify(spyOwner, times(1)).getPets();
	}


	/*
	 * Dummy Object & Test Spy ; Mockist; Behavior;
	 */

	@Test
	public void get_owner_by_saving_pet_and_log_it() {
		PetTimedCache spyPetTimedCache = Mockito.spy(petTimedCache);
		Logger spyLogger = Mockito.spy(logger);
		this.petManager = new PetManager(spyPetTimedCache, ownerRepository, spyLogger);
		for (Integer i = 0; i < 5; i++) {
			Owner owner = new Owner();
			owner.setId(i);
			Pet pet = new Pet();
			pet.setId(i);
			petManager.savePet(pet, owner);
			Mockito.verify(spyPetTimedCache, times(1)).save(pet);
			Mockito.verify(spyLogger).info("save pet {}", pet.getId());
		}
	}

	/*
	 * Dummy Object & Test Stub; Mockist; State;
	 */

	@Test
	public void returns_pet_types_by_id() {
		this.petManager = new PetManager(petTimedCache, ownerRepository, logger);
		Set<Pet> pets = new HashSet<>();
		Set<PetType> petsTypes = new HashSet<>();

		Owner owner = new Owner();
		owner.setId(0);

		for (Integer i = 0; i < 5; i++) {
			PetType petType = new PetType();
			petType.setName("Pet name!" + i);
			Pet pet = new Pet();
			pet.setId(i);
			pet.setType(petType);
			pets.add(pet);
			petsTypes.add(petType);
			when(ownerRepository.findById(i)).thenReturn(owner);
		}
		owner.setPetsInternal(pets);

		for (Pet pet : pets)
			assertEquals(petsTypes, petManager.getOwnerPetTypes(pet.getId()));
	}

	/*
	 * Dummy Object & Test Stub & Test Spy; Mockist; Behavior;
	 */

	@Test
	public void get_pet_id_by_dates_and_log_it() {
		Logger spyLogger = Mockito.spy(logger);
		this.petManager = new PetManager(petTimedCache, ownerRepository, spyLogger);

		Pet pet = new Pet();
		pet.setId(0);
		List<Visit> visits = new ArrayList<>();
		for (Integer i = 0; i < 5; i++) {
			Visit visit = new Visit().setDate(LocalDate.of(1990 - i, 1 + i, 1 + 2 * i));
			visits.add(visit);
		}
		Pet spyPet = Mockito.spy(pet);
		when(petTimedCache.get(anyInt())).thenReturn(spyPet);
		pet.setVisitsInternal(visits);

		for (Integer i = 0; i < 5; i++) {
			for (Integer j = 0; j < 5; j++) {
				petManager.getVisitsBetween(spyPet.getId(), visits.get(i).getDate(), visits.get(j).getDate());
			}
		}

		for (Integer i = 0; i < 5; i++) {
			for (Integer j = 0; j < 5; j++) {

				Mockito.verify(spyLogger)
					.info(
						"get visits for pet {} from {} since {}",
						spyPet.getId(),
						visits.get(i).getDate(),
						visits.get(j).getDate()
					);
				Mockito.verify(spyPet, times(1))
					.getVisitsBetween(visits.get(i).getDate(), visits.get(j).getDate());
			}
		}
	}


	/*
	 * Dummy Object & Test Stub & Test Spy; Mockist; Behavior;
	 */

	@Test
	public void get_owner_id_by_getting_owner_pet_types_and_log_it() {
		Logger spyLogger = Mockito.spy(logger);
		this.petManager = new PetManager(petTimedCache, ownerRepository, spyLogger);

		Owner owner = new Owner();
		owner.setId(0);
		Set<Pet> pets = new HashSet<>();

		for (Integer i = 0; i < 5; i++) {
			Pet pet = new Pet();
			pet.setId(i);
			pets.add(pet);
			PetType petType = new PetType();
			petType.setName("Pet name!" + i);
			pet.setType(petType);
		}
		when(ownerRepository.findById(anyInt())).thenReturn(owner);
		owner.setPetsInternal(pets);
		Owner spyOwner = Mockito.spy(owner);
		when(ownerRepository.findById(anyInt())).thenReturn(spyOwner);
		petManager.getOwnerPetTypes(spyOwner.getId());
		Mockito.verify(spyLogger).info("finding the owner's petTypes by id {}", owner.getId());
		Mockito.verify(spyOwner, times(1)).getPets();
	}

	/*
	 * Dummy Object & Test Stub; Mockist; State;
	 */

	@Test
	public void returns_visits_by_dates_and_pet_id() {
		this.petManager = new PetManager(petTimedCache, ownerRepository, logger);

		Pet pet = new Pet();
		pet.setId(0);
		List<Visit> visits = new ArrayList<>();
		for (Integer i = 0; i < 5; i++) {
			Visit visit = new Visit().setDate(LocalDate.of(1990 - i, 1 + i, 1 + 2 * i));
			visits.add(visit);
		}
		when(petTimedCache.get(anyInt())).thenReturn(pet);
		pet.setVisitsInternal(visits);

		for (Integer i = 0; i < 5; i++) {
			for (Integer j = 0; j < 5; j++) {
				assertEquals(
					pet.getVisitsBetween(visits.get(i).getDate(), visits.get(j).getDate()),
					petManager.getVisitsBetween(pet.getId(), visits.get(i).getDate(), visits.get(j).getDate())
				);
			}
		}
	}

	/*
	 * Dummy Object & Test Stub & Test Spy; Mockist; Behavior;
	 */

	@Test
	public void get_pet_dates_by_getting_visits_and_log_it() {
		Logger spyLogger = Mockito.spy(logger);
		this.petManager = new PetManager(petTimedCache, ownerRepository, spyLogger);

		Pet pet = new Pet();
		pet.setId(0);
		List<Visit> visits = new ArrayList<>();
		for (Integer i = 0; i < 5; i++) {
			Visit visit = new Visit().setDate(LocalDate.of(1990 - i, 1 + i, 1 + 2 * i));
			visits.add(visit);
		}
		Pet spyPet = Mockito.spy(pet);
		when(petTimedCache.get(anyInt())).thenReturn(spyPet);
		pet.setVisitsInternal(visits);

		for (Integer i = 0; i < 5; i++) {
			for (Integer j = 0; j < 5; j++) {
				petManager.getVisitsBetween(spyPet.getId(), visits.get(i).getDate(), visits.get(j).getDate());
			}
		}

		for (Integer i = 0; i < 5; i++) {
			for (Integer j = 0; j < 5; j++) {

				Mockito.verify(spyLogger)
					.info(
						"get visits for pet {} from {} since {}",
						spyPet.getId(),
						visits.get(i).getDate(),
						visits.get(j).getDate()
					);
				Mockito.verify(spyPet, times(1))
					.getVisitsBetween(visits.get(i).getDate(), visits.get(j).getDate());
			}
		}
	}
}
