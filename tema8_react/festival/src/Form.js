import React from  'react';
class Form extends React.Component {

    constructor(props) {
        super(props);
        this.state = {id: '', name: '', location: '', date: '', availableTicketsNumber: ''};
    }

    handleIdChange=(event) =>{
        this.setState({id: event.target.value});
    }

    handleNameChange=(event) =>{
        this.setState({name: event.target.value});
    }

    handleLocationChange=(event) =>{
        this.setState({location: event.target.value});
    }

    handleDateChange=(event) =>{
        this.setState({date: event.target.value});
    }

    handleTicketsChange=(event) =>{
        this.setState({availableTicketsNumber: event.target.value});
    }

    handleSubmit =(event) =>{
        var artist = {
            id:this.state.id,
            name: this.state.name,
            location: this.state.location,
            date: this.state.date,
            availableTicketsNumber: this.state.availableTicketsNumber
        }
        console.log('An artist was submitted: ');
        console.log(artist);
        this.props.addFunc(artist);
        event.preventDefault();
    }

    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <label>
                    Id      
                    <input type="text" value={this.state.id} onChange={this.handleIdChange}/>
                </label><br/>
                <label>
                    Name   
                    <input type="text" value={this.state.name} onChange={this.handleNameChange} />
                </label><br/>
                <label>
                    Location    
                    <input type="text" value={this.state.location} onChange={this.handleLocationChange} />
                </label><br/>
                <label>
                    Date    
                    <input type="text" value={this.state.date} onChange={this.handleDateChange} />
                </label><br/>
                <label>
                    Tickets
                    <input type="text" value={this.state.availableTicketsNumber} onChange={this.handleTicketsChange} />
                </label><br/>
                <input type="submit" value="Add" />
            </form>
        );
    }
}
export default Form;