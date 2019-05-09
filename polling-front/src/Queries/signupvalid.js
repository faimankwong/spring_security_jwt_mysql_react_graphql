import gql from 'graphql-tag';

export  const checkUsernameAvailability = gql`
query checkUsernameAvailability($username: String,$skip:Boolean=true) {
  	checkUsernameAvailability(username:$username,skip:$skip)  @skip(if: $skip)
  {
    available
  }
}
`;

export const checkEmailAvailability = gql`
query checkEmailAvailability($email: String,$skip:Boolean=true ) {
  	checkEmailAvailability(email:$email,skip:$skip)  @skip(if: $skip)
  {
    available
  }
}
`;
