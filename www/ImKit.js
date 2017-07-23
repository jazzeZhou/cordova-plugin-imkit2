function ImKit() {
};

ImKit.prototype.Init = function (success, error) {
  cordova.exec(success, error, 'ImKit', 'Init', []);
}
ImKit.prototype.Connect = function (str, str2, str3, success, error) {
  cordova.exec(success, error, 'ImKit', 'Connect', [str,str2,str3]);
}
ImKit.prototype.GetUserInfo = function (str, success, error) {
  cordova.exec(success, error, 'ImKit', 'GetUserInfo', [str]);
}
ImKit.prototype.GetConversationList = function (success, error) {
  cordova.exec(success, error, 'ImKit', 'GetConversationList', []);
}
ImKit.prototype.Exit = function (success, error) {
  cordova.exec(success, error, 'ImKit', 'Exit', []);
}
ImKit.prototype.LaunchChats = function (success, error) {
  cordova.exec(success, error, 'ImKit', 'LaunchChats', []);
}
ImKit.prototype.LaunchChat = function (str1, str2, success, error) {
  cordova.exec(success, error, 'ImKit', 'LaunchChat', [str1,str2]);
}
ImKit.prototype.LaunchSystem = function (str1, str2, success, error) {
  cordova.exec(success, error, 'ImKit', 'LaunchSystem', [str1,str2]);
}
ImKit.prototype.RemoveConversation = function (str1, str2, success, error) {
  cordova.exec(success, error, 'ImKit', 'RemoveConversation', [str1,str2]);
}

ImKit.install = function () {
  if (!window.plugins) {
    window.plugins = {};
  }
  window.plugins.ImKit = new ImKit();
  return window.plugins.ImKit;
};

cordova.addConstructor(ImKit.install);
