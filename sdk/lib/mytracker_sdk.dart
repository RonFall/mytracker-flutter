import 'dart:async';

import 'package:flutter/services.dart';

const MethodChannel _apiChannel = MethodChannel('_mytracker_api_channel');

/// Possible values of the region.
enum MyTrackerRegion { RU, EU }

/// Possible values of the user's gender.
enum MyTrackerGender { UNSPECIFIED, UNKNOWN, MALE, FEMALE }

/// Main facade to access MyTracker API
class MyTracker {
  MyTracker._internal();

  /// Returns the instance of MyTrackerConfig
  ///
  /// NOTE: it's recommended to configure myTracker before call [MyTracker.init(id)]
  static final MyTrackerConfig trackerConfig = MyTrackerConfig._internal();

  /// Returns the instance of MyTrackerParams
  static final MyTrackerParams trackerParams = MyTrackerParams._internal();

  /// Returns the attribute whether the tracker prints debug information.
  static Future<bool?> isDebugMode() async {
    final isDebugMode = await _apiChannel.invokeMethod<bool>("isDebugMode");

    return isDebugMode;
  }

  /// Enables or disable printing debug information.  If [debugMode] true,
  /// the tracker prints debug information to LogCat.
  static Future<void> setDebugMode(bool debugMode) async =>
      _apiChannel.invokeMethod("setDebugMode", {"debugMode": debugMode});

  /// Performs initialization of tracker.
  ///
  /// NOTE: this method should be called right after
  /// setup tracker configuration. [id] identifier of your application.
  static Future<void> init(String id) async => _apiChannel.invokeMethod<bool>(
        "init",
        {"id": id},
      );

  /// Force sends all saved events to the server.
  static Future<void> flush() async => _apiChannel.invokeMethod("flush");

  /// Tracks user defined event with custom name and optional key-value
  /// parameters. [name] is user defined event name. Max length is 255
  /// symbols. Additional key-value [eventParams] params can be added. Max
  /// length for key or value is 255 symbols.
  static Future<void> trackEvent(
    String name,
    Map<String, String>? eventParams,
  ) async =>
      _apiChannel.invokeMethod(
        "trackEvent",
        {"name": name, "eventParams": eventParams},
      );

  /// Track user login event. Call the method right after user successfully
  /// authorized in the app and got an unique identifier. [userId] Unique
  /// user identifier. Additional key-value [eventParams] params can be added.
  /// Max length for key or value is 255 symbols.
  static Future<void> trackLoginEvent(
    String userId,
    Map<String, String>? eventParams,
  ) async =>
      _apiChannel.invokeMethod(
        "trackLoginEvent",
        {"userId": userId, "eventParams": eventParams},
      );

  /// Track user registration event. Call the method right after user
  /// successfully authorized in the app and got an unique identifier
  /// [userId] Unique user identifier. Additional key-value [eventParams] params can be added.
  /// Max length for key or value is 255 symbols.
  static Future<void> trackRegistrationEvent(
    String userId,
    Map<String, String>? eventParams,
  ) async =>
      _apiChannel.invokeMethod(
        "trackRegistrationEvent",
        {"userId": userId, "eventParams": eventParams},
      );
}

/// Class for configuring myTracker
class MyTrackerConfig {
  MyTrackerConfig._internal();

  /// Returns identifier that was provided in [MyTracker.init(id)].
  Future<String?> getId() async {
    final id = await _apiChannel.invokeMethod<String>("getId");

    return id;
  }

  /// Returns buffering period. During this period every tracked event is
  /// stored in local storage.
  ///
  /// The value is in range [1 - 86400].
  /// Default value is 900 seconds.
  Future<int?> getBufferingPeriod() async {
    final bufferingPeriod = await _apiChannel.invokeMethod<int>(
      "getBufferingPeriod",
    );

    return bufferingPeriod;
  }

  /// Sets the buffering period [bufferingPeriod] in seconds. The value should
  /// be in range [1 - 86400]. Otherwise it will be rejected.
  ///
  /// NOTE: it's recommended to call this method before [MyTracker.init(id)]
  /// call.
  Future<void> setBufferingPeriod(int bufferingPeriod) async =>
      _apiChannel.invokeMethod(
        "setBufferingPeriod",
        {"bufferingPeriod": bufferingPeriod},
      );

  /// Returns forcing period in seconds. During this period every tracked
  /// event leads to flushing tracker. The start of the period is install
  /// or update of application.
  ///
  /// The value is in range [0 - 432000].
  /// Default value is 0. It means, that forcing period is disabled by default.
  Future<int?> getForcingPeriod() async {
    final forcingPeriod = await _apiChannel.invokeMethod<int>(
      "getForcingPeriod",
    );

    return forcingPeriod;
  }

  /// Sets the forcing period [forcingPeriod] in seconds. The value should
  /// be in range [0 - 432000]. Otherwise it will be  rejected.
  /// NOTE: it's recommended to call this method before [MyTracker.init(id)]
  Future<void> setForcingPeriod(int forcingPeriod) async =>
      _apiChannel.invokeMethod(
        "setForcingPeriod",
        {"forcingPeriod": forcingPeriod},
      );

  /// Returns launch timeout in in seconds. During this period
  /// start of the application after it close won't be considered as new launch.
  ///
  /// The value is in range [30 - 7200].
  /// Default value is 30.
  Future<int?> getLaunchTimeout() async {
    final launchTimeout = await _apiChannel.invokeMethod<int>(
      "getLaunchTimeout",
    );

    return launchTimeout;
  }

  /// Sets the launch timeout [launchTimeout] in seconds.
  ///
  /// NOTE: it's recommended to call this method before [MyTracker.init(id)]
  Future<void> setLaunchTimeout(int launchTimeout) async =>
      _apiChannel.invokeMethod(
        "setLaunchTimeout",
        {"launchTimeout": launchTimeout},
      );

  /// Sets the host [proxyHost] to which all requests will be sent.
  ///
  /// The value provided in parameter will be additionally processed:
  /// - HTTPS scheme could be added if necessary
  /// - Query and Fragment parts will be deleted
  /// - the protocol version will be added
  ///
  /// To reset proxy host to default call the method with null parameter.
  ///
  /// NOTE: it's mandatory to call this method before [MyTracker.init(id)]
  Future<void> setProxyHost(String? proxyHost) async =>
      _apiChannel.invokeMethod("setProxyHost", {"proxyHost": proxyHost});

  /// Sets the region. [region] The value switch the proxy host to predefined
  /// values or reset it to default value.
  ///
  /// Possible values are defined in [MyTrackerRegion]
  ///
  /// NOTE: it's mandatory to call this method before [MyTracker.init(id)]
  ///
  /// For example, setting region to EU
  /// ```dart
  /// MyTracker.trackerConfig.setRegion(MyTrackerRegion.EU)
  /// MyTracker.init(id)
  /// ```
  Future<void> setRegion(MyTrackerRegion region) async =>
      _apiChannel.invokeMethod("setRegion", {"region": region.index});

  /// Returns tracking environment state. Enabled state means that
  /// information about Wi-Fi and mobile networks will be collected.
  ///
  /// NOTE: this information are collected while sending request
  /// to the server. The impact to the battery is minimal.
  Future<bool?> isTrackingEnvironmentEnabled() async {
    final isTrackingEnvironmentEnabled = await _apiChannel.invokeMethod<bool>(
      "isTrackingEnvironmentEnabled",
    );

    return isTrackingEnvironmentEnabled;
  }

  /// Enables or disables collecting environment information.
  ///
  /// NOTE: it's recommended to call this method before [MyTracker.init(id)]
  Future<void> setTrackingEnvironmentEnabled(
    bool trackingEnvironmentEnabled,
  ) async =>
      _apiChannel.invokeMethod(
        "setTrackingEnvironmentEnabled",
        {"trackingEnvironmentEnabled": trackingEnvironmentEnabled},
      );

  /// Returns whether tracking application launches is enabled or not.
  Future<bool?> isTrackingLaunchEnabled() async {
    final isTrackingLaunchEnabled = await _apiChannel.invokeMethod<bool>(
      "isTrackingLaunchEnabled",
    );

    return isTrackingLaunchEnabled;
  }

  /// Enables or disables tracking application launches.
  ///
  /// NOTE: it's mandatory to call this method before [MyTracker.init(id)]
  Future<void> setTrackingLaunchEnabled(
    bool trackingLaunchEnabled,
  ) async =>
      _apiChannel.invokeMethod(
        "setTrackingLaunchEnabled",
        {"trackingLaunchEnabled": trackingLaunchEnabled},
      );

  /// Returns whether collecting current location is enabled or not.
  ///
  /// NOTE: this information are collected while sending request
  /// to the server. The impact to the battery is minimal.
  Future<bool?> isTrackingLocationEnabled() async {
    final isTrackingLocationEnabled = await _apiChannel.invokeMethod<bool>(
      "isTrackingLocationEnabled",
    );

    return isTrackingLocationEnabled;
  }

  /// Enables or disables collecting information about current location.
  ///
  /// NOTE: it's recommended to call this method before [MyTracker.init(id)]
  Future<void> setTrackingLocationEnabled(
    bool trackingLocationEnabled,
  ) async =>
      _apiChannel.invokeMethod(
        "setTrackingLocationEnabled",
        {"trackingLocationEnabled": trackingLocationEnabled},
      );
}

/// Class for specifying additional tracking parameters
class MyTrackerParams {
  MyTrackerParams._internal();

  /// Returns age of the current user.
  Future<int?> getAge() async {
    final age = await _apiChannel.invokeMethod<int>("getAge");

    return age;
  }

  /// Sets age [age] for the current user.
  Future<void> setAge(int age) async =>
      _apiChannel.invokeMethod("setAge", {"age": age});

  /// Returns gender of the current user.
  /// Possible values are defined in [MyTrackerGender]
  Future<MyTrackerGender?> getGender() async {
    final gender = await _apiChannel.invokeMethod<int>("getGender");

    return gender != null ? MyTrackerGender.values[gender] : null;
  }

  /// Sets gender of the current user [gender].
  /// Possible values are defined in [MyTrackerGender]
  ///
  /// ```dart
  /// MyTracker.trackerParams.setGender(MyTrackerGender.FEMALE)
  /// ```
  Future<void> setGender(MyTrackerGender gender) async =>
      _apiChannel.invokeMethod("setGender", {"gender": gender.index});

  /// Returns current language. The value can differ from the
  /// current language of the system.
  Future<String?> getLang() async {
    final lang = await _apiChannel.invokeMethod<String>("getLang");

    return lang;
  }

  /// Sets current language [lang]. You could use this method to
  /// override the system value of the language.
  Future<void> setLang(String lang) async =>
      _apiChannel.invokeMethod("setLang", {"lang": lang});

  /// Returns identifiers of the current user.
  Future<List<String>?> getCustomUserIds() async {
    final list = await _apiChannel.invokeMethod<List>(
      "getCustomUserIds",
    );

    return list?.cast<String>();
  }

  /// Sets the array of  identifiers of the current user [customUserIds].
  Future<void> setCustomUserIds(List<String> customUserIds) async =>
      _apiChannel.invokeMethod(
        "setCustomUserIds",
        {"customUserIds": customUserIds},
      );

  /// Returns tracked emails of the current user, previously set in [setEmails]
  Future<List<String>?> getEmails() async {
    final list = await _apiChannel.invokeMethod<List>("getEmails");

    return list?.cast<String>();
  }

  /// Sets list of emails [emails] of the current user.
  Future<void> setEmails(List<String> emails) async =>
      _apiChannel.invokeMethod("setEmails", {"emails": emails});

  /// Returns tracked phones of the current user, previously set in [setPhones]
  Future<List<String>?> getPhones() async {
    final list = await _apiChannel.invokeMethod<List>("getPhones");

    return list?.cast<String>();
  }

  /// Sets list of phone numbers [phones] of the current user.
  Future<void> setPhones(List<String> phones) async =>
      _apiChannel.invokeMethod("setPhones", {"phones": phones});
}
