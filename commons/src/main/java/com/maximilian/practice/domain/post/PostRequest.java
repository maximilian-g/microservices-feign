package com.maximilian.practice.domain.post;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record PostRequest(@NotNull @Size(min = 3, max = 255) String content,
                          @NotNull Long userId) {
}
