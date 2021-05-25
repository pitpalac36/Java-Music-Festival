import React from 'react';

class ArtistRow extends React.Component {
    handleClickDelete = (event) => {
        console.log('delete button pentru ' + this.props.artist.id);
        this.props.deleteFunc(this.props.artist.id);
    }

    handleClickUpdate = (event) => {
        console.log('update button pentru ' + this.props.artist.id);
        this.props.updateFunc(this.props.artist);
    }

    updateName = (event) => {
        console.log(event.target.textContent)
        this.props.artist.name = event.target.textContent
    }

    updateLocation = (event) => {
        console.log(event.target.textContent)
        this.props.artist.location = event.target.textContent
    }

    updateDate = (event) => {
        console.log(event.target.textContent)
        this.props.artist.date = event.target.textContent
    }

    updateTickets = (event) => {
        console.log(event.target.textContent)
        this.props.artist.tickets = event.target.textContent
    }

    render() {
        return (
            <tr>
                <td>{this.props.artist.id}</td>
                <td contenteditable="true" onInput={this.updateName}>{this.props.artist.name}</td>
                <td contenteditable="true" onChange={this.updateLocation}>{this.props.artist.location}</td>
                <td contenteditable="true" onChange={this.updateDate}>{this.props.artist.date}</td>
                <td contenteditable="true" onChange={this.updateTickets}>{this.props.artist.availableTicketsNumber}</td>
                <td contenteditable="true"><button onClick={this.handleClickDelete}>Delete</button></td>
                <td contenteditable="true"><button onClick={this.handleClickUpdate}>Update</button></td>
            </tr>
        );
    }
}

class ArtistTable extends React.Component {
    render() {
        var rows = [];
        var functieStergere=this.props.deleteFunc;
        var functieUpdate=this.props.updateFunc;
        this.props.artists.forEach(function(artist) {

            rows.push(<ArtistRow artist={artist} key={artist.id} deleteFunc={functieStergere} updateFunc={functieUpdate}/>);
        });
        return (<div className="ArtistTable">

            <table className="center">
                <thead>
                <tr>
                    <th>Id</th>
                    <th>Name</th>
                    <th>Location</th>
                    <th>Date</th>
                    <th>Tickets</th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>{rows}</tbody>
            </table>

            </div>
        );
    }
}

export default ArtistTable;