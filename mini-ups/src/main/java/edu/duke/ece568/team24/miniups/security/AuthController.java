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

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import edu.duke.ece568.team24.miniups.dto.*;
import edu.duke.ece568.team24.miniups.service.*;


@Controller
public class AuthController {

    private AccountService accountService;
    private OrderService orderService;
    private PackageService packageService;

    public AuthController(AccountService accountService, OrderService orderService, PackageService packageService) {
        this.accountService = accountService;
        this.orderService = orderService;
        this.packageService = packageService;
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

    @GetMapping("/account/order")
    public String getOrders(@AuthenticationPrincipal UserDetails user, Model model) {

        // 输入accountid即可查询

        AccountDto accountDto = accountService.findByUsername(user.getUsername());
        if(accountDto == null){
            return "index";
        }

        List<OrderDto> orders = orderService.findByOwner(accountDto.getUsername());

//        for(int i = 0;i<realorders.size();i++){
//            CombineMyOrder order = new CombineMyOrder();
//            order.setOrderID(realorders.get(i).getOrderID());
//            order.setDestinationX(realorders.get(i).getDestinationX());
//            order.setDestinationY(realorders.get(i).getDestinationY());
//
//            List<MyPackage> realpackages = myPackageService.findPackagesByOrderID(realorders.get(i).getOrderID());
//            order.setPackages(realpackages);
//            orders.add(order);
//        }

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

        AccountDto accountDto = accountService.findByUsername(user.getUsername());
        if(accountDto == null){
            return "redirect:/account/order?error=Invalid input for destination.";
        }

        String strorderID = request.getParameter("orderID");
        int id = Integer.parseInt(strorderID);
        OrderDto orderDto = orderService.findById(id);
        if(orderDto == null){
            return "redirect:/account/order?error=Invalid input for destination.";
        }

        boolean checkavailablepackage = false;
        for(int i = 0;i<orderDto.getPackages().size();i++){
            if(!orderDto.getPackages().get(i).getStatus().equals("delivered")){
                checkavailablepackage = true;
                break;
            }
        }
        if(!checkavailablepackage){
            return "redirect:/account/order?error=All the packages are out for delivery.";
        }

        OrderDto updatedorderDto = orderService.updateDestination(id, destinationForm.getNewDestinationX(), destinationForm.getNewDestinationY());

        // 发更改送货地址的消息给amazon

        return "redirect:/account/order?success=Destination updated successfully.";
    }
}
