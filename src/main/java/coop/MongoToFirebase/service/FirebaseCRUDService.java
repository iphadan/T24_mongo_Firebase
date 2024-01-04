package coop.MongoToFirebase.service;


import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

import coop.MongoToFirebase.models.ReportModel;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class FirebaseCRUDService {
    
    public String createReportModel(ReportModel reportModel) throws ExecutionException, InterruptedException {
        Firestore firestore= FirestoreClient.getFirestore();
        ApiFuture<WriteResult> colletectionsApiFuture= (ApiFuture<WriteResult>) firestore.collection("cbo-daily").document(reportModel.getId().toString()).set(reportModel);

return  colletectionsApiFuture.get().getUpdateTime().toString();
    }

}
