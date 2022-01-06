package bdd;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.*;

import static org.junit.Assert.*;

public class PetServiceFeatureSteps {
	@Autowired
	PetService petService;

	@Autowired
	OwnerRepository ownerRepository;

	@Autowired
	PetRepository petRepository;

	@Autowired
	PetTypeRepository petTypeRepository;

	private Owner myOwner;
	private Pet myPet;
	private Pet myNewPet;
	private Owner foundOwner;
	private PetType petType;

	@Given("There exists an owner with id equal to {int}")
	public void thereIsOwnerWithId(int id) {
		assertNotNull(ownerRepository.findById(id));
	}

	@When("User wants to find the owner with id equal to {int}")
	public void findAccountWithId(int id) {
		foundOwner = petService.findOwner(id);
	}

	@Then("The owner with id equal to {} will return")
	public void accountIsFound(int id) {
		assertNotNull(foundOwner);
		assertEquals(java.util.Optional.of(id), foundOwner.getId());
	}

	@Given("There exists no owner with id equal to {int}")
	public void thereIsNoOwnerWithId(int id) {
		assertNull(ownerRepository.findById(id));
	}

	@Then("NULL owner will return")
	public void noOwnerIsFound() {
		assertNull(foundOwner);
	}


	@Given("There exists a pet with id equal to {int}")
	public void thereIsAPetWithId(int id) {
		assertNotNull(petRepository.findById(id));
	}

	@Given("There exists no pet with id equal to {int}")
	public void thereIsNoPetWithId(int id) {
		assertNull(petRepository.findById(id));
	}

	@When("User wants to find the pet with id equal to {int}")
	public void findPetWithId(int id) {
		myPet = petService.findPet(id);
	}

	@Then("The pet with id equal to {} will return")
	public void petIdReturned(int id) {
		assertNotNull(myPet);
		assertEquals(java.util.Optional.of(id), myPet.getId());
	}

	@Then("NULL pet will return")
	public void noPetIsFound() {
		assertNull(myPet);
	}

	@Given("There exists an owner named {string}")
	public void myOwner(String ownerName) {
		myOwner = new Owner();
		myOwner.setFirstName(ownerName);
		ownerRepository.save(myOwner);
	}

	@When("He wants a new pet")
	public void myOwnerAsksFormyNewPet() {
		myNewPet = petService.newPet(myOwner);
	}

	@When("He adds a pet to his list")
	public void myOwnerPerformsSavePetService() {
		myNewPet = new Pet();
		myNewPet.setType(petType);
		petService.savePet(myNewPet, myOwner);
	}

	@Then("The pet is saved successfully")
	public void thePetIsSaved() {
		assertNotNull(petService.findPet(myNewPet.getId()));
	}
}
