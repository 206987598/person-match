import MyAxios from "../plugins/myAxios";
import {getCurrentUserState, setCurrentUserState} from "../global/state.ts";

/**
 * 异步获取当前用户信息
 * 此函数通过发送网络请求来获取当前登录用户的信息，如果用户已登录，则返回用户信息，否则返回null
 * 使用了async/await语法，使得代码更加简洁易读
 *
 * @returns {Promise} 返回一个Promise对象，包含用户信息或null
 */
export const getCurrentUser = async () => {
    // // 先尝试获取当前用户的基本信息
    // const currentUser = getCurrentUserState();
    // if (currentUser) {
    //     return currentUser;
    // }
    // 如果用户已登录，则发送请求获取详细的用户信息
    const res = await MyAxios.get('/user/current');
    // 检查请求返回的状态码，如果为0，则表示请求成功
    if (res.code === 0) {
        // 更新当前用户的状态信息
        setCurrentUserState(res.data)
        // 返回用户的详细信息
        return res.data;
    }
    // 如果请求成功，但返回的状态码不为0，则返回null
    return null;
}
