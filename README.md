# spring_security_jwt_mysql_react_graphql (Still in Progress)
Installation
-----------
## Steps to Setup the Spring Boot Back end app (polling-app-server)

1. **Clone the application**

	```bash
	git clone https://github.com/callicoder/spring-security-react-ant-design-polls-app.git
	cd polling-app-server
	```

2. **Create MySQL database**

	```bash
	create database polling_app
	```

3. **Change MySQL username and password as per your MySQL installation**

	+ open `src/main/resources/application.properties` file.

	+ change `spring.datasource.username` and `spring.datasource.password` properties as per your mysql installation

4. **Run the app**

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
What is it? 
-----------
Thank you for the excellent tutorial made by  Rajeev Singh[1]. This program is “modified based on 
Building a Full Stack Polls app similar to twitter polls with Spring Boot, 
Spring Security, JWT, React and Ant Design” (ref.). 

Technical choices
-----------
React is the choice for front-end developing. Node.js is used for the back-end. 
Redux is used for the state management. The system gets data from Open Exchange Rates API. 
Semantic is the library for the user-interface. Axios is used for the extraction of data.

Reason for the choices
-----------
React can automatically manage all UI updates. The One-direction data flow can ensure that 
changes of child structures do not affect their parents. There is a lot of data flow in this 
system.Thus, it gives a better data management. 

Redux is used to ensure the changing state is step by step. The state can only change by sending action. 
Thus, there is no mess up with the other state.

Open Exchange Rates provides a simple, lightweight and portable JSON API with live and historical foreign 
exchange rates which fits the aim of the project. 

  

Example
-----------
Due to the restriction of accessing the time series API in Open Exchange Rate. 
Providing last 30 days records can only be done by changing the data of historical API. 
by using promise all, This allows fetching api in parallel which reduce colossal amount of time during the process.
Below is the code to get data by changing the API parameter recursively:
```
export const ChangeCurrencyMonthly_api = async (date,name) => {
    let arry=[];
    let date_target = new Date(date);
    let now = new Date();
    let arry_str=[];
    for (let i = 0; i < 30; i++) {
        let date_form = formatDate(date_target);
        let obj={[date_form]:`https://openexchangerates.org/api/historical/${date_form}.json?app_id=${api_key}&symbols=${name}`}
        arry_str.push( obj)
        date_target.setDate(date_target.getDate() - 1)
    }
    // Note that async functions return a promise
    const promises = arry_str.map(async (item) => {
        let key=Object.keys(item);
        let value=Object.values(item);
        const response = await fetch(`${value}`)
        const {rates} = await  response.json();
        if (rates===undefined) {
            i--;
            return;
        }
        // Log individual results as they finish
        let target_object = {'key':key[0],'value':(1/rates[name]).toFixed(4),'per':''};
        return target_object;
    });

    let now2 = new Date();
    const results = await Promise.all(promises);
    return  results;
}

```
Contributors
-----------
Fai Man Kwong
