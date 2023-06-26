$(function() {
	 $('#animate').removeClass('d-none');
  $('#animate').addClass('animate__fadeInUp');
  setTimeout(function(){
	  	$('#title').removeClass('d-none');
	    $('#title').addClass('animate__flipInX');
  },800);
  clickToRotate();

});

function clickToRotate() {
	const roulette = document.querySelector("#roulette");
	const clickBtn = document.querySelector(".click");
	clickBtn.addEventListener('click', function() {
		roulette.classList.add('rotate');
	})
}