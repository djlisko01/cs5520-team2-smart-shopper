package com.example.smartshopper.services;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CloudStorageService {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    public CloudStorageService() {
    }

    /**
     * Upload a file from memory to the cloud storage
     *
     * @param path  The path to the file in the cloud storage
     * @param bytes The bytes of the file to be uploaded
     * @return The URI to the file in the cloud storage
     */
    public String uploadFile(String path, byte[] bytes) {
        StorageReference fileRef = storageRef.child(path);
        fileRef.putBytes(bytes);
        return fileRef.getDownloadUrl().toString();
    }
}