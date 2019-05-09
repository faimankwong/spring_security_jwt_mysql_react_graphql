import gql from 'graphql-tag';

export default gql`
  mutation Login($usernameOrEmail: String, $password: String) {
    login(usernameOrEmail:$usernameOrEmail, password: $password) {
      accessToken,
      tokenType
    }
  }
`;
