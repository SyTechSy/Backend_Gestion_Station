package com.example.Gestion_Station01.services;

import com.example.Gestion_Station01.exceptions.DuplicateException;
import com.example.Gestion_Station01.exceptions.NotFoundException;
import com.example.Gestion_Station01.models.Admin;
import com.example.Gestion_Station01.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.NotContextException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AdminService {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    PhotoServise photoServise;

    @Autowired
    EmailService emailService;

    public Admin ajouterAdmin(Admin admin) {
        if (adminRepository.findByEmailAdmin(admin.getEmailAdmin()) == null) {
            return adminRepository.save(admin);
        } else {
            throw new DuplicateException("Cet email existe déjà");
        }
    }

    // POUR CONNEXION D'ADMINISTRATEUR SERVICE
    public Admin connexionAdmin(String emailAdmin, String motDePasseAdmin) {
        if (adminRepository.findByEmailAdminAndMotDePasseAdmin(emailAdmin, motDePasseAdmin) != null) {
            return adminRepository.findByEmailAdminAndMotDePasseAdmin(emailAdmin, motDePasseAdmin);
        } else {
            throw new NotFoundException("Email ou votre mot de passe est incorret");
        }
    }

    // AJOUTER PHOTO DE PROFIL DES GETIONNAIRES
    public Admin ajouterPhotoDeProfil(Long idAdmin, MultipartFile imageFile) throws Exception {

        Optional<Admin> optionalAdmin = adminRepository.findById(idAdmin);

        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();
            String photoUrl = photoServise.ajouterPhoto(imageFile);

            admin.setPhoto(photoUrl);
            return adminRepository.save(admin);
        } else {
            throw new NotContextException("Adminavec ID " + idAdmin + "non trouvé");
        }
    }

    // MODIFICATION PHOTO DE PROFIL DES GETIONNAIRES
    public Admin modifierPhotoDeProfil(Long idAdmin, MultipartFile imageFile) throws Exception {

        Optional<Admin> optionalAdmin = adminRepository.findById(idAdmin);

        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();
            String photoUrl = photoServise.modifierPhoto(imageFile);

            admin.setPhoto(photoUrl);
            return adminRepository.save(admin);
        } else {
            throw new NotContextException("Une erreur s'est produite lors de la mise à jour de l'annonce avec l'ID : " + idAdmin);
        }
    }

    public void generateVerificationCode(Admin admin) {
        String code = String.format("%05d", new Random().nextInt(10000));

        admin.setVerificationCode(code);

        admin.setVerificationCodeExpiration(LocalDateTime.now().plusMinutes(10));

        adminRepository.save(admin);

        emailService.sendVerificationEmail(admin.getEmailAdmin(), code);
    }

    public boolean verfyCode(Admin admin, String code) {
        return code.equals(admin.getVerificationCode()) &&
                admin.getVerificationCodeExpiration().isAfter(LocalDateTime.now());
    }

    public Admin findByEmail(String emailAdmin) {
        return adminRepository.findByEmailAdmin(emailAdmin);
    }

}
