Feature: Movimientos API

  Background:
    * url 'http://localhost:8082'
    * def clienteReplica = { clienteId: 99, nombre: 'Test Karate', estado: true }
    * configure headers = { 'Content-Type': 'application/json' }

  Scenario: Registrar deposito
    Given path '/api/movimientos'
    And request { numeroCuenta: '225487', tipoMovimiento: 'Deposito', valor: 100 }
    When method post
    Then status 201
    And match response.saldo == '#number'
