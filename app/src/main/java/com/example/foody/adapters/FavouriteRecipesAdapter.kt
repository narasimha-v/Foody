package com.example.foody.adapters

import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foody.R
import com.example.foody.data.database.entities.FavouritesEntity
import com.example.foody.databinding.FavouritesRecipesRowLayoutBinding
import com.example.foody.ui.fragments.favourites.FavouriteRecipesFragmentDirections
import com.example.foody.util.RecipesDiffUtil
import com.example.foody.viewModels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.favourites_recipes_row_layout.view.favouriteRecipesRowLayout
import kotlinx.android.synthetic.main.favourites_recipes_row_layout.view.favouriteRowCardView

class FavouriteRecipesAdapter(
    private val requireActivity: FragmentActivity,
    private val mainViewModel: MainViewModel
) :
    RecyclerView.Adapter<FavouriteRecipesAdapter.MyViewHolder>(),
    ActionMode.Callback {

    private lateinit var mActionMode: ActionMode
    private lateinit var rootView: View

    private var multiSelection = false
    private var selectedRecipes = arrayListOf<FavouritesEntity>()
    private var myViewHolders = arrayListOf<MyViewHolder>()

    private var favouriteRecipes = emptyList<FavouritesEntity>()

    class MyViewHolder(
        private val binding: FavouritesRecipesRowLayoutBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(favouritesEntity: FavouritesEntity) {
            binding.favouritesEntity = favouritesEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavouritesRecipesRowLayoutBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        myViewHolders.add(holder)
        rootView = holder.itemView.rootView
        val currentRecipe = favouriteRecipes[position]
        holder.bind(currentRecipe)

        // Single click listener
        holder.itemView.favouriteRecipesRowLayout.setOnClickListener {
            if (multiSelection) {
                applySelection(holder, currentRecipe)
            } else {
                val action =
                    FavouriteRecipesFragmentDirections.actionFavouriteRecipesFragmentToDetailsActivity(
                        currentRecipe.result
                    )
                holder.itemView.findNavController().navigate(action)
            }
        }

        // Long click listener
        holder.itemView.favouriteRecipesRowLayout.setOnLongClickListener {
            if (!multiSelection) {
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder, currentRecipe)
                true
            } else {
                multiSelection = false
                false
            }
        }
    }

    private fun applySelection(
        holder: MyViewHolder,
        currentRecipe: FavouritesEntity
    ) {
        if (selectedRecipes.contains(currentRecipe)) {
            selectedRecipes.remove(currentRecipe)
            changeRecipeStyle(
                holder, R.color.cardBackgroundColor, R.color.strokeColor
            )
            applyActionModeTitle()
        } else {
            selectedRecipes.add(currentRecipe)
            changeRecipeStyle(
                holder, R.color.cardBackgroundLightColor, R.color.colorPrimary
            )
            applyActionModeTitle()
        }
    }

    private fun changeRecipeStyle(
        holder: FavouriteRecipesAdapter.MyViewHolder,
        cardBackgroundColor: Int,
        strokeColor: Int
    ) {
        holder.itemView.favouriteRecipesRowLayout.setBackgroundColor(
            ContextCompat.getColor(
                requireActivity,
                cardBackgroundColor
            )
        )
        holder.itemView.favouriteRowCardView.strokeColor =
            ContextCompat.getColor(
                requireActivity,
                strokeColor
            )
    }

    private fun applyActionModeTitle() {
        when (selectedRecipes.size) {
            0 -> {
                mActionMode.finish()
            }

            1 -> {
                mActionMode.title = "${selectedRecipes.size} item selected"
            }

            else -> {
                mActionMode.title = "${selectedRecipes.size} items selected"
            }
        }
    }

    override fun getItemCount(): Int {
        return favouriteRecipes.size
    }

    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(
            R.menu.favourites_contextual_menu,
            menu
        )
        mActionMode = actionMode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {
        if (menu?.itemId == R.id.deleteFavouriteRecipeMenu) {
            selectedRecipes.forEach {
                mainViewModel.deleteFavoriteRecipe(it)
            }
            showSnackBar("${selectedRecipes.size} Recipe/s removed.")
            multiSelection = false
            selectedRecipes.clear()
            actionMode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        myViewHolders.forEach { holder ->
            changeRecipeStyle(
                holder, R.color.cardBackgroundColor, R.color.strokeColor
            )
        }
        multiSelection = false
        selectedRecipes.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }

    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor = ContextCompat.getColor(
            requireActivity,
            color
        )
    }

    fun setData(newFavouriteRecipes: List<FavouritesEntity>) {
        val favouriteRecipesDiffUtil = RecipesDiffUtil(
            favouriteRecipes,
            newFavouriteRecipes
        )
        val diffUtilResult = DiffUtil.calculateDiff(favouriteRecipesDiffUtil)
        favouriteRecipes = newFavouriteRecipes
        diffUtilResult.dispatchUpdatesTo(this)
    }

    fun clearContextualActionMode() {
        if (this::mActionMode.isInitialized) {
            mActionMode.finish()
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            rootView,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}.show()
    }
}