package com.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.model.Deal;

public interface DealRepository extends JpaRepository<Deal,Long> {

}
