package coop.MongoToFirebase.models;

import lombok.Data;
import lombok.Generated;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("report_model")
public class ReportModel {

    @Id
    private ObjectId id;

    private String fbusinessDate;
    private String noCredit;
    private String noDebit;
    private String noTr;
    private String ttlAmount;
    private String ttlCrAmt;
    private String ttlDrAmt;

    @LastModifiedDate
    private LocalDateTime lastModified;

}