package com.flechazo.jnyzdairy.entity;

import lombok.Getter;

/**
 * 用户状态枚举，用于定义用户在系统中的不同状态。
 *
 * @author Flechazo
 */
@Getter
public enum UserStatus {

    /**
     * 活跃状态，表示用户可以正常登录和使用系统功能。
     */
    ACTIVE("活跃"),

    /**
     * 已禁用状态，表示用户的账户已被管理员禁用，无法登录或使用系统功能。
     */
    DISABLED("已禁用"),

    /**
     * 已删除状态，表示用户的账户已被标记为删除，通常不再显示在用户列表中。
     */
    DELETED("已删除");

    /**
     * 状态描述，简要说明该状态的含义。
     * <p>
     * -- GETTER --
     * <p>
     *  获取状态描述。
     */
    private final String description;

    /**
     * 构造函数，初始化用户状态及其描述信息。
     *
     * @param description 状态描述
     */
    UserStatus(String description) {
        this.description = description;
    }

    /**
     * 判断用户是否处于活跃状态。
     *
     * @return 如果用户处于活跃状态则返回 true，否则返回 false。
     */
    public boolean isActive() {
        return this == ACTIVE;
    }

    /**
     * 判断用户是否被禁用。
     *
     * @return 如果用户被禁用则返回 true，否则返回 false。
     */
    public boolean isDisabled() {
        return this == DISABLED;
    }

    /**
     * 判断用户是否已删除。
     *
     * @return 如果用户已删除则返回 true，否则返回 false。
     */
    public boolean isDeleted() {
        return this == DELETED;
    }
}