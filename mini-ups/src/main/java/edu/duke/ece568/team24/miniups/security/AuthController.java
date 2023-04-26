package edu.duke.ece568.team24.miniups.security;

import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.duke.ece568.team24.miniups.model.Account;
import edu.duke.ece568.team24.miniups.service.AccountService;

@Controller
public class AuthController {

    private AccountService accountService;

    private PasswordEncoder passwordEncoder;

    public AuthController(AccountService accountService, PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/account/signup")
    public String getAccountSignup(Model model) {
        model.addAttribute("newUser", new SignupForm());
        return "account-signup";
    }

    @PostMapping("/account/signup")
    public String postAccountSingup(@Valid @ModelAttribute("newUser") SignupForm newUser,
            BindingResult result, Model model) {
        if (accountService.findByAccountName(newUser.getUsername()).isPresent()) {
            result.rejectValue("username", null, "Username exists");
        }
        if (newUser.getEmail() != "" && accountService.findByEmail(newUser.getEmail()).isPresent()) {
            result.rejectValue("email", null, "Email exists");
        }
        if (!newUser.getPassword().equals(newUser.getConfirmPassword())) {
            result.rejectValue("confirmPassword", null, "Passwords do not match");
        }
        if (result.hasErrors()) {
            model.addAttribute("newUser", newUser);
            return "account-signup";
        }
        accountService
                .createAccount(new Account(newUser.getUsername(),
                        passwordEncoder.encode(newUser.getPassword()),
                        newUser.getEmail(), "USER"));
        return "redirect:/account/login?signupsuccess=true";
    }

    @GetMapping("/account/login")
    public String getAccountSignin(Model model) {
        return "account-login";
    }

    @GetMapping("/account/profile")
    public String getAccountProfile(@AuthenticationPrincipal UserDetails user,
            Model model) {
        Account userAccount = accountService.findByAccountName(user.getUsername()).get();
        model.addAttribute("user", userAccount);
        model.addAttribute("emailForm", new EmailForm());
        model.addAttribute("passwordForm", new PasswordForm());
        return "account-profile";
    }

    @PostMapping("/account/profile/email/update")
    public String postAccountProfileEmailUpdate(@AuthenticationPrincipal UserDetails user,
            @Valid @ModelAttribute("emailForm") EmailForm emailForm,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "redirect:/account/profile?error=Invalid email format.";
        }
        Account userAccount = accountService.findByAccountName(user.getUsername()).get();
        userAccount.setEmail(emailForm.getEmail());
        accountService.updateAccount(userAccount.getAccountID(), userAccount);
        return "redirect:/account/profile?success=Email updated successfully.";
    }

    @PostMapping("/account/profile/password/update")
    public String postAccountProfilePasswordUpdate(@AuthenticationPrincipal UserDetails user,
            @Valid @ModelAttribute("passwordForm") PasswordForm passwordForm,
            BindingResult result, Model model) {
        Account userAccount = accountService.findByAccountName(user.getUsername()).get();
        if (result.hasErrors()) {
            return "redirect:/account/profile?error=Invalid password format.";
        } else if (!passwordEncoder.matches(passwordForm.getOldPassword(), userAccount.getPassword())) {
            return "redirect:/account/profile?error=Old password doesn't match";
        } else if (!passwordForm.getnewPassword().equalsIgnoreCase(passwordForm.getConfirmPassword())) {
            return "redirect:/account/profile?error=Confirm password doesn't match";
        } else {
            userAccount.setPassword(passwordEncoder.encode(passwordForm.getConfirmPassword()));
            accountService.updateAccount(userAccount.getAccountID(), userAccount);
            return "redirect:/account/profile?success=Password updated successfully.";
        }
    }
}
