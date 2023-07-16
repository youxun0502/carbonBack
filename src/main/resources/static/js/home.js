const logoutBtn = document.querySelector('.logout_btn');
const id = document.querySelector('#memberId').textContent;
logoutBtn.addEventListener('click', function () {
	Swal.fire({
		position: 'center',
		icon: 'success',
		title: '登出成功',
		showConfirmButton: false,
		timer: 1000
	})
	setTimeout(function () { top.location = '/carbon/main/logout?id=' + id }, 1000);
})

const fromUserId = document.querySelector("#memberId").innerText;
let url = "ws://localhost:8080/carbon/chat/" + fromUserId;
const socket = new WebSocket(url);


socket.onopen = function () {

	if (socket.readyState === WebSocket.OPEN) {
		console.log("連線成功");
		sendMessage(fromUserId);
	}

}

socket.onmessage = function (event) {

	event.data;
	console.log("Received message:", message);
}

socket.onclose = function () {
	console.log("連線關閉");
}

function sendMessage() {
	let fromUserId = document.querySelector("#memberId").innerText;
	console.log(fromUserId);
	const message = {
		"from": fromUserId,
		"to": '1',
		"content": '你好'
	}
	socket.send(JSON.stringify(message));
}


