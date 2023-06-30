const logoutBtn = document.querySelector('.logout_btn');
const form = document.querySelector('#logoutForm');
logoutBtn.addEventListener('click', function () {
	Swal.fire({
		position: 'center',
		icon: 'success',
<<<<<<< HEAD
		title: 'logout success',
=======
		title: '登出成功',
>>>>>>> a2d5c88cc0d8928ce9cfa9929c13e43720621142
		showConfirmButton: false,
		timer: 1000
	})
	setTimeout(function () {
		form.submit();
	}, 1000);

})