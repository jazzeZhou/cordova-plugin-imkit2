import { Injectable } from '@angular/core';
import {LogUtils} from "../log-utils";
import {ConstantsIm, ImUserInfo} from "../models/ImModels";
import {StorageProvider} from "./storage-provider";

/*

  //test:x18ywvqfx3ivc 目前和健康云服用同一个key
 ionic plugin add parentpath/cordova-plugin-imkit --variable APP_KEY=x18ywvqfx3ivc

//医生端:--variable APP_KEY=vnroth0kvqz0o

  Generated class for the RongimProvider provider.

  See https://angular.io/docs/ts/latest/guide/dependency-injection.html
  for more info on providers and Angular 2 DI.
*/

declare var window: any;

@Injectable()
export class RongimProvider {
  public rongim;

  private imUserInfo = "ImUserInfo";
  public static users: ImUserInfo[] = [];

  constructor(public storage: StorageProvider) {
    console.log('Hello RongimProvider Provider');

  }

  load() {
    try {
      this.rongim = window.plugins.ImKit;
    } catch (execption) {
      LogUtils.LogCatch(execption);
    }
    this.readUserInfo();
  }

  /**
   * 初始化,程序启动后先初始化,只能初始化一次
   */
  init(success, failure) {
    // var self = this;
    try {
      this.rongim.Init(function (res) {
        //有聊天消息就会返回ImMessage
        success();
      }, function () {
      });
    } catch (execption) {
      LogUtils.LogCatch(execption);
    }
  }

  /**
   * 连接,传入融云token,前端app的token(token必须是已经处理后可以直接用的),
   * im_token: 融云token
   * h_token: 前端app的token(token必须是已经处理后可以直接用的,插件内部不做转换)
   * get_user_url: 获取用户信息,返回data必须包含uid、portrait、name、username
   */
  connect(im_token: string, h_token: string, get_user_url: string) {
    return new Promise((resolve, reject) => {
      try {
        this.rongim.Connect(im_token, h_token, get_user_url, function (res) {
          //连接成功
          resolve(res);
        }, function (err) {
          reject(err);
        });
      } catch (execption) {
        LogUtils.LogCatch(execption);
        reject(execption);
      }
    });
  }

  /**
   * 获取用户信息ImUserInfo(storage里面有就从storage里面获取,storage没有就http请求并存储到storage)
   */
  getUserInfo(id: string) {
    var self = this;
    return new Promise((resolve, reject) => {
      try {
        for (var i=0; i<RongimProvider.users.length; i++) {
          if (RongimProvider.users[i].uid == id) {
            let info = RongimProvider.users[i];
            resolve(info);
            return;
          }
        }
        this.rongim.GetUserInfo(id, function (res) {
          if (res!=null) {
            let user = JSON.parse(res);
            let info = new ImUserInfo();
            info.uid = user.uid;
            info.portrait = user.portrait;
            if (user.name == null||user.name.length == 0) {
              info.name = user.username;
            } else {
              info.name = user.name;
            }
            self.saveUserInfo(info);
            resolve(info);
          } else {
            reject("");
          }
        }, function (err) {
          reject(err);
        });
      } catch (execption) {
        LogUtils.LogCatch(execption);
        reject(execption);
      }
    });
  }

  /**
   * 储存用户信息到数据库
   * @param info
   */
  saveUserInfo(info: ImUserInfo) {
    if (info == null) {
      return;
    }
    let bAdd = true;
    for (var i=0; i<RongimProvider.users.length; i++) {
      if (RongimProvider.users[i].uid == info.uid) {
        RongimProvider.users[i].name = info.name;
        RongimProvider.users[i].portrait = info.portrait;
        bAdd = false;
        break;
      }
    }
    if (bAdd) {
      RongimProvider.users.push(info);
    }
    this.storage.write(this.imUserInfo, RongimProvider.users);
  }

  /**
   * 读取用户信息
   */
  private readUserInfo() {
    let infos = this.storage.read(this.imUserInfo);
    if (infos!=null) {
      RongimProvider.users = <ImUserInfo[]>infos;
    }
  }

  /**
   * 获取消息列表
   */
  getConversationList() {
    return new Promise((resolve, reject) => {
      try {
        this.rongim.GetConversationList(function (res) {
          //获取用户消息列表 ImMessage
          resolve(JSON.parse(res));
        }, function (err) {
          reject(err);
        });
      } catch (execption) {
        LogUtils.LogCatch(execption);
        reject(execption);
      }
    });
  }

  /**
   * 进入聊天列表
   */
  launchChats() {
    return new Promise((resolve, reject) => {
      try {
        this.rongim.LaunchChats(function (str) {
          resolve(str);
        }, function (err) {
          reject(err);
        });
      } catch (execption) {
        LogUtils.LogCatch(execption);
        reject(execption);
      }
    });
  }

  /**
   * 进入私聊
   */
  launchChat(toUser: string) {
    return new Promise((resolve, reject) => {
      try {
        this.rongim.LaunchChat(toUser+"", "", function (str) {
          //返回后触发
          resolve(str);
        }, function (err) {
          reject(err);
        });
      } catch (execption) {
        LogUtils.LogCatch(execption);
        reject(execption);
      }
    });
  }

  /**
   * 进入系统消息聊天
   */
  launchSystem(toUser: string) {
    return new Promise((resolve, reject) => {
      try {
        this.rongim.LaunchSystem(toUser+"", "", function (str) {
          //返回后触发
          resolve(str);
        }, function (err) {
          reject(err);
        });
      } catch (execption) {
        LogUtils.LogCatch(execption);
        reject(execption);
      }
    });
  }

  /**
   * 退出
   */
  exit() {
    try {
      this.rongim.Exit(function (str) {
      }, function (str) {
      });
    } catch (execption) {
      LogUtils.LogCatch(execption);
    }
  }

  /**
   * 删除会话
   * userId: 会话
   * type:  会话类型
   */
  removeConversation(userId: string, type?: number) {
    try {
      if (!type) {
        type = ConstantsIm.ConversationType_Private;
      }
      this.rongim.RemoveConversation(userId, type, function (str) {
      }, function (str) {
      });
    } catch (execption) {
      LogUtils.LogCatch(execption);
    }
  }

}
