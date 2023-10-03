package coop.MongoToFirebase.repository;


import coop.MongoToFirebase.models.AccountInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountInfoRepository extends MongoRepository<AccountInfo,String> {
}
