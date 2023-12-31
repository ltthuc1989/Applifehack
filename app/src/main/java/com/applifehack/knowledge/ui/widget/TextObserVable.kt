package com.applifehack.knowledge.ui.widget

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.applifehack.knowledge.BR


class TextObserVable: BaseObservable(){

    private var _text = ""

    var text: String
        @Bindable get() {
            return _text
        }
        set(value) {
            if(value==null){
                _text = ""
            }else{
                _text = value
            }

            notifyPropertyChanged(BR.text)
        }



}