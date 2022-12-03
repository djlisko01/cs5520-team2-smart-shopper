package com.example.smartshopper.services;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.smartshopper.responseInterfaces.StringInterface;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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

    public void uploadWithURI(String path, Uri uri, StringInterface stringInterface) {
        StorageReference fileRef = storageRef.child(path);
        UploadTask uploadTask = fileRef.putFile(uri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                getImageURL(uri, taskSnapshot.getMetadata().getName(), stringInterface);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("IMAGES", e.getMessage());
            }
        });
    }

    public void getImageURL(Uri uri, String fileName, StringInterface stringInterface) {
        StorageReference imgRef = storageRef.child("images/" + fileName);
        UploadTask uploadTask = imgRef.putFile(uri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return imgRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    stringInterface.onCallback(downloadUri.toString());
                }
            }
        });
    }

}