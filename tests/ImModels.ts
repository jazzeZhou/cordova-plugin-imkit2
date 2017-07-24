/**
 *
 * 融云消息
 *
 * Created by jazzeZhou on 17/3/7.
 */

export class ConstantsIm {

  //私聊
  public static ConversationType_Private = 1;
  //系统消息
  public static ConversationType_System = 6;


}

export class ImMessage {

  id: string;
  name: string;
  url: string;
  conversationType: number;   //会话类型
  type: string;
  content: string;
  lastTime: number;
  unreadMessageCount: number;   //未读条数

}

export class ImUserInfo {

  uid: string;
  name: string;
  portrait: string;

}
