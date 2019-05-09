# Final Project: Self-Designed App

Implement a project with JavaScript and React Native from scratch.
Develop a workflow for developing mobile apps.
Design your own interfaces.

Installation
-----------
```
npm install
npm start
```
What is it? 
-----------
The Exchange Rate System provides the latest and historical exchange rate for the currency. 
Additionally, it provides conversion between different currencies on a certain day. 
Furthermore,it provides the difference of the exchange rate between 30 consecutive days. 
Last but not least, Line chart shows the trending of the exchanging rate of different currency.

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
