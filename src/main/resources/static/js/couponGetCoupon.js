$(function () {
	$('#animate').removeClass('d-none');
	$('#animate').addClass('animate__fadeInUp');
	setTimeout(function () {
		$('#title').removeClass('d-none');
		$('#title').addClass('animate__flipInX');
	}, 800);
	clickToRotate();

});

function clickToRotate() {

	const clickBtn = document.querySelector(".click");
	clickBtn.addEventListener('click', function () {


		getCoupon()

	})


}


function getCoupon() {
	const roulette = document.querySelector("#roulette");
	let memberId = document.querySelector('#memberId').innerText;
	console.log(memberId);
	axios({
		url: "http://localhost:8080/carbon/coupon/api/getCoupon",
		method: "get",
		params: {
			'memberId': memberId
		},
		headers: { 'Content-Type': 'application/x-www-form-urlencode' }
	})
		.then(response => {
			if (response.data == '您今日已參加過抽獎了!!!') {
				Swal.fire({
					title: response.data,
					icon: 'info',
				})
			} else {
				roulette.classList.add('rotate');
				setTimeout(function () {
					Swal.fire({
						title: response.data,
						icon: 'success',
					})
				}, 3000);

			}

		})
		.catch(err => {
			console.log(err);
		})
}