package com.example.foody.ui.fragments.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import coil.load
import com.example.foody.R
import com.example.foody.bindingAdapters.RecipesRowBinding
import com.example.foody.databinding.FragmentOverviewBinding
import com.example.foody.models.Result
import com.example.foody.util.Constants.Companion.RECIPE_RESULT
import org.jsoup.Jsoup

class OverviewFragment : Fragment() {
    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewBinding.inflate(
            inflater, container, false
        )


        val args = arguments
        val myBundle: Result = args!!.getParcelable<Result>(RECIPE_RESULT) as Result

        binding.mainImageView.load(myBundle.image)
        binding.titleTextView.text = myBundle.title
        binding.likesTextView.text = myBundle.aggregateLikes.toString()
        binding.timeTextView.text = myBundle.readyInMinutes.toString()

        RecipesRowBinding.parseHtml(binding.summaryTextView, myBundle.summary)

        if (myBundle.vegetarian) {
            setDishInfo(binding.vegetarianImageView, binding.vegetarianTextView)
        }
        if (myBundle.vegan) {
            setDishInfo(binding.veganImageView, binding.veganTextView)
        }
        if (myBundle.glutenFree) {
            setDishInfo(binding.glutenFreeImageView, binding.glutenFreeTextView)
        }
        if (myBundle.dairyFree) {
            setDishInfo(binding.dairyFreeImageView, binding.dairyFreeTextView)
        }
        if (myBundle.veryHealthy) {
            setDishInfo(binding.healthyImageView, binding.healthyTextView)
        }
        if (myBundle.cheap) {
            setDishInfo(binding.cheapImageView, binding.cheapTextView)
        }

        return binding.root
    }

    private fun setDishInfo(imageView: ImageView, textView: TextView) {
        imageView.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.green
            )
        )
        textView.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.green
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}