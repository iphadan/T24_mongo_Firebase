package coop.MongoToFirebase.service;
import java.time.Instant;
import java.time.LocalDateTime;

import com.mongodb.client.MongoClients;
import coop.MongoToFirebase.models.AccountInfo;
import coop.MongoToFirebase.repository.AccountInfoRepository;
import org.bson.Document;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.ExecutionException;

@Component
@EnableScheduling
public class MongoScheduledChangeService {
   private final AccountInfoRepository accountInfoRepository;
   private final FirebaseCRUDService firebaseCRUDService;

    public MongoScheduledChangeService(AccountInfoRepository accountInfoRepository, FirebaseCRUDService firebaseCRUDService) {
        this.accountInfoRepository = accountInfoRepository;
        this.firebaseCRUDService = firebaseCRUDService;
    }

    @Scheduled(fixedDelay = 1000000)
    public void findAll() throws ExecutionException, InterruptedException {

        accountInfoRepository.findAll().forEach(
                accountInfo -> {
                    // Perform actions on each accountInfo object
                    System.out.println(accountInfo.toString()+ LocalDateTime.now().getHour()+":"+LocalDateTime.now().getMinute()+":"+LocalDateTime.now().getSecond()); // Example: Print the accountInfo object
                    try {
                        AccountInfo accountInfo1=new AccountInfo();

                        accountInfo1.setFullName(accountInfo.getFullName());
                        accountInfo1.setAccountNumber(accountInfo.getAccountNumber());
                        accountInfo1.setAmount(accountInfo.getAmount());
                        accountInfo1.setId(accountInfo.getAccountNumber());
                       // accountInfo1.setLastModified(LocalDateTime.now());
                        firebaseCRUDService.createAccountInfo(accountInfo1);

                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
        );



    }

    private LocalDateTime  lastPollTime=LocalDateTime.now(); // Initial value, can be set to an appropriate timestamp

    @Scheduled(fixedDelay = 5000) // Poll every 5 seconds
    public void pollDatabase() {
        try (var client = MongoClients.create("mongodb://localhost:27017")) {
            var database = client.getDatabase("t24_to_mongo");
            var collection = database.getCollection("account_info");

            // Query for new or modified documents

            var query = new Document("lastModified", new Document("$gt", lastPollTime));
            var cursor = collection.find(query).iterator();
      /*  query.forEach((accountInfo,acc)->{
            System.out.println(acc.toString());
        });*/
            while (cursor.hasNext()) {
                var document = cursor.next();
                var lastModified = document.get("name"); // Get the value of the "lastModified" field

                // Process the changed document
                System.out.println("Received change event: " + document.toJson());
                AccountInfo accountInfo1=new AccountInfo();
                accountInfo1.setFullName(document.get("fullName").toString());
                accountInfo1.setAccountNumber(document.get("accountNumber").toString());
                accountInfo1.setAmount((Double) document.get("amount"));
                accountInfo1.setId(document.get("accountNumber").toString());
                firebaseCRUDService.createAccountInfo(accountInfo1);





            }

            // Update the last poll time to the current time
            lastPollTime = LocalDateTime.now();
            System.out.println(lastPollTime+" last Polling");
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
