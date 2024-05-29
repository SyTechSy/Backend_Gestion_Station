package com.example.Gestion_Station01.controller;

import com.example.Gestion_Station01.models.Admin;
import com.example.Gestion_Station01.services.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    // POUR INSCRIPTION D'ADMINISTRATEUR CONTRÔLLEUR
    @PostMapping("/ajouter")
    public ResponseEntity<Object> ajouterAdmin(@Valid @RequestBody Admin admin) {
        Admin verifAdmin = adminService.ajouterAdmin(admin);
        if (verifAdmin != null) {
            return new ResponseEntity<>(admin, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cette admin n'existe pas",HttpStatus.NOT_FOUND);
        }
    }

    // POUR CONNEXION D'ADMINISTRATEUR CONTRÔLLEUR
    @PostMapping("/connexion")
    public String Connexion(@RequestParam("emailAdmin") String emailAdmin,
                            @RequestParam("motDePasseAdmin") String motDePasseAdmin) {
        Admin admin = adminService.connexionAdmin(emailAdmin, motDePasseAdmin);
        if (admin != null && admin.getMotDePasseAdmin().equals(motDePasseAdmin)) {
            adminService.generateVerificationCode(admin);
            return "Le code de vérification de votre compte est envoyer sur votre email " + admin.getNomAdmin() + " " + admin.getPrenomAdmin() + " !";
        }
        return "Invalid credentials";
    }

    // LIST DES D'ADMINISTRATEUR CONTRÔLLEUR

    // MODIFICATION DE L'ADMINISTRATEUR CONTRÔLLEUR

    // SUPPRESSION DE L'ADMINISTRATEUR CONTRÔLLEUR

    // VERIFICATION DE CODE
    @PostMapping("/verif")
    public ResponseEntity<?> verifyCode(
            @RequestParam String emailAdmin,
            @RequestParam String verificationCode) {

        Admin admin = adminService.findByEmail(emailAdmin);
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
        }

        boolean isValid = adminService.verfyCode(admin, verificationCode);
        if (isValid) {
            return ResponseEntity.ok("Verification de votre code est fait avec succès");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Votre code verification est incorrect");
        }
    }

    // AJOUTER PHOTO DE PROFIL DES GETIONNAIRES
    @PostMapping("/{id}/ajouter-profil")
    public ResponseEntity<Admin> ajouterPhotoProfil(@PathVariable Long id, @RequestParam("photo") MultipartFile imageFile) {
        try {
            Admin adminMisAJour = adminService.ajouterPhotoDeProfil(id, imageFile);
            return ResponseEntity.ok(adminMisAJour);
        } catch (Exception e) {
            ResponseEntity.status(500).body(null);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // MODIFICATION PHOTO DE PROFIL DES GETIONNAIRES
    @PutMapping("/{id}/modifier-profil")
    public ResponseEntity<Admin> modifierPhotoProfil(@PathVariable Long id, @RequestParam("photo") MultipartFile imageFile) {
        try {
            Admin adminMisAJour = adminService.modifierPhotoDeProfil(id, imageFile);
            return ResponseEntity.ok(adminMisAJour);
        } catch (Exception e) {
            ResponseEntity.status(500).body(null);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
