package com.example.smartshopper.services;

import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class cloudStorageService {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    public cloudStorageService() {
    }

    // TODO: Add method to upload images and return image URIs
    // Might have to be from data in memory because its from a stream
    // https://firebase.google.com/docs/storage/android/upload-files


}
