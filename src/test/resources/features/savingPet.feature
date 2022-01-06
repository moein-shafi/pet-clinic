Feature: save pet
  Scenario: saving a pet
    Given There exists an owner named "haj moein"
    When He adds a pet to his list
    Then The pet is saved successfully

  Scenario: buying a pet
    Given There exists an owner named "haj moein"
    When He wants a new pet
    Then The pet is saved successfully
