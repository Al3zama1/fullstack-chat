package com.abranlezama.fullstackchatapplication.repository;

import com.abranlezama.fullstackchatapplication.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository extends MongoRepository<User, String> {
}
