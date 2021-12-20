package org.springframework.samples.petclinic.utility;

	import org.junit.jupiter.api.*;
	import org.springframework.samples.petclinic.visit.Visit;
	import org.springframework.samples.petclinic.owner.Pet;
	import java.time.LocalDate;
	import java.util.*;
	import static org.mockito.Mockito.*;
	import static org.junit.jupiter.api.Assertions.assertEquals;
	import static org.springframework.samples.petclinic.utility.PriceCalculator.*;

public class PriceCalculatorTest {
	private int INFANT_YEARS = 2;
	private double RARE_INFANCY_COEF = 1.4;
	private double BASE_RARE_COEF = 1.2;
	private int DISCOUNT_MIN_SCORE = 10;
	private int DISCOUNT_PRE_VISIT = 2;

	private int baseCharge = 100;
	private int basePricePerPet = 200;
	private static int maxAge = 4;
	private static final int minAge = 1;
	private static int greaterThanHunderedDays = 100;

	private static Pet pet = mock(Pet.class);
	private static Pet infantPet = mock(Pet.class);
	private static Visit visit = new Visit();
	private static List<Visit> visits = new ArrayList<>();

	@BeforeAll
	public static void setupClass() {
		visit.setDate(LocalDate.now().minusDays(greaterThanHunderedDays - 1));
		when(pet.getBirthDate()).thenReturn(LocalDate.now().minusYears(maxAge));
		when(infantPet.getBirthDate()).thenReturn(LocalDate.now().minusYears(minAge));
		when(infantPet.getVisitsUntilAge(minAge)).thenReturn(Collections.emptyList());
		when(pet.getVisitsUntilAge(maxAge)).thenReturn(Collections.emptyList());
	}

	@Test
	public void price_should_calculated_true_for_non_empty_pet_list() {
		List<Pet> myPets = new ArrayList<>();
		myPets.add(pet);
		assertEquals(basePricePerPet * BASE_RARE_COEF,
			calcPrice(myPets, baseCharge, basePricePerPet));
	}

	@Test
	public void price_should_be_equal_to_zero_for_empty_pet_list() {
		List<Pet> myPets = new ArrayList<>();
		assertEquals(0, calcPrice(myPets, baseCharge, basePricePerPet));
	}

	@Test
	public void price_should_be_true_for_infant_pet_list() {
		List<Pet> myPets = new ArrayList<>();
		myPets.add(infantPet);
		assertEquals(basePricePerPet * BASE_RARE_COEF * RARE_INFANCY_COEF,
			calcPrice(myPets, baseCharge, basePricePerPet));
	}

	@Test
	public void discount_counter_should_be_increase_by_two_for_infant_pets() {
		when(infantPet.getVisitsUntilAge(minAge)).thenReturn(visits);
		List<Pet> myPets = new ArrayList<>();
		for (int i = 0; i < DISCOUNT_MIN_SCORE; i = i + 2)
			myPets.add(infantPet);
		assertEquals((myPets.size() - 1) * basePricePerPet * BASE_RARE_COEF *
			RARE_INFANCY_COEF * DISCOUNT_PRE_VISIT + baseCharge + basePricePerPet *
			BASE_RARE_COEF * RARE_INFANCY_COEF, calcPrice(myPets, baseCharge, basePricePerPet));
	}

	@Test
	public void price_should_be_true_for_reached_discount_min_score_pet_list() {
		when(pet.getVisitsUntilAge(maxAge)).thenReturn(visits);
		List<Pet> myPets = new ArrayList<>();
		for (int i = 0; i < DISCOUNT_MIN_SCORE; ++i)
			myPets.add(pet);
		assertEquals((DISCOUNT_MIN_SCORE - 1) * basePricePerPet * BASE_RARE_COEF *
				DISCOUNT_PRE_VISIT + baseCharge + basePricePerPet * BASE_RARE_COEF,
			calcPrice(myPets, baseCharge, basePricePerPet));
	}

	@Test
	public void price_should_be_true_for_not_reached_discount_min_score_pet_list() {
		List<Pet> myPets = new ArrayList<>();
		for (int i = 0; i < DISCOUNT_MIN_SCORE - 1; i++)
			myPets.add(pet);
		assertEquals(myPets.size() * basePricePerPet * BASE_RARE_COEF,
			calcPrice(myPets, baseCharge, basePricePerPet));
	}

	@Test
	public void price_should_be_true_for_last_visits() {
		Visit visit2 = new Visit();
		visit2.setDate(LocalDate.now().minusDays(greaterThanHunderedDays * 2));
		List<Visit> visits2 = new ArrayList<>();
		visits2.add(visit2);
		when(pet.getVisitsUntilAge(maxAge)).thenReturn(visits2);
		List<Pet> myPets = new ArrayList<>();
		double priceBeforeMinScore = (DISCOUNT_MIN_SCORE - 1) * basePricePerPet * BASE_RARE_COEF;
		int oldVisitDiscount = (greaterThanHunderedDays * 2)/(greaterThanHunderedDays) + visits2.size();
		double price = (priceBeforeMinScore + baseCharge) * oldVisitDiscount + basePricePerPet * BASE_RARE_COEF;
		for (int i = 0; i < DISCOUNT_MIN_SCORE; i++)
			myPets.add(pet);

		assertEquals(price, calcPrice(myPets, baseCharge, basePricePerPet));
	}

	@Test
	public void price_should_be_true_for_pet_list_after_visits_reached_discount_min_score() {
		double priceBeforeMinScore = (DISCOUNT_MIN_SCORE - 1) * basePricePerPet * BASE_RARE_COEF;
		double priceAfterFirstDiscount = priceBeforeMinScore * DISCOUNT_PRE_VISIT + baseCharge + basePricePerPet * BASE_RARE_COEF;
		double priceAfterSecondDiscount = priceAfterFirstDiscount * DISCOUNT_PRE_VISIT + baseCharge + basePricePerPet * BASE_RARE_COEF;
		double price = priceAfterSecondDiscount * DISCOUNT_PRE_VISIT + baseCharge + basePricePerPet * BASE_RARE_COEF;
		when(pet.getVisitsUntilAge(maxAge)).thenReturn(visits);
		List<Pet> myPets = new ArrayList<>();
		for (int i = 0; i < DISCOUNT_MIN_SCORE + 2; i++)
			myPets.add(pet);

		assertEquals(price, calcPrice(myPets, baseCharge, basePricePerPet));
	}
}
