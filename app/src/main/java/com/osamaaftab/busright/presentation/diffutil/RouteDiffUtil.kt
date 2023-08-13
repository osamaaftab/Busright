package com.osamaaftab.busright.presentation.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.osamaaftab.busright.domain.model.RouteModel

class RouteDiffUtil : DiffUtil.ItemCallback<RouteModel>() {
    override fun areItemsTheSame(oldItem: RouteModel, newItem: RouteModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: RouteModel, newItem: RouteModel): Boolean {
        return oldItem.id == newItem.id
    }
}