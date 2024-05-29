package com.example.Gestion_Station01.repositories;

import com.example.Gestion_Station01.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByEmailAdmin (String emailAdmin);
    Admin findByEmailAdminAndMotDePasseAdmin(String emailAdmin, String motDePasseAdmin);
}
