import gql from 'graphql-tag';

export  const getPollsCreatedBy =  gql`
query getPollsCreatedBy($username:String,$page: String,$size: String) {
  	getPollsCreatedBy(username:$username,page:$page,size:$size)  
  {
    content{
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
    page
    size
    totalElements
    totalPages
    last
  }
}
`;
