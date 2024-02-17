package com.ltthuc.billing.helper

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.limurse.iap.BillingClientConnectionListener
import com.limurse.iap.DataWrappers
import com.limurse.iap.IapConnector
import com.limurse.iap.PurchaseServiceListener
import com.ltthuc.billing.data.BillingStatus

class BillingManager(val context: Context,
                     val nonConsumableKeys: List<String> = emptyList(),
                     val consumableKeys: List<String> = emptyList(),
                     val subscriptionKeys: List<String> = emptyList(),
                     val key: String? = null,
                     val enableLogging: Boolean = false): LiveData<BillingStatus>() {

    private var iapConnector: IapConnector? = null
    private val TAG = "BillingManager"

    fun purchase(productId: String, activity: Activity) {
        if (iapConnector == null) {
            iapConnector = IapConnector(context, nonConsumableKeys, consumableKeys, subscriptionKeys, key, enableLogging)
            iapConnector?.addBillingClientConnectionListener(object :
                BillingClientConnectionListener {
                override fun onConnected(status: Boolean, billingResponseCode: Int) {
                    Log.d(TAG, "This is the status: $status and response code is: $billingResponseCode")

                    if (status) {
                        iapConnector?.purchase(activity, productId)
                    }

                    val billingStatus = if (status) BillingStatus.CONNECTED else BillingStatus.NOT_CONNECTED
                    postValue(billingStatus)

                }
            })

            iapConnector?.addPurchaseListener(object : PurchaseServiceListener {
                override fun onPricesUpdated(iapKeyPrices: Map<String, List<DataWrappers.ProductDetails>>) {
                    // list of available products will be received here, so you can update UI with prices if needed
                }

                override fun onProductPurchased(purchaseInfo: DataWrappers.PurchaseInfo) {

                    Log.d(TAG,"onPricesUpdated: ${purchaseInfo.sku}")
                    postValue(BillingStatus.PURCHASE_SUCCESS)
                }

                override fun onProductRestored(purchaseInfo: DataWrappers.PurchaseInfo) {
                    // will be triggered fetching owned products using IapConnector;
                    Log.d(TAG, "onProductRestored: ${purchaseInfo.sku}")
                    if (purchaseInfo.sku == productId) {
                        postValue(BillingStatus.RESTORED)
                    }

                }

                override fun onPurchaseFailed(purchaseInfo: DataWrappers.PurchaseInfo?, billingResponseCode: Int?) {
                    // will be triggered whenever a product purchase is failed
                    postValue(BillingStatus.PURCHASE_FAIL)
                }
            })

        } else {
            iapConnector?.purchase(activity, productId)
        }

    }

    fun close(){
        iapConnector?.destroy()
        iapConnector = null
    }
}