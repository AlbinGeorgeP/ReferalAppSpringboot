package com.social.referral.repository;

import com.social.referral.entities.Company;
import com.social.referral.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

    public Company findByName(String name);
}
