const logoutBtn = document.querySelector('.logout_btn');
const form = document.querySelector('#logoutForm');
logoutBtn.addEventListener('click', function () {
	Swal.fire({
		position: 'center',
		icon: 'success',
		title: '登出成功',
		showConfirmButton: false,
		timer: 1000
	})
	setTimeout(function () {
		form.submit();
	}, 1000);

})