package cz.osu.swi1.blog.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    private String userName;
    private String password;
    private String email;

    public String getUserName() {
        return userName;
    }

}
