package coop.MongoToFirebase.models;

import lombok.Data;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("account_info")
public class AccountInfo {

    @Id
    @Generated

    private String id;


    private String accountNumber;


    private String fullName;


    private double amount;
    @LastModifiedDate
    private LocalDateTime lastModified;
}
