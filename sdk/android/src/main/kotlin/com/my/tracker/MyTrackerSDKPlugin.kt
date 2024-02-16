package com.my.tracker

import android.app.Activity
import android.app.Application
import android.content.Context
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.lang.ref.WeakReference

class MyTrackerSDKPlugin : FlutterPlugin, ActivityAware, MethodCallHandler {

    private var activity: Activity? = null

    private var myTrackerChannel: MethodChannel? = null

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        val methodChannel = MethodChannel(flutterPluginBinding.binaryMessenger, CHANNEL_NAME)
        methodChannel.setMethodCallHandler(this)
        myTrackerChannel = methodChannel
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        myTrackerChannel?.setMethodCallHandler(null)
        myTrackerChannel = null
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {
        activity = null
    }

    override fun onDetachedFromActivity() {
        activity = null
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            INIT_METHOD -> {
                try {
                    val id = call.argument<String>("id")
                    if (activity != null && !id.isNullOrEmpty()) {
                        MyTracker.initTracker(id, activity!!.application)
                        MyTracker.trackLaunchManually(activity)
                        result.success(null)
                    } else {
                        result.error("-", "Id error", "id = null")
                    }

                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            FLUSH_METHOD -> {
                try {
                    MyTracker.flush()
                    result.success(null)
                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            TRACK_EVENT_METHOD -> {
                try {
                    val name = call.argument<String>("name")
                    if (!name.isNullOrEmpty()) {
                        MyTracker.trackEvent(
                            name,
                            call.argument<Map<String, String>>("eventParams")
                        )
                        result.success(null)
                    } else {
                        result.error("-", "Name error", "name = null")
                    }

                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            TRACK_LOGIN_METHOD -> {
                try {
                    val userId = call.argument<String>("userId")
                    if (!userId.isNullOrEmpty()) {
                        MyTracker.trackLoginEvent(
                            userId,
                            null,
                            call.argument<Map<String, String>>("eventParams")
                        )
                        result.success(null)
                    } else {
                        result.error("-", "UserId error", "userId = null")
                    }

                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            TRACK_REGISTRATION_METHOD -> {
                try {
                    val userId = call.argument<String>("userId")
                    if (!userId.isNullOrEmpty()) {
                        MyTracker.trackRegistrationEvent(
                            userId,
                            null,
                            call.argument<Map<String, String>>("eventParams")
                        )
                        result.success(null)
                    } else {
                        result.error("-", "UserId error", "userId = null")
                    }

                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            IS_DEBUG_MODE_METHOD -> {
                try {
                    val isDebugMode = MyTracker.isDebugMode()
                    result.success(isDebugMode)
                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            SET_DEBUG_MODE_METHOD -> {
                try {
                    val debugMode = call.argument<Boolean>("debugMode")
                    if (debugMode != null) {
                        MyTracker.setDebugMode(debugMode)
                        result.success(null)
                    } else {
                        result.error("-", "DebugMode error", "debugMode = null")
                    }
                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            GET_ID_METHOD -> {
                try {
                    val id = MyTracker.getTrackerConfig().id
                    result.success(id)
                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            GET_BUFFERING_PERIOD_METHOD -> {
                try {
                    val bufferingPeriod = MyTracker.getTrackerConfig().bufferingPeriod
                    result.success(bufferingPeriod)
                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            SET_BUFFERING_PERIOD_METHOD -> {
                try {
                    val bufferingPeriod = call.argument<Int>("bufferingPeriod")
                    if (bufferingPeriod != null) {
                        MyTracker.getTrackerConfig().bufferingPeriod = bufferingPeriod
                        result.success(null)
                    } else {
                        result.error("-", "BufferingPeriod error", "bufferingPeriod = null")
                    }

                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            GET_FORCING_PERIOD_METHOD -> {
                try {
                    val forcingPeriod = MyTracker.getTrackerConfig().forcingPeriod
                    result.success(forcingPeriod)
                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            SET_FORCING_PERIOD_METHOD -> {
                try {
                    val forcingPeriod = call.argument<Int>("forcingPeriod")
                    if (forcingPeriod != null) {
                        MyTracker.getTrackerConfig().forcingPeriod = forcingPeriod
                        result.success(null)
                    } else {
                        result.error("-", "ForcingPeriod error", "forcingPeriod = null")
                    }

                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            GET_LAUNCH_TIMEOUT_METHOD -> {
                try {
                    val launchTimeout = MyTracker.getTrackerConfig().launchTimeout
                    result.success(launchTimeout)
                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            SET_LAUNCH_TIMEOUT_METHOD -> {
                try {
                    val launchTimeout = call.argument<Int>("launchTimeout")
                    if (launchTimeout != null) {
                        MyTracker.getTrackerConfig().launchTimeout = launchTimeout
                        result.success(null)
                    } else {
                        result.error("-", "LaunchTimeout error", "launchTimeout = null")
                    }
                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            SET_PROXY_HOST_METHOD -> {
                try {
                    val proxyHost = call.argument<String>("proxyHost")
                    if (!proxyHost.isNullOrEmpty()) {
                        MyTracker.getTrackerConfig().setProxyHost(call.argument(proxyHost))
                        result.success(null)
                    } else {
                        result.error("-", "ProxyHost error", "proxyHost = null")
                    }

                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            SET_REGION_METHOD -> {
                try {
                    val region = call.argument<Int>("region")
                    if (region != null) {
                        MyTracker.getTrackerConfig().setRegion(region)
                        result.success(null)
                    } else {
                        result.error("-", "Region error", "region = null")
                    }
                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            IS_TRACKING_ENVIRONMENT_ENABLED -> {
                try {
                    val isTrackingEnvironmentEnabled =
                        MyTracker.getTrackerConfig().isTrackingEnvironmentEnabled
                    result.success(isTrackingEnvironmentEnabled)
                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            SET_TRACKING_ENVIRONMENT_ENABLED -> {
                try {
                    val isTrackingEnvironmentEnabled =
                        call.argument<Boolean>("trackingEnvironmentEnabled")
                    if (isTrackingEnvironmentEnabled != null) {
                        MyTracker.getTrackerConfig().isTrackingEnvironmentEnabled =
                            isTrackingEnvironmentEnabled
                        result.success(null)
                    } else {
                        result.error(
                            "-",
                            "IsTrackingEnvironmentEnabled error",
                            "isTrackingEnvironmentEnabled = null"
                        )
                    }
                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            IS_TRACKING_LAUNCH_ENABLED -> {
                try {
                    val isTrackingLaunchEnabled =
                        MyTracker.getTrackerConfig().isTrackingLaunchEnabled
                    result.success(isTrackingLaunchEnabled)
                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            SET_TRACKING_LAUNCH_ENABLED -> {
                try {
                    val isTrackingLaunchEnabled =
                        call.argument<Boolean>("trackingLaunchEnabled")

                    if (isTrackingLaunchEnabled != null) {
                        MyTracker.getTrackerConfig().isTrackingLaunchEnabled =
                            isTrackingLaunchEnabled
                        result.success(null)
                    } else {
                        result.error(
                            "-",
                            "IsTrackingLaunchEnabled error",
                            "isTrackingLaunchEnabled = null"
                        )
                    }
                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            IS_TRACKING_LOCATION_ENABLED -> {
                try {
                    val isTrackingLocationEnabled =
                        MyTracker.getTrackerConfig().isTrackingLocationEnabled
                    result.success(isTrackingLocationEnabled)
                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            SET_TRACKING_LOCATION_ENABLED -> {
                try {
                    val isTrackingLocationEnabled =
                        call.argument<Boolean>("trackingLocationEnabled")

                    if (isTrackingLocationEnabled != null) {
                        MyTracker.getTrackerConfig().isTrackingLocationEnabled =
                            isTrackingLocationEnabled
                        result.success(null)
                    } else {
                        result.error(
                            "-",
                            "IsTrackingLocationEnabled error",
                            "isTrackingLocationEnabled = null"
                        )
                    }
                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            SET_AGE -> {
                try {
                    val age = call.argument<Int>("age")
                    if (age != null) {
                        MyTracker.getTrackerParams().age = age
                        result.success(null)
                    } else {
                        result.error("-", "Age error", "age = null")
                    }
                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            GET_AGE -> {
                try {
                    val age = MyTracker.getTrackerParams().age.takeIf { it != -1 }
                    result.success(age)
                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            SET_GENDER -> {
                try {
                    val gender = call.argument<Int>("gender")
                    if (gender != null) {
                        MyTracker.getTrackerParams().gender = gender - 1
                        result.success(null)
                    } else {
                        result.error("-", "Gender error", "gender = null")
                    }

                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            GET_GENDER -> {
                try {
                    val gender = MyTracker.getTrackerParams().gender + 1
                    result.success(gender)
                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            SET_LANG -> {
                try {
                    val lang = call.argument<String>("lang")
                    if (lang != null) {
                        MyTracker.getTrackerParams().lang = lang
                        result.success(null)
                    } else {
                        result.error("-", "Lang error", "lang = null")
                    }
                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            GET_LANG -> {
                try {
                    val lang = MyTracker.getTrackerParams().lang
                    result.success(lang)
                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            SET_CUSTOM_USER_IDS -> {
                try {
                    val customUserIds = call.argument<List<String>>("customUserIds")
                    if (!customUserIds.isNullOrEmpty()) {
                        MyTracker.getTrackerParams().customUserIds = customUserIds.toTypedArray()
                        result.success(null)
                    } else {
                        result.error("-", "CustomUserIds error", "customUserIds = null")
                    }
                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            GET_CUSTOM_USER_IDS -> {
                try {

                    val ids = MyTracker.getTrackerParams().customUserIds.asList<String>()

                    result.success(ids)
                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            SET_EMAILS -> {
                try {
                    val emails = call.argument<List<String>>("emails")
                    if (!emails.isNullOrEmpty()) {
                        MyTracker.getTrackerParams().emails = emails.toTypedArray()
                        result.success(null)
                    } else {
                        result.error("-", "Emails error", "emails = null")
                    }
                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            GET_EMAILS -> {
                try {
                    val emails = MyTracker.getTrackerParams().emails?.toList()
                    result.success(emails)
                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            SET_PHONES -> {
                try {
                    val phones = call.argument<List<String>>("phones")
                    if (!phones.isNullOrEmpty()) {
                        MyTracker.getTrackerParams().phones = phones.toTypedArray()
                        result.success(null)
                    } else {
                        result.error("-", "Phones error", "phones = null")
                    }
                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            GET_PHONES -> {
                try {
                    val phones = MyTracker.getTrackerParams().phones?.toList()
                    result.success(phones)
                } catch (e: Exception) {
                    result.error("-", e.localizedMessage, e.message)
                }
            }

            else -> result.notImplemented()
        }
    }

    companion object {
        const val CHANNEL_NAME = "_mytracker_api_channel"

        const val INIT_METHOD = "init"

        const val FLUSH_METHOD = "flush"

        const val TRACK_EVENT_METHOD = "trackEvent"

        const val TRACK_LOGIN_METHOD = "trackLoginEvent"

        const val TRACK_REGISTRATION_METHOD = "trackRegistrationEvent"

        const val IS_DEBUG_MODE_METHOD = "isDebugMode"
        const val SET_DEBUG_MODE_METHOD = "setDebugMode"

        const val GET_ID_METHOD = "getId"

        const val GET_BUFFERING_PERIOD_METHOD = "getBufferingPeriod"
        const val SET_BUFFERING_PERIOD_METHOD = "setBufferingPeriod"

        const val GET_FORCING_PERIOD_METHOD = "getForcingPeriod"
        const val SET_FORCING_PERIOD_METHOD = "setForcingPeriod"

        const val GET_LAUNCH_TIMEOUT_METHOD = "getLaunchTimeout"
        const val SET_LAUNCH_TIMEOUT_METHOD = "setLaunchTimeout"

        const val SET_PROXY_HOST_METHOD = "setProxyHost"

        const val SET_REGION_METHOD = "setRegion"

        const val SET_TRACKING_ENVIRONMENT_ENABLED = "setTrackingEnvironmentEnabled"
        const val IS_TRACKING_ENVIRONMENT_ENABLED = "isTrackingEnvironmentEnabled"

        const val SET_TRACKING_LAUNCH_ENABLED = "setTrackingLaunchEnabled"
        const val IS_TRACKING_LAUNCH_ENABLED = "isTrackingLaunchEnabled"

        const val SET_TRACKING_LOCATION_ENABLED = "setTrackingLocationEnabled"
        const val IS_TRACKING_LOCATION_ENABLED = "isTrackingLocationEnabled"

        const val GET_AGE = "getAge"
        const val SET_AGE = "setAge"

        const val GET_GENDER = "getGender"
        const val SET_GENDER = "setGender"

        const val GET_LANG = "getLang"
        const val SET_LANG = "setLang"

        const val GET_CUSTOM_USER_IDS = "getCustomUserIds"
        const val SET_CUSTOM_USER_IDS = "setCustomUserIds"

        const val GET_EMAILS = "getEmails"
        const val SET_EMAILS = "setEmails"

        const val GET_PHONES = "getPhones"
        const val SET_PHONES = "setPhones"
    }
}
