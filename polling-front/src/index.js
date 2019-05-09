import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './app/App';
import registerServiceWorker from './registerServiceWorker';
import { BrowserRouter as Router } from 'react-router-dom';
import { ApolloProvider } from 'react-apollo';
import { ApolloClient, ApolloLink, InMemoryCache, HttpLink } from 'apollo-boost';
import { ACCESS_TOKEN } from './constants';




const httpLink = new HttpLink({  uri: '/graphql' });

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


const apolloClient = new ApolloClient({

    link:authLink.concat(httpLink),
    cache: new InMemoryCache(),
    connectToDevTools: true,
})

ReactDOM.render(
    <ApolloProvider client={apolloClient}>
    <Router>
        <App />
    </Router>
    </ApolloProvider> ,
    document.getElementById('root')
);

registerServiceWorker();
