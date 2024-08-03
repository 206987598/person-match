/**
 * 定义用户类型
 */
export type UserType = {
    id: number,
    username: string,
    userAccount: string,
    avatarUrl?: string,
    profile?: string,
    gender: number,
    phone: string,
    email: string,
    createTime: date,
    userStatus: number,
    userRole: number,
    planetCode: string,
    tags: string[],
}