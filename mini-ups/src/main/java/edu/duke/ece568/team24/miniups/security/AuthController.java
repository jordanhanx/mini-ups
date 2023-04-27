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

import edu.duke.ece568.team24.miniups.model.*;
import edu.duke.ece568.team24.miniups.service.*;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import static edu.duke.ece568.team24.miniups.model.myenum.MyPackageStatus.DELIVERING;

@Controller
public class AuthController {

    private AccountService accountService;
    private MyOrderService myorderService;
    private MyPackageService myPackageService;

    private PasswordEncoder passwordEncoder;

    public AuthController(AccountService accountService, MyOrderService myorderService, MyPackageService myPackageService, PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.myorderService = myorderService;
        this.myPackageService = myPackageService;
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
            result.rejectValue("confirmPassword", null, "Confirm password doesn't match");
        }
        if (result.hasErrors()) {
            model.addAttribute("newUser", newUser);
            return "account-signup";
        }
        accountService
                .createAccount(new Account(newUser.getUsername(),
                        passwordEncoder.encode(newUser.getPassword()),
                        newUser.getEmail(), "USER"));
        return "redirect:/account/login?success=Sign up successfully.";
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

    @GetMapping("/account/order")
    public String getOrders(@AuthenticationPrincipal UserDetails user, Model model) {

        // 输入accountid即可查询

        Optional<Account> checkuserAccount = accountService.findByAccountName(user.getUsername());
        Account userAccount;
        if(checkuserAccount.isPresent()){
            userAccount = checkuserAccount.get();
        }else{
            return "index";
        }

        Long userID = userAccount.getAccountID();

        List<MyOrder> realorders = myorderService.findOrdersByAccount(userID);
        List<CombineMyOrder> orders = new ArrayList<>();
        for(int i = 0;i<realorders.size();i++){
            CombineMyOrder order = new CombineMyOrder();
            order.setOrderID(realorders.get(i).getOrderID());
            order.setDestinationX(realorders.get(i).getDestinationX());
            order.setDestinationY(realorders.get(i).getDestinationY());

            List<MyPackage> realpackages = myPackageService.findPackagesByOrderID(realorders.get(i).getOrderID());
            order.setPackages(realpackages);
            orders.add(order);
        }

        model.addAttribute("orders",orders);
        model.addAttribute("destinationForm", new DestinationForm());

        return "order-list";
    }

    @PostMapping("/account/order/destinationupdate")
    public String postOrderDestinationUpdate(@AuthenticationPrincipal UserDetails user,
                                                @Valid @ModelAttribute("destinationForm") DestinationForm destinationForm,
                                                BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            return "redirect:/account/order?error=Invalid input for destination.";
        }
        Optional<Account> checkuserAccount = accountService.findByAccountName(user.getUsername());
        Account userAccount;
        if(checkuserAccount.isPresent()){
            userAccount = checkuserAccount.get();
        }else{
            return "redirect:/account/order?error=Invalid input for destination.";
        }

        String strorderID = request.getParameter("orderID");
        Long orderID = Long.parseLong(strorderID);
        Optional<MyOrder> checkmyorder = myorderService.getOrderById(orderID);
        MyOrder myorder;
        if(checkmyorder.isPresent()){
            myorder = checkmyorder.get();
        }else{
            return "redirect:/account/order?error=Invalid input for destination.";
        }

        List<MyPackage> realpackages = myPackageService.findPackagesByOrderID(myorder.getOrderID());
        boolean checkavailablepackage = false;
        for(int i = 0;i<realpackages.size();i++){
            if(realpackages.get(i).getStatus() == DELIVERING){
                checkavailablepackage = true;
                break;
            }
        }
        if(!checkavailablepackage){
            return "redirect:/account/order?error=All the packages are out for delivery.";
        }
        myorder.setDestinationX(destinationForm.getNewDestinationX());
        myorder.setDestinationY(destinationForm.getNewDestinationY());
        myorderService.updateOrder(orderID, myorder);
        return "redirect:/account/order?success=Destination updated successfully.";
    }
}
