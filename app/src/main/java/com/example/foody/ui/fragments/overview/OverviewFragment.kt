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
import com.example.foody.models.Result
import kotlinx.android.synthetic.main.fragment_overview.view.*
import org.jsoup.Jsoup

class OverviewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_overview, container, false
        )

        val args = arguments
        val myBundle: Result? = args?.getParcelable("recipeBundle")

        view.mainImageView.load(myBundle?.image)
        view.titleTextView.text = myBundle?.title
        view.likesTextView.text = myBundle?.aggregateLikes.toString()
        view.timeTextView.text = myBundle?.readyInMinutes.toString()
        myBundle?.summary.let {
            val summary = Jsoup.parse(it).text()
            view.summaryTextView.text = summary
        }

        if (myBundle?.vegetarian == true) {
            setDishInfo(view.vegetarianImageView, view.vegetarianTextView)
        }
        if (myBundle?.vegan == true) {
            setDishInfo(view.veganImageView, view.veganTextView)
        }
        if (myBundle?.glutenFree == true) {
            setDishInfo(view.glutenFreeImageView, view.glutenFreeTextView)
        }
        if (myBundle?.dairyFree == true) {
            setDishInfo(view.dairyFreeImageView, view.dairyFreeTextView)
        }
        if (myBundle?.veryHealthy == true) {
            setDishInfo(view.healthyImageView, view.healthyTextView)
        }
        if (myBundle?.cheap == true) {
            setDishInfo(view.cheapImageView, view.cheapTextView)
        }

        return view
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

}