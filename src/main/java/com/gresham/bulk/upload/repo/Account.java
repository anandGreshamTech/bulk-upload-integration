package com.gresham.bulk.upload.repo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Account {
    @Id
    String id;
}
