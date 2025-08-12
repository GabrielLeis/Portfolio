package com.roundforma.website.springboot_roundforma_website.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.roundforma.website.springboot_roundforma_website.models.StandardUser;

@Repository
public interface StandardUserRepository extends JpaRepository<StandardUser, Integer> {

}
