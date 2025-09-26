package com.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.domain.PayoutsStatus;
import com.ecom.model.Payouts;

import java.util.List;

public interface PayoutsRepository extends JpaRepository<Payouts,Long> {

    List<Payouts> findPayoutsBySellerId(Long sellerId);
    List<Payouts> findAllByStatus(PayoutsStatus status);
}
