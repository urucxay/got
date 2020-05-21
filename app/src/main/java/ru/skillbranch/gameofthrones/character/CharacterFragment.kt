package ru.skillbranch.gameofthrones.character

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_character.*
import kotlinx.coroutines.launch
import ru.skillbranch.gameofthrones.main.MainActivity
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.data.local.entities.CharacterFull
import ru.skillbranch.gameofthrones.repositories.GoTRepository
import ru.skillbranch.gameofthrones.utils.EventObserver

class CharacterFragment : Fragment() {

    private val args = navArgs<CharacterFragmentArgs>()
    private val viewModel by viewModels<CharacterViewModel> { CharacterViewModelFactory(args.value.id) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_character, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()

    }

    private fun initViewModel() {
        viewModel.character.observe(viewLifecycleOwner, Observer { updateInfo(it) })
        viewModel.openParentEvent.observe(viewLifecycleOwner, EventObserver{navigateToParent(it)})
    }

    private fun updateInfo(char: CharacterFull) {
        initToolbar()
        (activity as MainActivity).supportActionBar?.title = char.name
        iv_house_cover.setImageDrawable(resources.getDrawable(getHouseCoatOfArms(char.house), null))
        tv_words.text = char.words
        tv_born.text = char.born
        tv_titles.text = char.titles.joinToString("\n")
        tv_aliases.text = char.aliases.joinToString("\n")

        //init death snackbar
        if (char.died.isNotEmpty()) {
            Snackbar.make(requireView(), "Died: ${char.died}", Snackbar.LENGTH_INDEFINITE).show()
        }
        //init father
        if (char.father != null) {
            tv_father.visibility = View.VISIBLE
            btn_father.visibility = View.VISIBLE
            btn_father.text = char.father.name
            btn_father.setOnClickListener {
                openParent(char.father.id)
            }
        }
        //init mother
        if (char.mother != null) {
            tv_mother.visibility = View.VISIBLE
            btn_mother.visibility = View.VISIBLE
            btn_mother.text = char.mother.name
            btn_mother.setOnClickListener {
                openParent(char.mother.id)
            }
        }
    }

    private fun openParent(id: String) {
        viewModel.openParent(id)
    }

    private fun navigateToParent(id: String) {
        val action = CharacterFragmentDirections.actionCharacterFragmentSelf(id)
        findNavController().navigate(action)
    }

    private fun initToolbar() {
        (activity as MainActivity).registerToolbar(toolbar)
    }

    private fun getHouseCoatOfArms(house: String) : Int {
        return when {
            house.contains("Stark") -> R.drawable.stark_coast_of_arms
            house.contains("Lannister") -> R.drawable.lannister_coast_of_arms
            house.contains("Targaryen") -> R.drawable.targaryen_coast_of_arms
            house.contains("Baratheon") -> R.drawable.baratheon_coast_of_arms
            house.contains("Greyjoy") -> R.drawable.greyjoy_coast_of_arms
            house.contains("Martell") -> R.drawable.martel_coast_of_arms
            house.contains("Tyrell") -> R.drawable.tyrel_coast_of_arms
            else -> -1
        }
    }


}
