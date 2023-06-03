package com.example.foody.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.foody.R
import com.example.foody.databinding.IngredientsRowLayoutBinding
import com.example.foody.models.ExtendedIngredient
import com.example.foody.util.Constants.Companion.BASE_IMAGE_URL
import com.example.foody.util.RecipesDiffUtil
import java.util.*

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {
    private var ingredientsList = emptyList<ExtendedIngredient>()

    class MyViewHolder(
        val binding: IngredientsRowLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            IngredientsRowLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val ingredient = ingredientsList[position]
        holder.binding.ingredientImageView.load(
            BASE_IMAGE_URL + ingredient.image
        ) {
            crossfade(600)
            error(R.drawable.ic_error_placeholder)
        }
        holder.binding.ingredientName.text = ingredient.name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.ROOT
            ) else it.toString()
        }
        holder.binding.ingredientAmount.text = ingredient.amount.toString()
        holder.binding.ingredientUnit.text = ingredient.unit
        holder.binding.ingredientConsistency.text = ingredient.consistency
        holder.binding.ingredientOriginal.text = ingredient.original
    }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }

    fun setData(ingredients: List<ExtendedIngredient>) {
        val ingredientsDiffUtil = RecipesDiffUtil(
            ingredientsList,
            ingredients
        )
        val diffUtilResult = DiffUtil.calculateDiff(ingredientsDiffUtil)
        ingredientsList = ingredients
        diffUtilResult.dispatchUpdatesTo(this)
    }
}