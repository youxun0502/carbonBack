$(function () {
	$('#example').DataTable({
		scrollX: true,
		"dom": 'lrtip'
	});
})
updateBtn();
deleteMember();
restoreDelete();
const modalId = document.querySelector('#modalId');
const modalEmail = document.querySelector('#modalEmail');

const idEmail = document.querySelector('#idEmail')
const userId = document.querySelector('#userId');
const memberPwd = document.querySelector('#memberPwd');
const memberName = document.querySelector('#memberName');
const birthday = document.querySelector('#birthday');
const phone = document.querySelector('#phone');
const registration = document.querySelector('#registration');
const gender = document.querySelector('#gender');
const level = document.querySelector('#level');
const account = document.querySelector('#account');
const showSeachName = document.querySelector('#showSeachName')
let updateData;

//set modal 
function updateBtn() {
	let updateBtn = document.querySelectorAll('.btn-update');
	updateBtn.forEach(function (button) {
		button.addEventListener('click', () => {
			updateData = document.querySelector(`#id${button.dataset.id}`);
			let id = button.dataset.id;
			axios({
				url: 'http://localhost:8080/carbon/member/api/getUpdateData',
				method: 'get',
				params: {
					"id": id
				},
				headers: { 'Content-Type': 'application/x-www-form-urlencode' }
			})
				.then(response => {
					idEmail.innerText = `${response.data.innerId} - ${response.data.email}`;
					modalId.value = response.data.innerId;
					modalEmail.value = response.data.email
					userId.value = response.data.id;
					memberPwd.value = response.data.pwd;
					memberName.value = response.data.name
					birthday.value = response.data.birthday
					phone.value = response.data.phone
					registration.value = response.data.registration
					gender.value = response.data.gender
					level.value = response.data.level
					account.value = response.data.account

				})
				.catch(err => {
					console.log('err: ' + err);
				})
		})
	})

	//update modal
	let update = document.querySelector('#update');
	update.addEventListener('click', function () {
		let update_id = modalId.value;
		let update_email = modalEmail.value;
		let update_userId = userId.value;
		let update_memberPwd = memberPwd.value;
		let update_memberName = memberName.value;
		let update_origin_birthday = birthday.value;
		let update_phone = phone.value;
		let update_origin_registration = registration.value;
		let update_gender = gender.value;
		let update_level = level.value;
		let update_account = account.value;
		var options = {
			year: "numeric",
			month: "2-digit",
			day: "2-digit",

		};
		let update_birthday = new Date(update_origin_birthday).toLocaleDateString('zh', options).replace(/\//g, '-');
		let update_registration = new Date(update_origin_registration).toLocaleDateString('zh', options).replace(/\//g, '-');
		axios({
			url: 'http://localhost:8080/carbon/member/api/update',
			method: 'put',
			data: {
				"innerId": update_id,
				"id": update_userId,
				"email": update_email,
				"pwd": update_memberPwd,
				"name": update_memberName,
				"birthday": update_birthday,
				"gender": update_gender,
				"phone": update_phone,
				"registration": update_registration,
				"level": update_level,
				"account": update_account
			},
			headers: { 'Content-Type': 'application/json' }
		})
			.then(response => {

				Swal.fire({
					position: 'center',
					icon: 'success',
					title: 'udpate success',
					showConfirmButton: false,
					timer: 1000
				})
				let siblings = Array.from(updateData.parentNode.children).filter(function (child) {
					return child !== updateData;
				});
				siblings[0].innerText = update_userId;
				siblings[1].innerText = update_email;
				siblings[2].innerText = update_memberPwd;
				siblings[3].innerText = update_memberName;
				siblings[4].innerText = update_origin_birthday;
				siblings[5].innerText = update_gender;
				siblings[6].innerText = update_phone;
				siblings[7].innerText = update_level;
				siblings[8].innerText = update_account;
				siblings[9].innerText = update_origin_registration;
			})
			.catch(err => {
				console.log('err: ' + err);
			})
	})

}



/////////////////////////////////// delete //////////////////////////////////////


function deleteMember() {


	let deleteBtn = document.getElementsByClassName('btn-delete');
	for (i = 0; i < deleteBtn.length; i++) {
		deleteBtn[i].addEventListener('click', function (e) {
			let deleteId = this.getAttribute('data-id');
			deleteData = document.querySelector(`#id${deleteId}`);
			Swal.fire({
				title: 'Are you sure?',
				text: "Members will not be able to log in",
				icon: 'warning',
				showCancelButton: true,
				confirmButtonColor: '#3085d6',
				cancelButtonColor: '#d33',
				confirmButtonText: 'freeze'
			})

			let swal2Confirm = document.querySelector('.swal2-confirm');
			swal2Confirm.addEventListener('click', function () {
				console.log(deleteId);
				axios({
					url: 'http://localhost:8080/carbon/member/api/delete',
					method: 'put',
					data: {
						id: deleteId
					}
				})
					.then(function (response) {
						let siblings = Array.from(deleteData.parentNode.children).filter(function (child) {
							return child !== deleteData;
						})
						siblings[10].innerText = 2;
						const updateBtn = siblings[11].querySelector('.btn-update');
						if (updateBtn) {
							updateBtn.setAttribute('hidden', true);
						}

						const restoreBtn = siblings[12].querySelector('.btn-restore');
						if (restoreBtn) {

							restoreBtn.removeAttribute('hidden');

						}
						const deleteBtn = siblings[12].querySelector('.btn-delete');
						if (deleteBtn) {
							deleteBtn.setAttribute('hidden', true);
						}
					})
					.then(function () {
						Swal.fire(
							'Frozen!',
							'Member has been frozen.',
							'success'
						)
					})
					.catch(err => {
						console.log('err: ' + err);
						Swal.fire({
							icon: 'error',
							title: 'Oops...',
							text: 'Something went wrong!',
						})
					})
			})
		})
	}
}


/////////////////////////////// restore delete //////////////////////////////////////

function restoreDelete() {
	let restoreBtn = document.getElementsByClassName('btn-restore');
	for (i = 0; i < restoreBtn.length; i++) {
		restoreBtn[i].addEventListener('click', function (e) {
			let restoreId = this.getAttribute('data-id');
			restoreData = document.querySelector(`#id${restoreId}`);
			axios({
				url: 'http://localhost:8080/carbon/member/api/restore',
				method: 'put',
				data: {
					id: restoreId
				}
			})
				.then(function (response) {
					let siblings = Array.from(restoreData.parentNode.children).filter(function (child) {
						return child !== restoreData;
					})
					siblings[10].innerText = 1;
					const updateBtn = siblings[11].querySelector('.btn-update');
					if (updateBtn) {
						updateBtn.removeAttribute('hidden');
					}

					const restoreBtn = siblings[12].querySelector('.btn-restore');
					if (restoreBtn) {

						restoreBtn.setAttribute('hidden', true);

					}
					const deleteBtn = siblings[12].querySelector('.btn-delete');
					if (deleteBtn) {
						deleteBtn.removeAttribute('hidden');
					}
				})
				.then(function () {
					Swal.fire({
						title: 'Finish',
						text: 'The data has been restored',
						icon: 'success',
						timer: 1000,
						showConfirmButton: false
					}
					)
				})
				.catch(err => {
					console.log('err: ' + err);
					Swal.fire({
						icon: 'error',
						title: 'Oops...',
						text: 'Something went wrong!',
					})
				})
		})
	}
}




/////////////////////////////////// search ///////////////////////////////////////////////

const memberSearch = document.querySelector('#memberName_search');
memberSearch.addEventListener('input', function () {
	let inputName = this.value;
	axios({
		url: 'http://localhost:8080/carbon/member/api/seachByName',
		method: 'get',
		params: { "name": inputName },
		headers: { 'Content-Type': 'application/x-www-form-urlencode' }
	})
		.then(response => {

			console.log(JSON.stringify(response.data));
			htmlMaker(response.data);
		})
		.catch(err => {
			console.log('err ' + err);
		})
})


function htmlMaker(response) {
	$('#example').DataTable().destroy();
	let innerHtml = '';
	response.forEach(function (member, index) {
		innerHtml += `<tr> 
												<td id="id${member.innerId}">${member.innerId}</td>
												<td>${member.id}</td>
												<td>${member.email}</td>
												<td style="overflow:hidden;">${member.pwd}</td>
												<td>${member.name}</td>
												<td>${member.birthday}</td>
												<td>${member.gender}</td>
												<td>${member.phone}</td>
												<td>${member.level}</td>
												<td>${member.account}</td>
												<td>${member.registration}</td>
												<td>${member.status}</td>
												`;
		if (member.status == 1) {
			innerHtml += `<td>
													<button data-id="${member.innerId}" class="btn btn-info btn-update" 	data-bs-toggle="modal" data-bs-target="#exampleModal">更新</button>
												</td>
												<td>
														<button data-id="${member.innerId}" class="btn-outline-success btn-restore" hidden=true>復原</button>
														<button data-id="${member.innerId}" class="btn btn-danger btn-delete">凍結
														</button>
												</td>`;
		} else if (member.status == 2) {
			innerHtml += `<td>
													<button data-id="${member.innerId}" class="btn btn-info btn-update" 	data-bs-toggle="modal" data-bs-target="#exampleModal" hidden=true>更新</button>
												</td>
												<td>
														<button data-id="${member.innerId}" class="btn btn-outline-success btn-restore">復原</button>
														<button data-id="${member.innerId}" class="btn btn-danger btn-delete" hidden=true>凍結
														</button>
												</td>`;
		}
	})

	innerHtml += `</tr>`;
	showSeachName.innerHTML = innerHtml;

	updateBtn()
	deleteMember();
	restoreDelete();
	$('#example').DataTable({
		scrollX: true,
		"dom": 'lrtip'
	});
}

