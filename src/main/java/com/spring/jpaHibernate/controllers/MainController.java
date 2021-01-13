package com.spring.jpaHibernate.controllers;

import com.spring.jpaHibernate.config.StaticConfig;
import com.spring.jpaHibernate.entities.Hotel;
import com.spring.jpaHibernate.entities.Roles;
import com.spring.jpaHibernate.entities.Users;
import com.spring.jpaHibernate.services.HotelService;
import com.spring.jpaHibernate.services.SearchService;
import com.spring.jpaHibernate.services.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    UserService userService;
    //repository shouldn't be here

    @Autowired
    SearchService searchService;

    @Value("${file.ava.dir}")
    private String avaDirectory;

    @Value("${file.ava.dir.classpath}")
    private String avaDirectoryClasspath;

    @Value("${file.default.ava.dir.classpath}")
    private String defaultAvaDirectoryClasspath;

    @RequestMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "1") int page) {
        int finalPage = page - 1;
        if (finalPage < 0) {
            finalPage = 0;
        }

        int hotelsSize = hotelService.countAllHotels();
        int countPage = (hotelsSize - 1) / StaticConfig.PAGE_SIZE + 1;

        if (countPage < 1) {
            countPage = 1;
        }

        List<Hotel> hotels = hotelService.getAllHotelsPaged(finalPage, StaticConfig.PAGE_SIZE);
        model.addAttribute("count_page", countPage);
        model.addAttribute("all_hotels", hotels);
        model.addAttribute("current_user", getUser());
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/profile")
    public String profile(Model model) {
        model.addAttribute("current_user", getUser());
        return "profile";
    }

    @RequestMapping("/auth")
    public String auth(Model model) {
        return "login";
    }


    @GetMapping("/search")
    public String search(Model model,
                         @RequestParam(name = "page", defaultValue = "1") int page,
                         @RequestParam(name = "name", defaultValue = "", required = false) String name,
                         @RequestParam(name = "city", defaultValue = "", required = false) String city,
                         @RequestParam(name = "price_from", defaultValue = "0", required = false) double priceFrom,
                         @RequestParam(name = "price_to", defaultValue = "0", required = false) double priceTo,
                         @RequestParam(name = "rooms_amount", defaultValue = "0", required = false) byte roomsAmount,
                         @RequestParam(name = "people_amount", defaultValue = "0", required = false) byte peopleAmount) {

        int finalPage = page - 1;
        if (finalPage < 0) {
            finalPage = 0;
        }

        int hotelsSize = searchService.countHotels(city, priceFrom, priceTo, roomsAmount, peopleAmount);
        int countPage = (hotelsSize - 1) / StaticConfig.PAGE_SIZE + 1;

        if (countPage < 1) {
            countPage = 1;
        }
        List<Hotel> hotelsSearch = searchService.searchBookingEntities(finalPage, StaticConfig.PAGE_SIZE, city, priceFrom, priceTo, roomsAmount, peopleAmount);
        model.addAttribute("count_page", countPage);
        model.addAttribute("hotels", hotelsSearch);
        model.addAttribute("current_user", getUser());
        return "search";
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @RequestMapping(value = "/edit_hotel/{id}", method = RequestMethod.GET)
    public String editHotel(Model model,
                            @PathVariable Long id) {
        model.addAttribute("current_user", getUser());

        Hotel checkHotel = hotelService.getHotel(id);
        if (checkHotel != null) {
            model.addAttribute("hotel", checkHotel);
            return "edit_hotel";
        }
        return "redirect: /add_hotel?error";
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @RequestMapping(value = "/edit_hotel", method = RequestMethod.POST)
    public String editHotelPost(
            @RequestParam("hotel_id") Long id,
            @RequestParam("hotel_name") String hotelName,
            @RequestParam("hotel_country") String hotelCountry,
            @RequestParam("hotel_city") String hotelCity,
            @RequestParam("hotel_description") String hotelDescription,
            @RequestParam("hotel_price") double hotelPrice,
            @RequestParam("max_people_amount") byte peopleAmount,
            @RequestParam("rooms_amount") byte roomsAmount,
            @RequestParam("hotel_stars") Byte hotelStars) {


        Hotel hotel = new Hotel();
        hotel.setId(id);
        hotel.setName(hotelName);
        hotel.setCountry(hotelCountry);
        hotel.setStars(hotelStars);
        hotel.setCity(hotelCity);
        hotel.setDescription(hotelDescription);
        hotel.setPrice(hotelPrice);
        hotel.setPeopleAmount(peopleAmount);
        hotel.setRoomsAmount(roomsAmount);
        if (hotelService.saveHotel(hotel) != null) {
            return "redirect:/add_hotel";
        } else return "redirect:/edit_hotel/" + id + "?error";
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @RequestMapping(value = "/add_hotel", method = RequestMethod.GET)
    public String addHotel(Model model) {
        model.addAttribute("all_hotels", hotelService.getAllHotels());
        model.addAttribute("current_user", getUser());
        return "add_hotel";
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @RequestMapping(value = "/add_hotel", method = RequestMethod.POST)
    public String addHotelPost(
            @RequestParam("hotel_name") String hotelName,
            @RequestParam("hotel_country") String hotelCountry,
            @RequestParam("hotel_city") String hotelCity,
            @RequestParam("hotel_description") String hotelDescription,
            @RequestParam("hotel_price") double hotelPrice,
            @RequestParam("max_people_amount") byte peopleAmount,
            @RequestParam("rooms_amount") byte roomsAmount,
            @RequestParam("hotel_stars") Byte hotelStars) {

        Hotel hotel = new Hotel();
        hotel.setName(hotelName);
        hotel.setCountry(hotelCountry);
        hotel.setStars(hotelStars);
        hotel.setCity(hotelCity);
        hotel.setDescription(hotelDescription);
        hotel.setPrice(hotelPrice);
        hotel.setPeopleAmount(peopleAmount);
        hotel.setRoomsAmount(roomsAmount);
        hotelService.addHotel(hotel);
        return "redirect:/add_hotel";
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @PostMapping("/delete_hotel")
    public String deleteHotel(@RequestParam("hotel_id") Long id) {
        Hotel checkHotel = hotelService.getHotel(id);
        if (checkHotel != null) {
            hotelService.deleteHotel(checkHotel);
            return "redirect:/add_hotel";
        }
        return "redirect:/edit_hotel/" + id;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add_user")
    public String addUser(@RequestParam("email") String email,
                          @RequestParam("password") String password,
                          @RequestParam("re_password") String rePassword,
                          @RequestParam("full_name") String fullName) {

        if (password.equals(rePassword)) {
            List<Roles> roles = new ArrayList<>();
            roles.add(StaticConfig.ROLE_USER);
            Users user = new Users();
            user.setEmail(email);
            user.setPassword(password);
            user.setFullName(fullName);
            user.setRoles(roles);
            if (userService.registerUser(user) != null) {
                return "redirect:/users?success";
            }
        }
        return "redirect:/users?error";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/users")
    public String users(Model model,
                        @RequestParam(name = "page", defaultValue = "1") int page
    ) {
        int finalPage = page - 1;
        if (finalPage < 0) {
            finalPage = 0;
        }

        int usersSize = userService.getUserSize(StaticConfig.ROLE_USER);
        int countPage = (usersSize - 1) / StaticConfig.PAGE_SIZE + 1;

        if (countPage < 1) {
            countPage = 1;
        }

        List<Users> users = userService.getAllUsersPaged(finalPage, StaticConfig.PAGE_SIZE);
        model.addAttribute("count_page", countPage);
        model.addAttribute("current_user", getUser());
        model.addAttribute("users", users);
        return "users";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/user_details/{id}")
    public String userDetails(Model model,
                              @PathVariable Long id) {

        model.addAttribute("current_user", getUser());

        Users checkUser = userService.getUserById(id);
        if (checkUser != null) {
            model.addAttribute("user_details", checkUser);
            return "user_details";
        } else {
            return "redirect: /users";
        }

    }


    @RequestMapping("/accessdenied")
    public String accessdenied(Model model) {
        model.addAttribute("current_user", getUser());
        return "403";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/upload_ava")
    public String uploadAva(@RequestParam("ava_pic") MultipartFile file) {
        Users currentUser = getUser();
        if (currentUser != null) {
            if (file.getContentType().equals("image/png") || file.getContentType().equals("image/jpeg")) {
                String uniqueName = DigestUtils.sha1Hex("user_avatar" + currentUser.getId());
                String extension = "png";
                if (file.getContentType().equals("image/jpeg")) {
                    extension = "jpeg";
                }

                String fullPath = avaDirectory + uniqueName + "." + extension;
                try {
                    byte[] bytes = file.getBytes();
                    Path avaPath = Paths.get(fullPath);
                    Files.write(avaPath, bytes);
                    currentUser.setAva(fullPath);
                    currentUser.setAvaHash(uniqueName + "." + extension);
                    userService.updateUser(currentUser);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        return "redirect:/profile";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/profile_photo/{ava_hash}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public @ResponseBody
    byte[] profilePhoto(@PathVariable("ava_hash") String avaHash) throws IOException {

        String image = defaultAvaDirectoryClasspath + "default_user.png";

        if (!avaHash.equals("null")) {
            image = avaDirectoryClasspath + avaHash;
        }

        InputStream in;
        try {
            ClassPathResource classPathResource = new ClassPathResource(image);
            in = classPathResource.getInputStream();
        } catch (Exception e) {
            image = defaultAvaDirectoryClasspath + "default_user.png";
            ClassPathResource classPathResource = new ClassPathResource(image);
            in = classPathResource.getInputStream();

        }
        return IOUtils.toByteArray(in);
    }

    private Users getUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User securityUser = (User) authentication.getPrincipal();
            Users myUser = userService.getUserByEmail(securityUser.getUsername());
            return myUser;
        }
        return null;
    }
}
