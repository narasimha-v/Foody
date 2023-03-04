package com.example.foody.ui.fragments.recipes.bottomSheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.foody.R
import com.example.foody.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.foody.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.foody.viewModels.RecipesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.recipes_bottom_sheet.view.*
import java.util.*


class RecipesBottomSheet : BottomSheetDialogFragment() {
    private lateinit var recipesViewModel: RecipesViewModel
    private var mealTypeChipId = 0
    private var dietTypeChipId = 0
    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var dietTypeChip = DEFAULT_DIET_TYPE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel = ViewModelProvider(
            requireActivity()
        )[RecipesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mView = inflater.inflate(
            R.layout.recipes_bottom_sheet,
            container,
            false
        )

        recipesViewModel.readMealAndDietType
            .asLiveData()
            .observe(
                viewLifecycleOwner
            ) { value ->
                mealTypeChip = value.selectedMealType
                dietTypeChip = value.selectedDietType
                updateChip(value.selectedMealTypeId, mView.mealTypeChipGroup)
                updateChip(value.selectedDietTypeId, mView.dietTypeChipGroup)
            }

        mView.mealTypeChipGroup.setOnCheckedChangeListener { group, checkedId ->
            mealTypeChipId = checkedId
            val chip = group.findViewById<Chip>(checkedId)
            mealTypeChip = chip.text.toString().toLowerCase(Locale.ROOT)
        }

        mView.dietTypeChipGroup.setOnCheckedChangeListener { group, checkedId ->
            dietTypeChipId = checkedId
            val chip = group.findViewById<Chip>(checkedId)
            dietTypeChip = chip.text.toString().toLowerCase(Locale.ROOT)
        }

        mView.applyBtn.setOnClickListener {
            recipesViewModel.saveMealAndDietType(
                mealTypeChip,
                mealTypeChipId,
                dietTypeChip,
                dietTypeChipId
            )
            val action = RecipesBottomSheetDirections
                .actionRecipesBottomSheetToRecipesFragment(
                    true
                )
            findNavController().navigate(action)
        }

        return mView
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            } catch (e: Exception) {
                Log.d("RecipesBottomSheet", e.message.toString())
            }
        }
    }
}