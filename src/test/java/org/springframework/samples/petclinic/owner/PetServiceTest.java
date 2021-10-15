package org.springframework.samples.petclinic.owner;

import java.util.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.slf4j.LoggerFactory;
import org.springframework.samples.petclinic.utility.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(Parameterized.class)
public class PetServiceTest
{
	public Integer desired_id;
	public Pet desired_pet;
	public static PetService pet_service;
	private static PetTimedCache cache;

	public PetServiceTest(Pet desired_pet, Integer desired_id) {
		this.desired_id = desired_id;
		this.desired_pet = desired_pet;
		pet_service = new PetService(cache, null, LoggerFactory.getLogger(PetService.class));
	}

	@Parameterized.Parameters
	public static Collection<Object[]> parameters()
	{
		cache = mock(PetTimedCache.class);
		List<Object[]> params = new ArrayList<>();
		for (int i = 0; i < 5; i++)
		{
			Pet pet = new Pet();
			pet.setId(i);
			pet.setName("Petti" + i);
			params.add(new Object[]{pet, i});
			when(cache.get(i)).thenReturn(pet);
		}
		params.add(new Object[]{null, -1});
		when(cache.get(-1)).thenReturn(null);
		params.add(new Object[]{null, 100});
		when(cache.get(100)).thenReturn(null);
		return params;
	}

	@Test
	public void finding_pet_with_its_id_should_return_right_pet()
	{
		assertEquals(pet_service.findPet(desired_id), desired_pet);
	}
}
