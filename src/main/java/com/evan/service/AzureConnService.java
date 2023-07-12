package com.evan.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.springframework.stereotype.Service;


import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;


@Service
public class AzureConnService {

	public void uploadFile(String originalFilename, InputStream inputStream, long size) {

		String connectionString = "BlobEndpoint=https://carbongame.blob.core.windows.net/;QueueEndpoint=https://carbongame.queue.core.windows.net/;FileEndpoint=https://carbongame.file.core.windows.net/;TableEndpoint=https://carbongame.table.core.windows.net/;SharedAccessSignature=sv=2022-11-02&ss=bfqt&srt=sco&sp=rwdlacupiytfx&se=2023-08-12T00:40:12Z&st=2023-07-12T16:40:12Z&spr=https&sig=oD%2FQw6tgqT%2BV64Eoj%2BtzhcS%2BaPIxsp%2B0QQ3Tsl7gi7o%3D";
		String containerName = "carbon";
		String blobName = originalFilename;

		BlobContainerClient containerClient = new BlobContainerClientBuilder().connectionString(connectionString)
				.containerName(containerName).buildClient();

		BlobClient blobClient = containerClient.getBlobClient(blobName);
		blobClient.upload(inputStream, size);

	}

	public ByteArrayOutputStream downloadFile(String gameName) {
		String connectionString = "BlobEndpoint=https://carbongame.blob.core.windows.net/;QueueEndpoint=https://carbongame.queue.core.windows.net/;FileEndpoint=https://carbongame.file.core.windows.net/;TableEndpoint=https://carbongame.table.core.windows.net/;SharedAccessSignature=sv=2022-11-02&ss=bfqt&srt=sco&sp=rwdlacupiytfx&se=2023-08-12T00:40:12Z&st=2023-07-12T16:40:12Z&spr=https&sig=oD%2FQw6tgqT%2BV64Eoj%2BtzhcS%2BaPIxsp%2B0QQ3Tsl7gi7o%3D";
		String containerName = "carbon";
		String blobName = gameName;
		
		BlobContainerClient containerClient = new BlobContainerClientBuilder().connectionString(connectionString)
				.containerName(containerName).buildClient();
		BlobClient blobClient = containerClient.getBlobClient(blobName);
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		blobClient.downloadStream(os);
		
		return os;

	}
}
