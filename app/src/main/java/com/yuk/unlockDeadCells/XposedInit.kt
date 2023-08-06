package com.yuk.unlockDeadCells

import com.github.kyuubiran.ezxhelper.ClassUtils
import com.github.kyuubiran.ezxhelper.EzXHelper
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder.`-Static`.methodFinder
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

private const val TAG = "UnlockDeadCells"

class XposedInit : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        EzXHelper.setLogTag(TAG)
        EzXHelper.setToastTag(TAG)
        EzXHelper.initHandleLoadPackage(lpparam)
        when (lpparam.packageName) {
            "com.bilibili.deadcells" -> {
                try {

                    val thirdPartySDKClass = ClassUtils.loadClass("com.playdigious.deadcells.mobile.ThirdPartySDK")
                    val thirdPartySDKUserClass = ClassUtils.loadClass("com.playdigious.deadcells.mobile.ThirdPartySDK\$User")
                    thirdPartySDKClass.methodFinder().filterByName("doesOwnDLC").first().createHook { returnConstant(true) }
                    thirdPartySDKClass.methodFinder().filterByName("doesOwnGame").first().createHook { returnConstant(true) }
                    thirdPartySDKClass.methodFinder().filterByName("isLoggedIn").first().createHook { returnConstant(true) }
                    thirdPartySDKClass.methodFinder().filterByName("isSDKInit").first().createHook { returnConstant(true) }
                    thirdPartySDKUserClass.methodFinder().filterByName("isProfileComplete").first().createHook { returnConstant(true) }
                } catch (e: XposedHelpers.ClassNotFoundError) {
                    XposedBridge.log(e)
                }
            }

            else -> {
                return
            }
        }
    }

}