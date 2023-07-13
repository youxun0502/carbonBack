const status1 = document.querySelector("#status").innerText;
console.log(status1);
const verification = document.querySelector("#verification");
const alreadySend = document.querySelector("#alreadySend");
const restore = document.querySelector("#restore");
if (status1 == '驗證網址過期，請重新取得驗證網址') {
  verification.style.display='block';
}
const verifyBtn = document.querySelector("#verifyBtn");
verifyBtn.addEventListener('click', function () {
  let currentURL = window.location.href;
  let urlParams = new URLSearchParams(currentURL);
  let memberId = urlParams.get('userId');
  getUrlAgain(memberId);
  verifyBtn.setAttribute('disabled', true);
  restoreBtn()
  alreadySend.classList.remove('d-none');

})

function restoreBtn() {

  let second = 30;
  let interval1 = setInterval(function () {
    if (second == 0) {
      console.log(second);
      restore.innerText = '';
      alreadySend.classList.add('d-none');
      verifyBtn.removeAttribute('disabled');
      clearInterval(interval1);
    } else {
      restore.innerText = `${second} 秒後即可重新傳送`;
      second = second - 1;
    }
  }, 1000)
}


function getUrlAgain(memberId) {
  axios({
    url: "http://localhost:8080/carbon/main/api/getEmail",
    method: "get",
    params: {
      'id': parseInt(memberId)
    },
    headers: { 'Content-Type': 'application/x-www-form-urlencode' }
  })
    .then(response => {
      console.log(response.data);
    })
}