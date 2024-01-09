package com.ezyplanet.core.ui.base

import android.app.Dialog



import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.ezyplanet.core.R
import com.ezyplanet.core.ui.listener.RetryCallback
import com.ezyplanet.core.util.ScreenUtils.getScreenWidth
import com.ezyplanet.core.viewmodel.DialogEventVM


import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class MvvmDialogFrag<V : BaseViewModel<*,*>, B : ViewDataBinding> : DialogFragment(), MvvmView<V, B> {


    protected val TAG = this::class.java.simpleName
    override lateinit var binding: B
    private var mActivity: MvvmActivity<*, *>? = null
    protected lateinit var dialogEventVM: DialogEventVM
    protected var isFullScreen = true

    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    /**
     * Attempt to get viewModel lazily from [viewModelFactory] with the scope of given activity.
     *
     * @param activity given scope.
     * @return T an instance of requested ViewModel.
     */
//    inline fun <reified T : BaseViewModel<*,*>> getLazyViewModel(scope: ViewModelScope): Lazy<T> =
//            lazy {
//                when (scope) {
//                    ViewModelScope.ACTIVITY -> ViewModelProvider(requireActivity(), viewModelFactory)[T::class.java]
//                    ViewModelScope.FRAGMENT -> ViewModelProvider(this, viewModelFactory)[T::class.java]
//                }
//
//            }
//
//    inline fun <reified T : ViewModel> getLazyNormalViewModel(scope: ViewModelScope): Lazy<T> =
//            lazy {
//                when (scope) {
//                    ViewModelScope.ACTIVITY -> ViewModelProvider(requireActivity(), viewModelFactory)[T::class.java]
//                    ViewModelScope.FRAGMENT -> ViewModelProvider(this, viewModelFactory)[T::class.java]
//                }
//            }

    override fun onStart() {
        super.onStart()
        onChildStart()
    }

    open fun onChildStart() {
        val dialog = dialog
        if (dialog != null) {
            dialog.window!!
                    .setLayout((getScreenWidth(context) * .9).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (isFullScreen) {
            setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullScreenDialogStyle)
        }
        isCancelable = true
        val dialog = super.onCreateDialog(savedInstanceState)
        onCreatedDialogCustom(dialog)
        return dialog
    }

    open fun onCreatedDialogCustom(dialog: Dialog) {
        dialog.window!!.setBackgroundDrawable(
                ColorDrawable(Color.WHITE))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

    }


    override fun onAttach(context: Context) {
        // we should inject fragment dependencies before invoking super.onAttach()

        AndroidSupportInjection.inject(this)

        Log.d(TAG, "OnAttatch")
        super.onAttach(context)
        if (context is MvvmActivity<*, *>) {
            mActivity = getBaseActivity()
        }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // initialize binding
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.setLifecycleOwner(this)

        // set viewModel as an observer to this activity lifecycle events
        lifecycle.addObserver(viewModel)


        return binding.root
    }

    override fun onDetach() {
        super.onDetach()
        Log.e(TAG, "OnDetach")
    }

    override fun onStop() {
        super.onStop()
        Log.e(TAG, "OnStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e(TAG, "OnDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "OnDestroy")
    }

    abstract fun setUpNavigator()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // observe viewModel uiActions in order to pass parent activity as argument of uiAction
        setUpNavigator()
        viewModel.activityAction.observe(this, Observer { it?.invoke(requireActivity()) })
        viewModel.fragmentAction.observe(this, Observer { it?.invoke(this) })
        dialogEventVM = ViewModelProvider(activity!!).get(DialogEventVM::class.java)
        onViewInitialized(binding)

        if (savedInstanceState == null) {
            viewModel.setIsUiStageChange(true)

        }
    }


    interface Callback {

        fun onFragmentAttached()

        fun onFragmentDetached(tag: String)
    }

    fun connectionFail(restId: Int, retryCallback: RetryCallback<*>) {
        mActivity?.connectionFail(restId, retryCallback)
    }

    fun connectionFail(restId: Int) {
        mActivity?.connectionFail(restId)

    }

    fun openActivityOnTokenExpire() {
        mActivity?.openActivityOnTokenExpire()

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
        mActivity?.showAlert(message)

    }

    fun showAlert(@StringRes resId: Int, code: Int) {
        mActivity?.showAlert(resId, code)

    }

    fun hideProgress() {
        mActivity?.hideProgress()

    }

    fun showProgress() {
        mActivity?.showProgress()

    }

    fun showAlert(resId: Int) {

    }

    fun showAlert(title: String?,message: String?) {
        mActivity?.showAlert(title,message)

    }

    private fun getBaseActivity(): MvvmActivity<*, *>? {
        if (context == null) return null
        if (context is MvvmActivity<*, *>) return context as MvvmActivity<*, *>?
        return if (context is ContextWrapper) getBaseActivity() else null
    }


}