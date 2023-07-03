const managerLogin = document.querySelector('#managerLogin');
const oneClick = document.querySelector('#oneClick');
const loginForm = document.querySelector('#loginForm');
const email = document.querySelector('#email');
const memberPwd = document.querySelector('#memberPwd');
const rememberMe = document.querySelector('#rememberMe');
managerLogin.addEventListener('click', function() {
	email.value = 'sa@gmail.com';
	memberPwd.value = '000000';
	rememberMe.checked=true;
	loginForm.submit();
})

oneClick.addEventListener('click', function() {
	email.value = 'xiaoming20230703@gmail.com';
	memberPwd.value = 'Carbon123';
	rememberMe.checked=true;
	loginForm.submit();
})
