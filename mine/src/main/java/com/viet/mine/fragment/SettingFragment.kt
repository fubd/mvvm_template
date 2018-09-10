package com.viet.mine.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.viet.mine.R
import com.viet.news.core.domain.User
import com.viet.news.core.ext.click
import com.viet.news.core.ui.BaseFragment
import com.viet.news.core.ui.widget.CommonItem
import kotlinx.android.synthetic.main.fragment_mine_setting.*

/**
 * @Description 设置
 * @Author null
 * @Email zongjia.long@merculet.io
 * @Version
 */
class SettingFragment : BaseFragment() {

    private var mContainerView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainerView = inflater.inflate(R.layout.fragment_mine_setting, container, false)
        return mContainerView
    }

    override fun initView(view: View) {
        val languageSettingItem = view.findViewById<CommonItem>(R.id.item_language_setting)
        val helpItem = view.findViewById<CommonItem>(R.id.item_help)
        val feedBackItem = view.findViewById<CommonItem>(R.id.item_feed_back)

        languageSettingItem.setClickDelegate {
            onItemClick = {
                Navigation.findNavController(languageSettingItem).navigate(R.id.action_page1)
            }
        }

        helpItem.setClickDelegate {
            onItemClick = {
                Navigation.findNavController(helpItem).navigate(R.id.action_page2)
            }
        }

        feedBackItem.setClickDelegate {
            onItemClick = {
                Navigation.findNavController(feedBackItem).navigate(R.id.action_page3)
            }
        }

        btn_logout.click {
            User.currentUser.logout()
            activity?.finish()
        }
    }
}