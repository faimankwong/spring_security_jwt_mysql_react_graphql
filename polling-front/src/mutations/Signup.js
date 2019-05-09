import gql from 'graphql-tag';

export default gql`
  mutation Signup($name: String, $email: String, $username: String,$password: String) {
    signup(name:$name, email:$email, password: $password,username:$username) {
      name,
      email,
      password,
      username
    }
  }
`;

