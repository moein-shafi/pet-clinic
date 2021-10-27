package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class OwnerTest {
	Owner owner;

	@Mock
	Owner owner_mock;

	// Set up - Called before every test method.
	@BeforeEach
	public void setUp() {
		owner = new Owner();
		owner.setFirstName("George");
		owner.setLastName("Franklin");
		owner.setAddress("110 W. Liberty St.");
		owner.setCity("Madison");
		owner.setTelephone("6085551023");

		owner_mock = new Owner();
		owner_mock.setFirstName("George");
		owner_mock.setLastName("Franklin");
		owner_mock.setAddress("110 W. Liberty St.");
		owner_mock.setCity("Madison");
		owner_mock.setTelephone("6085551023");
	}

	// Tear down - Called after every test method.
	@AfterEach
	public void tearDown() {
		owner = null;
	}

	@Test
	void get_pet_should_returns_desired_existing_pet() {
		Pet mypet = new Pet();
		mypet.setOwner(owner);
		mypet.setName("petti");
		owner.addPet(mypet);
		assertEquals(mypet, owner.getPet("petti"));
	}

	@Test
	void get_pet_should_returns_desired_existing_pet_behavior() {
//		owner_mock.expects(once()).method("getPetsInternal").withAnyArguments();

		Pet mypet = new Pet();
		mypet.setOwner(owner);
		mypet.setName("petti");
		owner_mock.addPet(mypet);
		assertEquals(mypet, owner_mock.getPet("petti"));
	}
}
