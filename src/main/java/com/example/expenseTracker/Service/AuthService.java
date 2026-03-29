    package com.example.expenseTracker.Service;

    import java.time.LocalDateTime;
    import java.util.UUID;

    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.stereotype.Service;

    import com.example.expenseTracker.Dto.AuthRequestDto;
    import com.example.expenseTracker.Dto.LoginRequestDto;
    import com.example.expenseTracker.Entity.ProfileEntity;
    import com.example.expenseTracker.Entity.VerificationToken;
    import com.example.expenseTracker.Repository.ProfileRepository;
    import com.example.expenseTracker.Repository.VerificationTokenRepository;

    @Service
    public class AuthService {

        private final ProfileRepository repository;
        private final VerificationTokenRepository tokenRepository;
        private final EmailService emailService;

        public AuthService(ProfileRepository repository,
                           VerificationTokenRepository tokenRepository,
                           EmailService emailService) {
            this.repository = repository;
            this.tokenRepository = tokenRepository;
            this.emailService = emailService;
        }

        public String register(AuthRequestDto request) {
            if (repository.findByEmail(request.getEmail()).isPresent()) {
                return "Email already exists";
            }

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            ProfileEntity user = new ProfileEntity();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(encoder.encode(request.getPassword()));
            user.setVerified(false);

            ProfileEntity savedUser = repository.save(user);

            String tokenValue = UUID.randomUUID().toString();

            VerificationToken token = new VerificationToken();
            token.setToken(tokenValue);
            token.setExpiryTime(LocalDateTime.now().plusMinutes(30));
            token.setUser(savedUser);

            tokenRepository.save(token);

            emailService.sendVerificationEmail(savedUser.getEmail(), tokenValue);

            return "User registered successfully. Please verify your email.";
        }

        public String verifyEmail(String tokenValue) {
            VerificationToken token = tokenRepository.findByToken(tokenValue).orElse(null);

            if (token == null) {
                return "Invalid verification token";
            }

            if (token.getExpiryTime().isBefore(LocalDateTime.now())) {
                return "Verification token expired";
            }

            ProfileEntity user = token.getUser();
            user.setVerified(true);
            repository.save(user);

            tokenRepository.delete(token);

            return "Email verified successfully";
        }

        public String login(LoginRequestDto request) {
            ProfileEntity user = repository.findByEmail(request.getEmail()).orElse(null);

            if (user == null) {
                return "User not found";
            }

            if (!user.isVerified()) {
                return "Please verify your email first";
            }

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            if (!encoder.matches(request.getPassword(), user.getPassword())) {
                return "Invalid password";
            }

            return "Login Successful";
        }
    }