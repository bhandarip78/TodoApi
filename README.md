# TodoAPI

---

### Features 
- Serves as a middleware microservices for TODO list
- REST APIs cover all CRUD operation for TODO tasks
- Able to run the application as a JAR package
- Code is build using Maven
- H2 database is used as peristence database
- Used javax.persistance package for data persistence
- Integrated Swagger API docs
- Implemented scheduing feature using Quartz - it sends email on any uncompleted todos
- Implemented custom error handling
- Implemented logger - logs on terminal and on a rolling file
- Wrote Unit test and can be run with maven
- REST APIs security - okta integration
- Hypermedia as the Engine of Application State (HATEOAS) - added on get api (/api/tod/{id}
---

### Prerequisites/Dependencies
- spring-boot : 2.7.8
- java : 1.8
- spring-boot-starter-data-jpa
- spring-boot-starter-mail
- spring-boot-starter-validation
- spring-boot-starter-web
- spring-boot-devtools
- h2 database
- springfox-swagger2
- springfox-swagger-ui
- junit-jupiter-api
- spring-boot-starter-test
- spring-boot-starter-hateos
- maven
---

### Swagger UI URL
- http://localhost:8080/swagger-ui.html - 
    >goes to okta login ui; after login browse to the link again\
    >Implemented authorization header. So need to provide valid token to make api call\
    >Screen shots are included below\
    >![alt text](https://github.com/bhandarip78/TodoApi/blob/main/todo-okta-login.PNG?raw=true)\
    >![alt text](https://github.com/bhandarip78/TodoApi/blob/main/todo-apis-swagger-doc-1.PNG?raw=true)\
    >![alt text](https://github.com/bhandarip78/TodoApi/blob/main/todo-apis-swagger-doc-2.PNG?raw=true)\
    >![alt text](https://github.com/bhandarip78/TodoApi/blob/main/todo-api-swagger-unauthorized.PNG?raw=true)\
    >![alt text](https://github.com/bhandarip78/TodoApi/blob/main/todo-api-swagger-withvalid-token.PNG?raw=true)
---

### H2 Database Console UI URL
- http://localhost:8080/todo-ui - granted anonymous access. 
    >Pending - after h2 db login, okta is still blocking the next redirect.\
    >![alt text](https://github.com/bhandarip78/TodoApi/blob/main/todo-h2db.PNG?raw=true)
---

### SMTP Mail Server Setup
- Update the value of below properties in application.properties resource file accordingly to your SMTP server configuration
- SMTP properties:
    >spring.mail.host={your value}\
    >spring.mail.port={your post}\
    >spring.mail.username={your username}\
    >spring.mail.password={your password}\
    >spring.mail.properties.mail.smtp.auth=true\
    >spring.mail.properties.mail.smtp.starttls.enable=true\
    >client-to-email={email address where you want to get the notification for uncompleted task}\
    >![alt text](https://github.com/bhandarip78/TodoApi/blob/main/uncomplete-task-email-inbox.png?raw=true)
---

### Api Security Setup - OKTA
- Add Okta-Authentication-Properties as below in application.properties file:
  >okta.oauth2.issuer=https://{youroktaserver domain}/oauth2/default\
  >okta.oauth2.client-id={client id}
  >okta.oauth2.client-secret={client secret}
  >okta.oauth2.redirect-uri=/
- Sign up for okta account
- Create a new app integration with OIDC - OpenID Connect sign in method; Web Application as Application type
  >Note down the client id and client secrets - you need client id to add in application.properties file; you need client id and client secret to get bearer token for API authorization
- Add Authorization Server under Security/API
  >Name: default\
  >Audience: api://default\
  >Issuer URI would be {https://{your domain}/oauth2/default
  >Goto Tokens tab and create new token
- See postman screen shot below for reference on getting the token and calling api with the token
  >In order to get authenticated and receive API token, you need to make a POST call\
  >>URL for post call is https://{youroktaserver domain}/oauth2/default/v1/token\
  >>If you use postman, under Authorzation tab enter Username (it's your okta client id), Password (it's your client secret)\
  >>Under Body tab, make two entries:\
  >>>KEY - grant_type | VALUE - client_credentials\
  >>>KEY - scope | VALUE - {your access granted to the API token}\
  >>>Make sure to select radio button x-www-form-urlencoded\
  >>>Under headers make sure you have Content-Type header and value is applicaton/x-www-form-urlencoded
  >Once you get the access/API token, before calling any TodoApis add Authorization Type as Bearer Token and enter the token value.

### Build
- clone from github with command 'git clone https://github.com/bhandarip78/TodoApi
- have terminal to locate the folder
- cd into the folder
- make sure you have maven and java 1.8 sdk installed
- run 'mvn clean package' in your terminal or command line or git bash to build the package
---

### Run
- in order to start the API application, run this command 'java -jar .\target\TodoAPI-0.0.1-SNAPSHOT.jar' in your terminal/git bash/command line
- todoAPI application will be started on http://localhost:8080
---

### Available REST APIs
- POST - http://ocalhost:8080/api/todo - creates a new todo task. Required request body {
    "username" : "",
    "task" : "",
    "description": "",
    "targetdate": "yyyy-mm-dd",
    "isComplete": false
}
- GET - http://localhost:8080/api/todo/{id}
- GET - http://localhost:8080/api/todos/{username}
- GET - http://localhost:8080/api/todos/uncomplete
- PUT - http://localhost:8080/api/todo/update - update a todo task. Required request body {
    "id" : 2,
    "task" : "Grab a gallon of milk",
    "description": "Stop by at Kroger on the way home",
    "targetdate": "2023-02-04"
}
- PATCH - http://localhost:8080/api/todo/complete - mark a todo task as completed. Required request body {
    "id" : 2,
    "isComplete": true
}
- DELETE - http://localhost:8080/api/todo/{id} - delete a todo task.
---

### Postman Screenshot - Getting Okta token for API authentication and calling API with token for authorization
![alt text](https://github.com/bhandarip78/TodoApi/blob/main/okta-oauth2-token.PNG?raw=true)
---
![alt text](https://github.com/bhandarip78/TodoApi/blob/main/calling-api-with-token.PNG?raw=true)
---
![alt text](https://github.com/bhandarip78/TodoApi/blob/main/calling-api-without-token-unauthorized.PNG?raw=true)
---

![Tech Stack](https://skills.thijs.gg/icons?i=java,spring,maven,git)
