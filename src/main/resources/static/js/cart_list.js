function refreshPage() {
  location.reload();  // 刷新页面
}

function clearLocal(){
	var localCart = JSON.parse(localStorage.getItem('cartItems'));
	localCart = [];
	localStorage.setItem('cartItems', JSON.stringify(localCart));
}