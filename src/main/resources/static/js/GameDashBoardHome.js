var myChart;
//typeList chart.js
function chartJsPie(typeSort) {

	const ctx = document.getElementById('myChart');
	// 销毁之前的 Chart 实例
	if (myChart) {
		myChart.destroy();
	}
	const data = {
		labels: [
			typeSort.firstType,
			typeSort.secondType,
			typeSort.thirdType,
			typeSort.firstType + '+' + typeSort.secondType,
			typeSort.secondType + '+' + typeSort.thirdType,
			typeSort.firstType + '+' + typeSort.thirdType,
			typeSort.firstType + '+' + typeSort.secondType + '+' + typeSort.thirdType,
			'others'
		],
		datasets: [{
			label: '數據',
			data: [
				typeSort.firstTypeNum,
				typeSort.secondTypeNum,
				typeSort.thirdTypeNum,
				typeSort.firstSecondTypeNum,
				typeSort.secondthirdTypeNum,
				typeSort.firstThirdTypeNum,
				typeSort.firstsecondthirdTypeNum,
				(typeSort.allNum - typeSort.firstTypeNum - typeSort.secondTypeNum - typeSort.thirdTypeNum + typeSort.firstSecondTypeNum
					+ typeSort.secondthirdTypeNum + typeSort.firstThirdTypeNum - typeSort.firstsecondthirdTypeNum)],
			backgroundColor: [
				'rgba(255, 99, 132)',
				'rgba(255, 159, 64)',
				'rgba(255, 205, 86)',
				'rgba(75, 192, 192)',
				'rgba(54, 162, 235)',
				'rgba(153, 102, 255)',
				'rgba(201, 203, 207)',
				'#fff'
			],
			hoverOffset: 3,
			borderColor: 'rgb(0, 0, 0)',
		}]
	};
	myChart = new Chart(ctx, {
		type: 'doughnut',
		data: data,
		options: { plugins: { legend: { labels: { font: { size: 20, weight: 'bold' } } }, tooltip: { titleFont: { size: 16 } } } }
	});
}
function getSort(choose) {
	$.ajax({
		url: '/carbon/game/type/sort',
		type: 'GET',
		data: {
			choose: choose
		},
		success: function(response) {
			console.log(response);
			chartJsPie(response);

		},
		error: function(xhr, status, error) {
			console.log(圖片讀取失敗);
		}
	});
}


// 加載 datatable--------------------------------------------
$(function() {
	$.extend(true, $.fn.dataTable.defaults, {
		searching: false,
		// ordering: true,
	});

	$(document).ready(function() {
		$('#example1').DataTable();
		scrollX: true;
	});
})


//新增圖片=================================================================
var uploadButton = document.getElementById('uploadButton');
var fileInput = document.getElementById('fileInput');

uploadButton.addEventListener('click', function() {
	fileInput.click();
});

fileInput.addEventListener('change', function(event) {
	var file = event.target.files[0];
	if (file) {
		uploadFile(file);
	}
});

function uploadFile(file) {
	var formData = new FormData();
	formData.append('file', file);
	let gameId = $('#fileInput').attr('value');
	$.ajax({
		url: `/carbon/game/addImg/${gameId}`,
		type: 'POST',
		data: formData,
		processData: false,
		contentType: false,
		success: function(response) {
			console.log(response);
			$(".ImgInner").empty();
			printCarousel(response, gameId);
			showGames();
		},
		error: function(xhr, status, error) {
			console.error(error);
			printCarousel(response, gameId);
			showGames();
		}
	});
}
//刪除圖片============================================================================

$(document).ready(function() {
	function deleteImg() {
		$('.ImgInner').on('click', '.delete-icon', function() {
			let id = $(this).attr('value');
			let gameId = $(this).attr('name');

			$.ajax({
				url: `/carbon/game/deleteImg/${id}/${gameId}`,
				type: 'Delete',
				success: function(response) {
					console.log(response);
					$(".ImgInner").empty();
					printCarousel(response, gameId);
					showGames();
				},
				error: function(xhr, status, error) {
					printCarousel(response, gameId);
					showGames();
				}
			});
		});
	}

	deleteImg();
});

//取得標籤篩選的遊戲列表============================================================
function getGameListByType(button) {
	var typeName = $(button).text().substring(1);
	$('#TypeGameTitle').html('標籤：' + typeName);

	$.ajax({
		url: '/carbon/game/type/findGameListByType',
		type: 'GET',
		data: {
			typeName: typeName
		},
		success: function(response) {
			console.log(response);
			printGameListonType(response);
		},
		error: function(xhr, status, error) {
			console.log(遊戲列表讀取失敗);
		}
	});
}

function printGameListonType(gamesInfo) {
	let tbody = document.getElementById('showAllGameList');
	console.log(gamesInfo);
	tbody.innerHTML = '';

	gamesInfo.forEach(function(game) {

		let tr = document.createElement('tr');
		let gname = document.createElement('td');
		gname.textContent = game.gameName;

		let price = document.createElement('td');
		price.textContent = game.price;

		let gameTypes = document.createElement('td');
		gameTypes.setAttribute('class', 'types text-truncate ');
		game.gameTypes !== "" ? gameTypes.appendChild(createGameTypes(game.gameTypes)) : gameTypes.textContent = game.gameTypes;

		let gamPic = document.createElement('td');
		let img = document.createElement('img');
		img.setAttribute('alt', '');
		img.setAttribute('src',
			game.gamePhotoLists.length === 0 ? 'https://ps.w.org/404page/assets/icon.svg?rev=2451324' : '/carbon/gameFront/getImg/' + game.gamePhotoLists[0]);
		img.setAttribute('style', 'width:80px;height:60px');
		gamPic.appendChild(img);

		let buyerCount = document.createElement('td');
		buyerCount.textContent = game.buyerCount;

		let totalSales = document.createElement('td');
		totalSales.textContent = game.buyerCount * game.price;

		tr.appendChild(gamPic);
		tr.appendChild(gname);
		tr.appendChild(buyerCount);
		tr.appendChild(price);
		tr.appendChild(totalSales);
		tr.appendChild(gameTypes);
		tbody.appendChild(tr);
	})
}

//列印標籤的方法====================================================================
function createGameTypes(typeString) {
	var buttonStrings = typeString.split(',');

	var fragment = document.createDocumentFragment();

	for (var i = 0; i < buttonStrings.length; i++) {
		var button = document.createElement('button');
		button.setAttribute('class', 'types btn btn-outline-warning ms-3 mt-1');
		button.setAttribute('style', 'cursor:default;font-weight: bold');
		button.textContent = '#' + buttonStrings[i];
		fragment.appendChild(button);
	}

	return fragment;
}

//取得遊戲的圖片清單============================================================
function getPhotoIdList(button) {
	const output = document.querySelector('.ImgInner');
	var gameName = $(button).parent().siblings('.gameName').text();
	$('#ImgTitle').html('圖片管理：' + gameName);
	$(".ImgInner").empty();
	var gameId = $(button).val();

	$.ajax({
		url: '/carbon/game/getPhotoIdByGameId',
		type: 'GET',
		data: {
			GameId: gameId
		},
		success: function(response) {
			console.log(response.length);
			printCarousel(response, gameId);
			console.log(response)
			$('#fileInput').attr({ 'value': `${gameId}` });
		},
		error: function(xhr, status, error) {
			console.log(圖片讀取失敗);
		}
	});
}

//打印特定遊戲的圖片======================================================================
function printCarousel(response, gameId) {
	const output = document.querySelector('.ImgInner')
	if (response.length !== 0) {
		response.forEach((e, index) => {
			$('.carousel-control-next').removeClass('d-none');
			$('.carousel-control-prev').removeClass('d-none');
			let div = $("<div>").addClass(index === 0 ? 'carousel-item active my-5 image-container' : 'carousel-item my-5 image-container');
			let div1 = $('<div>').addClass('overlay');
			let div2 = $('<div>').addClass('delete-icon').attr({ 'value': `${e}`, 'name': `${gameId}` });
			let i = $('<i>').addClass('fas fa-trash');
			let img = $("<img>").attr({
				'src': `/carbon/gameFront/getImg/${e}`,
				'class': 'd-block w-100 ',
				'style': 'height:350px'
			});
			div.append(img);
			div.append(div1);
			div.append(div2.append(i));
			$(".ImgInner").append(div);
			console.log(e)
		})
		$('#imageTitle').html(`圖片數量 : ${response.length}張`)
	} else {
		const div = document.createElement('div')
		div.innerHTML += `<div>沒有任何圖片</div>`
		output.append(div)
		$('.carousel-control-next').addClass('d-none');
		$('.carousel-control-prev').addClass('d-none');
	}

}

//刪除遊戲的按鈕事件===================================================================

function deleteGame(button) {
	var value = $(button).val();
	var gameName = $(button).parent().siblings('.gameName').text();
	$('#deleteTitle').html('你確定要刪除 : ' + gameName);
	$("#selectedInput").val(value);
}
//上傳================================================
function uploadGame(button) {
	// 创建一个隐藏的文件输入框
	var gameName = $(button).val();
	var fileInput = $('<input type="file">').hide().appendTo('body');
	console.log(gameName)
	// 監聽文件
	fileInput.change(function() {
		var file = fileInput[0].files[0];

		var formData = new FormData();
		formData.append('gameFile', file);
		formData.append('gameName', gameName);
		$.ajax({
			url: '/carbon/game/uploadGame',
			type: 'Post',
			data: formData,
			processData: false,
			contentType: false,
			success: function(response) {
				Swal.fire('上傳成功目前成功上傳遊戲有：'+response, '', 'success').then(function() {

				});
			},
			error: function() {
				Swal.fire('上傳失敗', '', 'error');
			}
		});
	});

	// 触发点击事件
	fileInput.click();
}

//更新遊戲的按鈕事件====================================================================
function updateGame(button) {
	console.log($(button).parent().siblings().text())
	var gameId = $(button).parent().siblings('.gameId').text();
	$("#uGameId").val(gameId);
	var gameName = $(button).parent().siblings('.gameName').text();
	$(".updateTitle").text(gameName);
	$("#uGameName").val(gameName);
	var price = $(button).parent().siblings('.gamePrice').text();
	$("#uPrice").val(price);
	var createDate = $(button).parent().siblings('.createDate').text();
	$("#uCreateDate").val(createDate);
	var introduce = $(button).parent().siblings('.introduce').text();
	$("#uIntroduce").val(introduce);
	var types = $(button).parent().siblings('.types').text();
	var typeStrings = (types !== "") ? types.trim().replace(/#/g, ',').substring(1) : "";
	$("#uTypes").val(typeStrings);
	var buyCount = $(button).parent().siblings('.buyerCount').text();
	$("#uBuyerCount").val(buyCount);
}


//套件正則---------------------------------------------------------------------------------
function RunForm() {
	// Example starter JavaScript for disabling form submissions if there are invalid fields
	'use strict'
	// Fetch all the forms we want to apply custom Bootstrap validation styles to
	var forms = document.querySelectorAll('.needs-validation')
	// Loop over them and prevent submission
	Array.prototype.slice.call(forms)
		.forEach(function(form) {
			form.addEventListener('submit', function(event) {
				if (!form.checkValidity()) {
					event.preventDefault()
					event.stopPropagation()
				}

				form.classList.add('was-validated')
			}, false)
		})
		()

}

function oneClickEnter() {
	document.getElementById("copyName").value = "排數字大賽";
	document.getElementsByName("Price")[0].value = "500";
	document.getElementsByName("GameIntroduce")[0].value = "一款好玩又可以考驗頭腦的遊戲，傳說製作者自己都還沒有破關過";
	document.getElementsByName("GameTypes")[0].value = "趣味,動腦,數字,邏輯,好玩,java小遊戲";
}
//清除表單===================================================================
function clearButton() {
	console.log(1)
	$("#gameForm").trigger("reset");

}



//----------------------取得全部遊戲資訊============================================
function showGames() {
	let allTable = document.querySelector('#example');

	const xhttp = new XMLHttpRequest();
	xhttp.onload = function() {
		if (this.status === 200) {
			gamesInfos = JSON.parse(this.response);
			showGameContent(gamesInfos);
		}
	}
	xhttp.open("GET", "/carbon/game/getAllGames");
	xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	xhttp.send("action=query")

	allTable.style.display = "";
}

//創建table骨架 ----------------------------------------------------
function showGameContent(gamesInfos) {
	let tbody = document.getElementById('showAllGame');
	let today = document.getElementsByClassName('today');
	today[0].setAttribute('min', gamesInfos[0].today);
	today[0].setAttribute('value', gamesInfos[0].today);
	today[1].setAttribute('min', gamesInfos[0].today);
	console.log(gamesInfos);
	tbody.innerHTML = '';

	gamesInfos.forEach(function(game) {

		let tr = document.createElement('tr');
		let th = document.createElement('td');
		th.setAttribute('scope', 'row');
		th.setAttribute('class', 'gameId');
		th.textContent = game.gameId;

		let gname = document.createElement('td');
		gname.setAttribute('class', 'gameName');
		gname.textContent = game.gameName;

		let price = document.createElement('td');
		price.setAttribute('class', 'gamePrice');
		price.textContent = game.price;

		let createDate = document.createElement('td');
		createDate.setAttribute('class', 'createDate');
		createDate.textContent = game.createDate;

		let gameIntroduce = document.createElement('td');
		gameIntroduce.setAttribute('class', 'introduce text-truncate ');
		gameIntroduce.textContent = game.gameIntroduce;

		let gameTypes = document.createElement('td');
		gameTypes.setAttribute('class', 'types text-truncate ');
		game.gameTypes !== "" ? gameTypes.appendChild(createGameTypes(game.gameTypes)) : gameTypes.textContent = game.gameTypes;

		let gamPic = document.createElement('td');
		let img = document.createElement('img');
		img.setAttribute('alt', '');
		img.setAttribute('src',
			game.gamePhotoLists.length === 0 ? 'https://ps.w.org/404page/assets/icon.svg?rev=2451324' : '/carbon/gameFront/getImg/' + game.gamePhotoLists[0]);
		img.setAttribute('style', 'width:80px;height:60px');
		gamPic.appendChild(img);

		let buyerCount = document.createElement('td');
		buyerCount.setAttribute('class', 'buyerCount');
		buyerCount.textContent = game.buyerCount;

		let status = document.createElement('td');
		if (game.status == 1) {
			let icon = document.createElement('input');
			icon.setAttribute('type', 'checkbox');
			icon.setAttribute('value', '1');
			icon.setAttribute('checked', 'true');
			icon.setAttribute('id', 'iCheckbox' + game.gameId);
			//icon.setAttribute(' data-group-cls', 'btn-group-lg');
			status.appendChild(icon);
		} else if (game.status == 0) {
			let icon = document.createElement('input');
			icon.setAttribute('type', 'checkbox');
			icon.setAttribute('value', '0');
			icon.setAttribute('id', 'iCheckbox' + game.gameId);
			status.appendChild(icon);
		}

		let action = document.createElement('td');
		let imgButton = document.createElement('button');
		imgButton.setAttribute('value', game.gameId);
		imgButton.setAttribute('class', 'btn btn-success me-2');
		imgButton.setAttribute('data-bs-toggle', 'modal');
		imgButton.setAttribute('data-bs-target', '#updateImageModal');
		imgButton.setAttribute('onclick', 'getPhotoIdList(this)');
		let imgIcon = document.createElement('i');
		imgIcon.setAttribute('class', 'fa-regular fa-image fa-xl');
		imgIcon.setAttribute('style', 'color: #ffffff;');
		imgButton.appendChild(imgIcon);
		action.appendChild(imgButton);

		let updateButton = document.createElement('button');
		updateButton.setAttribute('value', game.gameId);
		updateButton.setAttribute('name', 'id');
		updateButton.setAttribute('class', 'btn btn-primary me-2');
		updateButton.setAttribute('data-bs-toggle', 'modal');
		updateButton.setAttribute('data-bs-target', '#updatemessage');
		updateButton.setAttribute('onclick', 'updateGame(this)');
		let updateIcon = document.createElement('i');
		updateIcon.setAttribute('class', 'fa-solid fa-pen-to-square fa-xl');
		updateIcon.setAttribute('style', 'color: #ffffff;');
		updateButton.appendChild(updateIcon);
		action.appendChild(updateButton);
		//插入 更新彈跳表格
		//let updateFormContent = document.createRange().createContextualFragment(updateForm(game));
		//update.appendChild(updateFormContent);

		let deleteButton = document.createElement('button');
		deleteButton.setAttribute('value', game.gameId);
		deleteButton.setAttribute('name', 'id');
		deleteButton.setAttribute('class', 'btn btn-danger me-2');
		deleteButton.setAttribute('data-bs-toggle', 'modal');
		deleteButton.setAttribute('data-bs-target', '#deletemessage');
		deleteButton.setAttribute('onclick', 'deleteGame(this)');
		let deleteIcon = document.createElement('i');
		deleteIcon.setAttribute('class', 'fa-solid fa-trash-can fa-xl');
		deleteIcon.setAttribute('style', 'color: #ffffff;');
		deleteButton.appendChild(deleteIcon);
		action.appendChild(deleteButton);
		//插入刪除彈跳表格
		//let deleteFormContent = document.createRange().createContextualFragment(deleteMsg(game));
		//deleteMethod.appendChild(deleteFormContent);

		let uploadFile = document.createElement('button');
		uploadFile.setAttribute('id', 'uploadBtn');
		uploadFile.setAttribute('class', 'btn btn-warning');
		uploadFile.setAttribute('onclick', 'uploadGame(this)');
		uploadFile.setAttribute('value', game.gameName);
		uploadIcon = document.createElement('i');
		uploadIcon.setAttribute('class', 'fa-solid fa-upload fa-xl');
		uploadIcon.setAttribute('style', 'color: #ffffff;');
		uploadFile.appendChild(uploadIcon);
		action.appendChild(uploadFile);

		tr.appendChild(th);
		tr.appendChild(gname);
		tr.appendChild(price);
		tr.appendChild(createDate);
		tr.appendChild(gameIntroduce);
		tr.appendChild(gameTypes);
		tr.appendChild(gamPic);
		tr.appendChild(buyerCount);
		tr.appendChild(status);
		tr.appendChild(action);
		tbody.appendChild(tr);

	});

	//---------------------命名不得重複
	document.getElementById("copyName").onblur = function() {
		var username = this.value;

		var xhttp;
		if (window.XMLHttpRequest) {
			xhttp = new XMLHttpRequest();
		} else {
			xhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		xhttp.open("GET", '/carbon/game/searchSameName?username=' + username);
		xhttp.send();
		xhttp.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
				if (this.responseText == "true") {
					document.getElementById("username_err").style.display = '';
					$('#confirm').prop('disabled', true);
				} else {
					document.getElementById("username_err").style.display = 'none';
					$('#confirm').prop('disabled', false);
				}
			}
		}
	}


	//jquery 預載入事件區---------------------------------------------
	$(function() {
		$('.introduce, .types').on('click', function() {
			$(this).toggleClass('text-truncate ');
		})

		$(':checkbox').checkboxpicker();//checkbox 套件




		//更新狀態 Ajex---------------------------------------------
		$(':checkbox').checkboxpicker().on('change', function() {

			let gameId = $(this).parent().siblings('.gameId').text();
			let statusVal = $(this).val();
			let afterChange = statusVal == 1 ? 0 : 1;
			let isChecked = $(this).prop('checked');
			console.log(isChecked + statusVal);

			if ((isChecked == true && statusVal == 0) || (isChecked == false && statusVal == 1)) {

				//sweet alert-----------------------------
				Swal.fire({
					title: '確定要更改遊戲的狀態', text: "這將會影響使用者能否查詢得到遊戲", icon: 'question',
					showCancelButton: true, color: '#ffffff', confirmButtonColor: '#3085d6',
					cancelButtonColor: '#d33', background: '#212529', cancelButtonText: '取消',
					confirmButtonText: '更改'
				}).then((result) => {
					if (result.isConfirmed) {
						console.log('change' + gameId + statusVal + afterChange);
						var checkbox = $(this);

						// ajax-------------------------------
						$.ajax({
							type: 'PUT',
							url: '/carbon/game/updateNoImg',
							data: {
								GameId: gameId,
								Status: afterChange
							},
							success: function() {
								$('#iCheckbox' + gameId).prop('checked', afterChange === 1);
								$(checkbox).prop('value', afterChange);
							},
							error: function(xhr, status, error) {
								$('#iCheckbox' + gameId).prop('checked', afterChange !== 1);
							}
						});
						Swal.fire({
							title: '已送出請求', text: '如果遊戲狀態依舊沒有改變請麻煩聯繫技術支援', icon: 'success',
							color: '#ffffff', background: '#212529'
						})
					} else {
						$('#iCheckbox' + gameId).prop('checked', afterChange !== 1);
					}
				})
			}
		});
		// 加載 datatable--------------------------------------------
		$.extend(true, $.fn.dataTable.defaults, {
			searching: false,
			// ordering: true,
		});

		$(document).ready(function() {
			$('#example').DataTable();
			scrollX: true;
		});


		//遊戲名稱模糊搜尋=====================================================================================
		$('#findName').click(function() {
			event.preventDefault();

			var searchName = $('input[name="gname"]').val();

			$.ajax({
				type: 'Get',
				url: '/carbon/game/getGameLikesName',
				data: {
					gameName: searchName
				},
				success: function(response) {
					gamesInfos = response;
					showGameContent(gamesInfos);
				},
				error: function(xhr, status, error) {
					console.log(xhr.responseText);
				}
			}
			);
		});


		//img預覽-------------------------------------
		/*		$('#file').change(function() {
		
					var file = $('#file')[0].files[0];
					var reader = new FileReader;
					reader.onload = function(e) {
						$('#demo').show();
						$('#demo').attr('src', e.target.result);
					};
					reader.readAsDataURL(file);
				});
			*/
		/*
	$('.file1').change(function() {
		var $fileInputs = $('.file1'); // 获取所有的文件输入元素
		$fileInputs.each(function() {
			var file = this.files[0];
			var reader = new FileReader();
			var $demo = $(this).siblings('.demo1'); // 获取当前文件输入元素的兄弟元素.demo1

			reader.onload = function(e) {
				$demo.show();
				$demo.attr('src', e.target.result);
			};

			if (file) {
				reader.readAsDataURL(file);
			}
		});
	});*/

	})

}



$("#file").change(function() {
	$("#demo").html(""); // 清除預覽
	readURL(this);
});

//多圖片新增輪播=====================================================================
function readURL(input) {
	if (input.files && input.files.length >= 0) {
		for (let i = 0; i < input.files.length; i++) {
			var reader = new FileReader();
			reader.onload = function(e) {
				$('#carouselExample').show();
				console.log(i);
				let div = $("<div>").addClass(i === 0 ? 'carousel-item active' : 'carousel-item');
				let img = $("<img>").attr({
					'src': e.target.result,
					'class': 'd-block w-100'
				});
				$("#demo").append(div.append(img));
			}
			reader.readAsDataURL(input.files[i]);
		}
	} else {
		var noPictures = $("<p>目前沒有圖片</p>");
		$("#demo").append(noPictures);
	}
}
/*
//清空原Table 刷新TableAll------------------------------------------------------------------------
function updateForm(game) {
	console.log(game.gameId);
	let ok = ``;
	if (game.status === 0) {
		ok = `<input type="radio" name="Status" value="0" checked>下架
				  <input type="radio" name="Status" value="1" style="margin-left:50px">上架`;
	} else {
		ok = `<input type="radio" name="Status" value="0" >下架
				  <input type="radio" name="Status" value="1"checked style="margin-left:50px">上架`;
	}
	return `
		  <div class="modal fade " id="d`+ game.gameId + `" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true" style="color: black">
			  <div class="modal-dialog">
				  <form action="/carbon/update.controller" method="post" enctype="multipart/form-data" class="needs-validation" novalidate>
					  <div class="modal-content" style="background-color: #252424 ; color: white">
						  <div class="modal-header">
							  <h5 class="modal-title" id="exampleModalLabel">`+ game.gameName + `更新</h5>
							  <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
						  </div>
						  <div class="modal-body">
							  <table>
								  <tr>
									  <td>遊戲ID
									  <td><input type="text" class="form-control" readonly value="`+ game.gameId + `" name="GameId" >
								  <tr>
									  <td>遊戲名稱
									  <td><input type="text" value="`+ game.gameName + `" name="GameName" class="form-control" required>
										  <div class="valid-feedback" style="color: rgb(114, 245, 132);">Looks good!</div></td>
								  <tr>
									  <td>遊戲價格
									  <td><input type="text" value="`+ game.price + `" name="Price" class="form-control" required>
										<div class="invalid-feedback" style="color: rgb(238, 146, 146);">Please enter Number</div>
								  <tr>
									  <td>上架時間
									  <td><input type="date" value="`+ game.createDate + `" name="CreateDate" class="form-control">
										  <div class="valid-feedback" style="color: rgb(114, 245, 132);">Looks good!</div>
								  <tr>
									  <td>遊戲介紹
									  <td><input type="text" value="`+ game.gameIntroduce + `" name="GameIntroduce" class="form-control" required>
										  <div class="valid-feedback" style="color: rgb(114, 245, 132);">Looks good!</div>
										  <div class="invalid-feedback" style="color: rgb(238, 146, 146);">Lazy!
										  </div>
								  <tr>
									  <td>圖片
									  <td><input id="file`+ game.gameId + `" type="file" value="`+ game.gamePicPath + `" name="GamePicPath" class="form-control file1" >
											<input type="hidden" name="GamePicPathL" value="`+ game.gamePicPath + `">
									  <img id="demo`+ game.gameId + `" alt="" class="demo1" src="/carbon/img/`+ game.gamePicPath + `" style="width: 80px;padding-top: 5px">
								  <tr>
									  <td>購買人數
									  <td><input type="text" value="`+ game.buyerCount + `" name="BuyerCount" class="form-control" required
										  pattern="[0-9]{1,}" required>
										  <div class="valid-feedback" style="color: rgb(114, 245, 132);">Looks good!</div>
										  <div class="invalid-feedback" style="color: rgb(238, 146, 146);">Give me the Number!
										  </div>
								  <tr>
									  <td>遊戲狀態
									  <td style="text-align: center">`+ ok + `
							  </table>
						  </div>
						  <div class="modal-footer">
							  <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
							  <button type="submit" class="btn btn-primary">確定</button>
						  </div>
					  </div>
				  </form>
			  </div>
		  </div>`;
}
*/
//刪除彈跳視窗
/*
function deleteMsg(game) {
	return `
			<div class="modal fade" id="dd` + game.gameId + `" tabindex="-1"
				role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content" style="background-color: #252424 ; color: white">
						<div class="modal-header">
							<h1 class="modal-title fs-5" id="exampleModalLabel" style="color: white">刪除` + game.gameName + `</h1>
							<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
						</div>
						<div class="modal-body" style="color: rgb(238, 146, 146);">
							注意：這是一個不可逆的動作</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
							<form method="post" action="/carbon/delete.controller">
							  <input type="hidden" name="id" value="`+ game.gameId + `">
							  <button type="submit" class="btn btn-primary">Delete Game</button>
							</form>
						</div>
					</div>
				</div>
			</div>
			`;
}
*/