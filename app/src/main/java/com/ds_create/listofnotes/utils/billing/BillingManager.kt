package com.ds_create.listofnotes.utils.billing

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.*
import com.ds_create.listofnotes.R

class BillingManager(private val activity: AppCompatActivity) {
    private var billingClient: BillingClient? = null

    init {
        setupBillingClient()
    }

    private fun setupBillingClient() {
        billingClient = BillingClient.newBuilder(activity)
            .setListener(getPurchaseListener())
            .enablePendingPurchases()
            .build()
    }

    private fun savePref(isPurchase: Boolean) {
        val preference = activity.getSharedPreferences(MAIN_PREF, Context.MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean(REMOVE_ADS_KEY, isPurchase)
        editor.apply()
    }

    fun startConnection() {
        billingClient?.startConnection(object: BillingClientStateListener {

            override fun onBillingServiceDisconnected() {
            }

            override fun onBillingSetupFinished(bResult: BillingResult) {
                getItem()
            }
        })
    }

    private fun getItem() {
        val skuList = ArrayList<String>()
        skuList.add(REMOVE_AD_ITEM)
        val skuDetails = SkuDetailsParams.newBuilder()
        skuDetails.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
        billingClient?.querySkuDetailsAsync(skuDetails.build()) {
            billingResult, list ->
            run {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        if (list != null) {
                            if (list.isNotEmpty()) {
                                val bFlowParams = BillingFlowParams.newBuilder()
                                    .setSkuDetails(list[0])
                                    .build()
                                billingClient?.launchBillingFlow(activity, bFlowParams)
                            }
                        }
                }
            }
        }
    }

    private fun getPurchaseListener(): PurchasesUpdatedListener {
        return PurchasesUpdatedListener {
                billingResult, list ->
            run {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    list?.get(0)?.let { nonConsumableItem(it) }
                }
            }
        }
    }

    private fun nonConsumableItem(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken).build()
                billingClient?.acknowledgePurchase(acParams) {
                    if (it.responseCode == BillingClient.BillingResponseCode.OK) {
                        savePref(true)
                        Toast.makeText(activity, activity.getString(R.string.thanks_for_purchase),
                            Toast.LENGTH_LONG).show()
                    } else {
                        savePref(false)
                        Toast.makeText(activity, activity.getString(R.string.exception_purchase),
                            Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    fun closeConnection() {
        billingClient?.endConnection()
    }

    companion object {
        const val REMOVE_AD_ITEM = "remove_ad_item_id"
        const val MAIN_PREF = "main_pref"
        const val REMOVE_ADS_KEY = "remove_ads_key"
    }
}