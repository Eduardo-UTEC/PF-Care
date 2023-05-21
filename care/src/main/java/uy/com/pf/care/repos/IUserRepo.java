package uy.com.pf.care.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uy.com.pf.care.model.documents.EmergencyService;
import uy.com.pf.care.model.documents.User;

import java.util.List;

@Repository
public interface IUserRepo extends MongoRepository<User, String> {

    User findByUserName(String userName);

}
