const personal_avatar = document.getElementsByClassName('personal_avatar')[0];
const avatarselects = document.getElementById('fullmodal-avatar');
const userlogin = document.getElementById('memberId');
const avatar_box = document.getElementById('edit-avatar');
const frame_box = document.getElementById('edit-frame');
const bg_box = document.getElementById('edit-bg');
let select_avatar = 0;
let select_frame = 0;
let select_bg = 0;


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
			let frames;
			let bgs;
			let avatarhtml;


			avatarhtml = `<div class="avatar_collection">
				<div class="avatarcollection_RowWrapper">
                <div class="avatarcollection_SingleRow">`;
			for (let i = 0; i < bonusLog.length; i++) {
				if (bonusLog[i].bonusitem.bonusType == "avatar") {
					if (avatar_index >= 4) {
						avatarhtml += `</div>
						<div class="avatarcollection_SingleRow">`;
						avatar_index = 0;
					}
					avatarhtml += `						
						<div class="avatarcollection_AvatarPreview avatarcollection_Large">
						<div class="select_avatar_frame">
					<img class="avatar_select"  data-img=${bonusLog[i].bonusitem.bonusId} src="/carbon/downloadImage/${bonusLog[i].bonusitem.bonusId}">
					</div>
					</div>
					`;
					avatar_index++;
					// <div class="avatarcollection_AvatarRowSpace avatar_select" data-img=${bonusLog[i].bonusitem.bonusId}></div>
				}
				else if (bonusLog[i].bonusitem.bonusType == "frame") {
					frame_box.innerHTML += `
					<div class="personal_avatar">
					<div class="personal_avatar_innersize">
						<div class="profile_avatar_frame">
							
						</div>
		
						<img class="profile_avatar_img" src="/carbon/downloadImage/${bonusLog[i].bonusitem.bonusId}">
					</div>
				</div>`;
				}
				else if (bonusLog[i].bonusitem.bonusType == "background") {
					bg_box.innerHTML += `
					<div class="personal_avatar">
					<div class="personal_avatar_innersize">
						<div class="profile_avatar_frame">
							
						</div>
		
						<img class="profile_avatar_img" src="/carbon/downloadImage/${bonusLog[i].bonusitem.bonusId}">
					</div>
				</div>`;
				}

			}

			avatarhtml += `</div>
			</div>
			</div>`;
			avatar_box.innerHTML = avatarhtml;


			edit_select();
			// 	avatar_box.innerHTML = `
			// 	<div class="personal_avatar">
			// 	<div class="personal_avatar_innersize">
			// 		<div class="profile_avatar_frame">

			// 		</div>

			// 		<img class="profile_avatar_img" src="/carbon/downloadImage/${bonusLog[0].bonusitem.bonusId}">
			// 	</div>
			// </div>`;


			console.log("DONE~~~~~~~~~~~~~~~");
		})
		.catch(err => {
			console.log("err:" + err);
		})

}

function edit_select() {
	let avatar_select = document.getElementsByClassName('avatar_select');

	for (let i = 0; i < avatar_select.length; i++) {
		avatar_select[i].addEventListener('click', function (e) {
			removeAvatarFrame();
			console.log("avatar_select[" + i + "]:" + this.parentElement.innerHTML);
			// 為目前點擊的 select_avatar_frame 添加 active 類別
			// this.parentElement.classList.add('active_select');

		})
	}
}

function removeAvatarFrame() {
	// 移除所有 select_avatar_frame 的 active 類別
	let aframes = document.getElementsByClassName('select_avatar_frame');
	console.log("aframes " + aframes[2].classList);
	for (let j = 0; j < frames.length; j++) {
		// 判斷是否為目前點擊的元素的父元素
		if (aframes[j].parentNode === this.parentNode) {
			console.log("aframes[j].classList.add('active');");
			aframes[j].classList.add('active');
		} else {
			console.log("aframes[j].classList.remove('active');");
			aframes[j].classList.remove('active');
		}
	}
}