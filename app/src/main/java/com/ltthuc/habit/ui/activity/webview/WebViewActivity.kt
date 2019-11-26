package com.ltthuc.habit.ui.activity.webview

import android.content.Intent
import android.net.Uri
import android.os.Handler
import com.ltthuc.habit.R
import com.ltthuc.habit.databinding.ActivityWebviewBinding
import com.ltthuc.habit.ui.widget.listener.ToolbarListener

import com.ezyplanet.core.ui.base.MvvmActivity
import com.ezyplanet.core.util.extension.getExtra




class WebViewActivity : MvvmActivity<ActivityWebviewBinding, WebViewVM>(), WebViewNav {
    override val viewModel: WebViewVM by getLazyViewModel<WebViewVM>()
    override val layoutId: Int = R.layout.activity_webview

    companion object {
        val TITLE = "title"
        val URL = "url"
        val NOTIFI_ID = "NOTIFI_ID"
    }

    override fun onViewInitialized(binding: ActivityWebviewBinding) {
        super.onViewInitialized(binding)
        binding.viewModel = viewModel
        viewModel.navigator = this
        viewModel.url.value = getExtra(URL)
        viewModel.title.value=getExtra(TITLE)



        lazyLoadWebview()
        binding.toolbarWebview.setListner(object : ToolbarListener {
            override fun onBack() { isSwitchSreen?.value = true
                finish()
            }
        })



    }

    override fun onResume() {
        super.onResume()

    }

    override fun onStop() {
        super.onStop()

    }


    override fun sendEmail(url: String?) {
        isSwitchSreen?.value = true
        sendMail(url)
    }

    override fun openPdfFile(url: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showWebcontent(url: String?) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    private fun lazyLoadWebview() {
        val webViewInflationHandler = Handler()
        webViewInflationHandler.postDelayed({
            inflateViewStub()
        }, 100)

    }

    private fun sendMail(url: String?) {
        val mail = getMailInfo(url)
        val emailIntent: Intent
        if (mail == null) {
            emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", url, null))
        } else {
            emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse(mail[0]))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, mail[1])
        }


        //startActivityResult(Intent.createChooser(emailIntent, "Send email..."), 1)

    }

    private fun getMailInfo(url: String?): Array<String?>? {
        val split = url?.split("\\?".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
        var result: Array<String?>? = null
        if (split!!.size > 1) {
            result = arrayOfNulls(2)
            result[0] = split[0]
            result[1] = split[1].split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
        }
        return result
    }

    private fun openPdfView(url: String) {

//        val intent = Intent(this, PdfViewActivity::class.java)
//        intent.putExtra("url", url)
//        AnimUtils.startActivityAnimated(this, intent, AnimUtils.ActivityAnimationType.NONE)
    }

    private fun inflateViewStub() {
        if (!binding.stubImport.isInflated) {
            binding.stubImport.viewStub?.inflate()
        }
    }

    override fun onLoaded() {
        hideProgress()
    }
}