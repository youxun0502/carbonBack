const pwdChangeForm = document.querySelector("#pwdChangeForm");
const newPwd = document.querySelector("#newPwd");
const newPwdChecked = document.querySelector("#newPwdChecked");
const container = document.querySelector("#thisPageContainer");
newPwdChecked.addEventListener('input', function () {
  newPwdChecked.classList.remove("is-invalid");
})
newPwd.addEventListener('input', function () {
  newPwd.classList.remove("is-invalid");
})

let regex = /[a-zA-Z0-9]{6,}/;

pwdChangeForm.addEventListener('submit', function (event) {
  event.preventDefault();
  if (newPwdChecked.value != newPwd.value) {
    newPwdChecked.classList.add("is-invalid");
  } else if(!regex.test(newPwd.value)){
	  newPwd.classList.add("is-invalid");
  } else {
    Swal.fire({
      title: '密碼更新成功',
      text: '您的密碼已更新',
      icon: 'success',
      timer: '1200',
      showConfirmButton: false
    })

    container.classList.add("vh-100");
    setTimeout(function () {
      pwdChangeForm.submit();
    }, 1250)
  }

})

