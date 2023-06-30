const logoutBtn = document.querySelector('.logout_btn');
const form = document.querySelector('#logoutForm');
logoutBtn.addEventListener('click', function () {
	Swal.fire({
		position: 'center',
		icon: 'success',
		title: 'logout success',
		showConfirmButton: false,
		timer: 1000
	})
	setTimeout(function () {
		form.submit();
	}, 1000);

})