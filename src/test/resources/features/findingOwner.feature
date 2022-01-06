Feature: findingOwner
  Scenario: finding an existing owner
    Given There exists an owner with id equal to 1
    When User wants to find the owner with id equal to 1
    Then The owner with id equal to 1 will return

  Scenario: finding a not existing owner
    Given There exists no owner with id equal to 2
    When User wants to find the owner with id equal to 2
    Then NULL owner will return
