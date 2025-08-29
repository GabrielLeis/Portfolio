package com.roundforma.website.springboot_roundforma_website.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.roundforma.website.springboot_roundforma_website.models.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

}
