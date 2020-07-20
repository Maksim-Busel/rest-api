package com.epam.esm.dto;

import com.epam.esm.entity.Identifable;
import com.epam.esm.entity.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto extends RepresentationModel<UserDto>  implements Identifable {

    private long id;

    @Pattern(regexp = "^[A-Za-z]([A-Za-z0-9-]{3,18})$")
    private String username;

    @Pattern(regexp = "\\A(?=\\w*[0-9])(?=\\w*[a-z])(?=\\w*[A-Z])\\S{6,25}\\z")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Null
    private List<Role> roles;
}
