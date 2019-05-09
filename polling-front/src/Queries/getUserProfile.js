import gql from 'graphql-tag';

export default   gql`
query getUserProfile($username: String) {
  	getUserProfile(username:$username)  
  {
    id
    username
    name
    joinedAt
    pollCount
    voteCount
  }
}
`;

