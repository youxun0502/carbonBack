package com.evan.service;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;

@Service
public class AzureConnService {

	@Value("account-name")
	private String storageAccountName;

	@Value("endpoint")
	private String storageAccountEndpoint;
	
	public void uploadFile(String originalFilename, InputStream inputStream, long size) {
		
		 String connectionString = "your-connection-string";
         String containerName = "your-container-name";
         String blobName = originalFilename;

         BlobContainerClient containerClient = new BlobContainerClientBuilder()
                 .connectionString(connectionString)
                 .containerName(containerName)
                 .buildClient();

         BlobClient blobClient = containerClient.getBlobClient(blobName);
         blobClient.upload(inputStream,size);
		
	}

}
