package com.osamaaftab.busright.presentation.ui.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Filter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.osamaaftab.busright.R
import com.osamaaftab.busright.databinding.ActivityRouteListBinding
import com.osamaaftab.busright.domain.model.RouteModel
import com.osamaaftab.busright.presentation.adapter.GenericListAdapter
import com.osamaaftab.busright.presentation.diffutil.RouteDiffUtil
import com.osamaaftab.busright.presentation.filtertype.RouteFilter
import com.osamaaftab.busright.presentation.viewholder.RouteViewHolder
import com.osamaaftab.busright.presentation.viewmodel.RoutesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RouteListActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private val routesViewModel: RoutesViewModel by viewModel()
    private var routeListAdapter: GenericListAdapter<RouteModel>? = null
    private lateinit var activityRouteListBinding: ActivityRouteListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRouteListBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_route_list)
        initListener()
        initAdapter()
        initObserver()
    }

    private fun initAdapter() {
        routeListAdapter = object : GenericListAdapter<RouteModel>(RouteDiffUtil()) {

            override fun getViewHolder(viewDataBinding: ViewDataBinding): RecyclerView.ViewHolder {
                return RouteViewHolder<RouteModel>(
                    viewDataBinding,
                    routesViewModel
                )
            }

            override fun getLayoutId(): Int {
                return R.layout.item_route
            }

            override fun getFilterType(): Filter {
                return RouteFilter(this, this.currentList)
            }
        }
        activityRouteListBinding.routeRecyclerView.adapter = routeListAdapter
    }

    private fun initObserver() {
        observeRouteListLiveData()
        observerProgressLiveData()
        observeShowErrorLiveData()
        observeRouteOnClickLiveData()
    }

    private fun observeShowErrorLiveData() {
        routesViewModel.getShowErrorLiveData().observe(this) {
            activityRouteListBinding.refreshLayout.isRefreshing = false
            if (it == true) {
                activityRouteListBinding.statusImg.visibility = View.VISIBLE
            } else activityRouteListBinding.statusImg.visibility = View.GONE
        }
    }

    private fun observerProgressLiveData() {
        routesViewModel.getShowProgressLiveData().observe(this) {
            if (it == true) {
                activityRouteListBinding.indeterminateBar.visibility = View.VISIBLE
            } else activityRouteListBinding.indeterminateBar.visibility = View.GONE
        }
    }

    private fun observeRouteListLiveData() {
        routesViewModel.getRouteListLiveData().observe(this) {
            routeListAdapter?.submitList(it)
            activityRouteListBinding.routeRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun observeRouteOnClickLiveData() {
        routesViewModel.getOnRouteOnClick().observe(this) {
            val intent = Intent(this, RouteDetailActivity::class.java)
            intent.putExtra(ROUTE_ID_KEY, it)
            startActivity(intent)
        }
    }

    private fun initListener() {
        activityRouteListBinding.refreshLayout.setOnRefreshListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            isIconified = true
        }
        return true
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            routeListAdapter?.filter?.filter(query)
        }
    }

    override fun onRefresh() {
        routesViewModel.refreshRouteList()
    }

    companion object {
        const val ROUTE_ID_KEY = "routeId"
    }
}