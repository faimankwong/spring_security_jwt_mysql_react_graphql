import gql from 'graphql-tag';

export default gql`    
mutation CreatePoll($question: String, $choices: [text],$pollLength:pollLength) {
createPoll(question:$question, choices: $choices,pollLength:$pollLength) {
  success
  message
}
}
`;
