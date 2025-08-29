package com.roundforma.website.springboot_roundforma_website.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.roundforma.website.springboot_roundforma_website.models.Base;

@Repository
public interface BaseRepository extends JpaRepository<Base, Integer> {

}
