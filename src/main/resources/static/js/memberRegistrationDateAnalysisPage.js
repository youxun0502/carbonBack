$(function () {
	$('#example').DataTable({
		scrollX: true,
		"dom": 'lrtip'
	});

	$('#gender').DataTable({
		scrollX: true,
		"dom": 'lrtip'
	});
})

const year = document.querySelector("#year");
getRegistrationDatechartJs(year.value);
getGenderChartJs(year.value);

const registerYearForm = document.querySelector("#registerYearForm");
year.addEventListener('change', function () {

	registerYearForm.submit();
})

function getRegistrationDatechartJs(year1) {
	axios({
		url: 'http://localhost:8080/carbon/member/api/memberRegistrationDateAnalysis',
		method: 'GET',
		params: { year: year1 },
		headers: { 'Content-Type': 'application/x-www-form-urlencode' }
	})
		.then(response => {
			const chartJS = document.querySelector('#chartJS');
			let responseData = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
			console.log(response.data);
			for (let dataInResponse of response.data) {
				responseData[dataInResponse[0] - 1] = dataInResponse[1];
				console.log(responseData[dataInResponse[0] - 1] = dataInResponse[1]);
			}
			const labels = ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月']
			const data = {
				labels: labels,
				datasets: [
					{
						label: '註冊人數',
						data: responseData,
						borderColor: 'rgb(255, 255, 255)',
						backgroundColor: 'rgb(255, 255, 255)',
						pointBackgroundColor: 'rgb(255, 255, 255)',
						pointBorderColor: 'rgb(255, 255, 255)',
						pointRadius: 8,
						pointHoverRadius: 10

					}
				]
			}

			new Chart(chartJS, {
				type: 'line',
				data: data,
				options: {
					scales: {
						x: {
							ticks: {
								color: 'rgb(255, 255, 255)',
								font: { size: 24 },
							}
						},
						y: {
							ticks: {
								color: 'rgb(255, 255, 255)',
								font: { size: 24 },
							}
						},
					},
					responsive: true,
					plugins: {
						legend: {
							labels: {
								color: 'rgb(255, 255, 255)',
								font: {
									size: 30
								}
							},
							position: 'top',
						},
						title: {
							display: true,
							text: '每月會員註冊人數',
							color: 'rgb(255, 255, 255)',
							font: {
								size: 30
							},
							position: 'bottom'
						}
					}
				},
			}
			)

		})
}
function getGenderChartJs(year1) {
	axios({
		url: 'http://localhost:8080/carbon/member/api/memberRegistrationgGenderAnalysis',
		method: 'GET',
		params: { year: year1 },
		headers: { 'Content-Type': 'application/x-www-form-urlencode' }
	})
		.then(response => {
			console.log(response.data);// 0是人數 1是性別
			const genderCharJs = document.querySelector("#genderCharJs")
			let responseData = [];
			let labels = [];
			let backgroundColorArray = [];
			response.data[0].forEach(element => {
				responseData.push(element);
			})

			response.data[1].forEach(element => {
				if (element == 1) {
					labels.push('男');
					backgroundColorArray.push('deepskyblue');
				} else {
					labels.push('女');
					backgroundColorArray.push('rgb(249, 71, 71)');
				}
			})


			const data = {
				labels: labels,
				datasets: [
					{
						label: '人數',
						data: responseData,
						backgroundColor: backgroundColorArray,
						hoverOffset: 4
					}
				]
			}

			chart = new Chart(genderCharJs, {
				type: 'doughnut',
				data: data,
				options: {
					plugins: {
						legend: {
							labels: {
								color: 'white',
								font: {
									size: 24
								}
							}
						}
					},
					tooltips: {
						callbacks: {
							labelFontSize: function (tooltipItem, chart) {
								return 24; // 设置字体大小为16像素
							}
						}
					}
				}
			}
			)
		})
}