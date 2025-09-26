package com.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.domain.AccountStatus;
import com.ecom.model.Seller;

import java.util.List;

public interface SellerRepository extends JpaRepository<Seller,Long> {

    Seller findByEmail(String email);
    List<Seller> findByAccountStatus(AccountStatus status);
}
