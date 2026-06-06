Feature: Movimientos API

  Background:
    * url 'http://localhost:' + karate.properties['server.port']
    * configure headers = { 'Content-Type': 'application/json' }

  Scenario: F3 - Rechazar retiro sin saldo disponible
    Given path '/api/movimientos'
    And request { numeroCuenta: '225487', tipoMovimiento: 'Retiro', valor: 575 }
    When method post
    Then status 409
    And match response.message == 'Saldo no disponible'

  Scenario: F2 - Registrar deposito y actualizar saldo
    Given path '/api/movimientos'
    And request { numeroCuenta: '225487', tipoMovimiento: 'Deposito', valor: 600 }
    When method post
    Then status 201
    And match response.saldo == 700
