package com.example.foody.ui.fragments.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foody.R
import com.example.foody.adapters.FavouriteRecipesAdapter
import com.example.foody.databinding.FragmentFavouriteRecipesBinding
import com.example.foody.viewModels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteRecipesFragment : Fragment() {
    private val mainViewModel: MainViewModel by viewModels()
    private val mAdapter: FavouriteRecipesAdapter by lazy {
        FavouriteRecipesAdapter(
            requireActivity(),
            mainViewModel
        )
    }

    private var _binding: FragmentFavouriteRecipesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteRecipesBinding.inflate(
            inflater,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.mAdapter = mAdapter

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.favourite_recipes_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.deleteAllFavouriteRecipesMenu) {
                    mainViewModel.deleteAllFavoriteRecipes()
                    showSnackBar()
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)


        setupRecyclerView(binding.favouriteRecipesRecyclerView)

        return binding.root
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun showSnackBar() {
        Snackbar.make(
            binding.root,
            "All recipes removed.",
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mAdapter.clearContextualActionMode()
    }
}