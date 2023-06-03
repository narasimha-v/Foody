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
import com.example.foody.databinding.RecipesBottomSheetBinding
import com.example.foody.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.foody.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.foody.viewModels.RecipesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.util.*


class RecipesBottomSheet : BottomSheetDialogFragment() {
    private lateinit var recipesViewModel: RecipesViewModel
    private var _binding: RecipesBottomSheetBinding? = null
    private val binding get() = _binding!!

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
    ): View {
        _binding = RecipesBottomSheetBinding.inflate(
            inflater, container, false
        )


        recipesViewModel.readMealAndDietType
            .asLiveData()
            .observe(
                viewLifecycleOwner
            ) { value ->
                mealTypeChip = value.selectedMealType
                dietTypeChip = value.selectedDietType
                updateChip(value.selectedMealTypeId, binding.mealTypeChipGroup)
                updateChip(value.selectedDietTypeId, binding.dietTypeChipGroup)
            }

        binding.mealTypeChipGroup.setOnCheckedStateChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId.first())
            val selectedMealType = chip.text.toString().lowercase(Locale.ROOT)
            mealTypeChipId = checkedId.first()
            mealTypeChip = selectedMealType
        }

        binding.dietTypeChipGroup.setOnCheckedStateChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId.first())
            val selectedDietType = chip.text.toString().lowercase(Locale.ROOT)
            dietTypeChipId = checkedId.first()
            dietTypeChip = selectedDietType
        }

        binding.applyBtn.setOnClickListener {
            recipesViewModel.saveMealAndDietTypeTemp(
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

        return binding.root
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                val targetView = chipGroup.findViewById<Chip>(chipId)
                targetView.isChecked = true
                chipGroup.requestChildFocus(targetView, targetView)
            } catch (e: Exception) {
                Log.d("RecipesBottomSheet", e.message.toString())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}