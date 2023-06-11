  //套件正則---------------------------------------------------------------------------------
 	function RunForm(){
	// Example starter JavaScript for disabling form submissions if there are invalid fields
	    'use strict'
	    // Fetch all the forms we want to apply custom Bootstrap validation styles to
	    var forms = document.querySelectorAll('.needs-validation')
	    // Loop over them and prevent submission
	    Array.prototype.slice.call(forms)
	      .forEach(function (form) {
	        form.addEventListener('submit', function (event) {
	          if (!form.checkValidity()) {
	            event.preventDefault()
	            event.stopPropagation()
	          }

	          form.classList.add('was-validated')
	        }, false)
	      })
	  ()
	  
	}     
		  


 //創建table骨架 ----------------------------------------------------
	  function showGameContent(gamesInfos){
	      let tbody = document.getElementById('showAllGame');
	      console.log(gamesInfos);
	      tbody.innerHTML='';
	      
	      gamesInfos.forEach(function (game) {
	          
	          let tr = document.createElement('tr');
	          let th = document.createElement('td');
	          th.setAttribute('scope','row');
	          th.textContent = game.gameId;

	          let gname = document.createElement('td');
	          gname.textContent = game.gameName;

	          let price = document.createElement('td');
	          price.textContent = game.price;

	          let createDate = document.createElement('td');
	          createDate.textContent = game.createDate;

	          let gameIntroduce = document.createElement('td');
	          gameIntroduce.textContent = game.gameIntroduce;

	          let gamPic =document.createElement('td');
	          let img =document.createElement('img');
	          img.setAttribute('alt','');
	          img.setAttribute('src','/group5/img/'+game.gamePicPath);
	          img.setAttribute('style','width:80px;height:60px');
	          gamPic.appendChild(img);

	          let buyerCount = document.createElement('td');
	          buyerCount.textContent = game.buyerCount;

	          let status = document.createElement('td');
	          if (game.status == 1) {
	              let icon = document.createElement('i');
	              icon.setAttribute('class', 'fa-solid fa-cart-shopping');
	              icon.setAttribute('style', 'color: #0ae62e;font-size:40px');
	              status.appendChild(icon);
	          } else if (game.status == 0) {
	              let icon = document.createElement('i');
	              icon.setAttribute('class', 'fa-sharp fa-solid fa-rectangle-xmark');
	              icon.setAttribute('style', 'color: #f00030;font-size:40px');
	              status.appendChild(icon);
	          }

	          let update = document.createElement('td');
	          let updateButton = document.createElement('button');
	          updateButton.setAttribute('value', game.gameId);
	          updateButton.setAttribute('name', 'id');
	          updateButton.setAttribute('class', 'btn btn-light');
	          updateButton.setAttribute('data-bs-toggle', 'modal');
	          updateButton.setAttribute('data-bs-target', '#'+game.gameId);
	          updateButton.textContent = '更新';
	          update.appendChild(updateButton);
	          //插入 更新彈跳表格
	          let updateFormContent = document.createRange().createContextualFragment(updateForm(game));
	          update.appendChild(updateFormContent);

	          let deleteMethod = document.createElement('td');
	          let deleteButton = document.createElement('button');
	          deleteButton.setAttribute('value', game.gameId);
	          deleteButton.setAttribute('name', 'id');
	          deleteButton.setAttribute('class', 'btn btn-light');
	          deleteButton.setAttribute('data-bs-toggle', 'modal');
	          deleteButton.setAttribute('data-bs-target', '#' + game.gameId+'d');
	          deleteButton.textContent = '刪除';
	          deleteMethod.appendChild(deleteButton);
	          //插入刪除彈跳表格
	           let deleteFormContent = document.createRange().createContextualFragment(deleteMsg(game));
	          deleteMethod.appendChild(deleteFormContent);

	          tr.appendChild(th);
	          tr.appendChild(gname);
	          tr.appendChild(price);
	          tr.appendChild(createDate);
	          tr.appendChild(gameIntroduce);
	          tr.appendChild(gamPic);
	          tr.appendChild(buyerCount);
	          tr.appendChild(status);
	          tr.appendChild(update);
	          tr.appendChild(deleteMethod);

	          tbody.appendChild(tr);
	          $.extend(true, $.fn.dataTable.defaults, {
		          searching : false,
		     // ordering: true,
		     });
	          
		     $(document).ready(function() {
		         $('#example').DataTable();
		     });
	      });
	      
	    //jquery 預載入事件區---------------------------------------------
			 $(function() {
				  $('#findName').click(function(){
					 event.preventDefault(); 

					 var searchName =$('input[name="gname"]').val();

					 $.ajax({
						type:'POST',
						url:basePath+'searchLikeName.controller',
						data:{
							gameName:searchName
						},
						success:function(response){
							gamesInfos = response;
							showGameContent(gamesInfos);
						},
						error: function(xhr,status,error){
							console.log(xhr.responseText);
						}
					 }
					 );
				  });
			     //img預覽-------------------------------------
				  $('#file').change(function () {
				  	
				  	var file = $('#file')[0].files[0];
				  	var reader = new FileReader;
				  	reader.onload = function (e) {
				  		$('#demo').show();
				  		$('#demo').attr('src', e.target.result);
				  	};
				  	reader.readAsDataURL(file);
				  }); 
			     
				  $('.file1').change(function () {
				  	console.log(88);
					  
				  	var file = $('.file1')[0].files[0];
				  	var reader = new FileReader;
				  	reader.onload = function (e) {
				  		$('.demo1').show();
				  		$('.demo1').attr('src', e.target.result);
				  	};
				  	reader.readAsDataURL(file);
				  }); 
			 })
	}

	//清空原Table 刷新TableAll------------------------------------------------------------------------
	function updateForm(game){
	      console.log(game.gameId);
	      let ok =``;
	      if(game.status === 0){
	          ok =`<input type="radio" name="Status" value="0" checked>下架
	              <input type="radio" name="Status" value="1" style="margin-left:50px">上架`;
	      }else{
	          ok =`<input type="radio" name="Status" value="0" >下架
	              <input type="radio" name="Status" value="1"checked style="margin-left:50px">上架`;
	      }
	      return `
	      <div class="modal fade " id="`+game.gameId+`" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true" style="color: black">
	          <div class="modal-dialog">
	              <form action="/group5/update.controller" method="post" enctype="multipart/form-data" class="needs-validation" novalidate>
	                  <div class="modal-content" style="background-color: #252424 ; color: white">
	                      <div class="modal-header">
	                          <h5 class="modal-title" id="exampleModalLabel">`+game.gameName+`更新</h5>
	                          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	                      </div>
	                      <div class="modal-body">
	                          <table>
	                              <tr>
	                                  <td>遊戲ID
	                                  <td><input type="text" class="form-control" readonly value="`+game.gameId+`" name="GameId" >
	                              <tr>
	                                  <td>遊戲名稱
	                                  <td><input type="text" value="`+game.gameName+`" name="GameName" class="form-control" required>
	                                      <div class="valid-feedback" style="color: rgb(114, 245, 132);">Looks good!</div></td>
	                              <tr>
	                                  <td>遊戲價格
	                                  <td><input type="text" value="`+game.price+`" name="Price" class="form-control" required>
	                             	  <div class="invalid-feedback" style="color: rgb(238, 146, 146);">Please enter Number</div>
	                              <tr>
	                                  <td>上架時間
	                                  <td><input type="date" value="`+game.createDate+`" name="CreateDate" class="form-control">
	                                      <div class="valid-feedback" style="color: rgb(114, 245, 132);">Looks good!</div>
	                              <tr>
	                                  <td>遊戲介紹
	                                  <td><input type="text" value="`+game.gameIntroduce+`" name="GameIntroduce" class="form-control" required>
	                                      <div class="valid-feedback" style="color: rgb(114, 245, 132);">Looks good!</div>
	                                      <div class="invalid-feedback" style="color: rgb(238, 146, 146);">Lazy!
	                                      </div>
	                              <tr>
	                                  <td>圖片
	                                  <td><input id="file1" type="file" value="`+game.gamePicPath+`" name="GamePicPath" class="form-control file1" >
	                             	      <input type="hidden" name="GamePicPathL" value="`+game.gamePicPath+`">
	                                  <img id="demo1" alt="" class="demo1" src="/group5/img/`+game.gamePicPath+`" style="width: 80px;padding-top: 5px">
	                              <tr>
	                                  <td>購買人數
	                                  <td><input type="text" value="`+game.buyerCount+`" name="BuyerCount" class="form-control" required
	                                      pattern="[0-9]{1,}" required>
	                                      <div class="valid-feedback" style="color: rgb(114, 245, 132);">Looks good!</div>
	                                      <div class="invalid-feedback" style="color: rgb(238, 146, 146);">Give me the Number!
	                                      </div>
	                              <tr>
	                                  <td>遊戲狀態
	                                  <td style="text-align: center">`+ok+`
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
	  
	//刪除彈跳視窗
		function deleteMsg(game){
			return`
			<div class="modal fade" id="` + game.gameId+ `d" tabindex="-1"
				role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content" style="background-color: #252424 ; color: white">
						<div class="modal-header">
							<h1 class="modal-title fs-5" id="exampleModalLabel" style="color: white">刪除` + game.gameName+ `</h1>
							<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
						</div>
						<div class="modal-body" style="color: rgb(238, 146, 146);">
							注意：這是一個不可逆的動作</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
							<form method="post" action="/group5/delete.controller">
							  <input type="hidden" name="id" value="`+game.gameId+`">
							  <button type="submit" class="btn btn-primary">Delete Game</button>
							</form>
						</div>
					</div>
				</div>
			</div>
			`;
		}
		   