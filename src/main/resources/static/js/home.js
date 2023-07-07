const logoutBtn = document.querySelector('.logout_btn');
const id = document.querySelector('#memberId').textContent;
logoutBtn.addEventListener('click', function () {
	Swal.fire({
		position: 'center',
		icon: 'success',
		title: '登出成功',
		showConfirmButton: false,
		timer: 1000
	})
	setTimeout(function(){top.location='/carbon/main/logout?id='+id} , 1000);  
})