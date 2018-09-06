package com.viet.mine.fragment

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import cn.magicwindow.core.ui.ItemClickSupport
import cn.magicwindow.mine.adapter.LanguageAdapter
import com.viet.mine.R
import com.viet.news.core.config.Config
import com.viet.news.core.ui.BaseFragment
import com.viet.news.core.utils.LanguageUtil
import com.viet.news.core.utils.SPHelper

class LanguageFragment : BaseFragment() {
    private lateinit var adapter: LanguageAdapter
    private var mContainerView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContainerView = inflater.inflate(R.layout.fragment_mine_setting_language, container, false)
        return mContainerView
    }

    override fun initView(view: View) {
        val rvLanguage = view.findViewById<RecyclerView>(R.id.rv_language)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = OrientationHelper.VERTICAL
        rvLanguage.layoutManager = linearLayoutManager
        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.shape_list_divider_gray_1dp)!!)
        rvLanguage.addItemDecoration(dividerItemDecoration)
        adapter = LanguageAdapter()
        rvLanguage.adapter = adapter
        adapter.addData(resources.getStringArray(R.array.language).toList())

        ItemClickSupport.addTo(rvLanguage).addOnItemClickListener(object : ItemClickSupport.OnItemClickListener {
            override fun onItemClicked(recyclerView: RecyclerView, position: Int, v: View) {
                if (LanguageUtil.needChange(position)) {
                    LanguageUtil.saveSelectLanguage(context!!, position)
                    LanguageUtil.routToMain(context!!)
                } else {
                    //当不需要切换语言时，判断是否是【跟随】 与【系统语言】之间的切换
                    val localIndex = SPHelper.create().getInt(Config.SELECTED_LANGUAGE)
                    if ((localIndex == 0 || position == 0) && localIndex != position) {
                        SPHelper.create().putInt(Config.SELECTED_LANGUAGE, position)
                    }
                    Navigation.findNavController(view).navigateUp()
                }
            }
        })
    }
}