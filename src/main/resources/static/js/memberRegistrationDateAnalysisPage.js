	$(function () {
			$('#example').DataTable({
				scrollX: true,
				"dom": 'lrtip'
			});
		})
		getRegistrationDatechartJs()

		function getRegistrationDatechartJs() {
			axios({
				url: 'http://localhost:8080/carbon/member/api/memberRegistrationDateAnalysis',
				method: 'GET',
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