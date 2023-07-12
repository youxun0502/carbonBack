package com.evan.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.evan.service.AzureConnService;



@RestController
@RequestMapping("blob")
public class BlobController {

	@Autowired
	private AzureConnService aService;
	    
		//azure 上傳檔案
		@PostMapping("/game/uploadGame")
		public String uplaodFileFromAzure(MultipartFile file) throws IOException {
			aService.uploadFile(file.getOriginalFilename(),file.getInputStream(),file.getSize());
			return "done";
		}
}
