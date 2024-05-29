package com.example.Gestion_Station01.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAdmin;

    @NotNull(message = "Champs nom laisser vide")
    @Column(nullable = false)
    private String nomAdmin;

    @NotNull(message = "Champs prenom laisser vide")
    @Column(nullable = false)
    private String prenomAdmin;

    @NotNull(message = "Champs email laisser vide")
    @Column(nullable = false)
    @Email(message = "Email invalid")
    private String emailAdmin;


    @Column(nullable = false)
    private String photo;

    @NotNull(message = "Champs mot de passe laisser vide")
    @Size(min = 8, message = "Le Mot de passe doit être 8 chiffre ou plus")
    @Column(nullable = false)
    private String motDePasseAdmin;

    @Column(nullable = false)
    private String verificationCode;

    //@Column(nullable = false)
    private LocalDateTime verificationCodeExpiration;
}
