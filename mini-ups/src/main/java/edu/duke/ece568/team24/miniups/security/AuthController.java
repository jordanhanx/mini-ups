package edu.duke.ece568.team24.miniups.security;

import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import edu.duke.ece568.team24.miniups.dto.AccountDto;
import edu.duke.ece568.team24.miniups.service.AccountService;

@Controller
public class AuthController {

    private AccountService accountService;

    public AuthController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/account/signup")
    public String getAccountSignup(Model model) {
        model.addAttribute("newUser", new SignupForm());
        return "account-signup";
    }

    @PostMapping("/account/signup")
    public String postAccountSingup(@Valid @ModelAttribute("newUser") SignupForm newUser,
            BindingResult result, Model model) {
        if (accountService.findByUsername(newUser.getUsername()) != null) {
            result.rejectValue("username", null, "Username exists");
        }
        if (newUser.getEmail() != "" && accountService.findByEmail(newUser.getEmail()) != null) {
            result.rejectValue("email", null, "Email exists");
        }
        if (!newUser.getPassword().equals(newUser.getConfirmPassword())) {
            result.rejectValue("confirmPassword", null, "Confirm password doesn't match");
        }
        if (result.hasErrors()) {
            model.addAttribute("newUser", newUser);
            return "account-signup";
        }
        if (newUser.getEmail() == "") {
            accountService.createAccount(newUser.getUsername(), newUser.getPassword(), "USER");
        } else {
            accountService.createAccount(newUser.getUsername(), newUser.getPassword(), "USER", newUser.getEmail());
        }
        return "redirect:/account/login?success=Sign up successfully.";
    }

    @GetMapping("/account/login")
    public String getAccountSignin(Model model) {
        return "account-login";
    }

    @GetMapping("/account/profile")
    public String getAccountProfile(@AuthenticationPrincipal UserDetails user,
            Model model) {
        AccountDto userAccount = accountService.findByUsername(user.getUsername());
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
        } else if (emailForm.getEmail() != "" && accountService.findByEmail(emailForm.getEmail()) != null) {
            return "redirect:/account/profile?error=Email exits.";
        }
        accountService.updateEmail(user.getUsername(), emailForm.getEmail());
        return "redirect:/account/profile?success=Email updated successfully.";
    }

    @PostMapping("/account/profile/password/update")
    public String postAccountProfilePasswordUpdate(@AuthenticationPrincipal UserDetails user,
            @Valid @ModelAttribute("passwordForm") PasswordForm passwordForm,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "redirect:/account/profile?error=Invalid password format.";
        } else if (!passwordForm.getnewPassword().equalsIgnoreCase(passwordForm.getConfirmPassword())) {
            return "redirect:/account/profile?error=Confirm password doesn't match";
        } else if (!accountService.matchPassword(user.getUsername(), passwordForm.getOldPassword())) {
            return "redirect:/account/profile?error=Old password doesn't match";
        } else {
            accountService.updatePassword(user.getUsername(), passwordForm.getConfirmPassword());
            return "redirect:/account/profile?success=Password updated successfully.";
        }
    }
}
