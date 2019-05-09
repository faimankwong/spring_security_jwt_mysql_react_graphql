import gql from 'graphql-tag';

export default   gql`
  {
    getCurrentUser {
  	 id
    username
    name
    }
  }
`;

