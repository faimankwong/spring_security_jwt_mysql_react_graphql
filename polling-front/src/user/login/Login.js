import React, { Component } from 'react';
import './Login.css';
import { Link } from 'react-router-dom';
import { ACCESS_TOKEN } from '../../constants';

import { Form, Input, Button, Icon, notification } from 'antd';
import {compose, graphql} from "react-apollo";
import mutation from '../../mutations/Login';
const FormItem = Form.Item;

class Login extends Component {
    render() {
        const AntWrappedLoginForm = Form.create()(graphql(mutation)(LoginForm))
        return (
            <div className="login-container">
                <h1 className="page-title">Login</h1>
                <div className="login-content">
                    <AntWrappedLoginForm onLogin={this.props.onLogin} />
                </div>
            </div>
        );
    }
}

class LoginForm extends Component {
    constructor(props) {
        super(props);
        //console.log(  this.props);
    }

    handleSubmit=(event)=> {
        event.preventDefault();
        this.props.form.validateFields((err, values) => {
            //console.log(  this.props);
            if (!err) {
                const loginRequest = Object.assign({}, values);
                this.props.mutate({
                    variables: loginRequest
                })
          //      login(loginRequest)
                .then(response => {
                    localStorage.setItem(ACCESS_TOKEN, response.data.login.accessToken);
                 //   localStorage.setItem(ACCESS_TOKEN, response.accessToken);
                    //console.log(localStorage.getItem(ACCESS_TOKEN));
                    this.props.onLogin();
                }).catch(error => {
                    const errors = error.graphQLErrors.map(error => error.extensions);
                     const errors_status=errors[0].errorCode;

                    if(errors_status === 401 ) {
                        notification.error({
                            message: 'Polling App',
                            description: 'Your Username or Password is incorrect. Please try again!'
                        });                    
                    } else {
                        notification.error({
                            message: 'Polling App',
                            description: error.message || 'Sorry! Something went wrong. Please try again!'
                        });                                            
                    }
                });
            }
        });
    }

    render() {
        const { getFieldDecorator } = this.props.form;
        return (
            <Form onSubmit={this.handleSubmit} className="login-form">
                <FormItem>
                    {getFieldDecorator('usernameOrEmail', {
                        rules: [{ required: true, message: 'Please input your username or email!' }],
                    })(
                    <Input 
                        prefix={<Icon type="user" />}
                        size="large"
                        name="usernameOrEmail" 
                        placeholder="Username or Email" />    
                    )}
                </FormItem>
                <FormItem>
                {getFieldDecorator('password', {
                    rules: [{ required: true, message: 'Please input your Password!' }],
                })(
                    <Input 
                        prefix={<Icon type="lock" />}
                        size="large"
                        name="password" 
                        type="password" 
                        placeholder="Password"  />                        
                )}
                </FormItem>
                <FormItem>
                    <Button type="primary" htmlType="submit" size="large" className="login-form-button">Login</Button>
                    Or <Link to="/signup">register now!</Link>
                </FormItem>
            </Form>
        );
    }

}


export default  graphql(mutation)(Login);