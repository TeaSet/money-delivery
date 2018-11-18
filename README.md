# RESTful Application for Banking Accounts #
[![Build Status](https://travis-ci.com/TeaSet/money-delivery.svg?branch=master)](https://travis-ci.com/TeaSet/money-delivery)

This is the implementation of RESTful API for money transfers between banking accounts. 

### Using technologies: ###
* Vert.x (core and web)
* Guice
* Spock framework

### How to run: ###
Run `mvn clean install` to build the application and run tests. Run `com.revolut.money.delivery.Starter` to launch 
the application. It is deployed under the Vert.x server, that is based on Netty server, on `8080` port. The application
uses in-memory data store.

All calls should start with `http://localhost:8080/accounts`

###Examples of usage: ###

* Create the account:
`POST { "holder": "TommyShelby", "amount": 768.4, "currencyCode": "RUB" }`

* Get the account:
`GET http://localhost:8080/accounts?accountHolder=TommyShelby&accountNum=1`

* Delete the account:
`DELETE http://localhost:8080/accounts?accountHolder=TommyShelby&accountNum=1`

* Deposit some money `/accounts/depost`:
`POST { "accountHolder" : "TommyShelby", "accountNum" : 1, "amount": 150.4, "currency": "RUB" }`

* Withdraw some money `/accounts/withdraw`:
`POST {"accountHolder" : "TommyShelby", "accountNum" : 1, "amount": 50.0, "currency": "RUB" }`

* Transfer from one account to another `/accounts/transfer`:
`POST {"fromAccountHolder" : "TommyShelby", "fromAccountNum" : 1, "toAccountHolder" : "JohnShelby", "toAccountNum" : 2, "amount": 500.0, "currency": "RUB" }`




  