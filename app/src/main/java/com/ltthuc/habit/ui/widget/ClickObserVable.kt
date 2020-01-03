package com.ltthuc.habit.ui.widget

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.ltthuc.habit.BR


class ClickObserVable(val action:(Boolean)->Unit={}) : BaseObservable(){

    private var _click = false

    var click: Boolean
        @Bindable get() {
            return _click
        }
        set(value) {
            _click = value
            action?.invoke(value)

            notifyPropertyChanged(BR.click)
        }

 interface ClickListener{
     fun onChecked(check:Boolean)
 }
}