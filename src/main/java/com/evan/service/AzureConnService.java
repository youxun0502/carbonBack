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

		String connectionString = "BlobEndpoint=https://carbongame.blob.core.windows.net/;QueueEndpoint=https://carbongame.queue.core.windows.net/;FileEndpoint=https://carbongame.file.core.windows.net/;TableEndpoint=https://carbongame.table.core.windows.net/;SharedAccessSignature=sv=2022-11-02&ss=bfqt&srt=sco&sp=rwdlacupiytfx&se=2023-07-28T21:05:31Z&st=2023-07-13T13:05:31Z&spr=https&sig=f%2BOhzEMYncUXL8WdK874iMQN2bcasM2MEO73NGypRvY%3D";
		String containerName = "carbon";
		String blobName = originalFilename;

		BlobContainerClient containerClient = new BlobContainerClientBuilder().connectionString(connectionString)
				.containerName(containerName).buildClient();

		BlobClient blobClient = containerClient.getBlobClient(blobName);
		blobClient.upload(inputStream, size);

	}

	public ByteArrayOutputStream downloadFile(String gameName) {
		String connectionString = "BlobEndpoint=https://carbongame.blob.core.windows.net/;QueueEndpoint=https://carbongame.queue.core.windows.net/;FileEndpoint=https://carbongame.file.core.windows.net/;TableEndpoint=https://carbongame.table.core.windows.net/;SharedAccessSignature=sv=2022-11-02&ss=bfqt&srt=sco&sp=rwdlacupiytfx&se=2023-07-28T21:05:31Z&st=2023-07-13T13:05:31Z&spr=https&sig=f%2BOhzEMYncUXL8WdK874iMQN2bcasM2MEO73NGypRvY%3D";
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
