package com.andigeeky.mvpapp.ui.lines

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.andigeeky.mvpapp.R
import com.andigeeky.mvpapp.databinding.ItemLineBinding
import com.andigeeky.mvpapp.lines.vo.Line
import com.andigeeky.mvpapp.ui.common.AppExecutors
import com.andigeeky.mvpapp.ui.common.DataBoundListAdapter

class LinesAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val callback: ((Line) -> Unit)?
) : DataBoundListAdapter<Line, ItemLineBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Line>() {
        override fun areItemsTheSame(oldItem: Line, newItem: Line): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Line, newItem: Line): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun createBinding(parent: ViewGroup): ItemLineBinding {
        val binding = DataBindingUtil
            .inflate<ItemLineBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_line,
                parent,
                false,
                dataBindingComponent
            )
        binding.root.setOnClickListener {
            binding.line?.let {
                callback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: ItemLineBinding, item: Line) {
        binding.line = item
    }
}
