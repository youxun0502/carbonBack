var cartItems;
// 在頁面加載完成後檢查使用者的購物車資料
$(document).ready(function() {

	checkUserCart();

});

// 檢查使用者的購物車資料
function checkUserCart() {
	var memberIdElement = document.getElementById('memberId');
	var memberId = memberIdElement.dataset.userId;

	// 發送 AJAX 請求到後端，檢查使用者的購物車資料
	$.ajax({
		url: '/carbon/gameCart',
		type: 'GET',
		data: { memberId: memberId },
		success: function(data) {
			console.log(data);
			cartItems = data;
			// 根據回傳的資料進行相應的處理
			if (data === null || data.length === 0 || data === "") {
				// 若資料為 null 或長度為 0，表示玩家沒有購物車資料，則使用 Local Storage 的購物車資料
				cartItems = localStorage.getItem('cartItems');
				var cartItemsArray = JSON.parse(cartItems);

				checkMemberOwnGame(function(gameNames) {
					console.log(gameNames);
					// 在這裡進行遊戲名稱比對和刪除 cartItems 的操作
					if (cartItems) {
						// 比對遊戲名字並刪除匹配的項目
						for (var i = cartItemsArray.length - 1; i >= 0; i--) {
							var cartGameName = cartItemsArray[i].gameName;
							console.log(cartGameName)
							if (gameNames.includes(cartGameName)) {
								cartItemsArray.splice(i, 1);
							}
						}

					}

					console.log(cartItemsArray)
					if (cartItems) {
						// 將 Local Storage 的購物車資料存入資料庫
						$.ajax({
							url: '/carbon/gameCart/add',
							type: 'POST',
							data: {
								memberId: memberId,
								cartItems: cartItemsArray
							},
							success: function(e) {
								console.log(e);
								cartItems = e;
								// 資料庫存儲成功後的處理
								console.log('資料庫存儲成功');
								updateCartItems();

							},
							error: function(error) {
								// 資料庫存儲失敗後的處理
								console.error('資料庫存儲失敗', error);
							}
						});
						clearLocal();
					}
				});
			} else {
				updateCartItems();
				clearLocal();
			}

		},
		error: function(error) {
			// 請求失敗後的處理
			console.error('請求失敗', error);
		}
	});
}



// 更新購物車區塊的顯示
function updateCartItems() {
	var memberIdElement = document.getElementById('memberId');
	var memberId = memberIdElement.dataset.userId;

	if (!cartItems) {
		// 若購物車資料不存在，清空購物車區塊的內容
		document.getElementById('cartItems').innerHTML = '';
		return;
	}

	// 更新購物車區塊的顯示
	const cartItemsContainer = document.getElementById('cartItems');
	cartItemsContainer.innerHTML = '';
	console.log(cartItems);
	// 迭代購物車項目，生成 HTML 內容
	let itemCount = 0;
	cartItems.forEach(function(item) {
		if (itemCount < 5) {
			const html = `
        <div class="nk-widget-post">
          <a href="/carbon/gameFront/${item.gameName}" class="nk-post-image">
            <img src="/carbon/gameFront/getImg/${item.photoId}" alt="In all revolutions of">
          </a>
          <h3 class="nk-post-title">
            <a href="#" class="nk-cart-remove-item" onclick="removeFromCart(event, '${item.gameName}','${item.gameId}','${memberId}')"><span class="ion-android-close"></span></a>
            <a href="/carbon/gameFront/${item.gameName}" style="font-size:16px">${item.gameName}</a>
          </h3>
          <div class="nk-product-price" style="font-size:16px">NT$${item.price}</div>
        </div>
      `;

			cartItemsContainer.insertAdjacentHTML('beforeend', html);
			itemCount++;
		}
		if (itemCount === 4 && cartItems.length > 5) {
			// 在第5個項目後插入"點擊購物車看更多"的文字
			const moreText = document.createElement('div');
			moreText.textContent = '點擊下面查看更多';
			cartItemsContainer.appendChild(moreText);
		}
	});
	updateCartItemCount();
	check();
	checkMemberOwnGame();
}

// 更新購物車項目數量
function updateCartItemCount() {

	var itemCount = 0;
	if (cartItems) {
		itemCount = cartItems.length;
	}

	var cartItemCountElement = document.getElementById('cartItemCount');
	cartItemCountElement.textContent = itemCount;
}

function check() {
	var addToCartButtons = document.querySelectorAll('.add-to-cart-button'); // 假設按鈕的 class 為 "add-to-cart-button"

	console.log(cartItems)
	if (cartItems) {
		// 為每個按鈕檢查遊戲是否已存在於購物車中
		addToCartButtons.forEach(function(button) {
			var gameName = button.getAttribute("data-game-name");

			var isGameInCart = cartItems.some(function(item) {
				return item.gameName === gameName;
			});

			if (isGameInCart) {
				// 若遊戲已存在於購物車中，將按鈕樣式設為綠色
				button.classList.add('nk-btn-color-success');
				button.classList.remove('nk-btn-hover-color-main-1');
				button.classList.remove('nk-btn-color-dark-3');
				button.textContent = '已加入購物車';
			} else {
				button.classList.remove('nk-btn-color-success');
				button.classList.add('nk-btn-hover-color-main-1');
				button.classList.add('nk-btn-color-dark-3');
				button.textContent = '加入購物車';
			}
		});
	}
}

// 將商品加入購物車
function addToCart(event) {
	event.preventDefault();

	var gameId = event.target.getAttribute("data-game-id");
	var gameName = event.target.getAttribute("data-game-name");
	var price = event.target.getAttribute("data-price");
	var photo = event.target.getAttribute("data-photo");
	var memberId = $('#memberId').attr('data-user-id');

	// 建立購物車物件
	const item = {
		gameId: gameId,
		gameName: gameName,
		price: price,
		photoId: photo,
		memberId: memberId
	};

	console.log(item);
	console.log(cartItems);

	if (!cartItems) {
		// 若沒有，則建立一個空的陣列
		cartItems = [];
	}

	// 檢查是否已經存在相同的遊戲名字
	var isGameNameExist = cartItems.some(function(e) {
		return e.gameName === gameName;
	});

	if (isGameNameExist) {
		// 若已經存在相同的遊戲名字，停止執行後續的程式碼
		removeFromCart(event, gameName, gameId, memberId);
		return;
	} else {
		$.ajax({
			url: '/carbon/gameCart/addOne',
			type: 'POST',
			data: {
				memberId: memberId,
				gameId: gameId
			},
			success: function(data) {
				cartItems = data;
				// 資料庫存儲成功後的處理
				console.log('資料庫存儲成功');

			},
			error: function(error) {
				// 資料庫存儲失敗後的處理
				console.error('資料庫存儲失敗', error);
			}
		});

	}

	// 將商品加入購物車
	cartItems.push(item);

	// 更新購物車區塊的顯示
	updateCartItems();

	// 修改按鈕文字為「已加入」
	event.target.textContent = '已加入購物車';
	event.target.classList.add('nk-btn-color-success');
	event.target.classList.remove('nk-btn-hover-color-main-1');
	event.target.classList.remove('nk-btn-color-dark-3');

}

// 從購物車中移除商品
function removeFromCart(event, gameName, gameId, memberId) {
	console.log(gameName);
	event.preventDefault();

	if (!cartItems) {
		return;
	}

	// 在購物車中尋找要移除的商品
	const index = cartItems.findIndex(function(item) {
		return item.gameName === gameName;
	});
	// 在購物車中尋找要移除的商品
	var localCart = JSON.parse(localStorage.getItem('cartItems'));
	if (localCart) {
		const indexlocal = localCart.findIndex(function(item) {
			return item.gameName === gameName;
		});
		if (indexlocal !== -1) { localCart.splice(indexlocal, 1); };
	}

	// 如果找到該商品，則從購物車中移除
	if (index !== -1) {

		cartItems.splice(index, 1);
		// 將更新後的購物車資料存回 Local Storage
		localStorage.setItem('cartItems', JSON.stringify(localCart));
		$.ajax({
			url: '/carbon/gameCart/delete',
			type: 'DELETE',
			data: {
				memberId: memberId,
				gameId: gameId
			},
			success: function(data) {
				cartItems = data;
				// 資料庫存儲成功後的處理
				console.log('資料庫存儲成功');
			},
			error: function(error) {
				// 資料庫存儲失敗後的處理
				console.error('資料庫存儲失敗', error);
			}
		});

	}

	// 更新購物車區塊的顯示
	updateCartItems();
	check();

}

//已經擁有的遊戲就不能再購買
function checkMemberOwnGame(callback) {
	var memberId = $('#memberId').data('userId');
	var gameNames = [];

	// 將 Local Storage 的購物車資料存入資料庫
	$.ajax({
		url: '/carbon/gameFront/getownGame',
		type: 'GET',
		data: {
			memberId: memberId,
		},
		success: function(gameList) {
			console.log(gameList);
			gameNames = $.map(gameList, function(item) {
				return item.gameName;
			});
			removeOwnGame(gameList);

			if ($.isFunction(callback)) {
				callback(gameNames);
			}
		},
		error: function(error) {
			// 資料庫存儲失敗後的處理
			console.error('遊戲列表讀取失敗', error);
		}
	});
}

function removeOwnGame(gameList) {
	if (gameList) {
		$('.add-to-cart-button').each(function() {
			var button = $(this);
			var gameName = button.attr('data-game-name');

			var isGameOwned = gameList.some(function(item) {
				console.log(item.gameName);
				return item.gameName === gameName;
			});

			if (isGameOwned) {
				button.addClass('nk-btn-color-warning');
				button.removeClass('nk-btn-hover-color-main-1 nk-btn-color-dark-3 add-to-cart-button');
				button.text('已購買');
				button.removeAttr('onclick');
				button.hover(
					function() {
						$(this).text('下載');
						$(this).removeClass('nk-btn-color-warning');
						$(this).addClass('nk-btn-color-success');
						var gameName = $(this).data('game-name');
						$(this).attr('title', '點擊下載遊戲：' + gameName);
						$(this).click(function() {
							download(gameName);
						});
					},
					function() {
						$(this).text('已購買');
						$(this).removeClass('nk-btn-color-success');
						$(this).addClass('nk-btn-color-warning');
						$(this).off('click')
						
					}
				);
			}
		});
	}
}

function download(gameName){
	  var downloadUrl = '/carbon/gameFront/downloadGame?gameName=' + encodeURIComponent(gameName);
      // 創建一个带有下载的<a>
      var downloadLink = $('<a>')
        .attr('href', downloadUrl)
        .attr('download', gameName + '.jar') 
        .hide()
        .appendTo('body');
      
      // 觸發點擊
      downloadLink[0].click();
      
      // 删除下载
      downloadLink.remove();
}

//清理local
function clearLocal() {
	var localCart = JSON.parse(localStorage.getItem('cartItems'));
	localCart = [];
	localStorage.setItem('cartItems', JSON.stringify(localCart));
}
