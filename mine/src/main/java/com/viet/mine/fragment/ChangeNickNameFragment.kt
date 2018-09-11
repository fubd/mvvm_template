package com.viet.mine.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.jakewharton.rxbinding2.widget.RxTextView
import com.viet.mine.R
import com.viet.mine.viewmodel.SettingViewModel
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.ext.clickWithTrigger
import com.viet.news.core.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_mine_setting_nickname.*

/**
 * @Description 修改昵称
 * @Author null
 * @Email zongjia.long@merculet.io
 * @Version
 */
class ChangeNickNameFragment : BaseFragment() {
    private var mContainerView: View? = null
    private val model by viewModelDelegate(SettingViewModel::class, true)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainerView = inflater.inflate(R.layout.fragment_mine_setting_nickname, container, false)
        return mContainerView
    }

    override fun initView(view: View) {
        initListener(view)
    }

    @SuppressLint("CheckResult")
    private fun initListener(view: View) {
        RxTextView.textChanges(nickname_input)
                .subscribe {
                    model.nickName.value = it.toString()
                    model.checkNickNameSubmitBtnEnable()
                }

        confirm_btn.clickWithTrigger {
            if (model.nickNameSubmitEnable()){
                model.updateNickName(this, nickname_input.text.toString()) { isOk ->
                    if (isOk) {
                        Navigation.findNavController(view).navigateUp()
                        Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "修改失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


}