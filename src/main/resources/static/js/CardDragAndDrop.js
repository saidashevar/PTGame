/*const boxes = document.querySelectorAll('.box');

boxes.forEach(box => {
    box.addEventListener('dragenter', dragEnter)
    box.addEventListener('dragover', dragOver);
    box.addEventListener('dragleave', dragLeave);
    box.addEventListener('drop', drop);
});
*/

function onDragStart(e) {
	e.dataTransfer.setData('text/plain', e.target.id.split("")[4]);
	setTimeout(() => {
        e.target.classList.add('hide');
    }, 0);
    let query = 'td[id ^= "1_' + turnSave.wave + '"]';
    const boxes = document.querySelectorAll(query);
	boxes.forEach(box => {
		if (box.textContent == "") box.classList.add('readyToDrop'); //bad, i need to make it another way
	});
}

function onDragEnd(e) {
	reloadHand(handSave);
	let places = document.querySelectorAll('.readyToDrop');
	places.forEach(place => {
		place.classList.remove("readyToDrop");
	});
}

function dragEnter(e) {
    e.preventDefault();
    e.target.classList.add('drag-over');
}

function dragOver(e) {
    e.preventDefault();
    e.target.classList.add('drag-over');
}

function dragLeave(e) {
    e.target.classList.remove('drag-over');
}

function dragDrop(e) {
	e.target.classList.remove('drag-over');
	let place = e.target.id;
	let cardInHand = e.dataTransfer.getData('text/plain');
	console.log(handSave);
	placeCard(place.split("_")[2], handSave[cardInHand].id);
}

function onClickShowCard(e) {
	displayCard(e);
}