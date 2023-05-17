# financialServices

Steps to test APIs:

1) Go to https://github.com/dibakar721304/financialServices

2) Execute below command in terminal:
git clone https://github.com/dibakar721304/financialServices.git

3) Execute below command to build application:
'mvn clean install'

4) Execute main application class FinancialServicesApplication

5) Go to http://localhost:8080/swagger-ui/index.html

6) Create a test customer from below end point:
http://localhost:8080/swagger-ui/index.html#/customer-controller/createCustomer
e.g parameters:
name: test
email: test@gmail.com
surName: testSurname

7) To create a new account, use below end pint:
http://localhost:8080/swagger-ui/index.html#/current-account-controller/createAccountForCustomer
e.g params:
initialCredit: 0.0 or 6.0
customer id: take from response (step 6)

8) To check details for customer, use below end point:
http://localhost:8080/swagger-ui/index.html#/customer-controller/getCustomerDetails

9) To update account with new transaction, please check below end point:
http://localhost:8080/swagger-ui/index.html#/current-account-controller/updateAccountForCustomer

e.g param: accountId (take from response of end point in step 7)
transactionType ( can be WITHDRAWAL or DEPOSIT)
transactionAmount( can have any value. Should give message 'balance is not enough' if this value
is more than account balance)

10) To check database, please use below end point:
http://localhost:8080/h2-console/login.jsp

        username:sa
        password:sa

11) Docker hub repository link : https://hub.docker.com/repository/docker/dibakar721304/banking-services
Steps to run it locally:
a) Execute below command in terminal :
'docker pull dibakar721304/banking-services:latestBankingApp'
b) Execute below command to run:
'docker run dibakar721304/banking-services:latestBankingApp'
All the  links should be available on localhost:8080

