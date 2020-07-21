package ru.skillbranch.gameofthrones.house

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
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

    private lateinit var adapter: CharactersAdapter
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
        setHasOptionsMenu(true)
        initToolbar()
        initRecyclerView()
        initViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val menuItem = menu.findItem(R.id.action_search)
        val searchView = menuItem.actionView as SearchView

//        if (binding.isSearch) {
//            menuItem.expandActionView()
//            searchView.setQuery(binding.searchQuery, false)
//            if (binding.isFocusedSearch) searchView.requestFocus()
//            else searchView.clearFocus()
//        }

        menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
//                viewModel.handleSearchMode(true)
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
//                viewModel.handleSearchMode(true)
                return true
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.handleSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.handleSearch(newText)
                return true
            }
        })

        searchView.setOnCloseListener {
//            viewModel.handleSearchMode(false)
            true
        }
    }

    private fun initRecyclerView() {
        adapter = CharactersAdapter {
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
        viewModel.getMembers().observe(viewLifecycleOwner, Observer {
            Log.d("TEST_SEARCH", "list -> $it")
            adapter.submitList(it)
        })
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
