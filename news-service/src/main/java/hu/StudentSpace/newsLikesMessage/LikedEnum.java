package hu.StudentSpace.newsLikesMessage;

public enum LikedEnum {
    LIKED,
    DISLIKED;

    public static LikedEnum fromBoolean(boolean liked) {
        return liked ? LIKED : DISLIKED;
    }
}
