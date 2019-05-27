# Apollo_spring_security_jwt_mysql_react_graphql (Still in Progress)

Installation
-----------
## Steps to Setup the Spring Boot Back end app (polling-app-server)

1. **Create MySQL database**

	```bash
	create database polling_app
	```

2. **Change MySQL username and password as per your MySQL installation**

	+ open `src/main/resources/application.properties` file.

	+ change `spring.datasource.username` and `spring.datasource.password` properties as per your mysql installation

3. **Run the app**

	You can run the spring boot app by typing the following command -

	```bash
	mvn spring-boot:run
	```

	The server will start on port 8080.

	You can also package the application in the form of a `jar` file and then run it like so -

	```bash
	mvn package
	java -jar target/polls-0.0.1-SNAPSHOT.jar
	```
4. **Run the Application**
```bash
npm install
npm start
```
What is it? 
-----------
Thank you for the excellent tutorial made by  Rajeev Singh[1]. This program is modified based on 
Building a Full Stack Polls app similar to twitter polls with Spring Boot, Graphql,
Spring Security, JWT, React and Ant Design. 

Technical choices
-----------
Front end: React(Ant Design)
Back end: Spring boot, Spring Security, JWT, mysql, GraphQL

Reason to use graphql
-----------
+ No more overfetching and underfetching
+ Back end compatible   

Sample GraphQL Queries
-----------
Front End: Getting the current login user
```
  {
    getCurrentUser {
    id
    username
    name
    }
  }

```
Result:Return exactly want we want: 
```
{
  "data": {
    "getCurrentUser": {
      "id": 16,
      "username": "tester1",
      "name": "tester1"
    }
  }
```
Mutation:
```
 mutation Login($usernameOrEmail: String, $password: String) {
    login(usernameOrEmail:$usernameOrEmail, password: $password) {
      accessToken,
      tokenType
    }
  }
```
Result:Generate accessToken
```
{
  "data": {
    "login": {
      "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxNyIsImlhdCI6MTU1NzczNzQxMSwiZXhwIjoxNTU4MzQyMjExfQ.JExptnJ3t9cfANJDrR4qTHpspegwgE_NIJVBkzVGCSxXh8DaY3BiO6btFMtGQV5RxdgA4yI_oDrOXqcFV0uH2Q",
      "tokenType": "Bearer"
    }
  },
  "extensions": {
    "tracing": {
      "version": 1,
      "startTime": "2019-05-13T08:50:11.483Z",
      "endTime": "2019-05-13T08:50:11.621Z",
      "duration": 138663600,
      "parsing": {
        "startOffset": 530100,
        "duration": 489200
      },
      "validation": {
        "startOffset": 2031400,
        "duration": 1465600
      },
      "execution": {
        "resolvers": [
          {
            "path": [
              "login"
            ],
            "parentType": "Mutation",
            "returnType": "JwtAuthenticationResponse",
            "fieldName": "login",
            "startOffset": 2481500,
            "duration": 135678000
          },
          {
            "path": [
              "login",
              "accessToken"
            ],
            "parentType": "JwtAuthenticationResponse",
            "returnType": "String",
            "fieldName": "accessToken",
            "startOffset": 138401100,
            "duration": 47900
          },
          {
            "path": [
              "login",
              "tokenType"
            ],
            "parentType": "JwtAuthenticationResponse",
            "returnType": "String",
            "fieldName": "tokenType",
            "startOffset": 138500800,
            "duration": 17800
          }
        ]
      }
    }
  }
}
```
Back End:
```
// Check the Authentication sent from react
    public DataFetcher getCurrentUser = (env) ->  {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal currentUser=null;
        if (!(authentication instanceof AnonymousAuthenticationToken))
            currentUser=(UserPrincipal)authentication.getPrincipal();
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
        return userSummary;
    };
```
Wiring:implement the buildRuntime function to wire your static schema with the resolvers.A DataFetcher fetches 
the Data for one field while the query is executed. While GraphQL Java is executing a query, it calls 
the appropriate DataFetcher for each field it encounters in query[2]
```
Wiring
@Component
public class Wiring {

    @Autowired
    GraphQLDataFetchers graphQLDataFetcher;
    @Autowired
    MutationDataFetchers mutationDataFetchers;
    public RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query",typeWiring -> typeWiring
                        .dataFetcher("checkUsernameAvailability",graphQLDataFetcher.checkUsernameAvailability )
                        .dataFetcher("checkEmailAvailability",graphQLDataFetcher.checkEmailAvailability )
                        .dataFetcher("getCurrentUser",graphQLDataFetcher.getCurrentUser )
                        .dataFetcher("getPolls",graphQLDataFetcher.getPolls )
                        .dataFetcher("getUserProfile",graphQLDataFetcher.getUserProfile )
                        .dataFetcher("getPollsCreatedBy",graphQLDataFetcher.getPollsCreatedBy )
                        .dataFetcher("getPollsVotedBy",graphQLDataFetcher.getPollsVotedBy )

                )
                .type("Mutation",typeWiring -> typeWiring
                        .dataFetcher("signup",mutationDataFetchers.registerUser )
                        .dataFetcher("login",mutationDataFetchers.authenticateUser )
                        .dataFetcher("createPoll",mutationDataFetchers.createPoll )
                        .dataFetcher("castVote",mutationDataFetchers.castVote )
                )
                .scalar(Scalars.GraphQLInstant)
                .build();
    }
 }
```
Example Code for security 
-----------

Middlewares are used to inspect and modify every request made over the link.It joined with the HttpLink. 
It checks to see if we have a token (JWT, for example) and passes that token into the HTTP header 
of the request, so we can authenticate interactions with GraphQL performed through our network interface.[3]
```
const authLink = new ApolloLink((operation, forward) => {
    // Retrieve the authorization token from local storage.
    const token = localStorage.getItem(ACCESS_TOKEN);

    // Use the setContext method to set the HTTP headers.
    operation.setContext({
        headers: {
            authorization: token ? `Bearer ${token}` : ''
        }
    });

    // Call the next link in the middleware chain.
    return forward(operation);
});
```
Password Encryption:org.springframework.security.crypto.password.PasswordEncode is used
```
@Autowired
PasswordEncoder passwordEncoder;
user.setPassword(passwordEncoder.encode(user.getPassword()));
```

Part of schema
-----------
```
type Query {
    checkUsernameAvailability(username:String,skip:Boolean!): UserIdentityAvailability,
    checkEmailAvailability(email:String,skip:Boolean!):UserIdentityAvailability,
    getCurrentUser:UserSummary,
    getPolls(page:String,size:String):PagedResponse,
    getUserProfile(username:String):UserProfile,
    getPollsCreatedBy(username:String,page:String,size:String):PagedResponse,
    getPollsVotedBy(username:String,page:String,size:String):PagedResponse
}
```


Issue existing
-----------
Auto querying when loading componant
Possible Solution:Using ApolloConsumer

Reference
-----------
[1]R. Singh, "callicoder/spring-security-react-ant-design-polls-app", GitHub, 2019. [Online]. Available: https://github.com/callicoder/spring-security-react-ant-design-polls-app. [Accessed: 01- May- 2019].

[2]A. Marek and B. Baker, "Getting started with GraphQL Java and Spring Boot", Graphql-java.com, 2019. [Online]. Available: https://www.graphql-java.com/tutorials/getting-started-with-spring-boot/. [Accessed: 01- May- 2019].

[3]"Network layer (Apollo Link)", Apollo Docs, 2019. [Online]. Available: https://www.apollographql.com/docs/react/advanced/network-layer. [Accessed: 01- May- 2019].

Contributors
-----------
Fai Man Kwong
