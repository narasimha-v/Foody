package com.example.foody.bindingAdapters

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foody.adapters.FavouriteRecipesAdapter
import com.example.foody.data.database.entities.FavouritesEntity

class FavouriteRecipesBinding {
    companion object {
        @BindingAdapter("viewVisibility", "setData", requireAll = false)
        @JvmStatic
        fun setDataAndViewVisibility(
            view: View,
            favouritesEntity: List<FavouritesEntity>?,
            mAdapter: FavouriteRecipesAdapter?
        ) {
            if (favouritesEntity.isNullOrEmpty()) {
                when (view) {
                    is RecyclerView -> {
                        view.visibility = View.INVISIBLE
                    }

                    else -> {
                        view.visibility = View.VISIBLE
                    }
                }
            } else {
                when (view) {
                    is RecyclerView -> {
                        view.visibility = View.VISIBLE
                        mAdapter?.setData(favouritesEntity)
                    }

                    else -> {
                        view.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }
}