import gql from 'graphql-tag';

export  const getAllPolls =  gql`
query getPolls($page: String,$size: String) {
  	getPolls(page:$page,size:$size)
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
