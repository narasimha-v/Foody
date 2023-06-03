package com.example.foody.bindingAdapters

import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foody.adapters.FavouriteRecipesAdapter
import com.example.foody.data.database.entities.FavouritesEntity

class FavouriteRecipesBinding {
    companion object {
        @BindingAdapter("setVisibility", "setData", requireAll = false)
        @JvmStatic
        fun setDataAndViewVisibility(
            view: View,
            favouritesEntity: List<FavouritesEntity>?,
            mAdapter: FavouriteRecipesAdapter?
        ) {
            when (view) {
                is RecyclerView -> {
                    val dataCheck = favouritesEntity.isNullOrEmpty()
                    view.isInvisible = dataCheck
                    if (!dataCheck) {
                        favouritesEntity?.let { mAdapter?.setData(it) }
                    }
                }

                else -> {
                    view.isVisible = favouritesEntity.isNullOrEmpty()
                }
            }
        }
    }
}