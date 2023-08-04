const insertCouponForm = document.querySelector("#insertCouponForm");
const insertBtn = document.querySelector("#insertBtn");
insertCouponForm.addEventListener('submit', function (event) {
	event.preventDefault();
	Swal.fire({
		title: '新增成功',
		text: '優惠券已成功加入系統',
		icon: 'success',
		showConfirmButton: false,
		timer: 1000
	})
	setTimeout(function () {
		insertCouponForm.submit();
	}, 1000)
})


const typeName = document.querySelector("#typeName");
const discount = document.querySelector("#discount");
const couponName = document.querySelector("#couponName");
const weight = document.querySelector("#weight");
const status = document.querySelector("#status");
const autoInputBtn = document.querySelector("#autoInput");

autoInputBtn.addEventListener('click', function () {
	typeName.value = 1;
	discount.value = 0.8;
	couponName.value = '射擊類遊戲8折';
	weight.value = 50;
	status.value = 2;
})