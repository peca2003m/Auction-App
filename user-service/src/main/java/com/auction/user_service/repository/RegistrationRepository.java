package com.auction.user_service.repository;


import com.auction.user_service.entity.Registration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RegistrationRepository extends CrudRepository<Registration, String> {

    Optional<Registration> findByToken(UUID token);


}
