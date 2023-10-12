package coop.MongoToFirebase.service;
import java.time.LocalDateTime;
import coop.MongoToFirebase.models.AccountInfo;
import coop.MongoToFirebase.repository.AccountInfoRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
                        firebaseCRUDService.createAccountInfo(accountInfo1);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
        );



    }
}
