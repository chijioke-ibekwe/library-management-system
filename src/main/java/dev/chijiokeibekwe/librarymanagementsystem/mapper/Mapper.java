package dev.chijiokeibekwe.librarymanagementsystem.mapper;

import dev.chijiokeibekwe.librarymanagementsystem.dto.CustomUserDetails;
import dev.chijiokeibekwe.librarymanagementsystem.dto.response.BookResponse;
import dev.chijiokeibekwe.librarymanagementsystem.dto.response.PatronResponse;
import dev.chijiokeibekwe.librarymanagementsystem.dto.response.RoleResponse;
import dev.chijiokeibekwe.librarymanagementsystem.dto.response.UserResponse;
import dev.chijiokeibekwe.librarymanagementsystem.entity.Book;
import dev.chijiokeibekwe.librarymanagementsystem.entity.Patron;
import dev.chijiokeibekwe.librarymanagementsystem.entity.Role;
import dev.chijiokeibekwe.librarymanagementsystem.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class Mapper {

    public static CustomUserDetails toCustomUserDetails(User user){

        CustomUserDetails customUserDetails = new CustomUserDetails();
        BeanUtils.copyProperties(user, customUserDetails);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(user.getRole().getName().getValue()));

        user.getRole().getPermissions().forEach(p ->  {
            authorities.add(new SimpleGrantedAuthority(p.getName()));
        });

        customUserDetails.setAuthorities(authorities);
        return customUserDetails;
    }

    public static UserResponse toUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(user, userResponse, "role");

        userResponse.setRole(toRoleResponse(user.getRole()));
        return userResponse;
    }

    public static RoleResponse toRoleResponse(Role role) {
        RoleResponse roleResponse = new RoleResponse();
        BeanUtils.copyProperties(role, roleResponse, "permissions");

        List<String> permissions = new ArrayList<>();
        role.getPermissions().forEach(p -> permissions.add(p.getName()));
        roleResponse.setPermissions(permissions);

        return roleResponse;
    }

    public static BookResponse toBookResponse(Book book) {
        BookResponse bookResponse = new BookResponse();
        BeanUtils.copyProperties(book, bookResponse);

        return bookResponse;
    }

    public static PatronResponse toPatronResponse(Patron patron) {
        PatronResponse patronResponse = new PatronResponse();
        BeanUtils.copyProperties(patron, patronResponse);

        return patronResponse;
    }
}
