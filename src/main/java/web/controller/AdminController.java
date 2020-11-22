package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import web.model.Role;
import web.model.User;
import web.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/login");
        return modelAndView;
    }

    @GetMapping("userslist")
    public String userslist(ModelMap model) {
        model.addAttribute("user", userService.findAll());
        return "admin/userslist";
    }

//    @GetMapping("adduser")
//    public ModelAndView addUser() {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("admin/adduser");
//        modelAndView.addObject("user", new User());
//        modelAndView.addObject("roles", userService.findAllRole());
//        return modelAndView;
//    }
//
//    @PostMapping("adduser")
//    public ModelAndView addUser(User user) {
//        ModelAndView modelAndView = new ModelAndView();
//        userService.saveUser(user);
//        modelAndView.setViewName("redirect:/admin/test");
//        return modelAndView;
//    }
//
//    @GetMapping("edituser/{id}")
//    public ModelAndView updateUser(@PathVariable("id") Long id) {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("user", userService.findById(id));
//        modelAndView.addObject("roles", userService.findAllRole());
//        modelAndView.setViewName("admin/edituser");
//        return modelAndView;
//    }
//
//    @PostMapping("edituser/{id}")
//    public ModelAndView updateUser(User user) {
//        ModelAndView modelAndView = new ModelAndView();
//        userService.saveUser(user);
//        modelAndView.setViewName("redirect:/admin/userslist");
//        return modelAndView;
//    }
//
//    @GetMapping("delete/{id}")
//    public ModelAndView deleteUser(@PathVariable("id") Long id) {
//        ModelAndView modelAndView = new ModelAndView();
//        userService.deleteById(id);
//        modelAndView.setViewName("redirect:/admin/userslist");
//        return modelAndView;
//    }

    //================================//================================
    @GetMapping("/test")
    public ModelAndView test() {
        ModelAndView modelAndView = new ModelAndView("/admin/test");
        User thisUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        modelAndView.addObject("user", thisUser);
        modelAndView.addObject("listUsers", userService.findAll());
        modelAndView.addObject("allRoles", userService.findAllRole());
        return modelAndView;
    }

    @GetMapping("/test2")
    public ModelAndView test2() {
        ModelAndView modelAndView = new ModelAndView("/admin/test2");
        User thisUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        modelAndView.addObject("activeUser", thisUser);
        modelAndView.addObject("listUsers", userService.findAll());
        modelAndView.addObject("allRoles", userService.findAllRole());
        return modelAndView;
    }

    @PostMapping("/adduser")
    public ModelAndView addUser(User user) {
//        Set<Role> roleSe = new HashSet<>();
//        if (roles.contains("USER")){
//            roleSe.add(new Role(2L, "USER"));
//            user.setRoles(roleSe);
//        }
//        if (roles.contains("ADMIN")){
//            roleSe.add(new Role(1L, "ADMIN"));
//            user.setRoles(roleSe);
//        }
//        user.setRoles(roleSe);
        userService.saveUser(user);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/test2");
        return modelAndView;
    }

    @PostMapping("/delete")
    public ModelAndView deleteUserById(@RequestParam(value = "idDelete", required = false) Long id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/test2");
        userService.deleteById(id);
        return modelAndView;
    }

    @PostMapping("/edit")
    public ModelAndView editUser(@RequestParam(value = "idEdit", required = false) Long id,
                           @RequestParam(value = "firstNameEdit", required = false) String firstname,
                           @RequestParam(value = "lastNameEdit", required = false) String lastname,
                           @RequestParam(value = "ageEdit", required = false) int age,
                           @RequestParam(value = "emailEdit", required = false) String email,
                           @RequestParam(value = "passwordEdit", required = false) String password,
                           @RequestParam(value = "editRole", required = false) String role,
                           Model model) {
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/test2");
//        model.addAttribute("allRoles", userService.findAllRole());

        User user = userService.findById(id);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setAge(age);
        user.setMail(email);

        if (password == null || password == ""){
            user.setPassword(user.getPassword());
        } else {
            user.setPassword(password);
        }

        Set<Role> roleSet = new HashSet<>();
        if (role.contains("USER")){
            roleSet.add(new Role(2L, "USER"));
            user.setRoles(roleSet);
        }
        if (role.contains("ADMIN")){
            roleSet.add(new Role(1L, "ADMIN"));
            user.setRoles(roleSet);
        }

        //user.setRoles(roleSet);
        userService.saveUser(user);
        return modelAndView;

    }
}
