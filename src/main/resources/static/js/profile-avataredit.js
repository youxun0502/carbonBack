const personal_avatar = document.getElementsByClassName('personal_avatar')[0];
const avatarselects = document.getElementById('fullmodal-avatar');
const userlogin = document.getElementById('memberId');
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

//--------------------------click personal_avatar-------------------------------------------//
personal_avatar.addEventListener('click', function (e) {
	e.preventDefault();
	getBonusLog(userid);
});
//---------------------------get bonuslog-------------------------------------------------//
function getBonusLog(userid) {
	console.log("getBonusLog");
	axios({
		url: '/carbon/bonus-shop/api/findBonusLog',
		method: 'post',
		data: {
			memberId: userid,
		}
	})
		.then(res => {
			console.log("res:" + res.data[0].logId);
			bonusLog = res.data;

			avatarselects.innerHTML = infoHtmlString;
			avatarselects.classList.add("showDiv");
			console.log("DONE~~~~~~~~~~~~~~~");
		})
		.catch(err => {
			console.log("err:" + err);
		})
}

function htmlMaker(data) {
	if (avatarselects) {
		console.log("avatarselects is exist!!222222");
	} else {
		console.log("WROOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOONG");
	}
	let infoHtmlString = `
    <div class="fullmodal-avatar">
		<div class="modalContent modal_bg"></div>
		<div class="modalContent modal_active" tabindex="-1">
			<div class="modalPosition" tabindex="1">
				<div class="modalPosition_Content">
					<div class="modalPosition_topbar"></div>
					<div class="modalContainer">
						<div class="Header_Containner">
							<div>
								<div class="bonusfile_header Panel Focusable">BonusName</div>
								<div class="bonusfile_subheader">BonusType</div>
							</div>
						</div>
						<div class="bgPreview_Container">
							<div class="previewContainer itemPreviewContainer">IMG</div>

							<div class="itemButtonContainer">
								<div class="modalButton">
									<div class="pointer_Container">
										<div class="point_price">2000PRICE</div>
									</div>
								</div>
								<div class="modal_cancelButton">取消</div>
							</div>
						</div>
					</div>
				</div>

			</div>

		</div>
	</div>`;
	avatarselects.innerHTML = infoHtmlString;
	avatarselects.classList.add("showDiv");

}