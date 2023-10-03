package coop.MongoToFirebase.service;
import java.time.LocalDateTime;
import coop.MongoToFirebase.models.AccountInfo;
import coop.MongoToFirebase.repository.AccountInfoRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
                    System.out.println(accountInfo); // Example: Print the accountInfo object
                    try {
                        firebaseCRUDService.createAccountInfo(accountInfo);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
        );




    }
}
