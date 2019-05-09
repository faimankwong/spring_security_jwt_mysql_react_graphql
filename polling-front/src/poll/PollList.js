import React, { Component } from 'react';
import Poll from './Poll';
import LoadingIndicator  from '../common/LoadingIndicator';
import { Button, Icon, notification } from 'antd';
import { POLL_LIST_SIZE } from '../constants';
import { withRouter } from 'react-router-dom';
import './PollList.css';
import  * as getUserVotedPolls from "../Queries/getPollsVotedBy";
import  * as getUserCreatedPolls from "../Queries/getPollsCreatedBy";
import  * as getPolls from "../Queries/getPolls";
import {compose, graphql,ApolloConsumer} from "react-apollo";
import mutation from '../mutations/castVote';
class PollList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            polls: [],
            page: 0,
            size: 10,
            totalElements: 0,
            totalPages: 0,
            last: true,
            currentVotes: [],
            isLoading: false
        };
        //console.log('PollListPollList')
        //console.log(props)
    }

    loadPollList=(page = 0, size = POLL_LIST_SIZE) =>{
        let promise;
        if(this.props.username) {
            const username=this.props.username;
            if(this.props.type === 'USER_CREATED_POLLS') {
                //console.log("username "+username)
                promise = this.props.getPollsCreatedBy.refetch({username, page, size});
            } else if (this.props.type === 'USER_VOTED_POLLS') {
             //   promise = getUserVotedPolls(this.props.username, page, size);
                //console.log("this.props.type "+this.props.type)
                promise = this.props.getPollsVotedBy.refetch({username, page, size});
            }
        } else {
            //console.log("this.props.type "+this.props.type)
            promise = this.props.getAllPolls.refetch({page,size})
        }

        if(!promise) {
            return;
        }

        this.setState({
            isLoading: true
        });
        //console.log('promise')
        //console.log(promise)
        //console.log('this.props.type '+this.props.type)
        promise            
        .then(response => {
            //console.log('line 56')
            //console.log(response)
            const polls = this.state.polls.slice();
            const currentVotes = this.state.currentVotes.slice();
            //console.log('line 59 '+Object.keys(response.data)[0])
            const gql_content = response.data[Object.keys(response.data)[0]];
            this.setState({
                polls: polls.concat(gql_content.content),
                page: gql_content.page,
                size: gql_content.size,
                totalElements: gql_content.totalElements,
                totalPages: gql_content.totalPages,
                last: gql_content.last,
                currentVotes: currentVotes.concat(Array(gql_content.content.length).fill(null)),
                isLoading: false
            })
        }).catch(error => {
            this.setState({
                isLoading: false
            })
        });  
        
    }

    componentDidMount() {
        //console.log( this.state)
        this.loadPollList();
    }

    componentDidUpdate(nextProps) {
        if(this.props.isAuthenticated !== nextProps.isAuthenticated) {
            // Reset State
            this.setState({
                polls: [],
                page: 0,
                size: 10,
                totalElements: 0,
                totalPages: 0,
                last: true,
                currentVotes: [],
                isLoading: false
            });    
            this.loadPollList();
        }
    }

    handleLoadMore=()=> {
        this.loadPollList(this.state.page + 1);
    }

    handleVoteChange(event, pollIndex) {
        const currentVotes = this.state.currentVotes.slice();
        currentVotes[pollIndex] = event.target.value;

        this.setState({
            currentVotes: currentVotes
        });
    }


    handleVoteSubmit(event, pollIndex) {
        event.preventDefault();
        //console.log("handleVoteSubmithandleVoteSubmit")
        //console.log(this.props.isAuthenticated)
        if(!this.props.isAuthenticated) {
            this.props.history.push("/login");
            notification.info({
                message: 'Polling App',
                description: "Please login to vote.",          
            });
            return;
        }

        const poll = this.state.polls[pollIndex];
        const selectedChoice = this.state.currentVotes[pollIndex];

        const voteData = {
            pollId: poll.id,
            choiceId: selectedChoice
        };

      //  castVote(voteData)
        this.props.mutate({
            variables:  voteData
        })
       .then(response => {
            const polls = this.state.polls.slice();
            polls[pollIndex] = response.data.castVote;
            this.setState({
                polls: polls
            });        
        }).catch(error => {
            if(error.status === 401) {
                this.props.handleLogout('/login', 'error', 'You have been logged out. Please login to vote');    
            } else {
                notification.error({
                    message: 'Polling App',
                    description: error.message || 'Sorry! Something went wrong. Please try again!'
                });                
            }
        });
    }

    render() {
        const pollViews = [];
        this.state.polls.forEach((poll, pollIndex) => {
            pollViews.push(<Poll 
                key={poll.id} 
                poll={poll}
                currentVote={this.state.currentVotes[pollIndex]} 
                handleVoteChange={(event) => this.handleVoteChange(event, pollIndex)}
                handleVoteSubmit={(event) => this.handleVoteSubmit(event, pollIndex)} />)            
        });

        return (
            <div className="polls-container">

                {pollViews}
                {
                    !this.state.isLoading && this.state.polls.length === 0 ? (
                        <div className="no-polls-found">
                            <span>No Polls Found.</span>
                        </div>    
                    ): null
                }  
                {
                    !this.state.isLoading && !this.state.last ? (
                        <div className="load-more-polls"> 
                            <Button type="dashed" onClick={this.handleLoadMore} disabled={this.state.isLoading}>
                                <Icon type="plus" /> Load more
                            </Button>
                        </div>): null
                }              
                {
                    this.state.isLoading ? 
                    <LoadingIndicator />: null                     
                }
            </div>
        );
    }
}



export default compose(
    graphql(getPolls.getAllPolls, {
        name: "getAllPolls"
    }),
    graphql(getUserVotedPolls.getPollsVotedBy, {
        name: "getPollsVotedBy"
    }),
    graphql(getUserCreatedPolls.getPollsCreatedBy, {
        name: "getPollsCreatedBy"
    }),

)
(
    graphql(mutation)(withRouter(PollList))
);

