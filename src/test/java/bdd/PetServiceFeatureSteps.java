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

	private Owner myOwner;
	private Pet myPet;

	@Given("There exists an owner with id equal to {int}")
	public void ThereExistsAnOwnerWithId(int id) {
		assertNotNull(ownerRepository.findById(id));
	}

	@When("User wants to find the owner with id equal to {int}")
	public void UserWantsToFindTheOwnerWithIdEqualTo(int id) {
		myOwner = petService.findOwner(id);
	}

	@Then("The owner with id equal to {} will return")
	public void TheOwnerWithIdEqualToWillReturn(int id) {
		assertNotNull(myOwner);
		assertEquals(java.util.Optional.of(id), myOwner.getId());
	}

	@Given("There exists no owner with id equal to {int}")
	public void ThereExistsNoOwnerWithIdEqualTo(int id) {
		assertNull(ownerRepository.findById(id));
	}

	@Then("NULL owner will return")
	public void NULLOwnerWillReturn() {
		assertNull(myOwner);
	}


	@Given("There exists a pet with id equal to {int}")
	public void ThereExistsAPetWithIdEqualTo(int id) {
		assertNotNull(petRepository.findById(id));
	}

	@Given("There exists no pet with id equal to {int}")
	public void ThereExistsNoPetWithIdEqualTo(int id) {
		assertNull(petRepository.findById(id));
	}

	@When("User wants to find the pet with id equal to {int}")
	public void UserWantsToFindThePetWithIdEqualTo(int id) {
		myPet = petService.findPet(id);
	}

	@Then("The pet with id equal to {} will return")
	public void ThePetWithIdEqualToWillReturn(int id) {
		assertNotNull(myPet);
		assertEquals(java.util.Optional.of(id), myPet.getId());
	}

	@Then("NULL pet will return")
	public void NULLPetWillReturn() {
		assertNull(myPet);
	}

	@Given("There exists an owner named {string}")
	public void ThereExistsAnOwnerNamed(String ownerName) {
		myOwner = new Owner();
		myOwner.setFirstName(ownerName);
		ownerRepository.save(myOwner);
	}

	@When("He wants a new pet")
	public void HeWantsANewPet() {
		myPet = petService.newPet(myOwner);
	}

	@When("He adds a pet to his list")
	public void HeAddsAPetToHisList() {
		myPet = new Pet();
		petService.savePet(myPet, myOwner);
	}

	@Then("The pet is saved successfully")
	public void ThePetIsSavedSuccessfully() {
		assertNotNull(petService.findPet(myPet.getId()));
	}
}
