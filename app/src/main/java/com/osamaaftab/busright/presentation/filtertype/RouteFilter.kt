package com.osamaaftab.busright.presentation.filtertype

import android.widget.Filter
import com.osamaaftab.busright.domain.model.RouteModel
import com.osamaaftab.busright.presentation.adapter.GenericListAdapter

class RouteFilter(var listAdapter: GenericListAdapter<RouteModel>, var list: List<RouteModel>) : Filter() {

    private var filteredList: ArrayList<RouteModel> = ArrayList()

    override fun performFiltering(charSequence: CharSequence): FilterResults {
        filteredList.clear()
        val results = FilterResults()

        if (charSequence.isEmpty()) {
            filteredList.addAll(list)
        } else {
            val filterPattern: String = charSequence.toString().lowercase().trim()
            for (model in list) {
                if (model.name.lowercase().contains(filterPattern)) {
                    filteredList.add(model)
                }
            }
        }
        results.values = filteredList
        results.count = filteredList.size
        return results
    }

    override fun publishResults(p0: CharSequence, result: FilterResults) {
        listAdapter.submitList(result.values as ArrayList<RouteModel>)
    }
}
