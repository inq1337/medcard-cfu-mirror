package org.cfuv.medcard.dto.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(@NotBlank(message = "Email cannot be blank")
                            @Email(message = "Invalid email format")
                            String email,

                            @NotBlank(message = "Password cannot be blank")
                            @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
                            String password,

                            @NotBlank(message = "Surname cannot be blank")
                            @Size(max = 255, message = "firstname must be less then 255 characters")
                            String firstname,

                            @NotBlank(message = "Surname cannot be blank")
                            @Size(max = 255, message = "surname must be less then 255 characters")
                            String surname,

                            @Size(max = 255, message = "patronymic must be less then 255 characters")
                            String patronymic) {
}
