package com.viet.follow.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.magicwindow.core.ui.ItemClickSupport
import com.viet.follow.R
import com.viet.follow.adapter.FunsAndFollowAdapter
import com.viet.follow.viewmodel.FansAndFollowViewModel
import com.viet.news.core.delegate.viewModelDelegate
import com.viet.news.core.ui.InjectFragment
import com.viet.news.core.vo.Status
import kotlinx.android.synthetic.main.fragment_follow_tab.*
import javax.inject.Inject

/**
 * @Description 关注tab
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 03/09/2018 11:13 AM
 * @Version 1.0.0
 */
class FollowTabFragment : InjectFragment() {

    @Inject
    internal lateinit var adapter: FunsAndFollowAdapter
    private val model by viewModelDelegate(FansAndFollowViewModel::class)
    var page_number = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_follow_tab, container, false)
    }

    override fun initView(view: View) {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, OrientationHelper.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.shape_list_divider_gray_05dp)!!)
        recyclerView.addItemDecoration(dividerItemDecoration)

        refreshLayout.autoRefresh()
        refreshLayout.setOnRefreshListener { initData(false) }
        refreshLayout.setOnLoadMoreListener { initData(true) }
        multiStatusView.setLoadingButtonClickListener(View.OnClickListener { refreshLayout.autoRefresh() })
        ItemClickSupport.addTo(recyclerView).addOnChildClickListener(R.id.btn_follow, listener)
    }

    private val listener = object : ItemClickSupport.OnChildClickListener {
        override fun onChildClicked(recyclerView: RecyclerView, position: Int, v: View) {
            model.follow(this@FollowTabFragment,adapter.getData()[position].id) {
                adapter.getData()[position].follow_flag = true
                adapter.notifyItemChanged(position)
            }
        }
    }

    private fun initData(loadMore: Boolean) {
        if (loadMore) {
            page_number += 1
        } else {
            page_number = 1
        }
        model.followList(page_number)
                .observe(this, Observer {
                    when (it?.status) {
                        Status.SUCCESS -> {
                            multiStatusView.showContent()
                            if (loadMore) {
                                if (it.data?.data?.list == null || it.data?.data?.list!!.isEmpty()) {
                                    refreshLayout.finishLoadMoreWithNoMoreData()
                                } else {
                                    refreshLayout.finishLoadMore()
                                    adapter.addData(it.data?.data?.list)
                                }
                            } else {
                                if (it.data?.data?.list == null || it.data?.data?.list!!.isEmpty()) {
                                    multiStatusView.showEmpty()
                                    refreshLayout.setEnableLoadMore(false)
                                }
                                adapter.setData(it.data?.data?.list)
                                refreshLayout.setNoMoreData(false)
                                refreshLayout.finishRefresh()
                            }
                        }
                        Status.ERROR -> {
                            multiStatusView.showError()
                            if (loadMore) {
                                refreshLayout.finishLoadMore(false)//传入false表示加载失败
                            } else {
                                refreshLayout.finishRefresh(false)
                            }
                        }
                        else -> {
                        }
                    }
                })
    }
}