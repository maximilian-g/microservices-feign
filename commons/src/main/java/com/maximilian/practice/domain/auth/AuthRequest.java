package com.maximilian.practice.domain.auth;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record AuthRequest(@NotNull @Size(min = 4, max = 45) String username,
                          @NotNull @Size(min = 4, max = 30) String password) {

}
