//////////////////////////////////載入畫製圖表/////////////////////////////////
$(function () {
	$('#example').DataTable({
		scrollX: true,
		"dom": 'lrtip'
	});
})

getRandomchartJs()
////////////////////////////////////config////////////////////////////////////////////
const chartJS = document.querySelector('#chartJS')
let chart;

const btnDelete = document.querySelectorAll('.btn-delete');
const buttonNum = document.querySelector('#buttonNum');
btnDelete.forEach(function (button) {
	button.addEventListener('click', () => {
		let id = button.dataset.id;
		if (buttonNum.innerText > 1) {
			buttonNum.innerText = parseInt(buttonNum.innerText) - 1;
			changeStatus(id, 2);
		} else {
			Swal.fire({
				title: '停用失敗',
				text: '這已經是最後一筆，禁止停用',
				icon: 'error',
				showConfirmButton: false,
				timer: 1000
			})
		}
	})
})

const btnRestore = document.querySelectorAll('.btn-restore');

btnRestore.forEach(function (button) {
	button.addEventListener('click', () => {
		let id = button.dataset.id;
		buttonNum.innerText = parseInt(buttonNum.innerText) + 1;
		changeStatus(id, 1);
	})
})




const COLORS = [
	'#F7F6C5',
	'#DE7C5A',
	'#5BC0EB',
	'#AFD2E9',
	'#E3DFFF',
	'#61A0AF',
	'#227C9D',
	'#81559B',
	'#00A7E1',
	'#476C9B'
];


function getRandomchartJs() {
	axios({
		url: 'http://localhost:8080/carbon/coupon/api/couponMangement',
		method: 'GET',
		headers: { 'Content-Type': 'application/x-www-form-urlencode' }
	})
		.then(response => {
			console.log(response.data);
			buttonNum.innerText = response.data.length;
			let responseData = [];
			let labels = [];
			let backgroundColorArray = [];
			for (let coupon of response.data) {
				responseData.push(coupon.random);
				labels.push(coupon.couponId + ':' + coupon.couponName);
				backgroundColorArray.push(color());
			}

			const data = {
				labels: labels,
				datasets: [
					{
						label: '機率',
						data: responseData,
						backgroundColor: backgroundColorArray,
						hoverOffset: 4
					}
				]
			}

			chart = new Chart(chartJS, {
				type: 'doughnut',
				data: data,
				options: {
					plugins: {
						legend: {
							labels: {
								color: 'white',
								font: {
									size: 20
								}
							}
						}
					}
				}
			}
			)
		})
}

function color() {
	return COLORS[parseInt(Math.random() * 10) % COLORS.length];
}

function updateCharData(newlabelArray, newdataArray) {
	chart.data.labels = newlabelArray;
	chart.data.datasets[0].data = newdataArray;
	chart.update();
}

function updateFrozenTable(datas) {

	let isNew = 0;

	for (let data of datas) {
		let deleteData = document.querySelector(`#id${data.couponId}`);
		let siblings = Array.from(deleteData.parentNode.children).filter(function (child) {
			return child !== deleteData;
		})
		if (data.typeName == 'null') {
			siblings[0].innerHTML = '不限類型';
		} else {
			siblings[0].innerHTML = data.typeName;
		}
		siblings[1].innerText = data.discount;
		siblings[2].innerText = data.couponName;
		siblings[3].innerText = data.weight;
		if (data.status == 1) {
			siblings[4].innerText = '使用中';
		} else {
			siblings[4].innerText = '已停用';
		}

		siblings[5].innerText = data.random;

		//能拿到的data.status都是1


		const restoreBtn = siblings[6].querySelector('.btn-restore');
		if (restoreBtn) {

			restoreBtn.setAttribute('hidden', true);

		}
		const deleteBtn = siblings[6].querySelector('.btn-delete');
		if (deleteBtn && deleteBtn.hasAttribute('hidden')) {
			deleteBtn.removeAttribute('hidden');
		}

		if (data.couponId != data.updateInteger && isNew == 0) {
			let deleteData = document.querySelector(`#id${datas[0].updateInteger}`);
			let siblings = Array.from(deleteData.parentNode.children).filter(function (child) {
				return child !== deleteData;
			})
			siblings[4].innerText = '已停用';
			siblings[5].innerText = null;

			const restoreBtn = siblings[6].querySelector('.btn-restore');
			if (restoreBtn) {

				restoreBtn.removeAttribute('hidden');

			}
			const deleteBtn = siblings[6].querySelector('.btn-delete');
			if (deleteBtn) {
				deleteBtn.setAttribute('hidden', true);
			}
		} else {
			isNew = 1;
		}

	}

}


function changeStatus(id, status) {
	axios({
		url: "http://localhost:8080/carbon/coupon/api/couponFrozen",
		method: 'put',
		data: {
			couponId: id,
			status: status
		}
	})
		.then(response => {
			let responseData = [];
			let labels = [];

			for (let coupon of response.data) {
				responseData.push(coupon.random);
				labels.push(coupon.couponId + ':' + coupon.couponName);
			}

			updateCharData(labels, responseData);
			updateFrozenTable(response.data);

		})
		.catch(err => {
			console.log(err);
		})
}




