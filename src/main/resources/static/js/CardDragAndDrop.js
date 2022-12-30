/*const boxes = document.querySelectorAll('.box');

boxes.forEach(box => {
    box.addEventListener('dragenter', dragEnter)
    box.addEventListener('dragover', dragOver);
    box.addEventListener('dragleave', dragLeave);
    box.addEventListener('drop', drop);
});
*/

function onDragStart(event) {
	event.dataTransfer.setData('text/plain', event.target.id.split("")[4]);
	setTimeout(() => {
        event.target.classList.add('hide');
    }, 0);
    requestAvailablePlaces();
}

function onDragEnd(e) {
	reloadHand(handSave);
	let places = document.querySelectorAll('.readyToDrop');
	places.forEach(place => {
		place.classList.remove('readyToDrop');
		place.removeEventListener('dragenter', dragEnter);
		place.removeEventListener('dragover', dragOver);
		place.removeEventListener('dragleave', dragLeave);
		place.removeEventListener('drop', dragDrop);
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