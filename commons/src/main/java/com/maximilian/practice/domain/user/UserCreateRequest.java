package com.maximilian.practice.domain.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record UserCreateRequest(@NotNull @Size(min = 3, max = 40) String nickname,
                                @NotNull @Size(min = 3, max = 40) String displayName,
                                String bio) {

}
