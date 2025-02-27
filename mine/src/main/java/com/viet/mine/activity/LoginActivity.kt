package com.viet.mine.activity

import android.arch.lifecycle.Observer
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.KeyEvent
import com.chenenyu.router.annotation.Route
import com.facebook.CallbackManager
import com.safframework.ext.click
import com.viet.mine.R
import com.viet.mine.viewmodel.LoginViewModel
import com.viet.news.core.config.Config
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.domain.LoginEvent
import com.viet.news.core.domain.User
import com.viet.news.core.domain.response.LoginRegisterResponse
import com.viet.news.core.dsl.addOnPageChangeListener
import com.viet.news.core.ext.clickWithTrack
import com.viet.news.core.ext.finishWithAnim
import com.viet.news.core.ext.toast
import com.viet.news.core.ui.InjectActivity
import com.viet.news.core.ui.TabFragmentAdapter
import com.viet.news.core.utils.RxBus
import com.viet.news.webview.WebActivity
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

/**
 * @Description 登录注册页面
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 06/09/2018 1:42 PM
 * @Version 1.0.0
 */
@Route(value = [(Config.ROUTER_LOGIN_ACTIVITY)])
class LoginActivity : InjectActivity() {

    @Inject
    internal lateinit var adapter: TabFragmentAdapter
    private val model by viewModelDelegate(LoginViewModel::class)
    private val callbackManager = CallbackManager.Factory.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
        initListener()
    }

    private fun initView() {
        swipeBackLayout.setEnableGesture(false)
        adapter.setTitles(model.titles)
        adapter.setFragment(model.fragments)
        viewpager.adapter = adapter
        tablayout.setViewPager(viewpager)
        setTabText(0, 1)

        //点击协议
        agreement_text.clickWithTrack(Config.login_userProtocol, 2000) {
           // WebActivity.launch(this, Config.PACT_URL)
            User.currentUser.login(LoginRegisterResponse().apply {
                this.phoneNumber = "18710040239"
                this.avatar = ""
                this.token = "1"
                this.roleId = "1"
                this.nickName = "逍遥才子"
                this.userId = "1"
                this.fansCount = 1223
                this.followCount = 455
            })

            RxBus.get().post(LoginEvent())
            finishWithAnim()
        }

        login_button.setOnLoginListener(this, callbackManager) {
            onSuccess = {
                it?.accessToken?.apply {
                    model.loginByFacebook(this@LoginActivity, userId, token, expires.toString()) {
                        finishWithAnim()
                    }
                }
            }
            onCancel = {
                //...
            }
            onError = {
                it?.message.let { toast(it)  }
            }
        }
    }

    private fun initListener() {
        //错误信息展示
        model.statusMsg.observe(this, Observer { it?.let { msg -> toast(msg)  } })
        viewpager.addOnPageChangeListener { onPageSelected = { setTabText(it, model.currentTab) } }
        iv_close.click { finishWithAnim(0, R.anim.activity_close) }
    }

    private fun setTabText(currentTab: Int, otherTab: Int) {
        tablayout.getTitleView(currentTab).textSize = 25F
        tablayout.getTitleView(otherTab).textSize = 15F
        tablayout.getTitleView(currentTab).typeface = Typeface.DEFAULT_BOLD
        tablayout.getTitleView(otherTab).typeface = Typeface.DEFAULT
        model.currentTab = currentTab
    }

    /**
     * 双击返回键退出
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return if (model.currentTab != 0) {
                tablayout.currentTab = 0
                true
            } else {
                finishWithAnim(0, R.anim.activity_close)
                false
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}