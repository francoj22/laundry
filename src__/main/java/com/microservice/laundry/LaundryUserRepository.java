package com.microservice.laundry;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LaundryUserRepository extends JpaRepository<LaundryUser, Long> {
}
