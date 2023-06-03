package com.example.foody.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.example.foody.R
import com.example.foody.adapters.PagerAdapter
import com.example.foody.data.database.entities.FavouritesEntity
import com.example.foody.databinding.ActivityDetailsBinding
import com.example.foody.ui.fragments.ingredients.IngredientsFragment
import com.example.foody.ui.fragments.instructions.InstructionsFragment
import com.example.foody.ui.fragments.overview.OverviewFragment
import com.example.foody.viewModels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var menuItem: MenuItem

    private var recipeSaved = false
    private var savedRecipeId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(
            ContextCompat.getColor(this, R.color.white)
        )
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")

        val resultBundle = Bundle()
        resultBundle.putParcelable("recipeBundle", args.result)

        val pagerAdapter = PagerAdapter(
            resultBundle,
            fragments,
            this
        )

        binding.viewPager2.isUserInputEnabled = false
        binding.viewPager2.apply {
            adapter = pagerAdapter
        }

        TabLayoutMediator(
            binding.tabLayout, binding.viewPager2
        ) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        menuItem = menu!!.findItem(R.id.save_to_favourites_menu)
        checkSavedRecipes(menuItem)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.save_to_favourites_menu && !recipeSaved) {
            saveToFavourites(item)
        } else if (item.itemId == R.id.save_to_favourites_menu && recipeSaved) {
            removeFromFavourites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveToFavourites(
        item: MenuItem
    ) {
        val favoritesEntity = FavouritesEntity(
            0,
            args.result
        )
        mainViewModel.insertFavoriteRecipe(favoritesEntity)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Added to Favorites.")
        recipeSaved = true
        savedRecipeId = favoritesEntity.id
    }

    private fun removeFromFavourites(
        item: MenuItem
    ) {
        val favoritesEntity = FavouritesEntity(
            savedRecipeId,
            args.result
        )
        mainViewModel.deleteFavoriteRecipe(favoritesEntity)
        changeMenuItemColor(item, R.color.white)
        showSnackBar("Removed from Favorites.")
        recipeSaved = false
        savedRecipeId = 0
    }

    private fun checkSavedRecipes(menuItem: MenuItem) {
        mainViewModel.readFavoriteRecipes.observe(this) { favoritesEntity ->
            try {
                for (savedRecipe in favoritesEntity) {
                    if (savedRecipe.result.recipeId == args.result.recipeId) {
                        changeMenuItemColor(menuItem, R.color.yellow)
                        savedRecipeId = savedRecipe.id
                        recipeSaved = true
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun showSnackBar(message: String) {
        val snackBar = Snackbar.make(
            binding.detailsLayout,
            message,
            Snackbar.LENGTH_SHORT
        )
        snackBar.setAction("Okay") {}
        snackBar.show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon?.setTint(ContextCompat.getColor(this, color))
    }

    override fun onDestroy() {
        super.onDestroy()
        changeMenuItemColor(menuItem, R.color.white)
        savedRecipeId = 0
        recipeSaved = false
    }
}