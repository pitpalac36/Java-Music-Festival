import React from  'react';
import ArtistTable from './Artist';
import Form from './Form';
import {GetArtists, DeleteArtist, AddArtist, UpdateArtist} from './utils/rest'


class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {artists:[],
        deleteFunc:this.deleteFunc.bind(this),
        addFunc:this.addFunc.bind(this),
        updateFunc:this.updateFunc.bind(this)
        }
    }

    addFunc(artist) {
        console.log('inside add Func ' + artist);
        AddArtist(artist)
            .then(res => GetArtists())
            .then(artists => this.setState({artists}))
            .catch(erorr => console.log('eroare add ',erorr));
    }


    deleteFunc(artist){
        console.log('inside deleteFunc ' + artist);
        DeleteArtist(artist)
            .then(res => GetArtists())
            .then(artists => this.setState({artists}))
            .catch(error => console.log('eroare delete', error));
    }


    updateFunc(artist){
      console.log('inside updateFunc ' + artist);
      UpdateArtist(artist)
          .then(res => GetArtists())
          .then(artists => this.setState({artists}))
          .catch(error => console.log('eroare update', error));
  }



    componentDidMount(){
        console.log('inside componentDidMount')
        GetArtists().then(artists => {
          console.log(artists);
          this.setState({artists});
          });
    }

    render(){
        return(
            <div className="App">
                <h1>Music Festival Artists</h1>
                <Form addFunc={this.state.addFunc}/>
                <br/>
                 <ArtistTable artists={this.state.artists} deleteFunc={this.state.deleteFunc} updateFunc={this.state.updateFunc}/>
            </div>
        );
    }
}

export default App;