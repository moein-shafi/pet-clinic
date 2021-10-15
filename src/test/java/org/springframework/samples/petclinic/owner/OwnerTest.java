package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.*;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class OwnerTest {
	Owner owner;
	// Set up - Called before every test method.
	@BeforeEach
	public void setUp()
	{
		owner = new Owner();
		owner.setFirstName("George");
		owner.setLastName("Franklin");
		owner.setAddress("110 W. Liberty St.");
		owner.setCity("Madison");
		owner.setTelephone("6085551023");
	}

	// Tear down - Called after every test method.
	@AfterEach
	public void tearDown()
	{
		owner = null;
	}

	@Test
	void adding_one_pet_should_add_pet_to_its_owner()
	{
		Pet mypet = new Pet();
	    mypet.setOwner(owner);
	    mypet.setName("petti");
	    owner.addPet(mypet);
		assertEquals(mypet, owner.getPet("petti"));
	}

	@Test
	void getting_pets_should_return_pet_list_alphabetically()
	{
		Pet mypet1 = new Pet();
	    mypet1.setOwner(owner);
	    mypet1.setName("essi");

		Pet mypet2 = new Pet();
	    mypet2.setOwner(owner);
	    mypet2.setName("petti");

		Pet mypet3 = new Pet();
	    mypet3.setOwner(owner);
	    mypet3.setName("asghar");

	    owner.addPet(mypet1);
	    owner.addPet(mypet2);
	    owner.addPet(mypet3);

	    List<Pet> mypets = new ArrayList<>();

		mypets.add(mypet3);
		mypets.add(mypet1);
		mypets.add(mypet2);

		assertEquals(mypets, owner.getPets());
	}

	@Test
	void removing_one_pet_should_delete_pet_from_hashset()
	{
		Pet my_pet = new Pet();
		my_pet.setName("shafi");
		owner.addPet(my_pet);
		owner.removePet(my_pet);
		assertNull(owner.getPet("shafi"));
	}

	@Test
	void removing_non_existing_pet_should_not_throw_any_exception()
	{
		Pet my_pet = new Pet();
		try
		{
			owner.removePet(my_pet);
		}
		catch(Exception exception)
		{
			fail();
		}
	}

	@Test
	void getting_with_ignoreNew_should_not_return_new_pet()
	{
		Pet my_pet = new Pet();
		my_pet.setName("shafi");
		owner.addPet(my_pet);
		assertNull(owner.getPet("shafi", true));
	}

	@Test
	void getting_with_ignoreNew_should_return_old_pet()
	{
		Pet my_pet = new Pet();
		my_pet.setName("shafi");
		owner.addPet(my_pet);
		my_pet.setId(22);
		assertEquals(my_pet, owner.getPet("shafi", true));
	}

	@Test
	void adding_old_pet_should_not_be_added_to_pet_list_of_owner()
	{
		Pet my_pet = new Pet();
		my_pet.setName("shafi");
		my_pet.setId(22);
		owner.addPet(my_pet);
		assertNull(owner.getPet("shafi", false));
	}

	@Test
	void getting_pet_with_different_capitalized_letters_should_work()
	{
		Pet my_pet = new Pet();
		my_pet.setName("essi");

		owner.addPet(my_pet);
		assertEquals(my_pet, owner.getPet("ESSi"));
	}

	@Test
	void adding_one_pet_multiple_times_should_add_just_one_time()
	{
		Pet my_pet = new Pet();
		my_pet.setName("essi");

		owner.addPet(my_pet);
		owner.addPet(my_pet);
		owner.addPet(my_pet);
		assertEquals(1, owner.getPets().size());
	}
}
