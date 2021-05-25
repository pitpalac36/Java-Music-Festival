import {BASE_URL} from './consts';

function status(response) {
    console.log('response status '+response.status);
    if (response.status >= 200 && response.status < 300) {
        return Promise.resolve(response)
    } else {
        return Promise.reject(new Error(response.statusText))
    }
}

function json(response) {
    return response.json()
}

export function GetArtists(){
    var myHeaders = new Headers();
    myHeaders.append('Access-Control-Allow-Headers','Origin, X-Requested-With, Content-Type, Accept');
    myHeaders.append('Access-Control-Allow-Origin', '*');
    myHeaders.append('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE');
    myHeaders.append('Accept', 'application/json');
    myHeaders.append("Content-Type","application/json");
    var myInit = { method: 'GET',
        headers: myHeaders,
        mode: 'cors'
    };
    var request = new Request(BASE_URL, myInit);

    console.log('Inainte de fetch pentru '+ BASE_URL)

    return fetch(request)
        .then(status)
        .then(json)
        .then(data => {
            console.log('Request succeeded with JSON response', data);
            return data;
        }).catch(err => {
            if (err.response) {
              console.log('client received an error response (5xx, 4xx)');
            } else if (err.request) {
              console.log('client never received a response, or request never left');
            } else {
              console.log('anything else');
            }
            return err;
        });

}

export function DeleteArtist(id){
    console.log('inainte de fetch delete')
    var myHeaders = new Headers();
    myHeaders.append('Access-Control-Allow-Origin', '*');
    myHeaders.append('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS');
    myHeaders.append('Access-Control-Allow-Headers', 'Content-Type');
    myHeaders.append('Access-Control-Allow-Credentials', true);
    myHeaders.append("Accept", "application/json");
    myHeaders.append("Content-Type","application/json");

    var antet = { method: 'DELETE',
        headers: myHeaders,
        mode: 'cors'
    };

    var deleteUrl = BASE_URL + '/' + id;

    return fetch(deleteUrl, antet)
        .then(status)
        .then(response => {
            console.log('Delete status ' + response.status);
            return response.text();
        }).catch(e => {
            console.log('error ' + e);
            return Promise.reject(e);
        });

}

export function AddArtist(artist) {
    console.log('inainte de fetch post' + JSON.stringify(artist));
    var myHeaders = new Headers();
    myHeaders.append('Access-Control-Allow-Origin', '*');
    myHeaders.append('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS');
    myHeaders.append('Access-Control-Allow-Headers', 'Content-Type');
    myHeaders.append('Access-Control-Allow-Credentials', true);
    myHeaders.append("Accept", "application/json");
    myHeaders.append("Content-Type","application/json");

    var antet = { method: 'POST',
        headers: myHeaders,
        mode: 'cors',
        body:JSON.stringify(artist)
    };

    return fetch(BASE_URL,antet)
        .then(status)
        .then(response => {
            return response.text();
        }).catch(error => {
            console.log('Request failed', error);
            return Promise.reject(error);
        });
}


export function UpdateArtist(artist) {
    console.log('inainte de fetch put' + JSON.stringify(artist));

    var myHeaders = new Headers();
    myHeaders.append('Access-Control-Allow-Origin', '*');
    myHeaders.append('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS');
    myHeaders.append('Access-Control-Allow-Headers', 'Content-Type');
    myHeaders.append('Access-Control-Allow-Credentials', true);
    myHeaders.append("Accept", "application/json");
    myHeaders.append("Content-Type","application/json");

    var antet = { method: 'PUT',
        headers: myHeaders,
        mode: 'cors',
        body:JSON.stringify(artist)};

    var updateUrl = BASE_URL + '/' + artist.id;

    return fetch(updateUrl, antet)
        .then(status)
        .then(response => {
            return response.text();
        }).catch(error => {
            console.log('Request failed', error);
            return Promise.reject(error);
        });
}