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
Thank you for the excellent tutorial made by  Rajeev Singh[1]. This program is “modified based on 
Building a Full Stack Polls app similar to twitter polls with Spring Boot, 
Spring Security, JWT, React and Ant Design” (ref.). 

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


Reference
-----------
[1]R. Singh, "callicoder/spring-security-react-ant-design-polls-app", GitHub, 2019. [Online]. Available: https://github.com/callicoder/spring-security-react-ant-design-polls-app. [Accessed: 01- May- 2019].
Contributors
-----------
Fai Man Kwong
