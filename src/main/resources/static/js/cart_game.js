var memberId = $('#memberId').attr('data-user-id');
function check() {
	var addToCartButtons = document.querySelectorAll('.add-to-cart-button'); // 假設按鈕的 class 為 "add-to-cart-button"

	// 檢查 Local Storage 中是否已經有購物車資料
	var cartItems = localStorage.getItem('cartItems');
	if (cartItems) {
		// 若購物車資料存在，將其解析為陣列
		cartItems = JSON.parse(cartItems);

		// 為每個按鈕檢查遊戲是否已存在於購物車中
		addToCartButtons.forEach(function (button) {
			var gameName = button.getAttribute("data-game-name");

			var isGameInCart = cartItems.some(function (item) {
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
	updateCartItemCount();
}

window.addEventListener('DOMContentLoaded', check);

// 更新購物車項目數量
function updateCartItemCount() {


	var cartItems = localStorage.getItem('cartItems');
	var itemCount = 0;

	if (cartItems) {
		cartItems = JSON.parse(cartItems);
		itemCount = cartItems.length;
	}

	var cartItemCountElement = document.getElementById('cartItemCount');
	cartItemCountElement.textContent = itemCount;
}

// 將商品加入購物車
function addToCart(event) {
	event.preventDefault();

	var gameId = event.target.getAttribute("data-game-id");
	var gameName = event.target.getAttribute("data-game-name");
	var price = event.target.getAttribute("data-price");
	var photo = '/carbon/gameFront/getImg/' + event.target.getAttribute("data-photo");

	// 建立購物車物件
	const item = {
		gameId: gameId,
		gameName: gameName,
		price: price,
		photo: photo
	};

	console.log(item);

	// 檢查 Local Storage 中是否已經有購物車資料
	let cartItems = localStorage.getItem('cartItems');
	
	console.log(cartItems);

	if (!cartItems) {
		// 若沒有，則建立一個空的陣列
		cartItems = [];
	} else {
		// 若已存在，將購物車資料解析為陣列
		cartItems = JSON.parse(cartItems);
	}

	// 檢查是否已經存在相同的遊戲名字
	var isGameNameExist = Array.isArray(cartItems) && cartItems.some(function (item) {
		return item.gameName === gameName;
	});

	if (isGameNameExist) {
		// 若已經存在相同的遊戲名字，停止執行後續的程式碼
		removeFromCart(event, gameName);
		return;
	}

	// 將商品加入購物車
	cartItems.push(item);

	// 將購物車資料存回 Local Storage
	localStorage.setItem('cartItems', JSON.stringify(cartItems));

	// 更新購物車區塊的顯示
	updateCartItems();

	// 修改按鈕文字為「已加入」
	event.target.textContent = '已加入購物車';
	event.target.classList.add('nk-btn-color-success');
	event.target.classList.remove('nk-btn-hover-color-main-1');
	event.target.classList.remove('nk-btn-color-dark-3');
	updateCartItemCount();
}



// 更新購物車區塊的顯示
function updateCartItems() {
	// 獲取購物車資料
	let cartItems = localStorage.getItem('cartItems');

	if (!cartItems) {
		// 若購物車資料不存在，清空購物車區塊的內容
		document.getElementById('cartItems').innerHTML = '';
		return;
	}else{
		
	// 解析購物車資料為陣列
	cartItems = JSON.parse(cartItems);
	}

	// 更新購物車區塊的顯示
	const cartItemsContainer = document.getElementById('cartItems');
	cartItemsContainer.innerHTML = '';

	// 迭代購物車項目，生成 HTML 內容
	let itemCount = 0;
	
	cartItems.forEach(function (item) {
		if (itemCount < 5) {
			const html = `
        <div class="nk-widget-post">
          <a href="/carbon/gameFront/${item.gameName}" class="nk-post-image">
            <img src="${item.photo}" alt="In all revolutions of">
          </a>
          <h3 class="nk-post-title">
            <a href="#" class="nk-cart-remove-item" onclick="removeFromCart(event, '${item.gameName}')"><span class="ion-android-close"></span></a>
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
	});}


// 從購物車中移除商品
function removeFromCart(event, gameName) {
	event.preventDefault();

	// 從 Local Storage 中獲取購物車資料
	let cartItems = localStorage.getItem('cartItems');
	if (!cartItems) {
		return;
	}

	// 解析購物車資料為陣列
	cartItems = JSON.parse(cartItems);

	// 在購物車中尋找要移除的商品
	const index = cartItems.findIndex(function (item) {
		return item.gameName === gameName;
	});

	// 如果找到該商品，則從購物車中移除
	if (index !== -1) {
		cartItems.splice(index, 1);
	}

	// 將更新後的購物車資料存回 Local Storage
	localStorage.setItem('cartItems', JSON.stringify(cartItems));

	// 更新購物車區塊的顯示
	updateCartItems();
	check();

}

// 初始化頁面時更新購物車區塊的顯示
updateCartItems();