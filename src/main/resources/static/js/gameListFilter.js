function refreshGameList(jsonGameList) {
	const gameList = $('#showGame-list');
	gameList.empty();

	$.each(jsonGameList, function(index, game) {
		console.log(game)
		const gameListHTML = `
      <div class="col-lg-6">
        <div class="nk-product-cat-2">
          <a class="nk-product-image" href="/carbon/gameFront/${game.gameName}">
            <img src="/carbon/gameFront/getImg/${game.gamePhotoLists[0]}" alt="${game.gameName}">
          </a>
          <div class="nk-product-cont" style="color: #b9a5a5">
            <h3 class="nk-product-title h5">
              <a href="/carbon/gameFront/${game.gameName}">${game.gameName}</a>
            </h3>
            <div class="nk-gap-1"></div>
            <div class="nk-product-rating" data-rating="3.5">
              <i class="fa fa-star"></i> <i class="fa fa-star"></i> <i class="fa fa-star"></i> <i class="fas fa-star-half"></i> <i class="far fa-star"></i>
            </div>
            <div class="nk-gap-1"></div>
            ${game.gameIntroduce}
            <div class="nk-gap-2"></div>
            <div class="nk-product-price">NT$${game.price}</div>
            <div class="nk-gap-1"></div>
            <a href="#" class="nk-btn nk-btn-rounded nk-btn-color-dark-3 nk-btn-hover-color-main-1 add-to-cart-button"
              data-game-name="${game.gameName}" data-game-id="${game.gameId}" data-price="${game.price}" data-photo="${game.gamePhotoLists[0]}"
              onclick="addToCart(event)">加入購物車</a>
          </div>
        </div>
      </div>`;

		gameList.append(gameListHTML);
	});
	check();
	checkMemberOwnGame();
}

function clickType(event) {
	event.preventDefault();
	var typeName = event.target.getAttribute("data-typeName"); // 獲取typeName的值
	console.log(typeName)
	// 發送AJAX請求
	$.ajax({
		url: '/carbon/gameFront/findGameByType',
		type: 'Get',
		data: {
			typeName: typeName
		},
		success: function(response) {
			refreshGameList(response);
		},
		error: function(error) {
			// AJAX請求失敗後的處理
			console.error('AJAX請求失敗', error);
		}
	});

}
function clickgameName(event) {
	event.preventDefault();

	var searchName = $('input[name="gname"]').val();

	$.ajax({
		type: 'Get',
		url: '/carbon/gameFront/getGameLikesName',
		data: {
			gameName: searchName
		},
		success: function(response) {
			gamesInfos = response;
			refreshGameList(gamesInfos);
		},
		error: function(xhr, status, error) {
			console.log(xhr.responseText);
		}
	}
	);

}
function clickprice(event) {
	event.preventDefault(); // 防止<a>元素的預設行為

	// 取得使用者選擇的兩個數字
	var minValue = parseInt($('.nk-input-slider-value-0').text());
	var maxValue = parseInt($('.nk-input-slider-value-1').text());

	console.log(maxValue)
	// 發送AJAX請求
	$.ajax({
		url: '/carbon/gameFront/findPrice',
		type: 'Get', 
		data: {
			minValue: minValue,
			maxValue: maxValue
		},
		success: function(response) {
			// AJAX請求成功後的處理
			console.log(response);
			// 在這裡處理或顯示請求返回的資料
			refreshGameList(response);
		},
		error: function(error) {
			// AJAX請求失敗後的處理
			console.error('AJAX請求失敗', error);
		}
	});
}