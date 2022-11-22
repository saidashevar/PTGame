window.onload = loadPage;
var gameId;
var login;
var lastGameSave;

function loadPage() {
	getGameID();
	connectToSocket(gameId);
	requestBoard(gameId);
}

function getGameID() {
	const params = new Proxy(new URLSearchParams(window.location.search), {
  		get: (searchParams, prop) => searchParams.get(prop),
	});
	gameId = params.gameid;
	login = params.login;
	let paragraph = document.getElementById("ShowId");
	paragraph.textContent = login+", "+paragraph.textContent+gameId;
}

function connectToSocket(gameId) {

    console.log("connecting to the game");
    let socket = new SockJS(url + "/gameplay");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("connected to the frame: " + frame);
        stompClient.subscribe("/topic/game-progress/" + gameId, function (response) {
            let data = JSON.parse(response.body);
            lastGameSave = data;
            console.log(data);
            gameStatus = data.status;
        })
    })
}

function requestBoard(gameId) {
    $.ajax({
        url: url + "/game/loadgame",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
			"string": gameId	
		}),
        success: function (data) {
			lastGameSave = data;
			loadBoard(data);
            loadHand(data);
            console.log("Successfully loaded board")
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function loadBoard(data) {
	let opponent = getOpponentLogin(data);
	for (let i = 1; i < 4; i++) {
        for (let j = 1; j < 4; j++) {
            let id = i + "_" + j;
            let place = document.getElementById("1_"+id); 
            place.textContent = data.players[login].board[i-1][j-1];
            place.addEventListener('dragenter', dragEnter);
	    	place.addEventListener('dragover', dragOver);
    		place.addEventListener('dragleave', dragLeave);
    		//place.addEventListener('drop', drop);           
            if (opponent != undefined) $("#2_" + id).text(data.players[opponent].board[i-1][j-1]);
        }
    }		
}

function loadHand (data) {
	let cardsInHand = data.players[login].hand.length;
	if (cardsInHand <= 5) {
		let hand = document.getElementById("cardHolder");
		for (let i = 0; i < cardsInHand; i++) {
			let card = document.createElement('li');
			addCardInHand(data, hand, i);
		}		
	}
}

function reloadHand(data) {
	const cardsInHand = document.querySelectorAll('li[id ^= "hand"]');
	cardsInHand.forEach(card => {
    	card.remove();
	});
	loadHand(data);
}