Feature: findingPet
  Scenario: finding an existing pet
    Given There exists a pet with id equal to 1
    When User wants to find the pet with id equal to 1
    Then The pet with id equal to 1 will return

  Scenario: finding a not existing pet
    Given There exists no pet with id equal to 2
    When User wants to find the pet with id equal to 2
    Then NULL pet will return
