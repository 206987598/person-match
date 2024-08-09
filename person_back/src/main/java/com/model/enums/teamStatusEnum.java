package com.model.enums;

public enum teamStatusEnum {
    /**
     * 公开（所有人可看）
     */
    PUBLIC(0, "公开") ,
    /**
     * 私有（个人可看）
     */
    PRIVATE(1, "私有") ,
    /**
     * 有密码
     */
    SECRET(2, "加密");

   public static teamStatusEnum getTeamStatusEnumByValue(Integer value){
       if (value==null){
           return null;
       }
       // 根据给定的值查找并返回对应的teamStatusEnum枚举对象
       // 如果找不到对应的枚举对象，返回null
       //
       // 参数:
       //   value - 需要查找的枚举值
       //
       // 返回值:
       //   匹配的teamStatusEnum枚举对象，如果找不到则返回null
       teamStatusEnum[] values = teamStatusEnum.values();
       for (teamStatusEnum teamStatusEnum : values) {
           if (teamStatusEnum.value == value) {
               return teamStatusEnum;
           }
       }
       return null;
   }

    private int value;
    private String text;

    teamStatusEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
