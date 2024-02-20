package cg.tcarespb.controller.RESTcontroller.authentication;


import cg.tcarespb.config.JwtUtil;
import cg.tcarespb.controller.RESTcontroller.authentication.response.AuthResponse;
import cg.tcarespb.models.*;
import cg.tcarespb.models.enums.EGender;
import cg.tcarespb.models.enums.ERole;
import cg.tcarespb.models.enums.EStatus;
import cg.tcarespb.registration.event.listener.RegistrationCompleteEventListener;
import cg.tcarespb.registration.password.PasswordResetRequest;
import cg.tcarespb.registration.password.PasswordResetToken;
import cg.tcarespb.registration.password.PasswordResetTokenService;
import cg.tcarespb.registration.token.VerificationToken;
import cg.tcarespb.repository.*;
import cg.tcarespb.service.account.AccountService;
import cg.tcarespb.service.account.request.AccountSaveRequest;
import cg.tcarespb.service.account.request.LoginSaveRequest;
import cg.tcarespb.service.user.UserService;
import cg.tcarespb.util.AppUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static cg.tcarespb.models.enums.ERole.*;


@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
@Transactional
@CrossOrigin("https://t-care-center.vercel.app")
public class AuthResController {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final CartRepository cartRepository;
    private final AccountService accountService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final RegistrationCompleteEventListener eventListener;
    private final HttpServletRequest servletRequest;
    private final SalerRepository salerRepository;
    private final PasswordResetTokenService passwordResetTokenService;
    private final String SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    @PostMapping("/check-mail")
    public ResponseEntity<?> checkEmail(@RequestBody AccountSaveRequest request) {
        if (!accountRepository.existsByEmailIgnoreCase(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email không tồn tại");
        } else {
            return ResponseEntity.ok("Email oke");
        }
    }


    @Transactional
    @PostMapping("/users/cart/account")
    public ResponseEntity<?> register(@RequestBody AccountSaveRequest request) {

        if (accountRepository.existsByEmailIgnoreCase(request.getEmail()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email Đã Tồn Tại");
        var account = AppUtil.mapper.map(request, Account.class);
        account.setERole(ERole.valueOf(request.getRole()));
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        accountRepository.save(account);
        var user = AppUtil.mapper.map(request, User.class);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setGender(EGender.valueOf(request.getGender()));
        user.setPersonID(request.getPersonId());
        user.setPhoneNumber(request.getPhoneNumber());

        userRepository.save(user);
        account.setUser(user);
        account.setTime(LocalDate.now());
        accountRepository.save(account);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCreateAt(LocalDateTime.now());
        cartRepository.save(cart);

        return new ResponseEntity<>(cart.getId(), HttpStatus.CREATED);
    }
    @Transactional
    @PostMapping("/users/account")
    public ResponseEntity<?> registerUser(@RequestBody AccountSaveRequest request) {

        if (accountRepository.existsByEmailIgnoreCase(request.getEmail()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email Đã Tồn Tại");
        var account = AppUtil.mapper.map(request, Account.class);
        account.setERole(ERole.valueOf(request.getRole()));
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        accountRepository.save(account);
        var user = AppUtil.mapper.map(request, User.class);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setGender(EGender.valueOf(request.getGender()));
        user.setPersonID(request.getPersonId());
        user.setPhoneNumber(request.getPhoneNumber());

        userRepository.save(user);
        account.setUser(user);
        account.setTime(LocalDate.now());
        accountRepository.save(account);



        return new ResponseEntity<>(account.getUser().getId(), HttpStatus.CREATED);
    }

    @PostMapping("/employees/account")
    public ResponseEntity<?> registerEmployees(@RequestBody AccountSaveRequest request) {
        if (accountRepository.existsByEmailIgnoreCase(request.getEmail()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email Đã Tồn Tại");

        Account account = new Account();
        account.setEmail(request.getEmail());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setERole(ROLE_EMPLOYEE);
        accountRepository.save(account);
        Employee employee = new Employee();
        employee.setGender(EGender.valueOf(request.getGender()));
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setPersonID(request.getPersonId());
        employee.setPhoneNumber(request.getPhoneNumber());
        employeeRepository.save(employee);
        account.setEmployee(employee);
        account.setTime(LocalDate.now());
        accountRepository.save(account);
        String employeeId = employee.getId();

        return new ResponseEntity<>(employeeId, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginSaveRequest request) {
        var account = accountRepository.findAccountByEmail(request.getUsername());
        if (account.isPresent()) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(request.getPassword(), account.get().getPassword())) {
                if (account.get().getDeleted() == true) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tài khoản của bạn hiện tại không thể đăng nhập");
                }
                String token = jwtToken(account.get().getEmail());
                AuthResponse authResponse = new AuthResponse();

                authResponse.setJwt(token);
                authResponse.setIsAdmin(account.get().getERole().equals(ROLE_ADMIN));
                authResponse.setIsEmployee(account.get().getERole().equals(ROLE_EMPLOYEE));
                authResponse.setIsUser(account.get().getERole().equals(ROLE_USER));
                authResponse.setIsSale(account.get().getERole().equals(ROLE_SALER));
                if (account.get().getUser() != null) {
                    authResponse.setIdAccount(account.get().getUser().getId());
                } else if (account.get().getEmployee() != null) {
                    authResponse.setIdAccount(account.get().getEmployee().getId());
                } else if (account.get().getSaler() != null) {
                    authResponse.setIdAccount(account.get().getSaler().getId());
                } else {
                    authResponse.setIdAccount(account.get().getId());
                }
                return ResponseEntity.ok(authResponse);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tên Đăng Nhập Hoặc Mật Khẩu Không Đúng");
    }


//    @PostMapping("/loginGoogle")
//    public ResponseEntity<?> loginGoogle(@RequestBody LoginGoogleSaveRequest request){
//        List<String> roles = new ArrayList<>();
//        roles.add(ROLE_USER.toString());
//        if (!accountRepository.existsByEmailIgnoreCase(request.getEmail())) {
//
//            Account account = new Account();
//            account.setRole(Role.ROLE_USER);
//            account.setUsername(request.getEmail());
//            account.setPassword(passwordEncoder.encode(request.getEmail()));
//            account.setAvatar(file);
//            userRepository.save(user);
//
//            UserInfo userInfo = new UserInfo();
//            userInfo.setEmail(request.getEmail());
//            userInfo.setFullName(request.getName());
//            userInfo.setUser(user);
//            userInfoRepository.save(userInfo);
//
//        }
//        return ResponseEntity.ok(getToken(request.getEmail()));
//    }


    private String jwtToken(String username) {
        long expiredTime = 86400000;
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiredTime))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getToken(String email) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        email
                )
        );
        return jwtUtil.generateToken(email, ROLE_USER.toString());
    }

    //    @GetMapping("/verifyEmail")
//    public String sendVerificationToken(@RequestParam("token") String token){
//
//        String url = applicationUrl(servletRequest)+"/register/resend-verification-token?token="+token;
//
//        VerificationToken theToken = tokenRepository.findByToken(token);
//        if (theToken.getUser().isEnabled()){
//            return "This account has already been verified, please, login.";
//        }
//        String verificationResult = userService.validateToken(token);
//        if (verificationResult.equalsIgnoreCase("valid")){
//            return "Email verified successfully. Now you can login to your account";
//        }
//        return "Invalid verification link, <a href=\"" +url+"\"> Get a new verification link. </a>";
//    }


    @PostMapping("/password-reset-request")
    public ResponseEntity<?> resetPasswordRequest(@RequestBody PasswordResetRequest passwordResetRequest)
            throws MessagingException, UnsupportedEncodingException {

        Optional<Account> account = accountService.findByEmail(passwordResetRequest.getEmail());
        if (account.isPresent()) {
             PasswordResetToken token = passwordResetTokenRepository.findByAccount_Id(account.get().getId());
            if(token == null) {
                String passwordResetToken = UUID.randomUUID().toString();
                accountService.createPasswordResetTokenForUser(account.get(), passwordResetToken);
                passwordResetEmailLink(account.get(), "http://localhost:3000", passwordResetToken);
            } else if (token.getExpirationTime().toInstant().isBefore(Instant.now())){
                passwordResetTokenService.deleteTokenByAccount(account.get());
                String passwordResetToken = UUID.randomUUID().toString();
                accountService.createPasswordResetTokenForUser(account.get(), passwordResetToken);
                passwordResetEmailLink(account.get(), "http://localhost:3000", passwordResetToken);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token đã tồn tại");            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String passwordResetEmailLink(Account account, String applicationUrl,
                                          String passwordToken) throws MessagingException, UnsupportedEncodingException, MessagingException, UnsupportedEncodingException {
        String url = applicationUrl + "/reset-password?token=" + passwordToken;
        eventListener.sendPasswordResetVerificationEmail(url, account);
        return url;
    }

//    private void resendRegistrationVerificationTokenEmail(User theUser, String applicationUrl,
//                                                          VerificationToken verificationToken) throws MessagingException, UnsupportedEncodingException {
//        String url = applicationUrl + "/register/verifyEmail?token=" + verificationToken.getToken();
//        eventListener.sendVerificationEmail(url);
//        log.info("Click the link to verify your registration :  {}", url);
//    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody PasswordResetRequest passwordResetRequest,
                                @RequestParam("token") String token) {
        String tokenVerificationResult = passwordResetTokenService.validatePasswordResetToken(token);
        if (!tokenVerificationResult.equalsIgnoreCase("valid")) {
            passwordResetTokenService.deleteToken(token);
            return "Invalid token password reset token";
        }
        Optional<Account> theAccount = Optional.ofNullable(accountService.findAccountByPasswordToken(token));
        if (theAccount.isPresent()) {
            accountService.changePassword(theAccount.get(), passwordResetRequest.getNewPassword());
            passwordResetTokenService.deleteToken(token);
            return "Password has been reset successfully";
        }
        passwordResetTokenService.deleteToken(token);
        return "Invalid password reset token";
    }
//    @GetMapping("/{account_id}")
//    public String findByAccount( @PathVariable("idEmployee") String idEmployee)
    @PostMapping("/change-password")
    public String changePassword(@RequestBody PasswordResetRequest passwordResetRequest) {
        Account account = accountService.findByEmail(passwordResetRequest.getEmail()).get();
        if (!accountService.oldPasswordIsValid(account, passwordResetRequest.getOldPassword())) {
            return "Incorrect old password";
        }
        accountService.changePassword(account, passwordResetRequest.getNewPassword());
        return "Password changed successfully";
    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":"
                + request.getServerPort() + request.getContextPath();
    }

    @PostMapping("/saler/account")
    public ResponseEntity<?> registerSaler(@RequestBody AccountSaveRequest request) {
        if (accountRepository.existsByEmailIgnoreCase(request.getEmail()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email Đã Tồn Tại");

        Account account = new Account();
        account.setEmail(request.getEmail());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setERole(ROLE_SALER);
        accountRepository.save(account);
        Saler saler = new Saler();
        saler.setGender(EGender.valueOf(request.getGender()));
        saler.setFirstName(request.getFirstName());
        saler.setLastName(request.getLastName());
        saler.setPersonID(request.getPersonId());
        saler.setPhoneNumber(request.getPhoneNumber());
        salerRepository.save(saler);
        account.setSaler(saler);
        account.setTime(LocalDate.now());
        accountRepository.save(account);
        String salerId = saler.getId();

        return new ResponseEntity<>(salerId, HttpStatus.CREATED);
    }


}
