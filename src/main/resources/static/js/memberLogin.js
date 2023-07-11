const managerLogin = document.querySelector('#managerLogin');
const oneClick = document.querySelector('#oneClick');
const loginForm = document.querySelector('#loginForm');
const email = document.querySelector('#email');
const memberPwd = document.querySelector('#memberPwd');
const rememberMe = document.querySelector('#rememberMe');
const forgetPasswordBtn = document.querySelector('#forgetPasswordBtn');
const row = document.querySelector('.row');
const body = document.querySelector('body');
const forget = document.querySelector('#forget');
managerLogin.addEventListener('click', function () {
	email.value = 'sa@gmail.com';
	memberPwd.value = '000000';
	rememberMe.checked = true;
	loginForm.submit();
})

oneClick.addEventListener('click', function () {
	email.value = 'xiaoming20230703@gmail.com';
	memberPwd.value = 'Carbon123';
	rememberMe.checked = true;
	loginForm.submit();
})

forgetPasswordBtn.addEventListener('click', function () {
	let email = forget.value;
	axios({
		url: 'http://localhost:8080/carbon/main/api/forgetPwd',
		method: 'get',
		params: {
			email: email
		},
		header: { 'Content-Type': 'application/x-www-form-urlencode' }
	})
		.then(response => {
			Swal.fire({
				title: '已傳送驗證連結',
				text: ' 驗證連結已傳送至註冊信箱',
				icon: 'success',
				timer: '1500',
				showConfirmButton: false
			})
			row.style.marginTop = '25px';
			setTimeout(function () {
				row.style.marginTop = '0';
			}, 1650)

		})
		.catch(err => {
			Swal.fire({
				title: '伺服器發生錯誤',
				text: ' 請聯繫客服人員',
				icon: 'error',
				timer: '1500',
				showConfirmButton: false
			})
			row.style.marginTop = '25px';
			setTimeout(function () {
				row.style.marginTop = '0';
			}, 1650)
		})


})


