package org.springframework.samples.petclinic.owner;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.samples.petclinic.visit.Visit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(Theories.class)
public class PetTest {

	@DataPoints
	public static Visit[] data_points1 = new Visit[]{
		new Visit().setDate(LocalDate.of(2050, 8, 18)),
		new Visit().setDate(LocalDate.of(322, 4, 22)),
		new Visit().setDate(LocalDate.of(2088, 5, 24))
	};

	@DataPoints
	public static Visit[] data_points2 = new Visit[]{
		new Visit().setDate(LocalDate.of(70, 4, 30)),
		new Visit().setDate(LocalDate.of(68, 5, 3)),
	};

	@Theory
	public void Theorize_get_visits_test(Visit v1, Visit v2)
	{
		Pet mypet = new Pet();
		List<Visit> visits = new ArrayList<>();
		visits.add(v1);
		visits.add(v2);

		 for (Visit v : visits)
		 	mypet.addVisit(v);

		visits = visits.stream().distinct().collect(Collectors.toList());
		PropertyComparator.sort(visits, new MutableSortDefinition("date", false, false));
		assertEquals(visits, mypet.getVisits());
	}
}
