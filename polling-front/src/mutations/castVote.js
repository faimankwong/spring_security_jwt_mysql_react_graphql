import gql from 'graphql-tag';

export default gql`    
mutation castVote($pollId: Int, $choiceId:Int) {
castVote(pollId:$pollId, choiceId: $choiceId) {
        id                  
        question            
        choices {
        id
        text
        voteCount
        }        
        createdBy {
        id
        username
        name
        }        
        creationDateTime    
        expirationDateTime  
        isExpired           
        selectedChoice      
        totalVotes     
  }
}
`;

