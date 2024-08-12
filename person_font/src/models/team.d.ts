import {UserType} from "./user";

/**
 * 定义用户类型
 */
export type TeamType = {
    id: number,
    name: string,
    description: string,
    maxNum: number,
    // todo 定义枚举值更规范
    status: number,
    expireTime?: Date,
    password: string,
    createTime: Date,
    updateTime: Date,
    createUser?: UserType,
}