package coop.MongoToFirebase.service;



import com.mongodb.client.*;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bson.Document;


import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class MongoListenerService {
    private  MongoClient mongoClient;
    @Autowired
    public MongoListenerService(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }
    MongoDatabase mongoDatabase=mongoClient.getDatabase("coop_t24_db");
    MongoCollection<Document> collection = mongoDatabase.getCollection("mycollection");
    ChangeStreamIterable<Document> changeStreamIterable = collection.watch();

    MongoCursor<ChangeStreamDocument<Document>> cursor = changeStreamIterable.iterator();
    @Scheduled(cron = "0 0 8 * * ?")
    public void startListeningForChanges() {
        try (MongoCursor<ChangeStreamDocument<Document>> cursor = changeStreamIterable.iterator()) {
            while (cursor.hasNext()) {
                ChangeStreamDocument<Document> changeStreamDocument = cursor.next();
                Document fullDocument = changeStreamDocument.getFullDocument();
                System.out.printf(fullDocument.toJson());
            }
        }
    }
    }

