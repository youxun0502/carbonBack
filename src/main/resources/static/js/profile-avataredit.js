const personal_avatar = document.getElementsByClassName('personal_avatar')[0];
const avatarselects = document.getElementById('fullmodal-avatar');
const userlogin = document.getElementById('memberId');
const avatar_box = document.getElementById('edit-avatar');
const frame_box = document.getElementById('edit-frame');
const bg_box = document.getElementById('edit-bg');

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
			let avatars;
			let frames;
			let bgs;



			avatar_box.innerHTML += ` <div class="col-lg-8">
				<div class="nk-popup-gallery">
                <div class="row vertical-gap">`;
			for (let i = 0; i < bonusLog.length; i++) {
				if (bonusLog[i].bonusitem.bonusType == "avatar") {
					avatar_box.innerHTML += `				
						
						<div class="col-lg-4 col-md-6">
                        <div class="nk-gallery-item-box">
						
                                
						<img class="nk-gallery-item" src="/carbon/downloadImage/${bonusLog[i].bonusitem.bonusId}">
						</div>
						</div>
						
					`;
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

			avatar_box.innerHTML += `</div>
			</div>
			</div>`;



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
