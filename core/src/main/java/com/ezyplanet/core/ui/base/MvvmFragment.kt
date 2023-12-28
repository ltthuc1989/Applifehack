package com.ezyplanet.core.ui.base

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.ezyplanet.core.util.livedata.ColdEventObserver
import com.ezyplanet.core.viewmodel.DataChangeVM
import com.ezyplanet.core.ui.listener.RetryCallback
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class MvvmFragment<V : BaseViewModel<*,*>, B : ViewDataBinding> : Fragment(), MvvmView<V, B> {


    protected val TAG = this::class.java.simpleName
    override lateinit var binding: B
    lateinit var dataChangeVM: DataChangeVM

    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    /**
     * Attempt to get viewModel lazily from [viewModelFactory] with the scope of given activity.
     *
     * @param activity given scope.
     * @return T an instance of requested ViewModel.
     */
    inline fun <reified T : BaseViewModel<*,*>> getLazyViewModel(scope: ViewModelScope): Lazy<T> =
            lazy {
                when (scope) {
                    ViewModelScope.ACTIVITY -> ViewModelProviders.of(requireActivity(), viewModelFactory)[T::class.java]
                    ViewModelScope.FRAGMENT -> ViewModelProviders.of(this, viewModelFactory)[T::class.java]
                }

            }

    inline fun <reified T : ViewModel> getLazyNormalViewModel(scope: ViewModelScope): Lazy<T> =
            lazy {
                when (scope) {
                    ViewModelScope.ACTIVITY -> ViewModelProviders.of(requireActivity(), viewModelFactory)[T::class.java]
                    ViewModelScope.FRAGMENT -> ViewModelProviders.of(this, viewModelFactory)[T::class.java]
                }
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onAttach(context: Context) {
        // we should inject fragment dependencies before invoking super.onAttach()

        AndroidSupportInjection.inject(this)

        Log.d(TAG,"OnAttatch")
        super.onAttach(context)



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
        viewModel.activityAction.observe(viewLifecycleOwner, Observer { it?.invoke(requireActivity()) })
        viewModel.fragmentAction.observe(viewLifecycleOwner, Observer { it?.invoke(this) })
        onViewInitialized(binding)
        dataChangeVM = ViewModelProviders.of(activity!!).get(DataChangeVM::class.java)
        dataChangeVM?.isLogined?.observe(viewLifecycleOwner, ColdEventObserver(this) {
            viewModel.reLoadData()
        })
        dataChangeVM?.isNeedReload?.observe(viewLifecycleOwner, ColdEventObserver(this) {
            viewModel.reLoadData()
        })
        if(savedInstanceState==null){
            viewModel.setIsUiStageChange(true)

        }
    }


    interface Callback {

        fun onFragmentAttached()

        fun onFragmentDetached(tag: String)
    }

    fun connectionFail(restId: Int, retryCallback: RetryCallback<*>) {
        getBaseActivity()?.connectionFail(restId, retryCallback)
    }

    fun connectionFail(restId: Int) {
        getBaseActivity()?.connectionFail(restId)

    }

    fun openActivityOnTokenExpire() {
        getBaseActivity()?.openActivityOnTokenExpire()

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
        getBaseActivity()?.showAlert(message)

    }

    fun showAlert(@StringRes resId: Int, code: Int) {
        getBaseActivity()?.showAlert(resId, code)

    }

    fun hideProgress() {
        getBaseActivity()?.hideProgress()

    }

    fun showProgress() {
        getBaseActivity()?.showProgress()

    }
    fun showAlert(@StringRes resId: Int) {
        getBaseActivity()?.showAlert(resId)

    }
    fun showAlert(title: String?,message: String?) {
        getBaseActivity()?.showAlert(title,message)

    }
    private fun getBaseActivity(): MvvmActivity<*, *>? {
        if (context == null) return null
        if (context is MvvmActivity<*, *>) return context as MvvmActivity<*, *>?
        return if (context is ContextWrapper) getBaseActivity() else null
    }
//    fun <T>onDataChange(liveData: MutableLiveData<T>,mediatorLiveData: MediatorLiveData<T>){
//        mediatorLiveData.addSource(liveData){
//            mediatorLiveData.value=it
//        }
//        mediatorLiveData.observe(this, Observer {
//
//            viewModel.reLoadData()
//        })
//
//    }
//    fun <T>updateUser(mediatorLiveData: MediatorLiveData<Boolean>){
//        mediatorLiveData.addSource(viewModel.isLogIn){
//            mediatorLiveData.value=it
//        }
//        mediatorLiveData.observe(this, Observer {
//            if(it) viewModel.reLoadData()
//        })
//
//    }

}