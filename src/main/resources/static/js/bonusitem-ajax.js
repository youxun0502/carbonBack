/*
$(function () {
	////////// showDesc //////////
	$('.showDesc').on('click', function () {
		$(this).toggleClass('text-truncate');
	});

	////////// Datatables find by name  //////////
	// $('#gameitem_search').on('keyup', function () {
	// 	console.log($(this).val());
	// 	let thisval=$(this).val()
	// 	$('#example').DataTable()
	// 		.search(thisval)
	// 		.draw();
	// })
})
*/

//////////  Delete //////////
const deleteBtn = document.getElementsByClassName('delBtn');
for (i = 0; i < deleteBtn.length; i++) {
	deleteBtn[i].addEventListener('click', function (e) {
		let deleteId = this.getAttribute('data-id');
		let thisRow = $(this).closest('tr');
		e.preventDefault();

		Swal.fire({
			title: 'Are you sure?',
			text: "You won't be able to revert this!",
			icon: 'warning',
			showCancelButton: true,
			confirmButtonColor: '#3085d6',
			cancelButtonColor: '#d33',
			confirmButtonText: 'Yes, delete it!'
		}).then((result) => {
			if (result.isConfirmed) {
				console.log(deleteId);
				axios({
					url: 'http://localhost:8080/carbon/bonus/delete',
					method: 'delete',
					params: {
						id: deleteId
					}
				})
					.then(function (response) {
						console.log('response: ' + response.data);
						var table = $('#example').DataTable();
						table
							.row(thisRow)
							.remove()
							.draw()
					})
					.then(function () {
						Swal.fire(
							'Deleted!',
							'Your file has been deleted.',
							'success'
						)
					})
					.catch(err => {
						console.log('err: ' + err);
						Swal.fire({
							icon: 'error',
							title: 'Oops...',
							text: 'Something went wrong!',
						})
					})
			}
		})
	})
}

///////////Search by name/////////////


// $('#bonusitem_search').on('keyup change', function () {
// 	let searchVal = $(this).val();
// 	axios({
// 		url: 'http://localhost:8080/carbon/bonus/api/getBonusByName',
// 		method: 'get',
// 		params: {
// 			itemName: searchVal
// 		}
// 	})
// 		.then(response => {
// 			console.log('response: ' + JSON.stringify(response.data));
// 			htmlMaker(response.data);
// 		})
// 		.catch(err => {
// 			console.log('err: ' + err);
// 		})
// })

//////////////Search by name/////////////
let searchBtn = document.getElementById('name_search_btn');
searchBtn.addEventListener('click', function (e) {
	let searchForm = document.getElementById('bonusitem_search');
	console.log('searchF:' + searchForm.value);
	axios({
		url: 'http://localhost:8080/carbon/bonus/findLikeByName',
		method: 'get',
		params: {
			bonus_search: searchForm.value
		}
	})
		.then(res => {
			console.log('res: ' + JSON.stringify(res.data));
			htmlMaker(res.data);
		})
		.catch(err => {
			console.log('err:' + err);
		})
})


////////// table for ajax //////////
function htmlMaker(data) {
	$('#example').DataTable().destroy();
	let tbody = document.getElementById('showSeachItem');
	tbody.innerHTML = ``;
	tbody = '<tbody>';
	data.forEach(item => {


		tbody += `<tr>`;
		tbody += `<td class="tdCenter">${item.bonusId}</td>
	<td class="imgBox overflow-hidden">
		<img width="200px" src="/carbon/downloadImage/ + ${item.bonusId}" alt="${item.bonusName}">
	</td>
	<td>${item.bonusName}</td>
	<td class="text-truncate">${item.bonusPrice}</td>
	<td class="showDesc text-truncate">${item.bonusDes}</td>`;
		if (item.status == true) {
			tbody += `<td class="ps-3">
				<i class="fa-regular fa-circle-check fa-2xl" style="color: #31dd39;"></i>
			  </td>`;
		} else {
			tbody += `<td class="ps-3">
				<i class="fa-solid fa-ban fa-2xl" style="color: #ff0000;"></i>
			  </td>`;
		}
		tbody += `<td>
		<form method="get" action="/group5/bonus/edit">
			<button type="submit" value="${item.bonusId}" name="itemId" class="btn btn-info">
				<i class="fa-solid fa-pen"></i>
			</button>
		</form>
	</td>
	<td>
		<button type="submit" class="delBtn btn btn-secondary" data-id="${item.bonusId}">
			<i class="fa-solid fa-trash-can"></i>
		</button>
	</td>`;
		tbody += `</tr>`;
	})
	tbody += '</tbody>';
	const itemTable = document.getElementById('example');
	itemTable.getElementsByTagName('tbody')[0].innerHTML = tbody;

	$('#example').DataTable({
		scrollX: true,
		"dom": 'lrtip'
	});

	////////// showDesc //////////
	$('.showDesc').on('click', function () {
		$(this).toggleClass('text-truncate');
	});
}