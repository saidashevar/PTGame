let gameOn = false;
const url = 'http://localhost:8080';
var gameStatus;

function loadBoard(data) {
	let opponent = getOpponentLogin(data);
	for (let i = 1; i < 4; i++) {
        for (let j = 1; j < 4; j++) {
            let id = i + "_" + j;
            $("#1_" + id).text(data.players[login].board[i-1][j-1]);
            $("#2_" + id).text(data.players[opponent].board[i-1][j-1]);
        }
    }		
}

function loadHand(game) {
	
}

function playerChoice(squadnumber, i, j) {
    $.ajax({
        url: url + "/game/gameplay",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "gameId": gameId,
            "requester": login,
            "squad": squadnumber,
            "coordinateX": i,
            "coordinateY": j
        }),
        success: function (data) {
            loadBoard(data);
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function takeCard() {
    $.ajax({
	    url: url + "/game/takecard",
	    type: 'POST',
	    dataType: "json",
	    contentType: "application/json",
	    data: JSON.stringify({
	        "gameId": gameId,
	        "requester": login,
	    }),
	    success: function (data) {
	        loadHand(data);
	    },
	    error: function (error) {
	        console.log(error);
	    }
	})
}

function getOpponentLogin(game) {
	let opponent = "";
	if (game.logins[0] == login) opponent = game.logins[1];
	else opponent = game.logins[0];
	return opponent;
}

$("[id ^= 1],[id ^= 2]").click(function () {
	if (gameStatus != "NEW") {
		let id = $(this).attr('id');
    	playerChoice(id.split("_")[0], id.split("_")[1], id.split("_")[2]);		
	}
});

$("#deckPlayer2").click(function () {
	takeCard();
});