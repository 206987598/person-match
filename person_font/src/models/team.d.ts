import {UserType} from "./user";

/**
 * 定义用户类型
 */
export type TeamType = {
    id: number,
    userId:number,
    name: string,
    description: string,
    maxNum: number,
    status: number,
    expireTime?: Date,
    password: string,
    createTime: Date,
    updateTime: Date,
    createUser?: UserType,
    hasJoin: boolean,
    hasJoinNum:number,
}