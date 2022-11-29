package com.ds_create.listofnotes.utils.billing

import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.*

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
    }
}