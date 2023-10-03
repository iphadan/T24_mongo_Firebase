package coop.MongoToFirebase.service;


import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import coop.MongoToFirebase.models.AccountInfo;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class FirebaseCRUDService {
    
    public String createAccountInfo(AccountInfo accountInfo) throws ExecutionException, InterruptedException {
        Firestore firestore= FirestoreClient.getFirestore();
        ApiFuture<WriteResult> colletectionsApiFuture= (ApiFuture<WriteResult>) firestore.collection("AccountInfo").document(accountInfo.getId().toString()).set(accountInfo);

return  colletectionsApiFuture.get().getUpdateTime().toString();
    }

}
