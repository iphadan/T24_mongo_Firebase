package coop.MongoToFirebase.service;
import java.time.LocalDateTime;

import com.mongodb.client.MongoClients;
import coop.MongoToFirebase.models.ReportModel;
import coop.MongoToFirebase.repository.ReportModelRepository;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@EnableScheduling
public class MongoScheduledChangeService {
   private final ReportModelRepository reportModelRepository;
   private final FirebaseCRUDService firebaseCRUDService;

    public MongoScheduledChangeService(ReportModelRepository reportModelRepository, FirebaseCRUDService firebaseCRUDService) {
        this.reportModelRepository = reportModelRepository;
        this.firebaseCRUDService = firebaseCRUDService;
    }

    @Scheduled(fixedDelay = 1000000)
    public void findAll() throws ExecutionException, InterruptedException {

        reportModelRepository.findAll().forEach(
                reportModel -> {
                    // Perform actions on each accountInfo object
                    System.out.println(reportModel.toString()+ LocalDateTime.now().getHour()+":"+LocalDateTime.now().getMinute()+":"+LocalDateTime.now().getSecond()); // Example: Print the accountInfo object
                    try {
                        ReportModel reportModel1 = getReportModel(reportModel);


                        /* accountInfo1.setFullName(accountInfo.getFullName());
                        accountInfo1.setAccountNumber(accountInfo.getAccountNumber());
                        accountInfo1.setAmount(accountInfo.getAmount());
                        accountInfo1.setId(accountInfo.getAccountNumber());
                       // accountInfo1.setLastModified(LocalDateTime.now());*/

                        firebaseCRUDService.createReportModel(reportModel1);

                    } catch (ExecutionException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
        );



    }

    private static ReportModel getReportModel(ReportModel reportModel) {
        ReportModel reportModel1=new ReportModel();


        reportModel1.setFbusinessDate(reportModel.getFbusinessDate());
        reportModel1.setNoTr(reportModel.getNoTr());
        reportModel1.setId(reportModel.getId());
        reportModel1.setNoDebit(reportModel.getNoDebit());
        reportModel1.setNoCredit(reportModel.getNoCredit());
        reportModel1.setTtlAmount(reportModel1.getTtlAmount());
        reportModel1.setLastModified(reportModel.getLastModified());
        reportModel1.setTtlCrAmt(reportModel.getTtlCrAmt());
        reportModel1.setTtlDrAmt(reportModel.getTtlDrAmt());
        return reportModel1;
    }

    private LocalDateTime  lastPollTime=LocalDateTime.now(); // Initial value, can be set to an appropriate timestamp

    @Scheduled(fixedDelay = 5000) // Poll every 5 seconds
    public void pollDatabase() {
        try (var client = MongoClients.create("mongodb://10.1.125.52:27017")) {
            var database = client.getDatabase("t24_to_mongo");
            var collection = database.getCollection("daily_report");

            // Query for new or modified documents

            var query = new Document("lastModified", new Document("$gt", lastPollTime));
            /*  query.forEach((accountInfo,acc)->{
            System.out.println(acc.toString());
        });*/
            System.out.println(collection.find(query));
            ;
            for (Document document : collection.find(query)) {
                var lastModified = document.get("name"); // Get the value of the "lastModified" field

                // Process the changed document
                System.out.println("Received change event: " + document.toJson());
                ReportModel reportModel1 = new ReportModel();

                reportModel1.setFbusinessDate(document.get("fbusinessDate").toString());
                reportModel1.setNoTr(document.get("noTr").toString());
                reportModel1.setId((ObjectId) document.get("id"));
                reportModel1.setNoDebit(document.get("noDebit").toString());
                reportModel1.setNoCredit(document.get("noCredit").toString());
                reportModel1.setTtlAmount(document.get("ttlAmount").toString());
                reportModel1.setLastModified((LocalDateTime) document.get("lastModified"));
                reportModel1.setTtlCrAmt(document.get("ttlCrAmt").toString());
                reportModel1.setTtlDrAmt(document.get("ttlDrAmt").toString());





                /* accountInfo1.setFullName(document.get("fullName").toString());
                accountInfo1.setAccountNumber(document.get("accountNumber").toString());
                accountInfo1.setAmount((Double) document.get("amount"));
                accountInfo1.setId(document.get("accountNumber").toString()); */

                firebaseCRUDService.createReportModel(reportModel1);


            }

            // Update the last poll time to the current time
            lastPollTime = LocalDateTime.now();
            System.out.println(lastPollTime+" last Polling");
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
