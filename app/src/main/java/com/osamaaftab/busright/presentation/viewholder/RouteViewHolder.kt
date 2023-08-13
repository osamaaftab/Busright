package com.osamaaftab.busright.presentation.viewholder

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.osamaaftab.busright.BR
import com.osamaaftab.busright.presentation.adapter.GenericListAdapter
import com.osamaaftab.busright.presentation.viewmodel.RoutesViewModel

class RouteViewHolder<T>(
    private val viewDataBinding: ViewDataBinding,
    private val routesViewModel: RoutesViewModel
) : RecyclerView.ViewHolder(viewDataBinding.root),
    GenericListAdapter.Binder<T> {

    override fun bind(data: T) {
        viewDataBinding.setVariable(BR.route, data)
        viewDataBinding.setVariable(BR.viewModel, routesViewModel)
    }
}