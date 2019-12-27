package com.ezyplanet.core.ui.base


import android.app.Service
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.*
import android.view.View
import android.view.WindowManager
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.ezyplanet.core.R
import com.ezyplanet.core.ui.listener.RetryCallback
import com.ezyplanet.core.ui.widget.TransparentProgressDialog
import com.ezyplanet.core.util.AlertUtils
import com.ezyplanet.core.util.CommonUtil
import com.ezyplanet.core.util.extension.finishActivity
import com.tapadoo.alerter.Alerter
import com.tapadoo.alerter.OnHideAlertListener
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


/**
 * Every Activity should inherit from this base activity in order to create relevant binding class,
 * inject dependencies and handling default actions.
 * @param V A ViewModel class that inherited from [BaseViewModel], will be used as default ViewModel of activity
 * @param B A Binding class that inherited from [ViewDataBinding], will be used for creating View of this activity
 */
open abstract class MvvmActivity<B : ViewDataBinding, V : BaseViewModel<*,*>> : AppCompatActivity(), MvvmView<V, B>, HasSupportFragmentInjector, MvvmFragment.Callback {
    private var checking: Boolean?= false
    override lateinit var binding: B

    private val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
    private val REQUEST_CROP_GALLERY = 301
    private val REQUEST_CROP_CAPTURE = 302
    private val REQUEST_TAKE_VIDEO = 303
    val REQUEST_PASSWORD_LOGIN_CODE = 304
    protected val EXPIRED_TOKEN = "expired_token"
    val REQUEST_CAMERA_PERMISSION = 101
    internal var hasShowingDlg: Boolean = false
    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory
    private var progressDialog: TransparentProgressDialog? = null
    var isSwitchSreen=MutableLiveData<Boolean>()
    protected var isOpenDrawer = false







    /**
     * Attempt to get viewModel lazily from [viewModelFactory] with the scope of given activity.
     *
     * @param activity given scope.
     * @return T an instance of requested ViewModel.
     */
    inline fun <reified T : BaseViewModel<*,*>> getLazyViewModel(): Lazy<T> =
            lazy { ViewModelProviders.of(this, viewModelFactory)[T::class.java] }


    override fun onCreate(savedInstanceState: Bundle?) {
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        // we should inject dependencies before invoking super.onCreate()
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
//        if (BuildConfig.DEBUG) {
//            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
//                    .detectDiskReads()
//                    .detectDiskWrites()
//                    .detectNetwork()   // or .detectAll() for all detectable problems
//                    .penaltyLog()
//                    .build())
//            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
//                    .detectLeakedSqlLiteObjects()
//                    .detectLeakedClosableObjects()
//                    .penaltyLog()
//                    .penaltyDeath()
//                    .build())
//        }

        // initialize binding
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this


        // set viewModel as an observer to this activity lifecycle events
        lifecycle.addObserver(viewModel)
        //todo:  viewModel.checkConnection()
        // observe viewModel uiActions in order to pass this activity as argument of uiAction
        viewModel.activityAction.observe(this, Observer { it?.invoke(this) })

        onViewInitialized(binding)
        viewModel.handleIntent(intent)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        viewModel.handleIntent(intent,true)
    }

    override fun onResume() {
        isSwitchSreen.value = false
        super.onResume()
    }


    fun hideProgress() {

        progressDialog?.let { if (it.isShowing) it.cancel() }
        viewModel.resetLoadingState = false
    }

    fun showProgress() {
        if (isFinishing) {
            return
        }
        progressDialog?.let { if (it.isShowing) return }
        try {
            progressDialog = CommonUtil.showLoadingDialog(this)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector
    override fun onFragmentAttached() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFragmentDetached(tag: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    fun connectionFail(restId: Int, retryCallback: RetryCallback<*>) {
        runOnUiThread {
            if (!hasShowingDlg) {
                hasShowingDlg = true
                AlertUtils.showCancelRetryAlertDlg(this@MvvmActivity, getString(restId), retryCallback,  {
                    hasShowingDlg = false
                    retryCallback.onRetry()
                },  {
                    hasShowingDlg = false
                    retryCallback.onCancel()
                    finish()


                })
            }
        }
    }

    fun connectionFail(restId: Int) {
        showAlert(restId)
    }

    open fun openActivityOnTokenExpire() {
//        val intent = Intent(this, LoginActivity::class.java)
//        startActivity(intent)
    }

    fun showErrorSnackBar(@StringRes resId: Int) {
        showErrorSnackBar(getString(resId))
    }


    fun showErrorSnackBar(message: String?) {
        if (message != null) {
            // showSnackBar(message)

        } else {
            // showSnackBar(getString(R.string.some_error));

        }
    }

    fun showAlert(message: String?) {
        if (!hasShowingDlg) {
            if (message != null) {
                hasShowingDlg = true
                AlertUtils.showOkAlertDialog(this, message){
                    hasShowingDlg = false }
            }
        }
    }

    fun showAlert(@StringRes resId: Int, code: Int) {
        if (code != 200) {
            showAlert(getString(resId) + " [code=" + code + "]")
        } else {
            showAlert(getString(resId))
        }
    }

    fun showAlert(@StringRes resId: Int) {
        showAlert(getString(resId))
    }

    fun showAlert(title: String?, message: String?) {
        AlertUtils.showOkAlert(this, title!!, message!!)
    }



    override fun onStop() {
        super.onStop()
        Alerter.hide()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishActivity()
    }


    fun notificationInApp(message: String, onClickListener: View.OnClickListener) {
        try {
            val vibratorService = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibratorService.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
            }else{
                vibratorService.vibrate(200)
            }

            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val r = RingtoneManager.getRingtone(applicationContext, notification)
            r.play()
            Handler().postDelayed({r.stop();  }, 5000)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Alerter.create(this)
                .setText(message)
                .setDuration(10000)
                .setIcon(R.drawable.outline_feedback_white)
                .setBackgroundColorRes(R.color.app_129af0) // or setBackgroundColorInt(Color.CYAN)
                .enableSwipeToDismiss()
                .setDismissable(true)
                .setOnClickListener(onClickListener)
                .setOnHideListener(OnHideAlertListener {})
                .show()

    }
    fun showAlertTimeOut(@StringRes resId: Int?){
        AlertUtils.showOkAlertDialog(this,getString(resId!!)){

        }



    }



}

