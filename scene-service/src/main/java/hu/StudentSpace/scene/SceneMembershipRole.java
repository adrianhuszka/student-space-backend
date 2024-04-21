package hu.StudentSpace.scene;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum SceneMembershipRole {
    ADMIN,
    MODERATOR,
    TEACHER,
    STUDENT;

    @Nullable
    @Contract(pure = true)
    public static SceneMembershipRole fromString(@NotNull String role) {
        return switch (role) {
            case "ADMIN" -> ADMIN;
            case "MODERATOR" -> MODERATOR;
            case "TEACHER" -> TEACHER;
            case "STUDENT" -> STUDENT;
            default -> null;
        };
    }
}
