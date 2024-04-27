package hu.StudentSpace.sceneItems;

import org.jetbrains.annotations.Nullable;

public enum SceneItemType {
    NEWS,
    FORUM,
    TEST,
    TASK,
    DOCUMENT;

    @Nullable
    public static SceneItemType fromString(String type) {
        for (SceneItemType sceneItemType : SceneItemType.values()) {
            if (sceneItemType.name().equalsIgnoreCase(type)) {
                return sceneItemType;
            }
        }
        return null;
    }
}
