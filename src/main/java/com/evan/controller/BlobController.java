package com.evan.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.evan.service.AzureConnService;



@RestController
public class BlobController {

	@Autowired
	private AzureConnService aService;
	    
		//azure 上傳檔案
		@PostMapping("/game/uploadGame")
		public List<String> uplaodFileFromAzure(@RequestParam Map<String, Object> formData,@RequestParam("gameFile")MultipartFile file) throws IOException {
			aService.uploadFile((String)formData.get("gameName"),file.getInputStream(),file.getSize());
			aService.getAzureList();
			return aService.getAzureList();
		}
		//azure 下載檔案
		@GetMapping("/gameFront/downloadGame")
		public ResponseEntity<byte[]> downLoadFileFromAzure(@RequestParam Map<String, Object> formData )  {
			
			byte[] downLoad = aService.downloadFile((String)formData.get("gameName")).toByteArray();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			// 檔案,header,Httpstatus
			return new ResponseEntity<byte[]>(downLoad, headers, HttpStatus.OK);
		}
		

		
}
