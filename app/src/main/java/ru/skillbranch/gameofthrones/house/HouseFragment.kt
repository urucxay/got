package ru.skillbranch.gameofthrones.house

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_house.*
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.house.page.PageFragmentDirections
import ru.skillbranch.gameofthrones.utils.EventObserver

class HouseFragment(private val house: String) : Fragment() {

    private lateinit var adapter: HouseAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private val viewModel: HouseViewModel by viewModels { HouseViewModelFactory(house) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_house, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initRecyclerView()
        initViewModel()
    }


    private fun initRecyclerView() {
        adapter = HouseAdapter {
            viewModel.openCharacter(it.id)
        }
        layoutManager = LinearLayoutManager(context)
        rv_characters.adapter = adapter
        rv_characters.layoutManager = layoutManager
        rv_characters.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun initViewModel() {
        viewModel.members.observe(viewLifecycleOwner, Observer { adapter.submitList(it) })
        viewModel.openCharacterEvent.observe(viewLifecycleOwner,
            EventObserver { openCharacter(it) })
    }

    private fun initToolbar() {

    }

    private fun openCharacter(id: String) {
        val action = PageFragmentDirections.actionHouseFragmentToCharacterFragment(id)
        findNavController().navigate(action)
    }

}
