const personal_avatar = document.getElementsByClassName('personal_avatar')[0];
const avatarselects = document.getElementById('fullmodal-avatar');
const userlogin = document.getElementById('memberId');
const avatar_box = document.getElementById('edit-avatar');
const frame_box = document.getElementById('edit-frame');
const bg_box = document.getElementById('edit-bg');
const avatar_input = document.getElementById('the-new-avatar');
const frame_input = document.getElementById('the-new-frame');
const bg_input = document.getElementById('the-new-bg');
const avatar_confirm = document.getElementById('avatar-confirm');
const avatar_cancel = document.getElementById('avatar-cancel');
let select_avatar = avatar_input.getAttribute('value');
let select_frame = frame_input.getAttribute('value');
let select_bg = bg_input.getAttribute('value');


let userid = 0;
if (userlogin == null) {
	userid = -9999;
} else {
	userid = userlogin.innerText;
}
let bonusLog;

if (avatarselects) {
	console.log("avatarselects is exist!!");
}
editboxMaker();
//--------------------------------確認按鈕-----------------------------------------------------------//
avatar_confirm.addEventListener('click', function (e) {
	e.preventDefault();
	avatar_input.setAttribute('value', select_avatar);
	frame_input.setAttribute('value', select_frame);
	bg_input.setAttribute('value', select_bg);
	// console.log("av:" + avatar_input.getAttribute('value'));
	// console.log("fr:" + frame_input.getAttribute('value'));
	// console.log("bg:" + bg_input.getAttribute('value'));
	let user_avatar = document.getElementById('user_avatar');
	let user_frame = document.getElementById('user_frame');
	let user_bg = document.getElementById('user_bg');
	user_avatar.setAttribute('src', '/downloadImage/' + select_avatar);
	user_frame.setAttribute('src', '/downloadImage/' + select_frame);
	user_bg.setAttribute('src', '/downloadImage/' + select_bg);
	console.log("user:" + user_avatar.getAttribute("src"));

	let form_submit = document.getElementById('avatar-modal-form');
	form_submit.submit();
	// axios({
	// 	url: '/carbon/bonus-shop/api/selectEdit',
	// 	method: 'get',
	// 	params: {
	// 		memberId: userid,
	// 		avatar: select_frame,
	// 		frame: select_frame,
	// 		background: select_bg,
	// 	}
	// })
})


//--------------------------click personal_avatar-------------------------------------------//
personal_avatar.addEventListener('click', function (e) {
	e.preventDefault();
	getBonusLog(userid);
});
//---------------------------get bonuslog-------------------------------------------------//
function getBonusLog(userid) {
	console.log("getBonusLog    userid:" + userid);

}

function editboxMaker() {
	axios({
		url: '/carbon/bonus-shop/api/findBonusLog',
		method: 'post',
		data: {
			memberId: userid,
		}
	})
		.then(res => {
			console.log("res:" + res.data[0].bonusitem.bonusName);
			console.log("length:" + res.data.length);
			bonusLog = res.data;
			let avatar_index = 0;
			let frame_index = 0;
			let bgs_index = 0;
			let avatarhtml;
			let framehtml;
			let bghtml;


			avatarhtml = `<div class="avatar_collection">
				<div class="avatarcollection_RowWrapper">
                <div class="avatarcollection_SingleRow">`;
			framehtml = `<div class="avatar_collection">
				<div class="avatarcollection_RowWrapper">
                <div class="avatarcollection_SingleRow">`;
			bghtml = `<div class="avatar_collection">
				<div class="avatarcollection_RowWrapper">
                <div class="avatarcollection_SingleRow">`;

			for (let i = 0; i < bonusLog.length; i++) {
				/*-----------------------------------------------AVATAR 區域網頁生成------------------------------------*/
				if (bonusLog[i].bonusitem.bonusType == "avatar") {
					if (avatar_index >= 4) {
						avatarhtml += `</div>
						<div class="avatarcollection_SingleRow">`;
						avatar_index = 0;
					}
					if (bonusLog[i].bonusitem.bonusId == 1) {
						avatarhtml += `						
						<div class="avatarcollection_AvatarPreview avatarcollection_Large">
						<div class="select_avatar_frame">
					<img class="avatar_select" style="width:184px;" data-img=${bonusLog[i].bonusitem.bonusId} src="/carbon/img/userAvatarDefault.gif">
					</div>
					</div>
					`;
					} else {
						avatarhtml += `						
							<div class="avatarcollection_AvatarPreview avatarcollection_Large">
							<div class="select_avatar_frame">
						<img class="avatar_select" style="width:184px;"  data-img=${bonusLog[i].bonusitem.bonusId} src="/carbon/downloadImage/${bonusLog[i].bonusitem.bonusId}">
						</div>
						</div>
						`;
					}
					avatar_index++;
				}
				/*-----------------------------------------------FRAME 區域網頁生成------------------------------------*/
				else if (bonusLog[i].bonusitem.bonusType == "frame") {
					if (frame_index >= 4) {
						framehtml += `</div>
						<div class="avatarcollection_SingleRow">`;
						frame_index = 0;
					}
					if (bonusLog[i].bonusitem.bonusId == 2) {
						framehtml += `						
						<div class="avatarcollection_AvatarPreview avatarcollection_Large">
						<div class="select_frame_frame">
					<img class="frame_select" style="width:184px;"  data-img=${bonusLog[i].bonusitem.bonusId} src="/carbon/img/userFrameDefault.png">
					</div>
					</div>
					`;
					} else {

						framehtml += `						
							<div class="avatarcollection_AvatarPreview avatarcollection_Large">
							<div class="select_frame_frame">
						<img class="frame_select" style="width:184px;"  data-img=${bonusLog[i].bonusitem.bonusId} src="/carbon/downloadImage/${bonusLog[i].bonusitem.bonusId}">
						</div>
						</div>
						`;
					}
					frame_index++;
				}
				/*-----------------------------------------------BG 區域網頁生成------------------------------------*/
				else if (bonusLog[i].bonusitem.bonusType == "background") {
					if (frame_index >= 4) {
						bghtml += `</div>
						<div class="avatarcollection_SingleRow">`;
						bgs_index = 0;
					}
					if (bonusLog[i].bonusitem.bonusId == 3) {
						bghtml += `						
						<div class="avatarcollection_AvatarPreview avatarcollection_Large">
						<div class="select_bg_frame">
					<img class="bg_select" style="width:184px;"  data-img=${bonusLog[i].bonusitem.bonusId} src="/carbon/img/userBgDefault.jpg">
					</div>
					</div>
					`;
					} else {
						bghtml += `						
							<div class="avatarcollection_AvatarPreview avatarcollection_Large">
							<div class="select_bg_frame">
						<img class="bg_select" style="width:184px;"  data-img=${bonusLog[i].bonusitem.bonusId} src="/carbon/downloadImage/${bonusLog[i].bonusitem.bonusId}">
						</div>
						</div>
						`;
					}
					bgs_index++;
				}



			}
			avatarhtml += `</div>
			</div>
			</div>`;
			framehtml += `</div>
			</div>
			</div>`;
			bghtml += `</div>
			</div>
			</div>`;


			avatar_box.innerHTML = avatarhtml;
			frame_box.innerHTML = framehtml;
			bg_box.innerHTML = bghtml;


			edit_select();
		})
		.catch(err => {
			console.log("err:" + err);
		})

}

function edit_select() {
	let avatar_select = document.getElementsByClassName('avatar_select');
	let frame_select = document.getElementsByClassName('frame_select');
	let bg_select = document.getElementsByClassName('bg_select');

	for (let i = 0; i < avatar_select.length; i++) {
		avatar_select[i].addEventListener('click', function (e) {
			select_avatar = this.getAttribute('data-img');
			removeAvatarFrame(this);
		})
	}
	for (let i = 0; i < frame_select.length; i++) {
		frame_select[i].addEventListener('click', function (e) {
			select_frame = this.getAttribute('data-img');
			removeFrameFrame(this);
		})
	}
	for (let i = 0; i < bg_select.length; i++) {
		bg_select[i].addEventListener('click', function (e) {
			select_bg = this.getAttribute('data-img');
			removeBgFrame(this);
		})
	}
}
//------------------------------------------------選擇AVATAR的紅框------------------------------------------------------------------//
function removeAvatarFrame(e) {
	// 移除所有 select_avatar_frame 的 active 類別
	let aframes = document.getElementsByClassName('select_avatar_frame');
	for (let j = 0; j < aframes.length; j++) {
		// 判斷是否為目前點擊的元素的父元素
		if (aframes[j] === e.parentNode) {

			aframes[j].classList.add('active_select');
		} else {

			aframes[j].classList.remove('active_select');
		}

	}
}
function removeFrameFrame(e) {
	// 移除所有 select_avatar_frame 的 active 類別
	let aframes = document.getElementsByClassName('select_frame_frame');
	for (let j = 0; j < aframes.length; j++) {
		// 判斷是否為目前點擊的元素的父元素
		if (aframes[j] === e.parentNode) {

			aframes[j].classList.add('active_select');
		} else {

			aframes[j].classList.remove('active_select');
		}

	}
}
function removeBgFrame(e) {
	// 移除所有 select_avatar_frame 的 active 類別
	let aframes = document.getElementsByClassName('select_bg_frame');
	for (let j = 0; j < aframes.length; j++) {
		// 判斷是否為目前點擊的元素的父元素
		if (aframes[j] === e.parentNode) {

			aframes[j].classList.add('active_select');
		} else {

			aframes[j].classList.remove('active_select');
		}

	}
}

